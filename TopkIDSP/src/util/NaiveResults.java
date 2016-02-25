package util;

import java.util.ArrayList;
import java.util.List;

/**
 * author: Hao 
 * date:Jan 21, 2016
 * time:11:28:00 AM
 * purpose: store the result
 */
public class NaiveResults {
	/**
	 * topK: store the ordered key of peer pattern 
	 * pkc: the comparator of peer key
	 * pkThreshold: the peer key threshold
	 * threshold: the threshold of pattern
	 * finalPatternList: the final pattern list, only available after call merge() function 
	 */
	public static List<NaivePattern> topK = new ArrayList<>();
	public static PeerKey pkThreshold = new PeerKey(0.0, 0.0, 0);
	public static NaivePatternComparator npc = new NaivePatternComparator();
	public static PeerKeyComparator pkc = new PeerKeyComparator();
	
	/**
	 * add pattern into the results
	 */
	public static void addResult(NaivePattern pattern){
		/** the size of results is less than K 
		 * !!! since we will merge pattern at the end, so we should keep more then K raw patterns in results
		 * **/
		if(topK.size() < Parameter.K){
			topK.add(pattern);
		}
		else{
			/** sort the peer keys list and update the threshold **/
			topK.sort(npc);
			pkThreshold = topK.get(0).getPeerKey();
			
			/** there are already K patterns in results, compare pattern with the first pattern in results **/
			PeerKey pPK = pattern.getPeerKey();
			int compare = pkc.compare(pPK, pkThreshold);
			if(compare < 0){
				/** pattern is not the top-k peer pattern, ignore it **/
				return;
			}else{
				/** store this peer pattern **/
				topK.add(pattern);
				
				/** remove the first pattern **/
				topK.remove(0);
			}
		}
	}
}

