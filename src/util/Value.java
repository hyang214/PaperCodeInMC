package util;

import java.util.BitSet;
import java.util.HashSet;
import java.util.Set;

import tools.CloneHelper;

/**
 * author: Hao 
 * date:Jan 5, 2016
 * time:2:52:28 PM
 * purpose:
 */
public class Value {
	/**
	 * closure: the upper of the itemset, namely the element
	 * generators: the set of generators, every generator is a set of items
	 */
	private BitSet closure;
	private HashSet<BitSet> generators;
	
	public Value(BitSet closure){
		this.closure = closure;
	}
	
	/** add new generator to this Value
	 * generator must be sub set of closure
	 **/
	public boolean addGenerator(BitSet generator){
		BitSet add = (BitSet)generator.clone();
		add.and(closure);
		if(add.equals(generator)){
			generators.add(add);
			return true;
		}
		else{
			return false;
		}
	}
	
	/** 
	 * split one closure by one itemset
	 * **/
	public Value split(BitSet splitterBS){
		
		BitSet tmp = (BitSet)splitterBS.clone();
		tmp.and(closure);
		if(tmp.equals(splitterBS)){
			Value splitter = new Value(splitterBS);
			Set<BitSet> removeSet = new HashSet<BitSet>();
			/** collect generators for splitter in the generator set of closure 
			 * and remove those generators from the generator set of closure 
			 * **/
			for(BitSet generator : this.generators){
				/** if a bitSet is the subset of splitterBS, it is generator of splitterBS **/
				BitSet tmpG = (BitSet)generator.clone();
				tmpG.and(splitterBS);
				if(tmpG.equals(generator)){
					splitter.addGenerator(tmpG);
					removeSet.add(generator);
				}
			}
			this.generators.removeAll(removeSet);
			
			return splitter;
		}
		else{
			/** if splitterBS is not the sub set of closure,
			 * the split function is error and should return null
			 *  **/
			return null;
		}
	}

	@Override
	public Value clone() {
		Value clone = new Value((BitSet)this.closure.clone());
		clone.generators = CloneHelper.generatorsClone(this.generators);
		return clone;
	}
	
	/************************************************
	 * Getter and Setter
	 ************************************************/
	public BitSet getClosure() {
		return closure;
	}

	public HashSet<BitSet> getGenerators() {
		return generators;
	}	
	
}
