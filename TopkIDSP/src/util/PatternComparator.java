package util;

import java.util.BitSet;
import java.util.Comparator;

/**
 * author: Hao 
 * date:Jan 22, 2016
 * time:3:37:41 PM
 * purpose:
 */

public class PatternComparator implements Comparator<Pattern>{
	private static PeerKeyComparator pkc = new PeerKeyComparator();
	
	/**
	 * Comparator of pattern, order by cRatio desc, length desc and pos_sup desc
	 */	
	@Override
	public int compare(Pattern o1, Pattern o2) {
		/** since the rules of comparators of both pattern and peerkey is the same, 
		 * we use pkc to implement the comparator of pattern 
		 * **/
		int p =  pkc.compare(o1.getPeerKey(), o2.getPeerKey());
		if(p != 0)
			return p;
		else{
			if(o1.getPosSeqIds().equals(o2.getPosSeqIds())){
				if(o1.getNegSeqIds().equals(o2.getNegSeqIds())){
					 for(int i = o1.getPosSeqIds().nextSetBit(0); i >= 0; i = o1.getPosSeqIds().nextSetBit(i+1)) {
					     BitSet a = o1.getPosOccurrences().get(i);
					     BitSet b = o2.getPosOccurrences().get(i);
					     if(!a.equals(b))
					    	 return -1;
					 }
					 for(int i = o1.getNegSeqIds().nextSetBit(0); i >= 0; i = o1.getNegSeqIds().nextSetBit(i+1)) {
					     BitSet a = o1.getNegOccurrences().get(i);
					     BitSet b = o2.getNegOccurrences().get(i);
					     if(!a.equals(b))
					    	 return -1;
					 }
					return 0;
				}else{
					return -1;
				}
			}else{
				return -1;
			}
		}
	}
}
