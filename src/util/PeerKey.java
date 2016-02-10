package util;
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
	
	public PeerKey(Double cRatio, Double posSup, int length) {
		this.cRatio = cRatio;
		this.posSup = posSup;
		this.length = length;
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
				return true;
			}else{
				return false;
			}
		}else{
			return false;
		}
	}
	
	@Override
	public int hashCode() {
		long key1 = Double.doubleToLongBits(cRatio);
		long key2 = Double.doubleToLongBits(posSup);
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
	
}
