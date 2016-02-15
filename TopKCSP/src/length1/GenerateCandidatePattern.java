package length1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import util.Element;
import util.HashMapSortHelper;
import util.Pattern;
import util.Results;

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
	
	private int trycount = 0;
	
	public GenerateCandidatePattern(HashMap<String, Element> posMap, HashMap<String, Element> negMap, int K, int minGap, int maxGap, double posSize, double negSize, boolean baseline) {
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

	public void genCP() {
		
		/** pruning rule 1 
		 * only use the elements that appeared in positive data set **/
		long start = System.currentTimeMillis();
		for(String firstE : posMap.keySet()){
			System.out.println(firstE);
			Pattern pattern = new Pattern(posMap.get(firstE), negMap.get(firstE), posSize, negSize);
			grows(pattern);
		}
		System.out.println("Trycount: "+trycount);
		long end = System.currentTimeMillis();
		System.out.println("Thread cost: "+ (end - start) +" ms.");
	}

	public void genCP2(){
			
		/** pruning rule 1 
		 * only use the elements that appeared in positive data set **/
		long start = System.currentTimeMillis();
		reList = HashMapSortHelper.INSTANCE.reorder(posMap,negMap);
		long end = System.currentTimeMillis();
		System.out.println("Sort cost: "+ (end - start) +" ms.");
		
		/** length-1 pruning **/
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
		}
		end = System.currentTimeMillis();
		System.out.println("length-1 cost: "+ (end - start) +" ms.");
		
		for(Element firstE : reList){
			Pattern pattern = new Pattern(firstE, negMap.get(firstE.getValue()), posSize, negSize);
			grows2(pattern, 0);
		}
		System.out.println("Trycount: "+trycount);
		end = System.currentTimeMillis();
		System.out.println("Thread cost: "+ (end - start) +" ms.");
	}
	
	private void grows(Pattern pattern) {
		trycount++;
				
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
			results.addPattern(pattern.clone());
//			System.out.println("New Pattern! "+ pattern.toString());
		}
		
		//TODO grows pattern
		/** add a new element into this pattern **/
		for(String nextS : posMap.keySet()){
			/** pruning rule 3 **/
			/** if a element is in the impossibleSet, it means that the positive support of this element is less than 
			 * threshold of the Top-k result set, so any pattern contains this element is an impossible pattern **/
			if(impossibleSet.contains(nextS)){
				continue;
			}
			Element nextE = posMap.get(nextS);
			if(results.isFull() && nextE.sup < results.getThreshold()){
				impossibleSet.add(nextS);
				continue;
			}
			
			/** grows **/
			Pattern newPattern = pattern.clone();
			newPattern.addNewElement(nextE, negMap.get(nextS), minGap, maxGap);
			grows(newPattern);
		}
	}
	
	private void grows2(Pattern pattern, int layer) {
		trycount++;
		
		if(pattern.posSup <= 0){
			return;
		}
		
		if(layer > 0){
			
			if( (results.isFull() && pattern.posSup < results.getThreshold())){
				/** pruning rule 2 **/
				/** the support of pattern is less than the threshold of the Top-k result set, so the support of 
				 * all super pattern of this are less than the threshold **/
				return;
			}
			
			/** add into results **/
			if(pattern.cRatio > 0){
				results.addPattern(pattern.clone());
//				System.out.println("New Pattern! "+ pattern.toString()+ " po: "+pattern.negOccurrence.keySet());
			}
		}
		
		//TODO grows pattern
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
			grows2(newPattern, layer+1);
		}
	}

	public Results getResults() {
		return results;
	}

}
