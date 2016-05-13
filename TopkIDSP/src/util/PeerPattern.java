package util;

import java.util.ArrayList;
import java.util.List;

/**
 * author: Hao 
 * date:Jan 28, 2016
 * time:1:42:33 PM
 * purpose: store the peer patterns, namely patterns with same cRatio and posSup
 */
public class PeerPattern {
	/**
	 * cRatio: the contrast ratio of these peer patterns
	 * posSup: the positive support of these peer patterns
	 * peerlist: the list of these peer patterns
	 */
	private Double cRatio;
	private Double posSup;
	private int length;
	private List<Pattern> peerlist;
	
	public PeerPattern(Pattern first){
		this.cRatio = first.getcRatio();
		this.posSup = first.getPosSup();
		this.length = first.getLength();
		this.peerlist = new ArrayList<Pattern>();
		this.peerlist.add(first);
	}
	
	/**
	 * add new peer pattern into this object
	 */
	public void addPattern(Pattern peer){
		if(cRatio.equals(peer.getcRatio()) && posSup.equals(peer.getPosSup()) && length == peer.getLength()){
			peerlist.add(peer);
		}
	}
	
	/**
	 * merge results if possible 
	 * **/
	public List<Pattern> merge() {
		List<Pattern> list = new ArrayList<>();
		
		boolean unchange = false;
		while(!unchange){
			unchange = true;
			for()
		}
		
		return list;
	}
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		for(Pattern p : peerlist){
			sb.append(" " + p.toString()+"\n");
		}
		return sb.toString();
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

	public List<Pattern> getPeerlist() {
		return peerlist;
	}

	public void setPeerlist(List<Pattern> peerlist) {
		this.peerlist = peerlist;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}
	
}
