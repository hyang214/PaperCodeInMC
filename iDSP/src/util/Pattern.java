package util;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;

public class Pattern
{
	/* combination is used to merge patterns */
	public BitSet combination = new BitSet();
	
	public ArrayList<PatternNode> elementList;
	public BitSet posSeqBitSet;
	public BitSet negSeqBitSet;
	public HashMap<Integer, BitSet> positionSet;
	public ItemMap itemMap;
	private Parameter parameter;

	public Pattern(PatternNode nextNode, ItemMap itemMap, Parameter parameter)
	{

		this.itemMap = itemMap;

		/* add element to the pattern */
		this.elementList = new ArrayList<>();
		this.elementList.add(nextNode);

		/* update the occurrence of this pattern in sequenceIds */
		this.posSeqBitSet = (BitSet) nextNode.closed.seqIdSet.clone();
		this.posSeqBitSet.and(parameter.posSeqId);
		this.negSeqBitSet = (BitSet) nextNode.closed.seqIdSet.clone();
		this.negSeqBitSet.and(parameter.negSeqId);
		
		/* generate ARRAY of this pattern */
		this.positionSet = HashMapClone.INSTANCE.hashMapClone1(nextNode.closed.positionSet);

	}

	public Pattern(ItemMap itemMap, Parameter parameter)
	{
		
		this.posSeqBitSet = new BitSet();
		this.negSeqBitSet = new BitSet();
		this.positionSet = new HashMap<Integer, BitSet>();
		this.parameter = parameter;
		this.itemMap = itemMap;
	}

	public boolean addElement(PatternNode nextNode, Parameter parameter)
	{
		for(int i=this.posSeqBitSet.nextSetBit(0); i>=0; i=this.posSeqBitSet.nextSetBit(i+1))
		{
			if(nextNode.closed.seqIdSet.get(i)==true)
			{
				BitSet newPosition = new BitSet();
				for(int j=this.positionSet.get(i).nextSetBit(0); j>=0; j=this.positionSet.get(i).nextSetBit(j+1))
				{
					for(int k=nextNode.closed.positionSet.get(i).nextSetBit(j+parameter.minGap+1); k>=0; k=nextNode.closed.positionSet.get(i).nextSetBit(k+1))
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
					this.positionSet.remove(i);
					this.posSeqBitSet.clear(i);
				}
				else
					this.positionSet.put(i, newPosition);
			}
			else
			{
				this.positionSet.remove(i);
				this.posSeqBitSet.clear(i);
			}
		}
		
		
		for(int i=this.negSeqBitSet.nextSetBit(0); i>=0; i=this.negSeqBitSet.nextSetBit(i+1))
		{
			if(nextNode.closed.seqIdSet.get(i)==true)
			{
				BitSet newPosition = new BitSet();
				for(int j=this.positionSet.get(i).nextSetBit(0); j>=0; j=this.positionSet.get(i).nextSetBit(j+1))
				{
					for(int k=nextNode.closed.positionSet.get(i).nextSetBit(j+parameter.minGap+1); k>=0; k=nextNode.closed.positionSet.get(i).nextSetBit(k+1))
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
					this.positionSet.remove(i);
					this.negSeqBitSet.clear(i);
				}
				else
					this.positionSet.put(i, newPosition);
			}
			else
			{
				this.positionSet.remove(i);
				this.negSeqBitSet.clear(i);
			}
		}
		
		/* add next element to pattern */
		elementList.add(nextNode);

		return true;
	}
	
