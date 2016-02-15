package concurrent2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import util.Element;
import util.Pattern;
import util.Results;
import util.ThreadCount;

/**
 * author: Hao 
 * date:2015年5月18日
 * time:下午3:07:30
 * purpose: 
 */
public class GrowsThread extends Thread{

	/** global parameters **/
	private Results results;
	private HashMap<String, Element> posMap;
	private HashMap<String, Element> negMap;
	private int minGap;
	private int maxGap;
	private HashSet<String> impossibleSet;
	private final double posSize;
	private final double negSize;
	private ArrayList<Element> reList;
	private ThreadCount tc;
	private int tIndex;
	
	/** local parameters **/
	private Results localResults;
	private final int threadNumber;
	private final int nodeID;
	private static int trycount;
	
	public GrowsThread(Results results, HashMap<String, Element> posMap,
			HashMap<String, Element> negMap, int minGap, int maxGap,
			HashSet<String> impossibleSet, double posSize, double negSize,
			ArrayList<Element> reList, int tIndex, int threadNumber, int nodeID,
			ThreadCount tc) {
		super();
		this.results = results;
		this.posMap = posMap;
		this.negMap = negMap;
		this.minGap = minGap;
		this.maxGap = maxGap;
		this.impossibleSet = impossibleSet;
		this.posSize = posSize;
		this.negSize = negSize;
		this.reList = reList;
		this.tIndex = tIndex;
		this.tc = tc;
		
		/** init top-k with length-1 patterns **/
//		localResults = new Results(results.K);
//		for(Pattern p : results.getpList()){
//			localResults.addPattern(p);
//		}
		this.threadNumber = threadNumber;
		this.nodeID = nodeID;
	}

	@Override
	public void run() {
		long start = System.currentTimeMillis();
//		for(int i = nodeID*interval ; i < (nodeID+1)*interval && i < reList.size() ; i++ ){

		int i = nodeID;
		for(; i < tIndex ; i = i + threadNumber){
			Element firstE = reList.get(i);
			System.out.println("Thread: "+nodeID+" length-1: "+firstE.getValue());
			Pattern pattern = new Pattern(firstE, negMap.get(firstE.getValue()), posSize, negSize);
			grows3(pattern);
		}
		for(; i < reList.size() ; i = i + threadNumber){
			Element firstE = reList.get(i);
			System.out.println("Thread: "+nodeID+" length-1: "+firstE.getValue());
			Pattern pattern = new Pattern(firstE, negMap.get(firstE.getValue()), posSize, negSize);
			grows2(pattern);
		}
		
		
//		printLocalResults();
		
		long end = System.currentTimeMillis();
		
		System.out.println("Thread cost: "+ (end - start) +" ms.");
		System.out.println("Thread end: "+ nodeID);
		
		synchronized (tc) {
			tc.count --;
		}
	}

	private void printLocalResults(){
		System.out.println("Thread: "+ nodeID);
		for(Pattern p : localResults.getpList())
			System.out.println(p);
	}
	
	private void grows2(Pattern pattern) {
		trycount ++;
		
//		System.out.println(trycount+ " "+pattern.toString());
		
		if(pattern.posSup <= 0){
			return;
		}
		
		if( (results.isFull() && pattern.posSup < results.getThreshold())){
			/** pruning rule 2 **/
			/** the support of pattern is less than the threshold of the Top-k result set, so the support of 
			 * all super pattern of this are less than the threshold **/
			return;
		}
		
		/** add into results **/
		if(pattern.cRatio > 0){
			if(pattern.cRatio > results.getThreshold()){
				synchronized (results) {
					results.addPattern(pattern);
//					System.out.println("size: "+ results.getpList().size());
				}
			}
//			System.out.println("New Pattern! "+ pattern.toString()+ " po: "+pattern.negOccurrence.keySet());
		}
		
		/** add a new element into this pattern **/
		for(Element nextE : reList){
			/** pruning rule 3 **/
			/** if a element is in the impossibleSet, it means that the positive support of this element is less than 
			 * threshold of the Top-k result set, so any pattern contains this element is an impossible pattern **/
			if(impossibleSet.contains(nextE.getValue())){
				continue;
			}
			if(results.isFull() && nextE.sup < results.getThreshold()){
				synchronized (impossibleSet) {
					impossibleSet.add(nextE.getValue());
				}
				continue;
			}
			
			/** grows **/
			Pattern newPattern = pattern.clone();
			newPattern.addNewElement(nextE, negMap.get(nextE.getValue()), minGap, maxGap);
			grows2(newPattern);
		}
	}

	private void grows3(Pattern pattern) {

		if(pattern.posSup <= 0){
			return;
		}
		
		if( (results.isFull() && pattern.posSup < results.getThreshold())){
			/** pruning rule 2 **/
			/** the support of pattern is less than the threshold of the Top-k result set, so the support of 
			 * all super pattern of this are less than the threshold **/
			return;
		}
		
		/** add a new element into this pattern **/
		for(Element nextE : reList){
			/** pruning rule 3 **/
			/** if a element is in the impossibleSet, it means that the positive support of this element is less than 
			 * threshold of the Top-k result set, so any pattern contains this element is an impossible pattern **/
			if(impossibleSet.contains(nextE.getValue())){
				continue;
			}
			if(results.isFull() && nextE.sup < results.getThreshold()){
				impossibleSet.add(nextE.getValue());
				continue;
			}
			
			/** grows **/
			Pattern newPattern = pattern.clone();
			newPattern.addNewElement(nextE, negMap.get(nextE.getValue()), minGap, maxGap);
			grows2(newPattern);
		}
	}
}
