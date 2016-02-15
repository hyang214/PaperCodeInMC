package util;

import java.util.BitSet;
import java.util.HashMap;

/**
 * author: Hao 
 * date:2015年3月4日
 * time:下午1:30:57
 * purpose: Store the information of elements 
 */
public class Element {
	/**
	 * value: the value of element
	 * k: the number of intervals
	 * pOccurrence: position occurrence information
	 * p: the possible that this element appear in this data set
	 */
	private final String value;
	private HashMap<Integer, BitSet> pOccurrence;
	public double sup;
	public double p;
	
	public Element(String value) {
		this.value = value;
		this.pOccurrence = new HashMap<Integer, BitSet>();
		this.sup = 0;
		this.p = 0;
	}

	/** add new position occurrence information of this element **/
	public void addPOccurrence(int seqId, int position){
		BitSet bs = pOccurrence.get(seqId);
		if(bs == null){
			bs = new BitSet();
			pOccurrence.put(seqId, bs);
		}
		bs.set(position, true);
		
		p++;
	}
	
	public String getValue() {
		return value;
	}
	
	public HashMap<Integer, BitSet> getpOccurrence() {
		return pOccurrence;
	}
	
	public void printInfo(){
		System.out.println("Element: "+value +" p: "+p);
		System.out.println("  Position Occurrence: ");
		for(Integer key:pOccurrence.keySet()){
			System.out.println("    "+key+": "+pOccurrence.get(key));
		}
	}
	
	public void finalCheck(double sum){
		p = p/sum;
		sup = pOccurrence.size();
	}
	
	@Override
	public Element clone() {
		Element clone = new Element(this.value);
		clone.pOccurrence = MyClone.INSTANCE.clone(this.pOccurrence);
		
		return clone;
	}
}
