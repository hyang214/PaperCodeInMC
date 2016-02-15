package util;

import java.util.BitSet;
import java.util.HashMap;
import java.util.Set;

public enum HashMapClone {
	INSTANCE;
	public HashMap<Integer,BitSet> hashMapClone1(HashMap<Integer,BitSet> origin){
		HashMap<Integer,BitSet> target = new HashMap<Integer,BitSet>();
		
		Set<Integer> key = origin.keySet();
		for(Integer i : key){
			Integer k = new Integer(i);
			BitSet v = (BitSet)origin.get(i).clone();
			target.put(k, v);
		}
		
		return target;
	}
	
	public HashMap<BitSet,Node> hashMapClone2(HashMap<BitSet,Node> origin){
		HashMap<BitSet,Node> target = new HashMap<BitSet,Node>();
		
		Set<BitSet> key = origin.keySet();
		for(BitSet i : key){
			BitSet k = (BitSet)i.clone();
			Node v = origin.get(i).clone();
			target.put(k, v);
		}
		
		return target;
	}
	
	
	public HashMap<BitSet,Integer> hashMapClone3(HashMap<BitSet,Integer> origin){
		HashMap<BitSet,Integer> target = new HashMap<BitSet,Integer>();
		
		Set<BitSet> key = origin.keySet();
		for(BitSet i : key){
			BitSet k = (BitSet)i.clone();
			Integer v = new Integer(origin.get(i));
			target.put(k, v);
		}
		
		return target;
	}
	
//	public <K,V> HashMap<K,V> hashMapClone(HashMap<K,V> origin){
//		HashMap<K,V> cloneMap = new HashMap<K,V>();
//		Iterator<Entry<K, V>> it = origin.entrySet().iterator();
//		while(it.hasNext()){
//			Map.Entry<K, V> entry = it.next();
//			K key = entry.getKey();
//			V value = entry.getValue();
//			cloneMap.put(key, value);
//		}
//		return cloneMap;
//	}
}
