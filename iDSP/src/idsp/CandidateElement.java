package idsp;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import util.Combination;
import util.FasterElement;
import util.FasterElementMap;
import util.ItemMap;
import util.MyQueue;
import util.NewNode;
import util.Node;
import util.Parameter;
import util.Sequences;
import util.UpdateInfo;

public class CandidateElement {
	/* index parameter */
	public FasterElementMap posElementMap;
	public FasterElementMap negElementMap;
	public ItemMap itemMap;
	public Parameter parameter;
	
	public CandidateElement(Parameter parameter, Sequences sequences) {
		/* Initialization of attributes */
		this.posElementMap = new FasterElementMap();
		this.negElementMap = new FasterElementMap();
		this.itemMap = new ItemMap();
		this.parameter = parameter; 

		/* generate candidate elements*/
		generatePosCandidateElements(sequences);
		generateNegCandidateElements(sequences);
		combinationV2();
		combination();
	}

	private void combinationV2() {
	}

	private void combination() {
		
		/* generate new elements which maybe generators in pos*/
		HashSet<FasterElement> posAddSet = new HashSet<FasterElement>();
		HashSet<FasterElement> negAddSet = new HashSet<FasterElement>();
		for(FasterElement negFE:negElementMap.getElementList()){
			for(FasterElement posFE:posElementMap.getElementList()){
				if(posFE.node.closed.equals(negFE.node.closed))
					continue;
				
				BitSet tmp = (BitSet)posFE.node.closed.clone();
				tmp.and(negFE.node.closed);
				
				if(tmp.cardinality() > 0){
					if(tmp.equals(negFE.node.closed)){
						if (!posElementMap.containsElement(tmp)) {
							Node tmpFollowNode = new Node(tmp);
							FasterElement newElement = new FasterElement(tmpFollowNode, itemMap);
							posAddSet.add(newElement);
						}
					}else if(tmp.equals(posFE.node.closed)){
						if (!negElementMap.containsElement(tmp)) {
							Node tmpFollowNode = new Node(tmp);
							FasterElement newElement = new FasterElement(tmpFollowNode, itemMap);
							negAddSet.add(newElement);
						}
					}else{
						if (!posElementMap.containsElement(tmp)) {
							Node tmpFollowNode = new Node(tmp);
							FasterElement newElement = new FasterElement(tmpFollowNode, itemMap);
							posAddSet.add(newElement);
						}
						if (!negElementMap.containsElement(tmp)) {
							Node tmpFollowNode = new Node(tmp);
							FasterElement newElement = new FasterElement(tmpFollowNode, itemMap);
							negAddSet.add(newElement);
						}
					}
				}
			}
		}
		
		/* add new element into elementMap */
		for(FasterElement fe:posAddSet){
			posElementMap.add(fe);
		}
		for(FasterElement fe:negAddSet){
			negElementMap.add(fe);
		}
		
		/* update the seqIds for both pos and neg elements */
		/* only update the seqIds of pos elements */
		updateSeqIdsAndFollowSet(posElementMap);
		/* only the nature elements have the information of follow elements */
		cloneForUpdate(posElementMap);
		removePosImpossible();
		/* only update the seqIds of pos elements */
		updateSeqIdsAndFollowSet(negElementMap);
		/* only the nature elements have the information of follow elements */
		cloneForUpdate(negElementMap);
		
		HashSet<FasterElement> removeNegSet = new HashSet<FasterElement>();
		
		/* add the seqIds from neg to pos in order to check the 1-length patterns */
		for(FasterElement negFE:negElementMap.getElementList()){
			FasterElement posFE = posElementMap.getElement(negFE.node.closed);
			if(posFE != null){
				posFE.node.seqIdSet.or(negFE.node.seqIdSet);
			}else{
				removeNegSet.add(negFE);
			}
		}
		
		for(FasterElement negFE:removeNegSet){
			negElementMap.remove(negFE);
		}
	}
	

	/* get candidate elements*/
	public Combination getIndex() {
		Combination combination = new Combination(posElementMap, negElementMap);
		return combination;
	}