	public boolean addPosElement(PatternNode nextNode, Parameter parameter)
	{
		for(int i=this.posSeqBitSet.nextSetBit(0); i>=0; i=this.posSeqBitSet.nextSetBit(i+1))
		{
			if(nextNode.closed.seqIdSet.get(i)==true)
			{
				BitSet newPosition = new BitSet();
				for(int j=this.positionSet.get(i).nextSetBit(0); j>=0; j=this.positionSet.get(i).nextSetBit(j+1))
				{
					for(int k=nextNode.closed.positionSet.get(i).nextSetBit(j+parameter.minGap+1); k>=0; k=nextNode.closed.positionSet.get(i).nextSetBit(k+1))
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
					this.positionSet.remove(i);
					this.posSeqBitSet.clear(i);
				}
				else
					this.positionSet.put(i, newPosition);
			}
			else
			{
				this.positionSet.remove(i);
				this.posSeqBitSet.clear(i);
			}
		}
		
		/* pruning */
		if(this.posSeqBitSet.cardinality() < parameter.alpha)
			return false;
		
		for(int i=this.negSeqBitSet.nextSetBit(0); i>=0; i=this.negSeqBitSet.nextSetBit(i+1))
		{
			if(nextNode.closed.seqIdSet.get(i)==true)
			{
				BitSet newPosition = new BitSet();
				for(int j=this.positionSet.get(i).nextSetBit(0); j>=0; j=this.positionSet.get(i).nextSetBit(j+1))
				{
					for(int k=nextNode.closed.positionSet.get(i).nextSetBit(j+parameter.minGap+1); k>=0; k=nextNode.closed.positionSet.get(i).nextSetBit(k+1))
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
					this.positionSet.remove(i);
					this.negSeqBitSet.clear(i);
				}
				else
					this.positionSet.put(i, newPosition);
			}
			else
			{
				this.positionSet.remove(i);
				this.negSeqBitSet.clear(i);
			}
		}
		
		/* add next element to pattern */
		elementList.add(nextNode);

		return true;
	}
	
	public boolean addNegElement(PatternNode nextNode, Parameter parameter)
	{
		
		for(int i=this.negSeqBitSet.nextSetBit(0); i>=0; i=this.negSeqBitSet.nextSetBit(i+1))
		{
			if(nextNode.closed.seqIdSet.get(i)==true)
			{
				BitSet newPosition = new BitSet();
				for(int j=this.positionSet.get(i).nextSetBit(0); j>=0; j=this.positionSet.get(i).nextSetBit(j+1))
				{
					for(int k=nextNode.closed.positionSet.get(i).nextSetBit(j+parameter.minGap+1); k>=0; k=nextNode.closed.positionSet.get(i).nextSetBit(k+1))
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
					this.positionSet.remove(i);
					this.negSeqBitSet.clear(i);
				}
				else
					this.positionSet.put(i, newPosition);
			}
			else
			{
				this.positionSet.remove(i);
				this.negSeqBitSet.clear(i);
			}
		}
		
		/* pruning */
		if(this.negSeqBitSet.cardinality() < parameter.alpha)
			return false;
		
		for(int i=this.posSeqBitSet.nextSetBit(0); i>=0; i=this.posSeqBitSet.nextSetBit(i+1))
		{
			if(nextNode.closed.seqIdSet.get(i)==true)
			{
				BitSet newPosition = new BitSet();
				for(int j=this.positionSet.get(i).nextSetBit(0); j>=0; j=this.positionSet.get(i).nextSetBit(j+1))
				{
					for(int k=nextNode.closed.positionSet.get(i).nextSetBit(j+parameter.minGap+1); k>=0; k=nextNode.closed.positionSet.get(i).nextSetBit(k+1))
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
					this.positionSet.remove(i);
					this.posSeqBitSet.clear(i);
				}
				else
					this.positionSet.put(i, newPosition);
			}
			else
			{
				this.positionSet.remove(i);
				this.posSeqBitSet.clear(i);
			}
		}
		
		/* add next element to pattern */
		elementList.add(nextNode);

		return true;
	}

	public ArrayList<PatternNode> getElementList()
	{
		return elementList;
	}

	public BitSet getLatestELement()
	{
		return elementList.get(elementList.size() - 1).closed.closed;
	}

	public String getValue()
	{
		String value = "";
		for (PatternNode f : elementList)
		{
			value += BitSetToItemSet.INSTANCE.bitSetToItemSet(f.closed.closed, itemMap) + " ";
		}
		return value;
	}

