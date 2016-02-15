package idspV2;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import util.Combination;
import util.FasterElement;
import util.FasterElementMap;
import util.ItemMap;
import util.NewNode;
import util.Node;
import util.Parameter;
import util.Pattern;
import util.PatternNode;
import util.ResultSet;

public class CandidatePatternV2 {

	private FasterElementMap posElementMap;
	// private FasterElementMap negElementMap;
	public ResultSet rSet;
	private ItemMap itemMap;
	public static int trycount = 0;
	private HashSet<Pattern> removeHelper;
	private Parameter parameter;

	public CandidatePatternV2(Combination combination) {
		this.posElementMap = combination.pos;
		// this.negElementMap = combination.neg;
		this.itemMap = posElementMap.itemMap;
		this.rSet = new ResultSet();
		this.removeHelper = new HashSet<Pattern>();
	}

	public void generate(Parameter parameter) {
		this.parameter = parameter;
		
		long start = System.currentTimeMillis();

		/* choose an element in the elmementMap as 1-length Pattern and grow */
		long end = System.currentTimeMillis();
		// System.out.println("Time Cost of initWithQuickListV2:"+(end-start));

		/* generate candidate patterns in pos */
		start = System.currentTimeMillis();
		for (FasterElement element : posElementMap.getElementList()) {
			/* pruning */
			if (posBelowAlpha(element.node, parameter)) {
				continue;
			}
			Pattern pattern = new Pattern(new PatternNode(element.node),
					itemMap, parameter);
			Candidate_Gen_Pos(pattern, 0, parameter);
		}
		end = System.currentTimeMillis();
		System.out.println("Time Cost of Generate Pos:" + (end - start));
//		 print("Original");

		/* remove patterns which is not minimum */
		start = System.currentTimeMillis();
		minimalityTest();
		end = System.currentTimeMillis();
		System.out.println("Time Cost of Minimum:" + (end - start));
//		 print("Minimum Test");

		/* merge patterns into accurate and simple way */
		start = System.currentTimeMillis();
		summarize();
		end = System.currentTimeMillis();
		System.out.println("Time Cost of Merging Patterns:" + (end - start));
		// print("Merge Operation");

		System.out.println("Try : " + trycount);
	}

	private void print(String string) {
		System.out.println(string + "***********");
		System.out.println("Pos: *****");
		for (Pattern p : rSet.posResults) {
			p.itemMap = posElementMap.itemMap;
//			p.print();
			p.print3();
		}
		System.out.println("*************");
	}

	/* do minimum test, use length map to reduce the search space */
	private void minimalityTest() {
		ArrayList<Integer> iSet = new ArrayList<Integer>();
		iSet.addAll(rSet.posLengthMap.keySet());
		for (int i = 0; i < iSet.size(); i++) {
			for (int j = i + 1; j < iSet.size(); j++) {
				/* do minimum test with less patterns */
				ArrayList<Pattern> testSet = rSet.posLengthMap.get(iSet.get(j));
				testSet = minimum(rSet.posLengthMap.get(iSet.get(i)), testSet);
			}
		}
		rSet.posResults.removeAll(removeHelper);
	}

	private ArrayList<Pattern> minimum(ArrayList<Pattern> shorterSet,
			ArrayList<Pattern> longerSet) {

		Set<Pattern> remove = new HashSet<Pattern>();

		for (int i = 0; i < shorterSet.size(); i++) {
			for (int j = 0; j < longerSet.size(); j++) {
				Pattern p1 = shorterSet.get(i);
				Pattern p2 = longerSet.get(j);

				if (p1.getSize() == p2.getSize())
					continue;

				/*
				 * a is the shorter pattern b is the longer pattern
				 */
				Pattern a = p1.getSize() < p2.getSize() ? p1 : p2;
				Pattern b = p1.getSize() > p2.getSize() ? p1 : p2;

				boolean flag = false;

				for (int k = 0; k < b.getSize(); k++) {
					if (a.getElementList().get(0).valueEquals(b.getElement(k))) {
						flag = true;
						if (b.getSize() - k < a.getSize()) {
							flag = false;
							break;
						}

						for (int l = 1; l < a.getSize(); l++) {
							if (!a.getElementList().get(l)
									.valueEquals(b.getElement(k + l))) {
								flag = false;
							}
						}
					}
				}

				if (flag == true) {
					// System.out.println("Minimum:  ");
					// a.print();
					// b.print();
					remove.add(b);
				}
			}
		}

		/* to help ignore the removed patterns when doing merge operation */
		removeHelper.addAll(remove);
		longerSet.removeAll(remove);

		return longerSet;
	}

