package util;

import java.util.BitSet;
import java.util.HashMap;

/**
 * author: Hao 
 * date:Jan 21, 2016
 * time:4:44:31 PM
 * purpose: mapping element to BitSet
 */
public class ItemMap {
	/****************************************
	 * mapping String to index or vice versa 
	 ****************************************/
	/**
	 * s2i: mapping string 2 integer
	 * i2s: mapping integer 2 string
	 */
	public static HashMap<String, Integer> s2i = new HashMap<>();
	public static HashMap<Integer, String> i2s = new HashMap<>();
	
	/**
	 * translate string 2 integer
	 */
	/** for positive data set **/
	public static int iEncode(String key){
		if(s2i.containsKey(key)){
			return s2i.get(key);
		}else{
			i2s.put(s2i.size(), key);
			s2i.put(key, s2i.size());
			return s2i.size() - 1;
		}
	}
	
	/**
	 * translate integer 2 string
	 */
	public static String iDecode(Integer key){
		String value = i2s.get(key);
		if(value == null){
			System.err.println("Error! Eecode a unexcepted word while key:" + key);
			System.exit(-1);
		}
		return value;
	}
	
	/****************************************
	 * mapping element to BitSet or vice versa 
	 ****************************************/
	/**
	 * translate String 2 BitSet
	 */
	public static BitSet eEncode(String element, int DATASET){
		BitSet bs = new BitSet();
		String[] items = element.split(Parameter.itemSeparator);
		if(DATASET == Parameter.POSITIVE){
			for(String item  : items){
				bs.set(iEncode(item), true);
			}
		}
		else if(DATASET == Parameter.NEGATIVE){
			for(String item  : items){
				/** Pruning rule 1: ignore the item that only occurs in negative data set **/
				if(s2i.containsKey(item)){
					bs.set(iEncode(item), true);					
				}
			}
		}else{
			//TODO error: input unknown DATASET in eEcode
		}
		return bs;
	}
	
	/**
	 * translate BitSet 2 String
	 */
	public static String eDecode(BitSet bs){
		StringBuffer sb = new StringBuffer();
		for(int i = bs.nextSetBit(0) ; i >= 0 ; i = bs.nextSetBit(i+1)){
			sb.append(iDecode(i)+",");
		}
		sb.deleteCharAt(sb.length() - 1);
		return sb.toString();
	}
	
	/**
	 * translate itemMap 2 string
	 */
	public static String staticToString() {
		StringBuffer sb = new StringBuffer();
		sb.append("Item Map: \n");
		for(Integer key : i2s.keySet()){
			sb.append("	<" + key + ", " + i2s.get(key)+"> \n");
		}
		return sb.toString();
	}
}
