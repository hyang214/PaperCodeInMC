package util;

import java.util.BitSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public enum UpdateInfo 
{
	INSTANCE;
	
	public void updateInfoWithoutII(Node to, Node from)
	{
		/* update element to by element from */
		
		/* update sequenceIds information */
		to.seqIdSet.or(from.seqIdSet);
		
		/* update positionSet information */
		Set<Integer> toSeqIdSet = to.positionSet.keySet();
		Set<Integer> fromSeqIdSet = from.positionSet.keySet();
		
		/* update sequenceIds information */
		for(Integer key : fromSeqIdSet)
		{
			if(!toSeqIdSet.contains(key))
			{
				/* key is a new seqID */		
				BitSet bitSet = (BitSet)from.positionSet.get(key).clone();
				to.positionSet.put(key, bitSet);
			}
			else
			{
				/* key is not a new seqID */
				to.positionSet.get(key).or(from.positionSet.get(key));
			}
		}
	}

	public void updateInfoFasterV2(FasterElement to, FasterElement from, FasterElementMap elementMap) {
		/* update element to by element from */
		
		/* update sequenceIds information */
		to.node.seqIdSet.or(from.node.seqIdSet);
		
		/* update positionSet information */
		Set<Integer> toSeqIdSet = to.node.positionSet.keySet();
		Set<Integer> fromSeqIdSet = from.node.positionSet.keySet();
		
		/* update sequenceIds information */
		for(Integer key : fromSeqIdSet)
		{
			if(!toSeqIdSet.contains(key))
			{
				/* key is a new seqID */		
				BitSet bitSet = (BitSet)from.node.positionSet.get(key).clone();
				to.node.positionSet.put(key, bitSet);
			}
			else
			{
				/* key is not a new seqID */
				to.node.positionSet.get(key).or(from.node.positionSet.get(key));
			}
		}
		
		/* update follow elements */
		HashMap<BitSet,NewNode> toMap = elementMap.getElement(to.node.closed).followMap;
		HashMap<BitSet,NewNode> fromMap = elementMap.getElement(from.node.closed).followMap;
		for(BitSet bs : fromMap.keySet()){
			if(toMap.containsKey(bs)){
				/* update the seqIds that 'nn' appears after 'to'*/
				toMap.get(bs).seqIds.or(fromMap.get(bs).seqIds);
			}else{
				/* add 'bs' to the follow set of 'to'*/
				NewNode nn = fromMap.get(bs).clone();
				toMap.put(bs, nn);
			}	
		}
	}
	
	public void updateInfoFasterV3(FasterElement to, FasterElement from) {
		/* update element to by element from */
		
		/* update sequenceIds information */
		to.node.seqIdSet.or(from.node.seqIdSet);
		
		/* update positionSet information */
		Set<Integer> toSeqIdSet = to.node.positionSet.keySet();
		Set<Integer> fromSeqIdSet = from.node.positionSet.keySet();
		
		/* update sequenceIds information */
		for(Integer key : fromSeqIdSet)
		{
			if(!toSeqIdSet.contains(key))
			{
				/* key is a new seqID */		
				BitSet bitSet = (BitSet)from.node.positionSet.get(key).clone();
				to.node.positionSet.put(key, bitSet);
			}
			else
			{
				/* key is not a new seqID */
				to.node.positionSet.get(key).or(from.node.positionSet.get(key));
			}
		}
		
		/* update follow elements */
		HashMap<BitSet,NewNode> toMap = to.followMap;
		HashMap<BitSet,NewNode> fromMap = from.followMap;
		for(BitSet bs : fromMap.keySet()){
			if(toMap.containsKey(bs)){
				/* update the seqIds that 'nn' appears after 'to'*/
				toMap.get(bs).seqIds.or(fromMap.get(bs).seqIds);
			}else{
				/* add 'bs' to the follow set of 'to'*/
				NewNode nn = fromMap.get(bs).clone();
				toMap.put(bs, nn);
			}	
		}
	}
	
	/* only update seqIds */
	public void updateSeqIdsOnly(FasterElement to, FasterElement from) {
		/* update element to by element from */
		/* update sequenceIds information */
		to.node.seqIdSet.or(from.node.seqIdSet);
	}
	public void updateSeqIdsAndPositionIds(FasterElement to, FasterElement from) {
		/* update element to by element from */
		/* update sequenceIds information */
		to.node.seqIdSet.or(from.node.seqIdSet);
		/* update position information */
		for(Integer key : from.node.positionSet.keySet())
		{
			if(!to.node.positionSet.keySet().contains(key))
			{
				/* key is a new seqID */		
				BitSet bitSet = (BitSet)from.node.positionSet.get(key).clone();
				to.node.positionSet.put(key, bitSet);
			}
			else
			{
				/* key is not a new seqID */
				to.node.positionSet.get(key).or(from.node.positionSet.get(key));
			}
		}
	}
	/* only update position Ids */
	public void updatePositionIdsOnly(FasterElement to, FasterElement from) {
		/* update element to by element from */	
		/* update position information */
		for(Integer key : from.node.positionSet.keySet())
		{
			if(!to.node.positionSet.keySet().contains(key))
			{
				/* key is a new seqID */		
				BitSet bitSet = (BitSet)from.node.positionSet.get(key).clone();
				to.node.positionSet.put(key, bitSet);
			}
			else
			{
				/* key is not a new seqID */
				to.node.positionSet.get(key).or(from.node.positionSet.get(key));
			}
		}
	}
	public void updatePositionIdsOnly(Node to, Node from) {
		/* update element to by element from */	
		/* update position information */
		for(Integer key : from.positionSet.keySet())
		{
			if(!to.positionSet.keySet().contains(key))
			{
				/* key is a new seqID */		
				BitSet bitSet = (BitSet)from.positionSet.get(key).clone();
				to.positionSet.put(key, bitSet);
			}
			else
			{
				/* key is not a new seqID */
				to.positionSet.get(key).or(from.positionSet.get(key));
			}
		}
	}
	/* only update follow sets */
	public void updateFollowSetSeqIdsOnly(FasterElement to, FasterElement from) {
		/* update element to by element from */
		/* update follow elements */
		HashMap<BitSet,NewNode> toMap = to.followMap;
		HashMap<BitSet,NewNode> fromMap = from.followMap;
		for(BitSet bs : fromMap.keySet()){
			if(toMap.containsKey(bs)){
				/* update the seqIds that 'nn' appears after 'to'*/
				toMap.get(bs).seqIds.or(fromMap.get(bs).seqIds);
			}else{
				/* add 'bs' to the follow set of 'to'*/
				NewNode nn = fromMap.get(bs).clone();
				toMap.put(bs, nn);
			}	
		}
	}
	/* only update position Ids and follow sets */
	public void updatePositionIdsAndFollowSet(FasterElement to, FasterElement from) {
		/* update element to by element from */
		to.updateFollowSetFlag = true;
		/* update position information */
		for(Integer key : from.node.positionSet.keySet())
		{
			if(!to.node.positionSet.keySet().contains(key))
			{
				/* key is a new seqID */		
				BitSet bitSet = (BitSet)from.node.positionSet.get(key).clone();
				to.node.positionSet.put(key, bitSet);
			}
			else
			{
				/* key is not a new seqID */
				to.node.positionSet.get(key).or(from.node.positionSet.get(key));
			}
		}
		
		/* update follow elements */
		HashMap<BitSet,NewNode> toMap = to.followMap;
		HashMap<BitSet,NewNode> fromMap = from.followMap;
		for(BitSet bs : fromMap.keySet()){
			if(toMap.containsKey(bs)){
				/* update the seqIds that 'nn' appears after 'to'*/
				toMap.get(bs).seqIds.or(fromMap.get(bs).seqIds);
			}else{
				/* add 'bs' to the follow set of 'to'*/
				NewNode nn = fromMap.get(bs).clone();
				toMap.put(bs, nn);
			}	
		}
	}

	public void updateInfoFasterV4(FasterElement to, FasterElement from) {
		/* update element to by element from */
		
		/* update follow elements */
		HashMap<BitSet,NewNode> toMap = to.followMap;
		HashSet<NewNode> toAddSet = new HashSet<NewNode>();
		HashMap<BitSet,NewNode> fromMap = from.followMap;
		for(BitSet bs : fromMap.keySet()){
			for(BitSet pos:toMap.keySet()){
				BitSet tmp = (BitSet)bs.clone();
				tmp.and(pos);
				
				if(tmp.cardinality() > 0){
					if(tmp.equals(pos)){
						toMap.get(pos).seqIds.or(fromMap.get(bs).seqIds);
					}else if(tmp.equals(bs)){
						if(!toMap.containsKey(tmp)){
							
						}
					}
						
				}
			}
			if(toMap.containsKey(bs)){
				/* update the seqIds that 'nn' appears after 'to'*/
				toMap.get(bs).seqIds.or(fromMap.get(bs).seqIds);
			}else{
				/* add 'bs' to the follow set of 'to'*/
				NewNode nn = fromMap.get(bs).clone();
				toMap.put(bs, nn);
			}	
		}
	}

}
