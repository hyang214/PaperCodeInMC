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
	public int compare(PeerKey target, PeerKey threshold) {
		if(target.getcRatio() > threshold.getcRatio()){
			return 1;
		}else if(target.getcRatio() < threshold.getcRatio()){
			return -1;
		}else{
			if(target.getLength() > threshold.getLength()){
				return 1;
			}else if(target.getLength() < threshold.getLength()){
				return -1;
			}else{
				if(target.getPosSup() > threshold.getPosSup()){
					return 1;
				}else if(target.getPosSup() < threshold.getPosSup()){
					return -1;
				}else{
					return 0;
				}
			}
		}
	}

}
