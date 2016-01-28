package util;

import java.util.Comparator;

/**
 * author: Hao 
 * date:Jan 28, 2016
 * time:3:08:35 PM
 * purpose:
 */
public class PeerKeyComparator implements Comparator<PeerKey>{

	@Override
	public int compare(PeerKey o1, PeerKey o2) {
		if(o1.getcRatio() > o2.getcRatio()){
			return 1;
		}else if(o1.getcRatio() < o2.getcRatio()){
			return -1;
		}else{
			if(o1.getPosSup() > o2.getPosSup()){
				return 1;
			}else if(o1.getPosSup() < o2.getPosSup()){
				return -1;
			}else{
				return 0;
			}
		}
	}

}
