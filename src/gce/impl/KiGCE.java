package gce.impl;

import java.util.BitSet;
import java.util.HashMap;
import java.util.List;

import gce.GenerateCandidateElement;
import tools.Verbase;
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
public class KiGCE extends GenerateCandidateElement{
	private Sequences sequences;
	private Alphabet alphabet;
	private List<Element> ceList;
	
	public KiGCE() {
		this.alphabet = new Alphabet();
	}
	
	/**
	 * get data sets
	 */
	@Override
	public void setDataset(Sequences sequences){
		this.sequences = sequences;
	}

	@Override
	public void generateCE() {
		/** scan positive data set **/
		scanDataset(Parameter.POSITIVE);
		/** scan negative data set **/
		scanDataset(Parameter.NEGATIVE);
		/** generate common element by natural elements **/
		generateCommonElementByNaturalElement();
		/** update the occurrences information for all elements in alphabet **/
		updateOccurrence();
		/** calculate the posSup, negSup and cRatio for each element in alphabet **/
		alphabet.buildHeuristicListJ18();
		/** get the final candidate element list **/
		ceList = alphabet.getHeuristicList();
		/** testing and validation **/
		Verbase.verbaseAtLevel(1, ItemMap.staticToString());
		Verbase.verbaseAtLevel(1, alphabet.toString());
		/** clear useless object **/
		clearUO();
	}
	
	@Override
	public List<Element> getCEList() {
		return ceList;
	}

	/**
	 * scan DATASET
	 */
	private void scanDataset(int DATASET){
		List<String> instance = sequences.getInstanceByDATASET(DATASET);
		/** for each sequence **/
		for(int seqId = 0 ; seqId < instance.size() ; seqId ++){
			String sequence = instance.get(seqId);
			/** for each element **/
			String[] elements = sequence.split(Parameter.elementSeparator);
			for(int positionId = 0 ; positionId < elements.length ; positionId ++){
				String element = elements[positionId];
				BitSet closure = ItemMap.eEncode(element, DATASET);
				
				Element e = alphabet.getElementByBitSet(closure);
				if(e == null){
					/** e is a new element **/
					e = new Element(closure);
					alphabet.addNaturalElement(e);
				
				}
				/** just update the occurrence of it **/
				e.occurAt(seqId, positionId, DATASET);
			}
		}
	}
	
	/**
	 * generate common element by natural elements
	 */
	private void generateCommonElementByNaturalElement() {
		/** generate common element of e and any exited elements in alphabet **/
		for(Element e : alphabet.getSourceList()){
			collectGeneratorsAndGenerateCommonElement(e);
		}
	}
	
	/** 
	 * collect generators and generate common element of e and any exited elements in alphabet 
	 */
	private void collectGeneratorsAndGenerateCommonElement(Element e) {
		BitSet eBitSet = e.getValue().getClosure();
		
		/** store the new generated common element by e and others **/
		HashMap<BitSet, Element> tmpMap = new HashMap<>(); 
		
		/** find the natural elements in alphabet that have common items with e **/
		for (int i = eBitSet.nextSetBit(0); i >= 0; i = eBitSet.nextSetBit(i + 1)) {
			for(Element another : alphabet.getSourceIndex().get(new Integer(i))){
				if(another.equals(eBitSet)){
					/** another is e itself **/
					continue;
				}
				
				BitSet tmp = (BitSet)another.getValue().getClosure().clone();
				tmp.and(eBitSet);
				if(tmp.cardinality() > 0){
					/** put it into the generator set of e and another **/
					if(tmp.equals(eBitSet)){
						/** e is subset of another **/
						another.getValue().addGenerator(tmp);
					}else if(tmp.equals(another.getValue().getClosure())){
						/** another is subset of e **/
						e.getValue().addGenerator(tmp);
					}
					else{
						/** tmp is subset of both e and another **/
						another.getValue().addGenerator(tmp);
						e.getValue().addGenerator(tmp);
						
						/** tmp may be a new common elements **/
						if(!tmpMap.containsKey(tmp) && !alphabet.getMap().containsKey(tmp)){
							/** tmp is not generated before and tmp is not an element in alphabet 
							 * create it */
							Element newOne = new Element(tmp);
							tmpMap.put(tmp, newOne);
						}
					}
				}
			}
		}
		
		/** put new element into the alphabet **/
		for(Element addOne : tmpMap.values()){
			alphabet.addElement(addOne);
		}
	}

	/**
	 * update the occurrence information of all elements in alphabet
	 */
	private void updateOccurrence() {
		/** for every elements update the occurrence information **/
		for(BitSet key : alphabet.getMap().keySet()){
			/** for every item in this element **/
			Element e = alphabet.getElementByBitSet(key);
			for (int i = key.nextSetBit(0); i >= 0; i = key.nextSetBit(i+1)) {
			     /** get the list of natural elements with this item **/
				List<Element> inList = alphabet.getNaturalListByItem(new Integer(i));  
				/** e need not update itself **/
				boolean flag = false;
				if(inList.contains(e)){
					inList.remove(e);
					flag = true;
				}
				
				for(Element natural : inList){
					BitSet tmp = (BitSet)key.clone();
					tmp.and(natural.getValue().getClosure());
					
					/** if tmp == key, it means e is sub element of natural **/
					if(tmp.cardinality() > 0 && tmp.equals(key)){
						e.updateOccurrence(natural);
					}
				}
				
				if(flag){
					/** since we remove e from inList if e used to be in it, put it back **/
					inList.add(e);
				}
			 }
		}
	}
	
	/** 
	 * clear useless object 
	 * **/
	private void clearUO() {
		sequences = null;
		alphabet.clearUO();
		alphabet = null;
	}

	@Override
	public Alphabet getAlphabet() {
		// TODO Auto-generated method stub
		return null;
	}
}
