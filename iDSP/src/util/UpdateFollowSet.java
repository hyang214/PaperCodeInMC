package util;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.HashSet;

public enum UpdateFollowSet {
	INSTANCE;

	public static void updateFollowSet(FasterElement fe, Parameter parameter,
			FasterElementMap elementMap) {

		/* remove impossible start element */
		// if(posBelowAlpha(fe.node, parameter) && negBelowAlpha(fe.node,
		// parameter)){
		// continue;
		// }

		fe.updateFollowSetFlag = true;

		/* get a list for reduce search space */
		ArrayList<BitSet> list = new ArrayList<BitSet>();
		list.addAll(fe.followMap.keySet());

		/* generate common element */
		HashMap<BitSet, NewNode> addMap = new HashMap<BitSet, NewNode>();

		for (int i = 0; i < list.size(); i++) {
			BitSet b1 = list.get(i);
			for (int j = i + 1; j < list.size(); j++) {

				BitSet b2 = list.get(j);

				BitSet tmp = (BitSet) b1.clone();
				tmp.and(b2);

				/* update according to sub or super relation */
				if (tmp.equals(b1)) {
					/* it means that b1 is sub element of b2, update b1 with b2 */
					fe.followMap.get(b1).seqIds.or(fe.followMap.get(b2).seqIds);
				} else if (tmp.equals(b2)) {
					/* it means that b2 is sub element of b1, update b2 with b1 */
					fe.followMap.get(b2).seqIds.or(fe.followMap.get(b1).seqIds);
				}

				/* generate new follow elements */
				else if (tmp.cardinality() > 0) {
					if (!fe.followMap.containsKey(tmp)
							&& !addMap.containsKey(tmp)) {
						/* generate new follow element */
						if (elementMap.getElement(tmp) == null)
							continue;
						NewNode fn = new NewNode(
								elementMap.getElement(tmp).node);
						fn.seqIds.or(fe.followMap.get(b1).seqIds);
						fn.seqIds.or(fe.followMap.get(b2).seqIds);
						addMap.put(tmp, fn);

						/*
						 * if tmp is also part of another follow element, we
						 * need update the seqIds
						 */
						for (int k = 0; k < list.size(); k++) {
							BitSet another = (BitSet) tmp.clone();
							another.and(list.get(k));

							if (another.equals(tmp)) {
								fn.seqIds
										.or(fe.followMap.get(list.get(k)).seqIds);
							}
						}
					}
				}
			}
		}
		for (NewNode fn : addMap.values()) {
			fe.followMap.put(fn.node.closed, fn);
		}

		/* update the seqIds information of follow elements */

		HashSet<BitSet> removeNN = new HashSet<BitSet>();
		/* remove impossible follow elements */
		for (BitSet bs : fe.followMap.keySet()) {
			/* seqIds: means the sequence that fe appears after e */
			BitSet seqIds = (BitSet) fe.followMap.get(bs).seqIds.clone();
			/* check the support in pos */
			BitSet posSeqIds = (BitSet) seqIds.clone();
			posSeqIds.and(parameter.posSeqId);
			/* check the support in neg */
			BitSet negSeqIds = (BitSet) seqIds.clone();
			negSeqIds.and(parameter.negSeqId);
			/*
			 * if support of a follow element is less than threshold alpha in
			 * both sides, we do not need add it to follow set
			 */
			if (posSeqIds.cardinality() < parameter.alpha
					&& negSeqIds.cardinality() < parameter.alpha) {
				removeNN.add(bs);
			}
		}

		for (BitSet bs : removeNN) {
			fe.followMap.remove(bs);
		}
	}

