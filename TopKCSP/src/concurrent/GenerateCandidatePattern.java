package concurrent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import util.Element;
import util.HashMapSortHelper;
import util.Pattern;
import util.Results;
import util.ThreadCount;

/**
 * author: Hao 
 * date:2015年3月19日
 * time:下午3:19:07
 * purpose: Generate candidate Pattern 
 */
public class GenerateCandidatePattern {

	private Results results;
	private HashMap<String, Element> posMap;
	private HashMap<String, Element> negMap;
	private int minGap;
	private int maxGap;
	private HashSet<String> impossibleSet;
	private final double posSize;
	private final double negSize;
	private ArrayList<Element> reList;
	private final boolean baseline;
	private final int threadNumber;
	
	private int trycount = 0;
	
	public GenerateCandidatePattern(int threadNumber, HashMap<String, Element> posMap, HashMap<String, Element> negMap, int K, int minGap, int maxGap, double posSize, double negSize, boolean baseline) {
		this.threadNumber = threadNumber;
		this.results = new Results(K);
		this.posMap = posMap;
		this.negMap = negMap;
		this.minGap = minGap;
		this.maxGap = maxGap;
		this.impossibleSet = new HashSet<String>();
		this.posSize = posSize;
		this.negSize = negSize;
		
		this.baseline = baseline;
	}

	public void genCP2(){
		/** pruning rule 1 
		 * only use the elements that appeared in positive data set **/
		
		ThreadCount tc = new ThreadCount(threadNumber);
		
		reList = HashMapSortHelper.INSTANCE.reorder(posMap,negMap);
		ArrayList<Pattern> pList = new ArrayList<>();
		for(Element firstE: reList){
			Pattern pattern = new Pattern(firstE, negMap.get(firstE.getValue()), posSize, negSize);
			if(pattern.cRatio > 0){
				if(results.isFull()){
					if(pattern.cRatio > results.getThreshold())
						results.addPattern(pattern);
				}else{
					results.addPattern(pattern);
				}
			}
			pList.add(pattern);
		}
		
		for(int i = 0 ; i < threadNumber ; i ++){
			GrowsThread gt = new GrowsThread(results, posMap, negMap, minGap, maxGap, impossibleSet, posSize, negSize, 
					reList, pList,threadNumber, i, tc);
			System.out.println("Thread start: "+ i);
			gt.start();	
		}
		
		try {
			while(true){
				synchronized (tc) {
					if(tc.count == 0)
						break;
				}
				Thread.sleep(1); 
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("Trycount: "+trycount);
	}
	

	public Results getResults() {
		return results;
	}

}
