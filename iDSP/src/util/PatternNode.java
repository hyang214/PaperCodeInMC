package util;

import java.util.BitSet;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

public class PatternNode
{
	public Node closed;
	public Set<BitSet> generatorSet;
	
	public PatternNode(Node node)
	{
		this.closed = node;
		this.generatorSet = new HashSet<BitSet>();
	}
	
	public void checkAndaddGenerate(Node node)
	{
		BitSet value = (BitSet)closed.closed.clone();
		value.and(node.closed);
		if(value.cardinality()>0 && value.equals(node.closed)){
			generatorSet.add(node.closed);
		}
	}
	
	public PatternNode clone()
	{
		PatternNode pn = new PatternNode(closed.clone());
		Set<BitSet> tmpSet = new HashSet<BitSet>();
		for(BitSet tmpB  : generatorSet)
		{
			tmpSet.add((BitSet)tmpB.clone());
		}
		pn.generatorSet = tmpSet;
		return pn;
	}
	
	public void addGenerator(BitSet newGenerator)
	{
		/* collect old generators that has been covered by new generator */
		Vector<BitSet> drop = new Vector<BitSet>();

		boolean add = true;
		for(BitSet tmpG:generatorSet)
		{
			BitSet tmpB = (BitSet)tmpG.clone();
			tmpB.and(newGenerator);
			if(tmpB.cardinality() > 0 && tmpB.equals(newGenerator))
			{
				/* generate is sub item set of tmpG*/ 
				add = false;
				break;
			}
			else if(tmpB.cardinality() > 0 && tmpB.equals(tmpG))
			{
				/* tmpG is sub item set of generate*/
				drop.add(tmpG);
				add = true;
			}
			else{
				add = true;
			}
		}
		
		if(add == true)
			generatorSet.add(newGenerator);
		
		/* remove old generators that has been covered by new generator */
		for(BitSet b : drop)
			generatorSet.remove(b);
	}
	
	public boolean valueEquals(PatternNode pn)
	{
		/* check closed */
		boolean closedFlag = closed.closed.equals(pn.closed.closed);
		boolean generatorFlag = this.generatorSet.containsAll(pn.generatorSet) && pn.generatorSet.containsAll( this.generatorSet);
		
		return closedFlag && generatorFlag;
		
	}
	

	
	public int compare(PatternNode pn)
	{
		if(this.valueEquals(pn))
		{
			/* the item set of two PatternNode.closed are the same*/
			/* if the generateSet are the same*/
			if(compareGenSet(pn.generatorSet))
				return 1;
			/* if the generateSet are not the same*/
			return 0;
		}
		else
		{
			BitSet bs = (BitSet)this.closed.closed.clone();
			bs.and(pn.closed.closed);
			if(bs.equals(this.closed.closed))
			{
				/* the item set of this is sub item set of pn and also one of the pn.generateSet*/
				if(pn.generatorSet.contains(this.closed.closed))
					return 2;
				else
					return 0;
			}
			else if(bs.equals(pn.closed.closed))
			{
				/* the item set of pn is sub item set of this and also one of the this.generateSet*/
				if(this.generatorSet.contains(pn.closed.closed))
					return 3;
				else
					return 0;
			}
		}
		/* other condition*/
		return 0;
	}
	
	public int compare2(PatternNode pn)
	{
		if(this.valueEquals(pn))
		{
			/* the item set of two PatternNode.closed are the same*/
			/* if the generateSet are the same*/
			if(compareGenSet(pn.generatorSet))
				return 1;
			/* if the generateSet are not the same*/
			return 0;
		}
		else
		{
			BitSet bs = (BitSet)this.closed.closed.clone();
			bs.and(pn.closed.closed);
			if(bs.equals(this.closed.closed))
			{
				/* the item set of this is sub item set of pn and the this.generateSet are covered by pn.generateSet*/
				if(generateCovered(this.generatorSet, pn.generatorSet))
					return 2;
				else
					return 0;
			}
			else if(bs.equals(pn.closed.closed))
			{
				/* the item set of pn is sub item set of this and the pn.generateSet are covered by this.generateSet*/
				if(generateCovered(pn.generatorSet, this.generatorSet))
					return 3;
				else
					return 0;
			}
		}
		/* other condition*/
		return 0;
	}

	public void mergeNode(PatternNode pn)
	{		
		if(this.valueEquals(pn))
			return;
		
		this.generatorSet.remove(pn.closed.closed);
		Set<BitSet> removeSet = new HashSet<BitSet>();
		for(BitSet pnGenerate : pn.generatorSet)
		{
			for(BitSet generate : this.generatorSet)
			{	
				BitSet tmpGen = (BitSet)pnGenerate.clone();
				tmpGen.and(generate);
				/* pnGenerate has been covered by generate*/
				if(tmpGen.cardinality() > 0 && tmpGen.equals(pnGenerate) && !tmpGen.equals(generate)) 
					removeSet.add(pnGenerate);
			}
		}
		this.generatorSet.addAll(pn.generatorSet);
		this.generatorSet.removeAll(removeSet);
	}
	
	public boolean compareGenSet(Set<BitSet> gSet)
	{
		boolean flag = true;
		for(BitSet tmpB : this.generatorSet)
		{
			if(!gSet.contains(tmpB))
				flag = false;
		}
		for(BitSet tmpB : gSet)
		{
			if(!this.generatorSet.contains(tmpB))
				flag = false;
		}
		return flag;
	}
	
	public boolean generateCovered(Set<BitSet> coveredSet, Set<BitSet> coveringSet)
	{

		int size = coveredSet.size();
		for(BitSet covered : coveredSet)
		{
			boolean coveredB = false;
			for(BitSet covering : coveringSet)
			{
				BitSet a = (BitSet)covered.clone();
				a.and(covering);
				if(a.equals(covered))
					coveredB = true;
			}
			if(coveredB)
				size -- ;
		}
		
		return size == 0 ? true:false;
	}
	
	
}
