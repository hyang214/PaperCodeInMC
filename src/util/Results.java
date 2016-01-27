package util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

/**
 * author: Hao 
 * date:Jan 21, 2016
 * time:11:28:00 AM
 * purpose: store the result
 */
public class Results {
	/**
	 * results: the results of top-k itemset-based DSP
	 */
	public static List<Pattern> results = new ArrayList<>();
	public static PatternComparator patternComparator = new PatternComparator();
	public static List<Double> topK = new ArrayList<>();
	public static double kThreshold = 0;
	
	/**
	 * add pattern into the results
	 */
	public static void addResult(Pattern pattern){
		/** the size of results is less than K 
		 * !!! since we will merge pattern at the end, so we should keep more then K raw patterns in results
		 * **/
		if(topK.size() < Parameter.K){
			results.add(pattern);
			results.sort(patternComparator);
			topK.add(new Double(pattern.getcRatio()));
		}
		else{
			/** there are already K patterns in results, compare pattern with the first pattern in results **/
			int compare = patternComparator.compare(pattern, results.get(0));
			if(compare < 0){
				/** pattern is not the top-k pattern, ignore it **/
				return;
			}else if(compare > 0){
				/** add it into the results **/
				results.add(pattern);
				results.sort(patternComparator);
				topK.add(new Double(pattern.getcRatio()));
				
				/** remove all pattern with cRatio is equal kThreshold **/
				results.removeAll(collectImpossible());
				
				/** get the new kThreshold of results **/
				kThreshold = topK.get(0);
			}else{
				/** pattern has almost same property as k-th pattern, keep it since it maybe part of k-th pattern
				 * we will merge them at the post-process **/
				results.add(pattern);
			}
		}
	}

	/** 
	 * collect all pattern of which the cRatio is equal kThreshold, all of them should be removed
	 */
	private static Collection<Pattern> collectImpossible() {
		HashSet<Pattern> remove = new HashSet<>();
		for(Pattern p : results){
			if(p.getcRatio() == kThreshold)
				remove.add(p);
		}
		return remove;
	}

	/**
	 * merge results if possible 
	 * **/
	public static void merge() {
		//TODO merge results
	}
}