	/* generate CE */
	private void generatePosCandidateElements(Sequences sequences) {
		long start, end;
		start = System.currentTimeMillis();
		
		/* get a queue to store element instance */
		MyQueue mq = new MyQueue(parameter.maxGap);

		for (int seqID = 0; seqID < parameter.posSeqId.cardinality(); seqID ++) {
			int positionID = 0;

			/* get elements from each sequence */
			String[] elements = sequences.instances.get(seqID).split(";");
			/* generate the bitSet of each (start) element */
			ArrayList<BitSet> bitSetList = new ArrayList<BitSet>();
			for (String e : elements) {
				/* generate the bitSet representing element e */
				bitSetList.add(stringToBitSet(e, itemMap));
			}

			for (BitSet current : bitSetList) {
				/* get elements related to tmpBit */
				FasterElement element = posElementMap.getElement(current);

				if (element != null) {
					/* element has already been a start-element, update the instance information */
					if(element.node.seqIdSet.cardinality() == 0){
						/* it means that 'element' is generate by method updateStartSetWithQuickList, and it is also an element that should be added into updateset */
						posElementMap.updateSet.add(element);
						posElementMap.buildUpdateQuickList(element);
					}
					
					element.node.occurAtPosition(seqID, positionID);
				} else {
					/* generate a new start-element, and this element should be added into updateset */
					element = new FasterElement(current, seqID, positionID);
					posElementMap.add(element);
					posElementMap.updateSet.add(element);
					posElementMap.buildUpdateQuickList(element);

					/* generate new start-element which is common of two existed start-element */
//					generateNewCommonStartSetWithQuickList(element);
					/* V2 only check the elements in updateQuickList */
					generateNewCommonStartSetWithQuickListV2(element,posElementMap);
				}

				/* add element into the follow set of the front elements */
				for(int frontI = parameter.minGap ; frontI <= parameter.maxGap ; frontI ++){
					FasterElement frontFE = mq.getFrontFasterElement(frontI);
					if(frontFE == null)
						break;
					
					if(frontFE.followMap.containsKey(current)){
						/* 'element' is already a follow element of 'frontFE' */
						NewNode nn = frontFE.followMap.get(current);
						nn.seqIds.set(seqID, true);
					}else{	
						NewNode nn = new NewNode(element.node);
						nn.seqIds.set(seqID, true);
						frontFE.addFollowSet(nn);
					}	
				}
				
				mq.add(element);
				positionID++;
			}
			
			/* clear the queue */
			mq.clear();
		}

		end = System.currentTimeMillis();
		System.out.println("Time Cost of Build Draft Index: " + (end - start));
		
		updateSeqIdsAndFollowSet(posElementMap);

//		removePosImpossible();
		
		/* use to map bitSet to String */
		posElementMap.itemMap = itemMap;
	}
	private void generateNegCandidateElements(Sequences sequences) {
		long start, end;
		start = System.currentTimeMillis();
		
		/* get a queue to store element instance */
		MyQueue mq = new MyQueue(parameter.maxGap);

		for (int seqID = parameter.posSeqId.cardinality(); seqID < sequences.instances.size(); seqID ++) {
			int positionID = 0;

			/* get elements from each sequence */
			String[] elements = sequences.instances.get(seqID).split(";");
			/* generate the bitSet of each (start) element */
			ArrayList<BitSet> bitSetList = new ArrayList<BitSet>();
			for (String e : elements) {
				/* generate the bitSet representing element e */
				bitSetList.add(stringToBitSet(e, itemMap));
			}

			for (BitSet current : bitSetList) {
				/* get elements related to tmpBit */
				FasterElement element = negElementMap.getElement(current);

				if (element != null) {
					/* element has already been a start-element, update the instance information */
					if(element.node.seqIdSet.cardinality() == 0){
						/* it means that 'element' is generate by method updateStartSetWithQuickList, and it is also an element that should be added into updateset */
						negElementMap.updateSet.add(element);
						negElementMap.buildUpdateQuickList(element);
					}
					
					element.node.occurAtPosition(seqID, positionID);
				} else {
					/* generate a new start-element, and this element should be added into updateset */
					element = new FasterElement(current, seqID, positionID);
					negElementMap.add(element);
					negElementMap.updateSet.add(element);
					negElementMap.buildUpdateQuickList(element);

					/* generate new start-element which is common of two existed start-element */
					generateNewCommonStartSetWithQuickListV2(element,negElementMap);
				}

				/* add element into the follow set of the front elements */
				for(int frontI = parameter.minGap ; frontI <= parameter.maxGap ; frontI ++){
					FasterElement frontFE = mq.getFrontFasterElement(frontI);
					if(frontFE == null)
						break;
					
					if(frontFE.followMap.containsKey(current)){
						/* 'element' is already a follow element of 'frontFE' */
						NewNode nn = frontFE.followMap.get(current);
						nn.seqIds.set(seqID, true);
					}else{	
						NewNode nn = new NewNode(element.node);
						nn.seqIds.set(seqID, true);
						frontFE.addFollowSet(nn);
					}	
				}
				
				mq.add(element);
				positionID++;
			}
			
			/* clear the queue */
			mq.clear();
		}

		end = System.currentTimeMillis();
		System.out.println("Time Cost of Build Draft Index: " + (end - start));
		
		updateSeqIdsAndFollowSet(negElementMap);
		
		/* use to map bitSet to String */
		negElementMap.itemMap = itemMap;
	}

	/* only update the seqIds */
	private void updateSeqIdsAndFollowSet(FasterElementMap elemetMap) {
		long start = System.currentTimeMillis();
		for(FasterElement fe:elemetMap.updateSet){
			updateSeqIdsWithQuickList(fe,elemetMap);
		}
		long end = System.currentTimeMillis();
		System.out.println("Time cost of updateAllFollowSet: "+ (end - start)+" ms");
	}

