package util;

import java.util.BitSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class Node 
{
	public BitSet closed;
	public HashSet<BitSet> generators = new HashSet<BitSet>();
	public BitSet seqIdSet;
	public HashMap<Integer, BitSet> positionSet = new HashMap<Integer, BitSet>();
	private Parameter parameter;
	
	public boolean updatePositionIdsFlag = false;
	
	public Node(){
	}

	public Node(BitSet value, int sid, int pid)
	{
		this.closed = value;
		this.seqIdSet = new BitSet();
		BitSet bitSet = new BitSet();
		
		/* occurrence in sequence */ 
		seqIdSet.set(sid);
		/* the position of occurrence in sequence sid */
		
		bitSet.set(pid);
//		System.out.println(sid+" "+pid);
		positionSet.put(sid, bitSet);
	} 
	
	public Node(BitSet value, BitSet sequenceIdSet, HashMap<Integer,BitSet> positionSet)
	{
		this.closed = value;
		this.seqIdSet = sequenceIdSet;
		this.positionSet = positionSet;
	}
	
	public Node(BitSet value)
	{
		this.closed = (BitSet)value.clone();
		this.seqIdSet = new BitSet();
	}
	
	
	public void occurAtPosition(int sid, int pid)
	{
		
		/* it occured in sid */
		this.seqIdSet.set(sid);
		
		/* if this fellow is already occurred in this sequence, update the existing bitSet */ 
		if(positionSet.containsKey(sid))
			positionSet.get(sid).set(pid);
		/* else create a new BitSet and add it into map */ 
		else
		{
			BitSet bitSet = new BitSet();
			bitSet.set(pid);
			positionSet.put(sid, bitSet);
		}
	}
	
	public BitSet getOccurrenceInSeqId(int seqId)
	{
		if(positionSet.get(seqId) != null)
		{
			return (BitSet)positionSet.get(seqId);
		}
		return new BitSet();
	}
	
	public void print(ItemMap itemMap)
	{
		System.out.print(BitSetToItemSet.INSTANCE.bitSetToItemSet(closed, itemMap));
		BitSet sequenceIdSet = seqIdSet;
		System.out.print(" sup:"+seqIdSet.cardinality());
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
	
	
	public void updateInfo(Node f)
	{
		this.seqIdSet.or(f.seqIdSet);
		
		Set<Integer> positionKeySet = this.positionSet.keySet();
		Set<Integer> fpositionKeySet = f.positionSet.keySet();
		
		for(Integer key : fpositionKeySet)
		{
			if(!positionKeySet.contains(key))
			{
				/* key is a new seqId */
				BitSet bitSet = (BitSet)(f.positionSet.get(key)).clone();
				positionSet.put(key, bitSet);
				
			} 
			else
			{
				/* key is an old seqId */	
				positionSet.get(key).or(f.positionSet.get(key));
			}
		}
	}
	
	public Node clone()
	{
		Node f = new Node((BitSet)this.closed.clone());
		f.seqIdSet = (BitSet)this.seqIdSet.clone();
		f.positionSet = HashMapClone.INSTANCE.hashMapClone1(this.positionSet);
		f.generators = new HashSet<BitSet>();
		for(BitSet g:this.generators){
			f.generators.add((BitSet)g.clone());
		}
		f.parameter = this.parameter;
		return f;
	}
	
	public void setValue(BitSet value) {
		this.closed = value;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((seqIdSet == null) ? 0 : seqIdSet.hashCode());
		result = prime * result + ((closed == null) ? 0 : closed.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Node other = (Node) obj;
		if (seqIdSet == null) {
			if (other.seqIdSet != null)
				return false;
		} else if (!seqIdSet.equals(other.seqIdSet))
			return false;
		if (closed == null) {
			if (other.closed != null)
				return false;
		} else if (!closed.equals(other.closed))
			return false;
		return true;
	}
	
	/*
	 * private methods
	 */
	
}
