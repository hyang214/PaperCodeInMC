package idsp;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import util.Combination;
import util.FasterElement;
import util.FasterElementMap;
import util.HashMapClone;
import util.ItemMap;
import util.MyQueue;
import util.NewNode;
import util.Node;
import util.Parameter;
import util.PatternNode;
import util.Sequences;
import util.UpdateInfo;

public class CandidateElementV2 {
	/* index parameter */
	public FasterElementMap posElementMap;
	public FasterElementMap negElementMap;
	public ItemMap itemMap;
	public Parameter parameter;
	
	public CandidateElementV2(Parameter parameter, Sequences sequences) {
		/* Initialization of attributes */
		this.posElementMap = new FasterElementMap();
		this.negElementMap = new FasterElementMap();
		this.itemMap = new ItemMap();
		this.parameter = parameter; 

		/* generate candidate elements*/
		generatePosCandidateElements(sequences);
		generateNegCandidateElements(sequences);
		combinationV2();
		updateSeqIdsFromNeg();
		generateFollowSet();
	}

	private void updateSeqIdsFromNeg() {
		for(FasterElement fe:posElementMap.getElementList()){
			updateSeqIdsInNegOf(fe);
		}
	}

	private void updateSeqIdsInNegOf(FasterElement fe) {
		BitSet posBS = fe.node.closed;
		HashMap<Integer, ArrayList<FasterElement>> updateQuickList = negElementMap.updateQuickList;
		for (int i = posBS.nextSetBit(0); i >= 0; i = posBS.nextSetBit(i + 1)) {
			ArrayList<FasterElement> quickList = updateQuickList.get(i);
			if(quickList == null)
				continue;
			
			for(FasterElement negFE:quickList){
				BitSet tmp = (BitSet) posBS.clone();
				tmp.and(negFE.node.closed);
				
				if(tmp.equals(posBS)){
					/* negFE is the sub element of posFE */
					fe.node.seqIdSet.or(negFE.node.seqIdSet);
					
					/* update positionSet information */
					Set<Integer> toSeqIdSet = fe.node.positionSet.keySet();
					Set<Integer> fromSeqIdSet = negFE.node.positionSet.keySet();
					
					/* update sequenceIds information */
					for(Integer key : fromSeqIdSet)
					{
						if(!toSeqIdSet.contains(key))
						{
							/* key is a new seqID */		
							BitSet bitSet = (BitSet)negFE.node.positionSet.get(key).clone();
							fe.node.positionSet.put(key, bitSet);
						}
						else
						{
							/* key is not a new seqID */
							fe.node.positionSet.get(key).or(negFE.node.positionSet.get(key));
						}
					}
				}
			}
		}
	}

	private void generateFollowSet() {
		for(FasterElement fe:posElementMap.getElementList()){
//			generateFollowSetOf(fe);
			generateFollowSetOfV2(fe);
		}
	}

	private void generateFollowSetOf(FasterElement fe) {
		for(FasterElement next:posElementMap.getElementList()){
			/* check the support with seqIds */
			BitSet seqIds = (BitSet) fe.node.seqIdSet.clone();
			seqIds.and(next.node.seqIdSet);

			/* if the number of common seqIds of fe and next is less than alpha, next cannot be a successor element */
			if(seqIds.cardinality() >= parameter.alpha){
				NewNode nn = new NewNode(next.node);
				fe.followMap.put(next.node.closed, nn);
				nn.seqIds = (BitSet) next.node.seqIdSet.clone();
			}
		}
	}
	
	private void generateFollowSetOfV2(FasterElement fe) {
		for(FasterElement next:posElementMap.getElementList()){
			/* check the support with seqIds */
			BitSet seqIds = (BitSet) fe.node.seqIdSet.clone();
			BitSet successorBS = isSuccessor(fe, next, parameter);
			seqIds.and(successorBS);
			/* if the number of common seqIds of fe and next is less than alpha, next cannot be a successor element */
			if(seqIds.cardinality() >= parameter.alpha){
				NewNode nn = new NewNode(next.node);
				fe.followMap.put(next.node.closed, nn);
				nn.seqIds = successorBS;
			}
		}
	}

	private void combinationV2() {
		HashMap<BitSet,FasterElement> addMap = new HashMap<BitSet,FasterElement>();
		for(FasterElement posFE:posElementMap.getElementList()){
			split(posFE, addMap);
		}
		

		for(BitSet add:addMap.keySet()){
			posElementMap.add(addMap.get(add));
		}
	}