	/* only update the seqIds */
	private void updateSeqIdsWithQuickList(FasterElement e1, FasterElementMap elemetMap) {
		BitSet eBitSet = (BitSet) e1.node.closed.clone();
		HashMap<Integer, ArrayList<FasterElement>> quickList = elemetMap.quickList;

		/* find the elements in elementMap that have common items with e */
		for (int i = eBitSet.nextSetBit(0); i >= 0; i = eBitSet.nextSetBit(i + 1)) {
			for (FasterElement e2 : quickList.get(i)) {
				if (e2.node.closed.equals(e1.node.closed))
					continue;

				BitSet tmpBitSet = (BitSet) e1.node.closed.clone();
				tmpBitSet.and(e2.node.closed);

				if (tmpBitSet.cardinality() == e2.node.closed.cardinality()
						&& tmpBitSet.equals(e2.node.closed)) {
					/* e1 contains e2 */
					UpdateInfo.INSTANCE.updateInfoFasterV3(e2, e1);
				}
			}
		}
	}
	
	
	/* use update quick list to reduce the search space */
	private void generateNewCommonStartSetWithQuickListV2(FasterElement element, FasterElementMap elemetMap) {
		BitSet eBitSet = element.node.closed;
		
		/* find the elements in updateSet that have common items with e */
		for (int i = eBitSet.nextSetBit(0); i >= 0; i = eBitSet.nextSetBit(i + 1)) {
			for (FasterElement tmpE : elemetMap.updateQuickList.get(i)) {
				if(tmpE.node.closed.equals(eBitSet))
					continue;
				
				BitSet tmpBitSet = (BitSet) tmpE.node.closed.clone();
				tmpBitSet.and(eBitSet);
			
				if (tmpBitSet.cardinality() > 0 && !tmpBitSet.equals(eBitSet)) {
					/* if they have common items and this itemset is not an start element, generate it */
					if (!elemetMap.containsElement(tmpBitSet)) {
						Node tmpFollowNode = new Node(tmpBitSet);
						FasterElement newElement = new FasterElement(tmpFollowNode, itemMap);
						elemetMap.add(newElement);
					}
				}
			}
		}
	}
	
	/* use update quick list to reduce the search space */
	private void generateNewCommonStartSetWithQuickListV2ForNeg(FasterElement element) {
		BitSet eBitSet = element.node.closed;
		
		/* find common for neg */
		for (int i = eBitSet.nextSetBit(0); i >= 0; i = eBitSet.nextSetBit(i + 1)) {
			for (FasterElement tmpE : negElementMap.updateQuickList.get(i)) {
				if(tmpE.node.closed.equals(eBitSet))
					continue;
				
				BitSet tmpBitSet = (BitSet) tmpE.node.closed.clone();
				tmpBitSet.and(eBitSet);
			
				if (tmpBitSet.cardinality() > 0 && !tmpBitSet.equals(eBitSet)) {
					/* if they have common items and this itemset is not an start element, generate it */
					if (!negElementMap.containsElement(tmpBitSet)) {
						Node tmpFollowNode = new Node(tmpBitSet);
						FasterElement newElement = new FasterElement(tmpFollowNode, itemMap);
						negElementMap.add(newElement);
					}
				}
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

	public void prinElement() {
		System.out.println("prinElements~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		posElementMap.setItemMap(itemMap);
		posElementMap.print();
	}

	public void removePosImpossible() {

		long start = System.currentTimeMillis();
		HashSet<FasterElement> removeSet = new HashSet<FasterElement>();
		for(FasterElement e:posElementMap.getElementList()){
			/*remove impossible start element*/
//			System.out.println(e.node.closed+" "+e.node.seqIdSet);
			if(posBelowAlpha(e.node, parameter)){
				removeSet.add(e);
//				System.out.println(" Removed!");
			}
		}

		/* remove in once */
		posElementMap.remove(removeSet);
		/* remove QuickList */
		Set<Integer> keySet = posElementMap.quickList.keySet();
		for(Integer key:keySet){
			ArrayList<FasterElement> list = posElementMap.quickList.get(key);
			list.removeAll(removeSet);
		}
		
		long end = System.currentTimeMillis();
		System.out.println("Time cost of remove Impossible: "+ (end - start)+" ms");
	}
	
	private void cloneForUpdate(FasterElementMap elementMap){
		/* copy the update elements */
		for(Integer I : elementMap.updateQuickList.keySet()){
			ArrayList<FasterElement> copyList = new ArrayList<FasterElement>();
			for(FasterElement fe:elementMap.updateQuickList.get(I)){
				copyList.add(fe.cloneForUpdate());
			}
			elementMap.updateQuickList.put(I, copyList);
		}
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