	/* merge pattern */
	private ArrayList<Pattern> merge(ArrayList<Pattern> patternSet) {
		Set<Pattern> mergedSet = new HashSet<Pattern>();
		Set<Pattern> used = new HashSet<Pattern>();
		for (int i = 0; i < patternSet.size(); i++) {
			Pattern p1 = patternSet.get(i).clone();

			boolean changing = true;
			while (changing) {
				changing = false;
				for (int j = 0; j < patternSet.size(); j++) {

					Pattern p2 = patternSet.get(j);

					 Pattern printP = p1.clone();

					Pattern changedP = patternMerge(p1, p2);
					if (changedP != null) {
						/*
						 * this condition means that p2 has been a upper pattern
						 * and was added into merged Set, but it is a lower
						 * pattern now, so we have to remove it from the merged
						 * Set
						 */

						used.add(patternSet.get(i));

						 System.out.println("Merge:  ");
						 printP.print();
						 p2.print();
						 System.out.println("To: ");
						 changedP.print();

						changing = true;
						mergedSet.add(changedP);
					}
				}
			}
		}

		patternSet.removeAll(used);

		ArrayList<Pattern> next = new ArrayList<Pattern>(mergedSet);
		next.addAll(patternSet);

		if (used.size() != 0)
			patternSet = merge(next);
		return patternSet;
	}

	private ArrayList<Pattern> mergeV2(ArrayList<Pattern> patternSet, HashMap<BitSet,Pattern> addMap) {
		boolean continueFlag = false; 
		ArrayList<Pattern> removeSet = new ArrayList<Pattern>();
		ArrayList<Pattern> mergedSet = new ArrayList<Pattern>();
	
		for (int i = 0; i < patternSet.size(); i++) {
			for (int j = 0; j < patternSet.size(); j++) {
				if (i == j)
					continue;

				Pattern p1 = patternSet.get(i).clone();

				Pattern p2 = patternSet.get(j);

				// Pattern printP = p1.clone();

				Pattern changedP = patternMerge(p1, p2);
				if (changedP != null) {
					/*
					 * this condition means that p2 has been a upper pattern and
					 * was added into merged Set, but it is a lower pattern now,
					 * so we have to remove it from the merged Set
					 */

					p1.combination.or(patternSet.get(i).combination);
					p1.combination.or(patternSet.get(j).combination);

					removeSet.add(patternSet.get(i));
//					mergeRemove.add(patternSet.get(j));
					removeSet.add(patternSet.get(j));
					collectAllMerge(p1, patternSet, removeSet);

					int addHelper = 0;
					for (BitSet containedBS : addMap.keySet()) {
						if (containedBS.equals(p1.combination)) {
							break;
						}
						
						BitSet tmp = (BitSet) p1.combination.clone();
						tmp.and(containedBS);
						if (tmp.equals(p1.combination)) {
							/* usedBS is sub set of contained  */
							/* this combination (usedBS) is already be used  */
							break;
						} else if (tmp.equals(containedBS)) {
							/* the pattern with combination of containedBS is covered by new combination usedBS, remove covered pattern  */
							removeSet.add(addMap.get(containedBS));
							addHelper++;
							
//							System.out.println(p1.combination);
//							System.out.println(containedBS);
//							System.out.println("Removed");
//							addMap.get(containedBS).print();
//							System.out.println(addMap.get(containedBS).combination);
//							System.out.println("Above");
//							p1.print();
						}else {
							/* usedBS is a new combination, add it */
							addHelper++;
						}
					}
					if (addHelper == addMap.size()) {
						/* This condition means that the combination of usedBS is not covered by any existed combinations */
						mergedSet.add(p1);
						addMap.put(p1.combination, p1);
						continueFlag = true;
						
//						System.out.println("i:"+i+" j:"+j+" type");
//						System.out.println("Add");
//						System.out.println(p1.combination);
//						p1.print();
					}
				}
			}
		}

		/* this order should not change, some patterns in removeSet are in mergedSet and some in patternSet */
		patternSet.addAll(mergedSet);
		patternSet.removeAll(removeSet);

		if (continueFlag)
			patternSet = mergeV2(patternSet,addMap);
		return patternSet;
	}
	