	/* used since V9, all operation of generating follow set will be done only if we need use the follow set */
	public static void updateFollowSetV2(FasterElement fe, Parameter parameter, FasterElementMap elementMap){
		
		/* the follow set of 'fe' will be update */
		fe.updateFollowSetFlag = true;
		
		/* get a list for reduce search space */
		ArrayList<BitSet> list = new ArrayList<BitSet>();
		list.addAll(fe.followMap.keySet());
		
		/* generate common element */
		HashMap<BitSet,NewNode> addMap = new HashMap<BitSet,NewNode>();
		
		for (int i = 0 ; i < list.size() ; i ++){
			BitSet b1 = list.get(i);
			for(int j = i+1 ; j < list.size() ; j ++){
				
				BitSet b2 = list.get(j);
				
				BitSet tmp = (BitSet)b1.clone();
				tmp.and(b2);
				
				/* update according to sub or super relation */
				if(tmp.equals(b1)){
					/* it means that b1 is sub element of b2, update b1 with b2 */
					fe.followMap.get(b1).seqIds.or(fe.followMap.get(b2).seqIds);
				}else if(tmp.equals(b2)){
					/* it means that b2 is sub element of b1, update b2 with b1 */
					fe.followMap.get(b2).seqIds.or(fe.followMap.get(b1).seqIds);
				}
				
				/* generate new follow elements */
				else if(tmp.cardinality() > 0){
					if(!fe.followMap.containsKey(tmp) && !addMap.containsKey(tmp)){
						/* generate new follow element */
						if(elementMap.getElement(tmp) == null)
							continue;
						NewNode fn = new NewNode(elementMap.getElement(tmp).node);
						fn.seqIds.or(fe.followMap.get(b1).seqIds);
						fn.seqIds.or(fe.followMap.get(b2).seqIds);
						addMap.put(tmp,fn);
						
						/* if tmp is also part of another follow element, we need update the seqIds */
						for(int k = 0 ; k < list.size() ; k ++){
							BitSet another = (BitSet)tmp.clone();
							another.and(list.get(k));
							
							if(another.equals(tmp)){
								fn.seqIds.or(fe.followMap.get(list.get(k)).seqIds);
							}
						}
					}
				}
			}
		}
		for(NewNode fn: addMap.values()){
			fe.followMap.put(fn.node.closed, fn);
		}
		
		/* update the seqIds information of follow elements */
		
		HashSet<BitSet> removeNN = new HashSet<BitSet>();
		/* remove impossible follow elements */
		for(BitSet bs:fe.followMap.keySet()){
			/* seqIds: means the sequence that fe appears after e */
			BitSet seqIds = (BitSet)fe.followMap.get(bs).seqIds.clone();
			/* check the support in pos */
			BitSet posSeqIds = (BitSet)seqIds.clone();
			posSeqIds.and(parameter.posSeqId);
			/* check the support in neg */
			BitSet negSeqIds = (BitSet)seqIds.clone();
			negSeqIds.and(parameter.negSeqId);
			/* if support of a follow element is less than threshold alpha in both sides, we do not need add it to follow set*/
			if(posSeqIds.cardinality() < parameter.alpha){
				removeNN.add(bs);
			}
		}
		
		for(BitSet bs:removeNN){
			fe.followMap.remove(bs);
		}
	}
	public static void updateFollowSetV2ForNeg(FasterElement fe, Parameter parameter, FasterElementMap elementMap){
		
		/* the follow set of 'fe' will be update */
		fe.updateFollowSetFlag = true;
		
		/* get a list for reduce search space */
		ArrayList<BitSet> list = new ArrayList<BitSet>();
		list.addAll(fe.followMap.keySet());
		
		/* generate common element */
		HashMap<BitSet,NewNode> addMap = new HashMap<BitSet,NewNode>();
		
		for (int i = 0 ; i < list.size() ; i ++){
			BitSet b1 = list.get(i);
			for(int j = i+1 ; j < list.size() ; j ++){
				
				BitSet b2 = list.get(j);
				
				BitSet tmp = (BitSet)b1.clone();
				tmp.and(b2);
				
				/* update according to sub or super relation */
				if(tmp.equals(b1)){
					/* it means that b1 is sub element of b2, update b1 with b2 */
					fe.followMap.get(b1).seqIds.or(fe.followMap.get(b2).seqIds);
				}else if(tmp.equals(b2)){
					/* it means that b2 is sub element of b1, update b2 with b1 */
					fe.followMap.get(b2).seqIds.or(fe.followMap.get(b1).seqIds);
				}
				
				/* generate new follow elements */
				else if(tmp.cardinality() > 0){
					if(!fe.followMap.containsKey(tmp) && !addMap.containsKey(tmp)){
						/* generate new follow element */
						if(elementMap.getElement(tmp) == null)
							continue;
						NewNode fn = new NewNode(elementMap.getElement(tmp).node);
						fn.seqIds.or(fe.followMap.get(b1).seqIds);
						fn.seqIds.or(fe.followMap.get(b2).seqIds);
						addMap.put(tmp,fn);
						
						/* if tmp is also part of another follow element, we need update the seqIds */
						for(int k = 0 ; k < list.size() ; k ++){
							BitSet another = (BitSet)tmp.clone();
							another.and(list.get(k));
							
							if(another.equals(tmp)){
								fn.seqIds.or(fe.followMap.get(list.get(k)).seqIds);
							}
						}
					}
				}
			}
		}
		for(NewNode fn: addMap.values()){
			fe.followMap.put(fn.node.closed, fn);
		}
		
		/* update the seqIds information of follow elements */
	}

	private static boolean posBelowAlpha(Node f, Parameter parameter) {
		BitSet b = (BitSet) f.seqIdSet.clone();
		b.and(parameter.posSeqId);
		return (b.cardinality() < parameter.alpha) ? true : false;
	}

	private static boolean negBelowAlpha(Node f, Parameter parameter) {
		BitSet b = (BitSet) f.seqIdSet.clone();
		b.and(parameter.negSeqId);
		return (b.cardinality() < parameter.alpha) ? true : false;
	}
}
