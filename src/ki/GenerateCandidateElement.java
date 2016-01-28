package ki;

import java.util.BitSet;
import java.util.HashMap;
import java.util.List;

import util.Alphabet;
import util.Element;
import util.ItemMap;
import util.Parameter;
import util.Sequences;

/**
 * author: Hao 
 * date:Jan 21, 2016
 * time:3:30:05 PM
 * purpose:
 */
public class GenerateCandidateElement {

	private Sequences sequences;
	private Alphabet alphabet;
	
	public GenerateCandidateElement(Sequences sequences) {
		this.sequences = sequences;
		this.alphabet = new Alphabet();
	}

	public void generateCE() {
		scanPosDataset();
		scanNegDataset();
		updateOccurrence();
	}

	/**
	 * scan the positive data set and generate elements
	 */
	private void scanPosDataset() {
		/** for each sequences **/
		for(int seqId = 0 ; seqId < sequences.posInstances.size() ; seqId ++){
			/** for each elements **/
			String sequence = sequences.posInstances.get(seqId);
			String[] elements = sequence.split(Parameter.elementSeparator);
			for(int positionId = 0 ; positionId < elements.length ; positionId ++){
				String element = elements[positionId];
				BitSet closure = ItemMap.eEncode(element, Parameter.POSITIVE);
				
				Element e = alphabet.getElementByBitSet(closure);
				if(e == null){
					/** e is a new element **/
					e = new Element(closure);
					alphabet.addNaturalElement(e);
					
					/** generate common element of e and any exited elements in alphabet **/
					generateCommonElement(e);
				}
				/** just update the occurrence of it **/
				e.occurAt(seqId, positionId, Parameter.POSITIVE);
			}
		}
	}
	
	/**
	 * scan the negative data set and generate elements
	 */
	private void scanNegDataset() {
		/** for each sequences **/
		for(int seqId = 0 ; seqId < sequences.negInstances.size() ; seqId ++){
			/** for each elements **/
			String sequence = sequences.negInstances.get(seqId);
			String[] elements = sequence.split(Parameter.elementSeparator);
			for(int positionId = 0 ; positionId < elements.length ; positionId ++){
				String element = elements[positionId];
				BitSet closure = ItemMap.eEncode(element, Parameter.NEGATIVE);
				
				Element e = alphabet.getElementByBitSet(closure);
				if(e == null){
					/** e is a new element **/
					e = new Element(closure);
					alphabet.addNaturalElement(e);
					
					/** generate common element of e and any exited elements in alphabet **/
					generateCommonElement(e);
				}
				/** just update the occurrence of it **/
				e.occurAt(seqId, positionId, Parameter.NEGATIVE);
			}
		}
	}
	
	private void updateOccurrence() {
		/** for every elements update the occurrence information **/
		for(BitSet key : alphabet.getMap().keySet()){
			/** for every item in this element **/
			for (int i = key.nextSetBit(0); i >= 0; i = key.nextSetBit(i+1)) {
			     /** get the list of natural elements with this item **/
				List<Element> inList = alphabet.getNaturalListByItem(new Integer(i));  
			 }
		}
	}
	
	/** 
	 * generate common element of e and any exited elements in alphabet 
	 */
	private void generateCommonElement(Element e) {
		BitSet eBitSet = e.getValue().getClosure();
		
		/** store the new generated common element by e and others **/
		HashMap<BitSet, Element> tmpMap = new HashMap<>(); 
		
		/** find the elements in alphabet that have common items with e **/
		for (int i = eBitSet.nextSetBit(0); i >= 0; i = eBitSet.nextSetBit(i + 1)) {
			for(Element another : alphabet.getInverseIndex().get(new Integer(i))){
				if(another.equals(eBitSet)){
					/** another is e itself **/
					continue;
				}
				
				BitSet tmp = (BitSet)another.getValue().getClosure().clone();
				tmp.and(eBitSet);
				if(tmp.cardinality() > 0 && !tmpMap.containsKey(tmp) && !alphabet.getMap().containsKey(tmp)){
					/** if 1) tmp is not null; 2) tmp is not generated before; 3) tmp is not a element in alphabet */
					Element newOne = new Element(tmp);
					tmpMap.put(tmp, newOne);
				}
			}
		}
		
		/** put new element into the alphabet **/
		for(Element addOne : tmpMap.values()){
			alphabet.addElement(addOne);
		}
	}

	/************************************************
	 * Getter and Setter
	 ************************************************/
	public Sequences getSequences() {
		return sequences;
	}

	public void setSequences(Sequences sequences) {
		this.sequences = sequences;
	}

	public Alphabet getAlphabet() {
		return alphabet;
	}

	public void setAlphabet(Alphabet alphabet) {
		this.alphabet = alphabet;
	}	
}
