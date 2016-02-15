package naive;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.HashSet;

import util.BitSetToItemSet;
import util.ItemMap;
import util.Node;
import util.Parameter;
import util.Sequences;

public class NaiveIndexer {
	
	/* index parameter */
	public NodeMap nodeMap = new NodeMap();
	public ItemMap itemMap;
	private Parameter parameter;
	int create = 0;
	int mapping = 0;

	public NaiveIndexer(Parameter parameter, Sequences sequences) {
		this.itemMap = new ItemMap();
		this.parameter = parameter;
//		initItemMap(sequences);
		generatePosCandidateElements(sequences);
		generateNegCandidateElements(sequences);
		System.out.println("create: "+create);
		System.out.println("mapping: "+mapping);
	}
	
	private void initItemMap(Sequences sequences) {
		for (int seqID = 0; seqID < sequences.instances.size(); seqID++) {
			String[] elements = sequences.instances.get(seqID).split(";");

			for (String e : elements) {
				/* generate the bitSet representing element e */
				stringToBitSet(e, itemMap);
			}
		}
	}

	public NodeMap getIndex() {
		return nodeMap;
	}

	/* generate Candidate Elements */
	private void generatePosCandidateElements(Sequences sequences) {
		long start, end;
		start = System.currentTimeMillis();

		for (int seqID = 0; seqID < parameter.posSeqId.cardinality(); seqID++) {
			/* get elements from each sequence */
			String[] elements = sequences.instances.get(seqID).split(";");

			/* generate the bitSet of each (start) element and add it to elementMap */
			int positionID = 0;

			ArrayList<BitSet> bitSetList = new ArrayList<BitSet>();
			for (String e : elements) {
				/* generate the bitSet representing element e */
				bitSetList.add(stringToBitSet(e, itemMap));
			}

			for (BitSet tmpBit : bitSetList) {
				/* generate all sub element of tmpBit*/
				ArrayList<BitSet> subList = SubElementList.INSTANCE.getSub(tmpBit);
				
				/* create or update elements related bs*/
				for(BitSet bs:subList){
					Node node = nodeMap.getNode(bs);
					
					if( node != null){
						mapping ++;
						node.occurAtPosition(seqID, positionID);
					}else{
						create ++;
						node = new Node(bs, seqID, positionID);
						nodeMap.add(node);
					}
				}
				positionID++;
			}
		}
		end = System.currentTimeMillis();
		System.out.println("Time Cost of Build Index: " + (end - start));

	}
	
	private void generateNegCandidateElements(Sequences sequences) {
		
		for (int seqID = parameter.posSeqId.cardinality(); seqID < sequences.instances.size(); seqID++) {
			/* get elements from each sequence */
			String[] elements = sequences.instances.get(seqID).split(";");

			/* generate the bitSet of each (start) element and add it to elementMap */
			int positionID = 0;

			ArrayList<BitSet> bitSetList = new ArrayList<BitSet>();
			for (String e : elements) {
				/* generate the bitSet representing element e */
				bitSetList.add(stringToBitSet(e, itemMap));
			}

			for (BitSet tmpBit : bitSetList) {
				/* generate all sub element of tmpBit*/
				ArrayList<BitSet> subList = SubElementList.INSTANCE.getSub(tmpBit);
				
				/* create or update elements related bs*/
				for(BitSet bs:subList){
					Node node = nodeMap.getNode(bs);
					
					if( node != null){
						mapping ++;
						node.occurAtPosition(seqID, positionID);
					}
				}
				positionID++;
			}
		}
	}

	private BitSet stringToBitSet(String element, ItemMap itemMap) {
		String[] items = element.split(",");
		BitSet tmpBit = new BitSet();
		for (String i : items) {
			int idx = itemMap.getPosition(i);
			/* if the item is not already in the itemList */
			if (idx < 0) {
				itemMap.add(i);
				tmpBit.set(itemMap.getPosition(i));
			} else
				tmpBit.set(idx);
		}

		return tmpBit;
	}

	public void printIndexerBaseline(){
		for(Node node:nodeMap.getNodeList()){
			System.out.print(" "+BitSetToItemSet.INSTANCE.bitSetToItemSet(node.closed, itemMap)+"  ");
			BitSet sequenceIdSet = node.seqIdSet;
			HashMap<Integer,BitSet> positionSet = node.positionSet;
			System.out.print(" sup:"+node.seqIdSet.cardinality());
			System.out.print(" instances:{");
			//print the sequenceIds and the position info
			for(int j = 0 ; j < sequenceIdSet.length() ; j ++){
				// if this item set has occurred in j-th sequence
				if(sequenceIdSet.get(j)){
					//print j and the position info of this item set in this sequence
					System.out.print("<"+j+":"+positionSet.get(Integer.valueOf(j)).toString()+"> ");
				}
			}
			System.out.println("}");
		}
	}

	public void removeImpossible() {
		HashSet<Node> removeSet = new HashSet<Node>();
		for(Node e:nodeMap.getNodeList()){
			/*remove impossible start element*/
			if(posBelowAlpha(e, parameter) && negBelowAlpha(e, parameter))
				removeSet.add(e);
			
//			System.out.println("After: "+ e.followMap.size());
		}
//		System.out.println("Removing~ :"+elementMap.getSize());
//		System.out.println("RemoveSet :"+removeSet.size());
		for(Node e: removeSet){
			nodeMap.remove(e);
		}
//		System.out.println("Removing~ :"+elementMap.getSize());
	}
	
	private boolean posBelowAlpha(Node f, Parameter parameter)
	{
		BitSet b = (BitSet)f.seqIdSet.clone();
		b.and(parameter.posSeqId);
		return (b.cardinality() < parameter.alpha)? true : false;
	}
	
	private boolean negBelowAlpha(Node f, Parameter parameter)
	{
		BitSet b = (BitSet)f.seqIdSet.clone();
		b.and(parameter.negSeqId);
		return (b.cardinality() < parameter.alpha)? true : false;
	}
}
