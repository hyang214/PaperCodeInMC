package util;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;

import tools.CloneHelper;

/**
 * author: Hao 
 * date:Jan 5, 2016
 * time:11:29:53 AM
 * purpose: pattern without concise representation
 */
public class NaivePattern {
	/**
	 * valueList: the list of elements in pattern
	 * seqIds: the set of sequence Ids in which we can find this pattern
	 * occurrences: the sequence Ids and position Ids of the latest element of this pattern
	 * length: the length of this pattern
	 */
	private ArrayList<BitSet> valueList;
	private BitSet posSeqIds;
	private BitSet negSeqIds;
	private HashMap<Integer, BitSet> posOccurrences;
	private HashMap<Integer, BitSet> negOccurrences;
	private int length;
	private double posSup;
	private double negSup;
	private double cRatio;
	
	/** for clone **/
	private NaivePattern() {
	}
	
	/** for generate navie pattern by pattern with concise representation **/
	public NaivePattern(Pattern source){
		this.valueList = new ArrayList<>();
		this.posSeqIds = (BitSet)source.getPosSeqIds().clone();
		this.negSeqIds = (BitSet)source.getNegSeqIds().clone();
		this.posOccurrences = CloneHelper.occurrenceClone(source.getPosOccurrences());
		this.negOccurrences = CloneHelper.occurrenceClone(source.getNegOccurrences());
		this.length = source.getLength();
		this.posSup = source.getPosSup();
		this.negSup = source.getNegSup();
		this.cRatio = source.getcRatio();
	}
	
	public NaivePattern(Element e){
		this.valueList = new ArrayList<>();
		this.valueList.add(e.getValue().getClosure());
		this.posSeqIds = (BitSet)e.getPosSeqIds().clone();
		this.negSeqIds = (BitSet)e.getNegSeqIds().clone();
		this.posOccurrences = CloneHelper.occurrenceClone(e.getPosOccurrences());
		this.negOccurrences = CloneHelper.occurrenceClone(e.getNegOccurrences());
		this.length = 1;
		calculate();
	}
	
	/**
	 * add new element into this pattern 
	 */
	public boolean addElement(Element next){
		/** find the common sequence ids **/
		BitSet possible = (BitSet)posSeqIds.clone();
		possible.and(next.getPosSeqIds());
		if(!maxPossiblePosSupportCheck(possible)){
			/** the max possible cRatio is already less the kThreshold, ignore this pattern **/
			return false;
		}
		
		/** generate the occurrence of pattern in positive data set **/
		boolean success = updatePosOccurrence(next);
		if(!success){
			/** the posSup is less than the kThreshold **/
			return false;
		}
		/** generate the occurrence of pattern in negative data set **/
		updateNegOccurrence(next);
		
		/** update pattern **/
		this.valueList.add(next.getValue().getClosure());
		this.length ++;
		calculate();
		
		return true;
	}

	/**
	 * update the positive seqIds and occurrence
	 */
	private boolean updatePosOccurrence(Element next) {
		/** for the common positive sequence ids of both this pattern and next **/
		for(int i = this.posSeqIds.nextSetBit(0); i >= 0; i = this.posSeqIds.nextSetBit(i+1)){
			if(next.getPosSeqIds().get(i) == true){
				/** calculate the occurrence of new pattern in i-th sequence **/
				BitSet newPosition = new BitSet();
				for(int j = this.posOccurrences.get(i).nextSetBit(0); j >= 0; j = this.posOccurrences.get(i).nextSetBit(j+1)){
					for(int k = next.getPosOccurrences().get(i).nextSetBit(j + Parameter.minGap + 1); k>=0; k = next.getPosOccurrences().get(i).nextSetBit(k + 1)){
						if(k <= j + Parameter.maxGap+1){
							newPosition.set(k);
						}
						else{
							break;
						}
					}
				}
				
				/** if there are no occurrence of new pattern in this sequence, remove it **/
				if(newPosition.cardinality()==0){
					this.posSeqIds.clear(i);
					this.posOccurrences.remove(new Integer(i));
					
					if(maxPossiblePosSupportCheck(posSeqIds)){
						/** the max possible cRatio is already less the kThreshold, ignore this pattern **/
						return false;
					}
				}
				else{
					/** replace old occurrence with newer one **/
					this.posOccurrences.put(i, newPosition);
				}
			}
			else{
				/** if the i-th sequence is not the common sequence of both pattern and next,
				 * remove i-th sequence and its occurrence records **/
				this.posSeqIds.clear(i);
				this.posOccurrences.remove(new Integer(i));
				
				if(maxPossiblePosSupportCheck(posSeqIds)){
					/** the max possible cRatio is already less the kThreshold, ignore this pattern **/
					return false;
				}
			}
		}
		return true;
	}
	
