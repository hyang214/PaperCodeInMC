package util;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
		
		Set<Pattern> usedSet = new HashSet<>();
		Set<Pattern> mergedSet = new HashSet<>();
		
		mergedSet.addAll(peerlist);
		boolean unchange = false;
		while(!unchange){
			unchange = true;
			Set<Pattern> nextIter = new HashSet<>();
			List<Pattern> mergedList = new ArrayList<>();
			mergedList.addAll(mergedSet);
			for(int i = 0 ; i < mergedList.size() ; i ++){
				if(!unchange)
					break;
				for(int j = i + 1; j < mergedList.size() ; j++){
					if(!unchange)
						break;
					Pattern a = mergedList.get(i);
					Pattern b = mergedList.get(j);
					int diffIndex = oneDiff(a,b);
					if(diffIndex != -1){
						int mergeable = mergeable(diffIndex,a.getValueList().get(diffIndex),b.getValueList().get(diffIndex));
						if(mergeable != 0){
							if(mergeable == -1){
								Pattern merged = merge(diffIndex,a,b);
								nextIter.add(merged);
							}else if(mergeable == 1){
								Pattern merged = merge(diffIndex,b,a);
								nextIter.add(merged);
							}else{
								System.err.println("Merge error!");
								System.exit(-1);
							}
							usedSet.add(a);
							usedSet.add(b);
							unchange = false;
							continue;
						}
					}
				}
			}
			mergedSet.addAll(nextIter);
			mergedSet.removeAll(usedSet);
		}
		list.addAll(mergedSet);
		
		return list;
	}
	
	/**
	 * can a and b merge?
	 * not: 0
	 * a merge b: -1
	 * b merge a: 1
	 */
	private int mergeable(int diffIndex, Value aV, Value bV) {
		if(bV.getGenerators().contains(aV.getClosure())){
			return 1;
		}else if(aV.getGenerators().contains(bV.getClosure())){
			return -1;
		}else
			return 0;
	}

	/**
	 * merge two pattern into a new one
	 * @param diffIndex 
	 */
	private Pattern merge(int diffIndex, Pattern a, Pattern b) {
		Pattern merged = a.trueClone();
		Value mV = merged.getValueList().get(diffIndex);
		Value bV = b.getValueList().get(diffIndex);
		Set<BitSet> removeSet = new HashSet<BitSet>();
		removeSet.add(bV.getClosure());
		for(BitSet mVG : mV.getGenerators()){
			for(BitSet bVG : bV.getGenerators()){
				BitSet bVGT = (BitSet)bVG.clone();
				bVGT.and(mVG);
				if(bVGT.equals(bVG)){
					removeSet.add(bVG);
				}
			}
		}
		mV.getGenerators().removeAll(removeSet);
		
		return merged;
	}

	/**
	 * only one elements is not the same between a and b in the same index?
	 */
	private int oneDiff(Pattern a, Pattern b) {
		int diffIndex = -1;
		int diffCount = 0;
		for(int i = 0 ; i < a.getLength() ; i ++){
			if(diffCount > 1)
				return -1;
			
			Value aiV = a.getValueList().get(i);
			Value biV = b.getValueList().get(i);
			if(aiV.getClosure().equals(biV.getClosure())){
				boolean same = sameGeneratorSet(aiV.getGenerators(),biV.getGenerators());
				if(!same){
					diffCount ++;
					diffIndex = i;
				}
			}else{
				diffCount ++;
				diffIndex = i;
			}
		}
		
		return diffIndex;
	}

	/**
	 * the generatorSet of aiV and biV is the same?
	 */
	private boolean sameGeneratorSet(Set<BitSet> aSet, Set<BitSet> bSet) {
		if(aSet.size() != bSet.size())
			return false;
		else{
			for(BitSet aB : aSet){
				if(!bSet.contains(aB))
					return false;
			}
			for(BitSet bB : bSet){
				if(!aSet.contains(bB))
					return false;
			}
			return true;
		}
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
