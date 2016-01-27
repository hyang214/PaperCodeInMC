package util;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;


/**
 * author: Hao 
 * date:Jan 21, 2016
 * time:10:52:12 AM
 * purpose:
 */
public class CloneHelper {

	/**
	 * clone the hashMap of occurrence
	 * @param occurrences
	 * @return the clone
	 */
	public static HashMap<Integer, BitSet> occurrenceClone(HashMap<Integer, BitSet> occurrences) {
		HashMap<Integer, BitSet> clone = new HashMap<>();
		for(Entry<Integer, BitSet> entry : occurrences.entrySet()){
			Integer key = new Integer(entry.getKey());
			BitSet value = (BitSet)entry.getValue();
			clone.put(key, value);
		}
		return clone;
	}
	
	/**
	 * copy the value list of pattern, not deeply clone
	 * @param valueList
	 * @return the clone
	 */
	public static ArrayList<Value> valueListCopy(ArrayList<Value> valueList) {
		ArrayList<Value> list = new ArrayList<>();
		for(Value c : valueList){
			list.add(c);
		}
		return list;
	}

	/**
	 * clone the value list of pattern, 
	 * @param valueList
	 * @return the clone
	 */
	public static ArrayList<Value> valueListClone(ArrayList<Value> valueList) {
		ArrayList<Value> list = new ArrayList<>();
		for(Value c : valueList){
			list.add(c.clone());
		}
		return list;
	}

	/**
	 * clone the generators set of Value object
	 * @param generators
	 * @return the clone
	 */
	public static HashSet<BitSet> generatorsClone(HashSet<BitSet> generators) {
		HashSet<BitSet> clone = new HashSet<>();
		for(BitSet generator : generators){
			clone.add((BitSet)generator.clone());
		}
		return clone;
	}
}