	private ArrayList<Pattern> mergeV3(ArrayList<Pattern> patternSet, HashMap<BitSet,Pattern> addMap) {
		boolean continueFlag = false; 
		ArrayList<Pattern> removeSet = new ArrayList<Pattern>();
		ArrayList<Pattern> mergedSet = new ArrayList<Pattern>();
	
		for (int i = 0; i < patternSet.size(); i++) {
			for (int j = i+1; j < patternSet.size(); j++) {

				Pattern p1 = patternSet.get(i);

				Pattern p2 = patternSet.get(j);

				// Pattern printP = p1.clone();

				Pattern changedP = patternMergeV2(p1, p2);
				if (changedP != null) {
					/*
					 * this condition means that p2 has been a upper pattern and
					 * was added into merged Set, but it is a lower pattern now,
					 * so we have to remove it from the merged Set
					 */

//					System.out.println("Merging:");
//					patternSet.get(i).print();
//					patternSet.get(j).print();
					
					changedP.combination.or(patternSet.get(i).combination);
					changedP.combination.or(patternSet.get(j).combination);

					removeSet.add(patternSet.get(i));
//					mergeRemove.add(patternSet.get(j));
					removeSet.add(patternSet.get(j));
					collectAllMerge(changedP, patternSet, removeSet);

					int addHelper = 0;
					for (BitSet containedBS : addMap.keySet()) {
						if (containedBS.equals(changedP.combination)) {
//							System.out.println("Contained");
							break;
						}
						
						BitSet tmp = (BitSet) changedP.combination.clone();
						tmp.and(containedBS);
						if (tmp.equals(changedP.combination)) {
							/* usedBS is sub set of contained  */
							/* this combination (usedBS) is already be used  */
							break;
						} else if (tmp.equals(containedBS)) {
							/* the pattern with combination of containedBS is covered by new combination usedBS, remove covered pattern  */
							removeSet.add(addMap.get(containedBS));
							addHelper++;
							
//							System.out.println(p1.combination);
//							System.out.println(containedBS);
//							System.out.println("Removed");
//							addMap.get(containedBS).print();
//							System.out.println(addMap.get(containedBS).combination);
//							System.out.println("Above");
//							p1.print();
						}else {
							/* usedBS is a new combination, add it */
							addHelper++;
						}
					}
					if (addHelper == addMap.size()) {
						/* This condition means that the combination of usedBS is not covered by any existed combinations */
						mergedSet.add(changedP);
						addMap.put(changedP.combination, changedP);
						continueFlag = true;
						
//						System.out.println("i:"+i+" j:"+j+" type");
//						System.out.println("Add");
//						System.out.println(p1.combination);
//						System.out.println("To:");
//						changedP.print();
					}
				}
			}
		}

		/* this order should not change, some patterns in removeSet are in mergedSet and some in patternSet */
		patternSet.addAll(mergedSet);
		patternSet.removeAll(removeSet);

		if (continueFlag)
			patternSet = mergeV3(patternSet,addMap);
		return patternSet;
	}

	private void collectAllMerge(Pattern p1, ArrayList<Pattern> patternSet,
			ArrayList<Pattern> removeSet) {
		boolean flag = true;
		while (flag) {
			flag = false;
			for (int i = 0; i < patternSet.size(); i++) {
				Pattern changedP = patternMerge(p1, patternSet.get(i));
				if (changedP != null) {
					removeSet.add(patternSet.get(i));
//					patternSet.get(i).print();
//					mergeRemove.add(patternSet.get(i));
					p1.combination.or(patternSet.get(i).combination);;
					flag = true;
				}
			}
		}
	}