	/**
	 * update the negative seqIds and occurrence
	 */
	private void updateNegOccurrence(Element next) {
		/** for the common positive sequence ids of both this pattern and next **/
		for(int i = this.negSeqIds.nextSetBit(0); i >= 0; i = this.negSeqIds.nextSetBit(i+1)){
			if(next.getNegSeqIds().get(i) == true){
				/** calculate the occurrence of new pattern in i-th sequence **/
				BitSet newPosition = new BitSet();
				for(int j = this.negOccurrences.get(i).nextSetBit(0); j >= 0; j = this.negOccurrences.get(i).nextSetBit(j+1)){
					for(int k = next.getNegOccurrences().get(i).nextSetBit(j + Parameter.minGap + 1); k>=0; k = next.getNegOccurrences().get(i).nextSetBit(k + 1)){
						if(k <= j + Parameter.maxGap+1){
							newPosition.set(k);
						}
						else{
							break;
						}
					}
				}
				
				/** if there are no occurrence of new pattern in this sequence, remove it **/
				if(newPosition.cardinality()==0){
					this.negSeqIds.clear(i);
					this.negOccurrences.remove(new Integer(i));
				}
				else{
					/** replace old occurrence with newer one **/
					this.negOccurrences.put(i, newPosition);
				}
			}
			else{
				/** if the i-th sequence is not the common sequence of both pattern and next,
				 * remove i-th sequence and its occurrence records **/
				this.negSeqIds.clear(i);
				this.negOccurrences.remove(new Integer(i));
			}
		}
	}

	/**
	 * check whether the positive support of this pattern is already less than kThreshold in the results
	 * @return false: this pattern is impossible, ignore it
	 * 		   true: go on
	 */
	private boolean maxPossiblePosSupportCheck(BitSet possible){
		double maxPossibleCRatio = (double)possible.cardinality() / Parameter.posSize;
		if( maxPossibleCRatio < Results.pkThreshold.getcRatio()){
			/** the max possible cRatio is already less the threshold, ignore this pattern **/
			return false;
		}
		return true;
	}
	
	@Override
	public NaivePattern clone() {
		NaivePattern clone = new NaivePattern();
		clone.valueList = CloneHelper.valueListCopy(this.valueList);
		clone.posSeqIds = (BitSet)this.posSeqIds.clone();
		clone.negSeqIds = (BitSet)this.negSeqIds.clone();
		//TODO occurrence information may be useless in NaivePatternExtract.java
		clone.posOccurrences = CloneHelper.occurrenceClone(this.posOccurrences);
		clone.negOccurrences = CloneHelper.occurrenceClone(this.negOccurrences);
		clone.length = this.length;
		clone.posSup = this.posSup;
		clone.negSup = this.negSup;
		clone.cRatio = this.cRatio;
		
		return clone;
	}
	
	/**
	 * generate the peer key for this pattern
	 */
	public PeerKey getPeerKey(){
		return new PeerKey(this);
	}
	
	/** 
	 * calculate posSup, negSup and cRatio
	 * **/
	private void calculate(){
		this.posSup = (double)this.posSeqIds.cardinality() / (double)Parameter.posSize;
		this.negSup = (double)this.negSeqIds.cardinality() / (double)Parameter.negSize;
		this.cRatio = posSup - negSup;
		
	}
	
	@Override
	public String toString(){
		StringBuffer sb = new StringBuffer();
		sb.append("	" + getPeerKey().toString() + " ");
		for(BitSet v : valueList){
			sb.append("{" + ItemMap.eDecode(v) + "},");
		}
		sb.deleteCharAt(sb.length()-1);
		sb.append("\n");
		return sb.toString();
	}
	
	public boolean equals(NaivePattern obj) {
		if(this.valueList.size() != obj.valueList.size()){
			return false;
		}else{
			for(int i = 0 ; i < this.valueList.size() ; i ++){
				if(!this.valueList.get(i).equals(obj.valueList.get(i))){
					return false;
				}
			}
			return true;
		}
	}
	
	/************************************************
	 * used for NaivePatternExtract.java
	 ************************************************/
	/**
	 * add a new element into 'valueList'
	 */
	public void addValue(BitSet element){
		valueList.add(element);
	}
	
	
	/************************************************
	 * Getter and Setter
	 ************************************************/
	public ArrayList<BitSet> getValueList() {
		return valueList;
	}

	public void setValueList(ArrayList<BitSet> valueList) {
		this.valueList = valueList;
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

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public double getcRatio() {
		return cRatio;
	}

	public void setcRatio(double cRatio) {
		this.cRatio = cRatio;
	}

	public double getPosSup() {
		return posSup;
	}

	public void setPosSup(double posSup) {
		this.posSup = posSup;
	}

	public double getNegSup() {
		return negSup;
	}

	public void setNegSup(double negSup) {
		this.negSup = negSup;
	}

	public static NaivePattern getEmptyNaivePattern(){
		NaivePattern p = new NaivePattern();
		p.valueList = new ArrayList<>();
		p.posSeqIds = new BitSet();
		p.negSeqIds = new BitSet();
		p.posOccurrences = new HashMap<Integer,BitSet>();
		p.negOccurrences = new HashMap<Integer,BitSet>();
		p.length = 0;
		p.posSup = 0.0;
		p.negSup = 0.0;
		p.cRatio = 0.0;
		return p;
	}
}
