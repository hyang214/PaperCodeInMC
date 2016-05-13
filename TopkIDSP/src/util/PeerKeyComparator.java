package util;

import java.util.BitSet;
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
			return 3;
		}else if(target.getcRatio() < threshold.getcRatio()){
			return -3;
		}else{
			if(target.getPosSup() > threshold.getPosSup()){
				return 2;
			}else if(target.getPosSup() < threshold.getPosSup()){
				return -2;
			}else{
				if(target.getLength() > threshold.getLength()){
					return 1;
				}else if(target.getLength() < threshold.getLength()){
					return -1;
				}else{
					if(target.getPosSeqIds().equals(threshold.getPosSeqIds())){
						if(target.getNegSeqIds().equals(threshold.getNegSeqIds())){
							 for(int i = target.getPosSeqIds().nextSetBit(0); i >= 0; i = target.getPosSeqIds().nextSetBit(i+1)) {
							     BitSet a = target.getPosOccurrences().get(i);
							     BitSet b = threshold.getPosOccurrences().get(i);
							     if(!a.equals(b))
							    	 return -1;
							 }
							 for(int i = target.getNegSeqIds().nextSetBit(0); i >= 0; i = threshold.getNegSeqIds().nextSetBit(i+1)) {
							     BitSet a = target.getNegOccurrences().get(i);
							     BitSet b = threshold.getNegOccurrences().get(i);
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
	}

}
