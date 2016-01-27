package util;

import java.util.BitSet;
import java.util.HashMap;

/**
 * author: Hao 
 * date:Jan 5, 2016
 * time:11:29:44 AM
 * purpose:
 */
public class Element{
	private Value value;
	private BitSet posSeqIds;
	private BitSet negSeqIds;
	private HashMap<Integer, BitSet> posOccurrences;
	private HashMap<Integer, BitSet> negOccurrences;
	private double posSup;
	private double negSup;
	private double cRatio;
	
	private Element(){
	}
	
	public Element(BitSet closure){
		this.value = new Value(closure);
		this.posSeqIds = new BitSet();
		this.negSeqIds = new BitSet();
		posOccurrences = new HashMap<>();
	}
	
	/** value has occur at the 'positionId'-th index of the 'seqId'-th line in data set d (0: positive; 1: negative)**/
	public void occurAt(int seqId, int positionId, int d){
		if(d == Parameter.POSITIVE){
			/** set the seqIds **/
			posSeqIds.set(seqId, true);
			
			/** set the occurrences **/
			BitSet occurrence = posOccurrences.get(posSeqIds);
			if(occurrence == null){
				occurrence = new BitSet();
			}
			occurrence.set(positionId, true);
			posOccurrences.put(seqId, occurrence);
		}else if(d == Parameter.NEGATIVE){
			/** set the seqIds **/
			negSeqIds.set(seqId, true);
			
			/** set the occurrences **/
			BitSet occurrence = negOccurrences.get(negSeqIds);
			if(occurrence == null){
				occurrence = new BitSet();
			}
			occurrence.set(positionId, true);
			negOccurrences.put(seqId, occurrence);
		}else{
			//TODO error: add occurrence from unknown data set
		}
	}

	@Override
	public Element clone() {
		Element clone = new Element();
		clone.value = this.value.clone();
		clone.posSeqIds = (BitSet)this.posSeqIds;
		clone.negSeqIds = (BitSet)this.negSeqIds;
		clone.posOccurrences = CloneHelper.occurrenceClone(this.posOccurrences);
		clone.negOccurrences = CloneHelper.occurrenceClone(this.negOccurrences);
		clone.posSup = this.posSup;
		clone.negSup = this.negSup;
		clone.cRatio = this.cRatio;
		
		return clone;
	}
	
	/**
	 * calculate the posSup, negSup and cRatio for this element
	 * this method should be call before sort the alphabet
	 */
	public void calculate(){
		this.posSup = posSeqIds.cardinality();
		this.negSup = negSeqIds.cardinality();
		this.cRatio = posSup - negSup;
	}
	
	/************************************************
	 * Getter and Setter
	 ************************************************/
	public BitSet getPosSeqIds() {
		return posSeqIds;
	}

	public Value getValue() {
		return value;
	}

	public void setValue(Value value) {
		this.value = value;
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

	public double getcRatio() {
		return cRatio;
	}

	public void setcRatio(double cRatio) {
		this.cRatio = cRatio;
	}
}
