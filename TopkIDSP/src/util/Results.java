package util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * author: Hao 
 * date:Jan 21, 2016
 * time:11:28:00 AM
 * purpose: store the result
 */
public class Results {
	/**
	 * peerStore: store the results of top-k itemset-based DSP by peer pattern
	 * topK: store the ordered key of peer pattern 
	 * pkc: the comparator of peer key
	 * pkThreshold: the peer key threshold
	 * threshold: the threshold of pattern
	 * finalPatternList: the final pattern list, only available after call merge() function 
	 */
	public static HashMap<PeerKey, PeerPattern> peerStore = new HashMap<PeerKey, PeerPattern>();
	public static List<PeerKey> topK = new ArrayList<>();
	public static PeerKeyComparator pkc = new PeerKeyComparator();
	public static PeerKey pkThreshold = new PeerKey(Pattern.getEmptyPattern());
	public static List<Pattern> finalPatternList = new ArrayList<>();
	
	/**
	 * add pattern into the results
	 */
	public static void addResult(Pattern pattern){
		/** the size of results is less than K 
		 * !!! since we will merge pattern at the end, so we should keep more then K raw patterns in results
		 * **/
		if(topK.size() < Parameter.K){
			store(pattern);
		}
		else{
			/** sort the peer keys list and update the threshold **/
			topK.sort(pkc);
			pkThreshold = topK.get(0);
			
			/** there are already K peer patterns in results, compare peer pattern with the first pattern in results **/
			PeerKey pPK = pattern.getPeerKey();
			int compare = pkc.compare(pPK, pkThreshold);
			if(compare <= 0){
				/** pattern is not the top-k peer pattern, ignore it **/
				return;
			}else{
				/** store this peer pattern **/
				store(pattern);
				
				/** remove the peer pattern by the threshold if this pattern is not the peer pattern with
				 * threshold pattern **/
				if(compare > 0)
					remove(pkThreshold);
			}
		}
	}
	
	/**
	 * store pattern into peer pattern and topK
	 */
	private static void store(Pattern p){
		PeerKey pk = p.getPeerKey();
		PeerPattern pp = peerStore.get(pk);
		if(pp == null){
			topK.add(pk);
			pp = new PeerPattern(p);
			peerStore.put(pk, pp);
		}else{
			pp.addPattern(p);
		}
	}
	
	/**
	 * remove peer pattern by peer key
	 */
	private static void remove(PeerKey key){
		topK.remove(key);
		peerStore.remove(key);
	}

	/**
	 * merge results if possible only when the process of generate candidate pattern is end
	 * and order desc
	 * **/
	public static void merge() {
		for(PeerKey pk : Results.peerStore.keySet()){
			finalPatternList.addAll(Results.peerStore.get(pk).merge());
		}
		finalPatternList.sort(new PatternComparator());
	}
	
	/**
	 * clear
	 */
	public void clear(){
		peerStore = new HashMap<PeerKey, PeerPattern>();
		topK = new ArrayList<>();
		pkc = new PeerKeyComparator();
		pkThreshold = new PeerKey(Pattern.getEmptyPattern());
		finalPatternList = new ArrayList<>();
	}
}