	public void print2()
	{
		for(PatternNode f : elementList)
		{
			System.out.print(" <closed: ");
			System.out.print(BitSetToItemSet.INSTANCE.bitSetToItemSet(f.closed.closed, itemMap));
			System.out.print("  generators: ");
			for(BitSet b : f.generatorSet)
			{
				System.out.print(BitSetToItemSet.INSTANCE.bitSetToItemSet(b, itemMap));
			}
			System.out.print(">");
		}
		StringBuffer re = new StringBuffer();
		re.append(" SUP_IN_POS:" + posSeqBitSet.cardinality());
		re.append(" SUP_IN_NEG:" + negSeqBitSet.cardinality());
		System.out.println(new String(re));
	}

	public void print()
	{
		for(PatternNode f : elementList)
		{
			System.out.print(" <closed: ");
			System.out.print(BitSetToItemSet.INSTANCE.bitSetToItemSet(f.closed.closed, itemMap));
			System.out.print("  generators: ");
			for(BitSet b : f.generatorSet)
			{
				System.out.print(BitSetToItemSet.INSTANCE.bitSetToItemSet(b, itemMap));
			}
			System.out.print(">");
		}
		StringBuffer re = new StringBuffer();
		re.append(" SUP_IN_POS:" + posSeqBitSet.cardinality());
		re.append(" SUP_IN_NEG:" + negSeqBitSet.cardinality());
		System.out.println(new String(re));
	}
	
	public void print3()
	{
		System.out.print("* ");
		for(PatternNode f : elementList)
		{
			System.out.print("[{");
			for(BitSet ge:f.generatorSet){
				System.out.print(BitSetToItemSet.INSTANCE.bitSetToItemSet(ge, itemMap)+", ");
			}
			System.out.print("}, "+BitSetToItemSet.INSTANCE.bitSetToItemSet(f.closed.closed, itemMap)+"]  ");
		}
		StringBuffer re = new StringBuffer();
		re.append(" SUP_IN_POS:" + posSeqBitSet.cardinality());
		re.append(" SUP_IN_NEG:" + negSeqBitSet.cardinality());
		System.out.println(new String(re));
	}
	
	public void print4()
	{
		StringBuffer re = new StringBuffer();
		re.append("* ");
		int contrast = posSeqBitSet.cardinality() - negSeqBitSet.cardinality();
		re.append(" contrast: "+contrast+"  ");
		for(PatternNode f : elementList)
		{
			re.append("[{");
			for(BitSet ge:f.generatorSet){
				re.append(BitSetToItemSet.INSTANCE.bitSetToItemSet(ge, itemMap)+", ");
			}
			if(re.charAt(re.length()-1) == ',')
				re = re.deleteCharAt(re.length()-1);
			re.append("}, "+BitSetToItemSet.INSTANCE.bitSetToItemSet(f.closed.closed, itemMap)+"]  ");
		}
		re.append(" Pos: "+posSeqBitSet.cardinality()+" Neg: "+negSeqBitSet.cardinality());
		System.out.println(new String(re));
	}
	
	public Pattern clone()
	{
		Pattern p = new Pattern(this.itemMap, this.parameter);
		ArrayList<PatternNode> target = new ArrayList<PatternNode>();
		for (int i = 0; i < this.elementList.size(); i++)
		{
			PatternNode tmpFN = this.elementList.get(i).clone();
			target.add(tmpFN);
		}
		p.elementList = target;
		p.posSeqBitSet = (BitSet) this.posSeqBitSet.clone();
		p.negSeqBitSet = (BitSet) this.negSeqBitSet.clone();
		p.positionSet = HashMapClone.INSTANCE.hashMapClone1(this.positionSet);
		p.itemMap = this.itemMap;
		p.combination = (BitSet)this.combination.clone();
		return p;
	}

	public String toString()
	{
		StringBuffer re = new StringBuffer();
		re.append("Value: " + getValue());
		re.append(" SUP_IN_POS:" + posSeqBitSet.toString());
		re.append(" SUP_IN_NEG:" + negSeqBitSet.toString());
		return new String(re);
	}

	public int getSize()
	{
		return elementList.size();
	}

	public Pattern mergePattern(Pattern p)
	{
		/* keep  */
		int size = getSize();
		for(int i = 0 ; i < size ; i ++)
		{
			this.elementList.get(i).mergeNode(p.elementList.get(i));
		}
		return this;
	}
	
	public PatternNode getElement(int i)
	{
		return elementList.get(i);
	}
	
	
}
