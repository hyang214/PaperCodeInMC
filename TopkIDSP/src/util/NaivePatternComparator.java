package util;

import java.util.Comparator;

/**
 * author: Hao 
 * date:Jan 22, 2016
 * time:3:37:41 PM
 * purpose:
 */

public class NaivePatternComparator implements Comparator<NaivePattern>{
	private static PeerKeyComparator pkc = new PeerKeyComparator();
	
	/**
	 * Comparator of pattern, order by cRatio desc, length desc and pos_sup desc
	 */	
	@Override
	public int compare(NaivePattern o1, NaivePattern o2) {
		return pkc.compare(o1.getPeerKey(), o2.getPeerKey());
	}
}
