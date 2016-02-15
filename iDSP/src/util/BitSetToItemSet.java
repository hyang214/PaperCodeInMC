package util;

import java.util.BitSet;

/*
 * author: Hao
 * date:   2014-2-1
 * description:
 * methods:
 */
public enum BitSetToItemSet {
	INSTANCE;
	public String bitSetToItemSet(BitSet b , ItemMap i){
		String result ="{";
		int length = i.getSize();
		for(int k = 0 ; k < length ; k ++){
			//if the position k in bitSet b is true, it means that item k is in this set
			if(b.get(k)){
				result += i.getValue(k)+", ";
			}
		}
		result = result.substring(0, result.length()-2);
		result +="}";
		return result;
	}
}
