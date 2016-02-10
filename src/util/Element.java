package util;

import java.util.BitSet;
import java.util.HashMap;

import tools.CloneHelper;

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
	
	public Element(Value value){
		this.value = value;
		this.posSeqIds = new BitSet();
		this.negSeqIds = new BitSet();
		this.posOccurrences = new HashMap<>();
		this.negOccurrences = new HashMap<>();
	}
	
	public Element(BitSet closure){
		this.value = new Value(closure);
		this.posSeqIds = new BitSet();
		this.negSeqIds = new BitSet();
		this.posOccurrences = new HashMap<>();
		this.negOccurrences = new HashMap<>();
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
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("Element: " + value.toString());
		sb.append("		Positive:\n");
		sb.append("			seqIds: "+ posSeqIds + "\n");
		sb.append("			occurrences: \n"+ toString(posOccurrences));
		sb.append("		Negative:\n");
		sb.append("			seqIds: "+ negSeqIds + "\n");
		sb.append("			occurrences: \n"+ toString(negOccurrences));
		return sb.toString();
	}
	
	/**
	 * translate HashMap<Integer, BitSet> to String
	 */
	private String toString(HashMap<Integer, BitSet> occurrences) {
		StringBuffer sb = new StringBuffer();
		for(Integer key : occurrences.keySet()){
			sb.append("				"+key+": " + occurrences.get(key)+"\n");
		}
		return sb.toString();
	}

	/**
	 * return the value.toString() and cRatio, posSup and negSup
	 */
	public String simpleDetail() {
		StringBuffer sb = new StringBuffer();
		sb.append("Element: " + value.toString()+" ");
		sb.append("cRatio: " + cRatio+" ");
		sb.append("posSup: " + posSup+" ");
		sb.append("negSup: " + negSup+" ");
		sb.append("\n");
		return sb.toString();
	}
	
	/**
	 * calculate the posSup, negSup and cRatio for this element
	 * this method should be call before sort the alphabet
	 */
	public void calculate(){
		this.posSup = (double)posSeqIds.cardinality() / (double)Parameter.posSize;
		this.negSup = (double)negSeqIds.cardinality() / (double)Parameter.negSize;
		this.cRatio = posSup - negSup;
	}
	
	/************************************************
	 * update the occurrence information
	 ************************************************/
	/**
	 * update the occurrence information of this object by natural element
	 */
	public void updateOccurrence(Element natural) {
		/** for occurrence in one sequence of natural**/
		updateOccurrence(Parameter.POSITIVE, natural);
		updateOccurrence(Parameter.NEGATIVE, natural);
	}
	
	/**
	 * update the occurrence information of DATASET from natural
	 */
	private void updateOccurrence(int DATASET, Element natural) {
		BitSet toSeqId;
		HashMap<Integer, BitSet> toOccurrences;
		BitSet fromSeqId;
		HashMap<Integer, BitSet> fromOccurrences;
		if(DATASET == Parameter.POSITIVE){
			toSeqId = this.posSeqIds;
			toOccurrences = this.posOccurrences;
			fromSeqId = natural.posSeqIds;
			fromOccurrences = natural.posOccurrences;
		}
		else if(DATASET == Parameter.NEGATIVE){
			toSeqId = this.negSeqIds;
			toOccurrences = this.negOccurrences;
			fromSeqId = natural.negSeqIds;
			fromOccurrences = natural.negOccurrences;
		}
		else{
			//TODO error: unknown data set
			return;
		}
		updateOccurrence(toSeqId, toOccurrences, fromSeqId, fromOccurrences);
	}

	/**
	 * update the toSeqId and toOccurrences from fromSeqId and fromOccurrences
	 */
	private void updateOccurrence(BitSet toSeqId, HashMap<Integer, BitSet> toOccurrences, BitSet fromSeqId, HashMap<Integer, BitSet> fromOccurrences) {
		/** for every seqId from fromSeqId **/
		for (int seqId = fromSeqId.nextSetBit(0); seqId >= 0; seqId = fromSeqId.nextSetBit(seqId+1)) {
		     if(toSeqId.get(seqId)){
		    	 /** if toSeqId also has this seqId, combine the occurrence information from both sides **/
		    	 BitSet toOccurrence = toOccurrences.get(new Integer(seqId));
		    	 BitSet fromOccurrence = fromOccurrences.get(new Integer(seqId));
		    	 toOccurrence.or(fromOccurrence);
		     }
		     else{
		    	 /** if toSeqId does not have this seqId, just clone information from 'from' side and put it into 'to' **/
		    	 toSeqId.set(seqId, true);
		    	 BitSet occurrence = (BitSet)fromOccurrences.get(new Integer(seqId)).clone();
		    	 toOccurrences.put(new Integer(seqId), occurrence);
		     }
		 }
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
