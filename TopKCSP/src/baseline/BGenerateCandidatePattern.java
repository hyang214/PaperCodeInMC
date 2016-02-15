package baseline;

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
public class BGenerateCandidatePattern {

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
	
	public BGenerateCandidatePattern(HashMap<String, Element> posMap, HashMap<String, Element> negMap, int K, int minGap, int maxGap, double posSize, double negSize, boolean baseline) {
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
		for(String firstE : posMap.keySet()){
//			System.out.println(firstE);
			Pattern pattern = new Pattern(posMap.get(firstE), negMap.get(firstE), posSize, negSize);
			grows(pattern);
		}
		System.out.println("Trycount: "+trycount);
		
	}

	
	private void grows(Pattern pattern) {
		trycount++;
		if(pattern.posSup <= 0){
			return;
		}
//		if( (results.isFull() && pattern.posSup < results.getThreshold())){
//			/** pruning rule 2 **/
//			/** the support of pattern is less than the threshold of the Top-k result set, so the support of 
//			 * all super pattern of this are less than the threshold **/
//			return;
//		}
		
		/** add into results **/
		if(pattern.cRatio > 0){
			results.addPattern(pattern.clone());
//			System.out.println("New Pattern! "+ pattern.toString());
		}
		
		//TODO grows pattern
		/** add a new element into this pattern **/
		for(String nextS : posMap.keySet()){
//			/** pruning rule 3 **/
//			/** if a element is in the impossibleSet, it means that the positive support of this element is less than 
//			 * threshold of the Top-k result set, so any pattern contains this element is an impossible pattern **/
//			if(impossibleSet.contains(nextS)){
//				continue;
//			}
			Element nextE = posMap.get(nextS);
//			if(results.isFull() && nextE.sup < results.getThreshold()){
//				impossibleSet.add(nextS);
//				continue;
//			}
			
			/** grows **/
			Pattern newPattern = pattern.clone();
			newPattern.addNewElement(nextE, negMap.get(nextS), minGap, maxGap);
			grows(newPattern);
		}
	}
	
	public Results getResults() {
		return results;
	}

}