	/* merge pattern, use length map to reduce the search space */
	private void summarize() {
		// System.out.println("Test*******");
		rSet.posResults.clear();
		for (Integer I : rSet.posLengthMap.keySet()) {
			ArrayList<Pattern> list = rSet.posLengthMap.get(I);
			list.removeAll(removeHelper);
			HashMap<BitSet,Pattern> addMap = new HashMap<BitSet, Pattern>();
//			list = mergeV2(list,addMap);
			list = mergeV3(list,addMap);
//			list = merge(list);
//			list = redun(list);
			rSet.posResults.addAll(list);
		}
	}

	private ArrayList<Pattern> redun(ArrayList<Pattern> list) {
		Set<Pattern> removeSet = new HashSet<Pattern>();
		for (Pattern p1 : list) {
			for (Pattern p2 : list) {
				if (p1.equals(p2))
					continue;

				boolean reFlag = true;
				for (int i = 0; i < p1.getSize(); i++) {
					if (!patternNodecontains(p1.getElement(i), p2.getElement(i))) {
						reFlag = false;
					}
				}

				/* every elements of p2 is contains (equal) by p1 */
				if (reFlag) {
					trycount++;
					System.out.println("Re:  ");
					p1.print();
					System.out.println(p1.combination);
					p2.print();
					System.out.println(p2.combination);
					System.out.println("Nice!  ");
					removeSet.add(p2);
				}
			}
		}
		list.removeAll(removeSet);
		return list;
	}

	/* pn1 contains pn2 */
	private boolean patternNodecontains(PatternNode pn1, PatternNode pn2) {
		/* check closed */
		boolean closedFlag = false;
		if (!pn1.closed.closed.equals(pn2.closed.closed)) {
			BitSet tmp = (BitSet) pn1.closed.closed.clone();
			tmp.and(pn2.closed.closed);
			if (tmp.equals(pn2.closed.closed)) {
				if (!pn1.generatorSet.contains(tmp))
					closedFlag = true;
			}
		} else {
			closedFlag = pn1.generatorSet.containsAll(pn2.generatorSet)
					&& pn2.generatorSet.containsAll(pn1.generatorSet);
		}

		return closedFlag;
	}

