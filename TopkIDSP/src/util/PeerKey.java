package util;

import java.util.BitSet;
import java.util.HashMap;

/**
 * author: Hao 
 * date:Jan 28, 2016
 * time:2:17:49 PM
 * purpose: key for PeerPattern 
 */
public class PeerKey {
	private Double cRatio;
	private Double posSup;
	private int length;
	private BitSet posSeqIds;
	private BitSet negSeqIds;
	private HashMap<Integer, BitSet> posOccurrences;
	private HashMap<Integer, BitSet> negOccurrences;
	
	public PeerKey(Pattern p) {
		this.cRatio = p.getcRatio();
		this.posSup = p.getPosSup();
		this.length = p.getLength();
		this.posSeqIds = p.getPosSeqIds();
		this.negSeqIds = p.getNegSeqIds();
		this.posOccurrences = p.getPosOccurrences();
		this.negOccurrences = p.getNegOccurrences();
	}
	
	public PeerKey(NaivePattern p) {
		this.cRatio = p.getcRatio();
		this.posSup = p.getPosSup();
		this.length = p.getLength();
		this.posSeqIds = p.getPosSeqIds();
		this.negSeqIds = p.getNegSeqIds();
		this.posOccurrences = p.getPosOccurrences();
		this.negOccurrences = p.getNegOccurrences();
	}

	@Override
	public String toString() {
		return "C:" + cRatio + " S:" + posSup + " L:" + length + " hc:"+hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof PeerKey){
			PeerKey pk = (PeerKey)obj;
			if(pk.cRatio.equals(this.cRatio) && pk.posSup.equals(this.posSup) && pk.length == this.length){
				if(posSeqIds.equals(pk.getPosSeqIds())){
					if(negSeqIds.equals(pk.getNegSeqIds())){
//						 for(int i = posSeqIds.nextSetBit(0); i >= 0; i = posSeqIds.nextSetBit(i+1)) {
//						     BitSet a = posOccurrences.get(i);
//						     BitSet b = pk.getPosOccurrences().get(i);
//						     if(!a.equals(b))
//						    	 return false;
//						 }
//						 for(int i = negSeqIds.nextSetBit(0); i >= 0; i = negSeqIds.nextSetBit(i+1)) {
//						     BitSet a = negOccurrences.get(i);
//						     BitSet b = pk.getNegOccurrences().get(i);
//						     if(!a.equals(b))
//						    	 return false;
//						 }
						return true;
					}else{
						return false;
					}
				}else{
					return false;
				}
			}else{
				return false;
			}
		}
		else{
			return false;
		}
	}
	
	@Override
	public int hashCode() {
		long key1 = Double.doubleToLongBits(cRatio);
		for(int i = posSeqIds.nextSetBit(0); i >= 0; i = posSeqIds.nextSetBit(i+1)) {
		     key1 += i;
		}
		long key2 = Double.doubleToLongBits(posSup);
		for(int i = negSeqIds.nextSetBit(0); i >= 0; i = negSeqIds.nextSetBit(i+1)) {
			key2 -= i;
		}
		long key3 = (key1+key2) * length;
		return (int)(key3 ^ (key3 >>> 32));
	}

	/************************************************
	 * Getter and Setter
	 ************************************************/
	public Double getcRatio() {
		return cRatio;
	}

	public void setcRatio(Double cRatio) {
		this.cRatio = cRatio;
	}

	public Double getPosSup() {
		return posSup;
	}

	public void setPosSup(Double posSup) {
		this.posSup = posSup;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public BitSet getPosSeqIds() {
		return posSeqIds;
	}

	public void setPosSeqIds(BitSet posSeqIds) {
		this.posSeqIds = posSeqIds;
	}

	public BitSet getNegSeqIds() {
		return negSeqIds;
	}

	public void setNegSeqIds(BitSet negSeqIds) {
		this.negSeqIds = negSeqIds;
	}

	public HashMap<Integer, BitSet> getPosOccurrences() {
		return posOccurrences;
	}

	public void setPosOccurrences(HashMap<Integer, BitSet> posOccurrences) {
		this.posOccurrences = posOccurrences;
	}

	public HashMap<Integer, BitSet> getNegOccurrences() {
		return negOccurrences;
	}

	public void setNegOccurrences(HashMap<Integer, BitSet> negOccurrences) {
		this.negOccurrences = negOccurrences;
	}
}
