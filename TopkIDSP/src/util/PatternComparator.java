package util;

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
		return pkc.compare(o1.getPeerKey(), o2.getPeerKey());
		
		/** original implement **/
//		if(o1.getcRatio() < o2.getcRatio()){
//			return -1;
//		}else if(o1.getcRatio() > o2.getcRatio()){
//			return 1;
//		}else{
//			if(o1.getLength() < o2.getLength()){
//				return -1;
//			}else if(o1.getLength() > o2.getLength()){
//				return 1;
//			}else{
//				if(o1.getPosSup() < o2.getPosSup()){
//					return -1;
//				}else if(o1.getPosSup() > o2.getPosSup()){
//					return 1;
//				}else{
//					return 0;
//				}
//			}
//		}
	}
}