	private void Candidate_Gen_Pos(Pattern pattern, int layer,
			Parameter parameter) {
		/* pruning */
		if (pattern.posSeqBitSet.cardinality() < parameter.alpha)
			return;

		/* if this candidate is already a pattern, add it into the patternMap */
		if (pattern.negSeqBitSet.cardinality() <= parameter.beta) {
			// pattern.posSeqBitSet.cardinality() >= parameter.alpha &&
			rSet.addPosPattern(pattern);
			pattern.combination.set(rSet.posResults.size(), true);
			return;
		}

		/*
		 * else continue grow pattern: get value of the latest element of this
		 * pattern, get this element in posIndex
		 */
		FasterElement nextElement = posElementMap.getElement(pattern
				.getLatestELement());
		if (nextElement != null) {
			/* find all potential closed patterns */
			generateFollowSetOfV2(nextElement);
			for (NewNode next : nextElement.getFollowNodeList()) {
				/* pruning */
				if (posBelowAlpha(pattern, next, parameter))
					continue;

				Pattern p = pattern.clone();
				/* if closed is a legal grow in this candidate */
				if (p.addPosElement(new PatternNode(next.node), parameter)) {
					if (p.posSeqBitSet.cardinality() < parameter.alpha)
						continue;
					Candidate_Gen_Pos(p, layer + 1, parameter);
				}
			}
		}
		return;
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

	private boolean posBelowAlpha(Node f, Parameter parameter) {
		BitSet b = (BitSet) f.seqIdSet.clone();
		b.and(parameter.posSeqId);
		return (b.cardinality() < parameter.alpha) ? true : false;
	}

	private boolean posBelowAlpha(Pattern pattern, NewNode f,
			Parameter parameter) {
		BitSet b = (BitSet) f.node.seqIdSet.clone();
		b.and(f.seqIds);
		b.and(pattern.posSeqBitSet);
		b.and(parameter.posSeqId);
		return (b.cardinality() < parameter.alpha) ? true : false;
	}

	public Pattern patternMerge(Pattern p1, Pattern p2) {
		BitSet p1B = new BitSet();
		p1B.set(0, p1.getSize(), true);
		BitSet p2B = new BitSet();
		p2B.set(0, p2.getSize(), true);

		/*
		 * only the patterns which has n-1 common patternNode and one different
		 * patternNode can merge
		 */
		boolean oneDifference = false;
		int k = 0;
		for (int l = 0; l < p2.getSize(); l++) {
			int value = p1.getElement(k).compare(p2.getElement(l));
			if (value == 0) {
				/* p1.getElement(k) has no relationship with p2.getElement(l) */
				break;
			} else if (value == 1) {
				/* p1.getElement(k) equals with p2.getElement(l) */
				p1B.set(k, false);
				p2B.set(l, false);
				k++;
			} else if (value == 2) {
				if (oneDifference)
					break;

				/* p1.getElement(k) is sub item set of p2.getElement(l) */
				p1B.set(k, false);
				oneDifference = true;
				k++;
			} else {
				if (oneDifference)
					break;

				/* p2.getElement(l) is sub item set of p1.getElement(k) */
				p2B.set(l, false);
				oneDifference = true;
				k++;
			}
		}

		if (p2B.cardinality() == 0 && p1B.cardinality() == 0)
			return null;

		// if(p1B.cardinality() == 0)
		// {
		// /* p1 merges with p2, keep p2*/
		// System.out.println("Merge:  ");
		// p1.print();
		// p2.print();
		// p2.mergePattern(p1);
		// System.out.println("To:");
		// p2.print();
		// return p2;
		// }
		if (p2B.cardinality() == 0) {
			/* p2 merges with p1, keep p1 */
			// System.out.println("Merge:  ");
			// p1.print();
			// p2.print();
			p1.mergePattern(p2);
			// System.out.println("To:");
			// p1.print();
			return p1;
		}

		return null;
	}
	
	public Pattern patternMergeV2(Pattern p1, Pattern p2) {
		BitSet p1B = new BitSet();
		p1B.set(0, p1.getSize(), true);
		BitSet p2B = new BitSet();
		p2B.set(0, p2.getSize(), true);

		/*
		 * only the patterns which has n-1 common patternNode and one different
		 * patternNode can merge
		 */
		boolean oneDifference = false;
		int k = 0;
		for (int l = 0; l < p2.getSize(); l++) {
			int value = p1.getElement(k).compare(p2.getElement(l));
			if (value == 0) {
				/* p1.getElement(k) has no relationship with p2.getElement(l) */
				break;
			} else if (value == 1) {
				/* p1.getElement(k) equals with p2.getElement(l) */
				p1B.set(k, false);
				p2B.set(l, false);
				k++;
			} else if (value == 2) {
				if (oneDifference)
					break;

				/* p1.getElement(k) is sub item set of p2.getElement(l) */
				p1B.set(k, false);
				oneDifference = true;
				k++;
			} else {
				if (oneDifference)
					break;

				/* p2.getElement(l) is sub item set of p1.getElement(k) */
				p2B.set(l, false);
				oneDifference = true;
				k++;
			}
		}

		if (p2B.cardinality() == 0 && p1B.cardinality() == 0)
			return null;

		 if(p1B.cardinality() == 0)
		 {
			 /* p1 merges with p2, keep p2*/
//			 System.out.println("Merge:  ");
//			 p1.print();
//			 p2.print();
			 Pattern r = p2.clone();
			 r.mergePattern(p1);
//			 System.out.println("To:");
//			 p2.print();
			 return r;
		 }
		if (p2B.cardinality() == 0) {
			/* p2 merges with p1, keep p1 */
			// System.out.println("Merge:  ");
			// p1.print();
			// p2.print();
			 Pattern r = p1.clone();
			r.mergePattern(p2);
			// System.out.println("To:");
			// p1.print();
			return r;
		}

		return null;
	}
}
