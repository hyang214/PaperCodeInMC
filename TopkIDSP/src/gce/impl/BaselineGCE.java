package gce.impl;

import gce.GenerateCandidateElement;

import java.util.BitSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

import util.Alphabet;
import util.Element;
import util.ItemMap;
import util.Parameter;
import util.Sequences;
import util.SubElementList;

public class BaselineGCE extends GenerateCandidateElement {
	private Sequences sequences;
	private Alphabet alphabet;
	private List<Element> ceList;
	
	public BaselineGCE() {
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
		/** calculate the posSup, negSup and cRatio for each element in alphabet **/
//		alphabet.buildHeuristicListJ18();
		/** get the final candidate element list **/
		ceList = alphabet.getBaselineList();	
		/** calculate ratio and support **/
		ceList.stream().forEach(new Consumer<Element>() {
			@Override
			public void accept(Element t) {
				t.calculate();
			}
		});
		
		/** testing and validation **/
//		Verbase.verbaseAtLevel(1, ItemMap.staticToString());
//		Verbase.verbaseAtLevel(1, alphabet.toString());
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
				
				/** for all elements contained by this natural element **/
				List<BitSet> allSub = SubElementList.INSTANCE.getSub(closure);
				for(BitSet newClosure : allSub){
					Element e = alphabet.getElementByBitSet(newClosure);
					if(e == null){
						/** e is a new element **/
						e = new Element(newClosure);
						alphabet.addElement(e);
					}
					/** just update the occurrence of it **/
					e.occurAt(seqId, positionId, DATASET);
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