	private void split(FasterElement posFE,HashMap<BitSet,FasterElement> addMap){
		/* divide with negative element instance */
		for(FasterElement negFE:negElementMap.getElementList()){
			if(negFE.node.seqIdSet.cardinality() <= parameter.beta)
				continue;
			
			BitSet tmp = (BitSet) posFE.node.closed.clone();
			if(tmp.equals(negFE.node.closed))
				continue;
			
			tmp.and(negFE.node.closed);
			if(tmp.cardinality() > 0 && !tmp.equals(posFE.node.closed)){
//				System.out.println("add "+tmp+" to "+posFE.node.closed);
				addGenerator(posFE,tmp,addMap);
			}
		}
		/* divide with positive element instance */
		for(FasterElement another:posElementMap.getElementList()){
			if(another == posFE)
				continue;
			
			BitSet tmp = (BitSet) posFE.node.closed.clone();
			if(tmp.equals(another.node.closed))
				continue;
			
			tmp.and(another.node.closed);
			if(tmp.cardinality() > 0 && !tmp.equals(posFE.node.closed)){
//				System.out.println("add "+tmp+" to "+posFE.node.closed);
				addGenerator(posFE,tmp,addMap);
			}
		}
	}
	
	private void addGenerator(FasterElement posFE, BitSet tmp, HashMap<BitSet, FasterElement> addMap) {
		
		/* if tmp is already a generator of posFE, ignore it */
		if(!posFE.node.generators.contains(tmp)){
			/* find all generators in posFE which is subset of tmp */
			HashSet<BitSet> subSet = new HashSet<BitSet>();
			boolean containedFlag = false;
			for(BitSet g:posFE.node.generators){
				BitSet removeG = (BitSet)g.clone();
				removeG.and(tmp);
				if(removeG.equals(g)){
					subSet.add(removeG);
				}
				/* tmp is contained by some generators in posFE, ignore it*/
				else if(removeG.equals(tmp))
					containedFlag = true;
			}
//			System.out.println(1);
			if(containedFlag)
				return;
			/* if the new element already exists, we only need update the generator set of posFE */
			if(!posElementMap.containsElement(tmp) && !addMap.containsKey(tmp)){
				/* generate new element instance */
				Node node = new Node(tmp);
				node.seqIdSet = (BitSet) posFE.node.seqIdSet.clone();
				node.positionSet = HashMapClone.INSTANCE.hashMapClone1(posFE.node.positionSet);
				/* collect generators */
				node.generators.addAll(subSet);
				FasterElement newEI = new FasterElement(node);
				addMap.put(tmp,newEI);
				split(newEI, addMap);
			}
			posFE.node.generators.removeAll(subSet);
			posFE.node.generators.add(tmp);
//			System.out.println(2);
		}
//		System.out.println(3);
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
				
				mq.add(element);
				positionID++;
			}
			
			/* clear the queue */
			mq.clear();
		}

		end = System.currentTimeMillis();
		System.out.println("Time Cost of Build Draft Index: " + (end - start));
		
		updateSeqIdsAndFollowSet(posElementMap);

		removePosImpossible();
		
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
	
	public BitSet isSuccessor(FasterElement front, FasterElement successor, Parameter parameter)
	{
		BitSet frontPosSeqBitSet = (BitSet) front.node.seqIdSet.clone();
		frontPosSeqBitSet.and(parameter.posSeqId);
		BitSet successorPosSeqBitSet = (BitSet) successor.node.seqIdSet.clone();
		successorPosSeqBitSet.and(parameter.posSeqId);
		
		for(int i=frontPosSeqBitSet.nextSetBit(0); i>=0; i=frontPosSeqBitSet.nextSetBit(i+1))
		{
			if(successorPosSeqBitSet.get(i)==true)
			{
				BitSet newPosition = new BitSet();
				for(int j= front.node.positionSet.get(i).nextSetBit(0); j>=0; j= front.node.positionSet.get(i).nextSetBit(j+1))
				{
					for(int k= successor.node.positionSet.get(i).nextSetBit(j+parameter.minGap+1); k>=0; k= successor.node.positionSet.get(i).nextSetBit(k+1))
					{
						if(k<=j+parameter.maxGap+1)
						{
							newPosition.set(k);
						}
						else
							break;
					}
				}
				
				if(newPosition.cardinality()==0)
				{
					frontPosSeqBitSet.clear(i);
				}
			}
			else
			{
				frontPosSeqBitSet.clear(i);
			}
		}

		return frontPosSeqBitSet;
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
	
}
