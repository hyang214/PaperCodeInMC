package util;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.HashSet;

/**
 * author: Hao 
 * date:2015年3月19日
 * time:下午3:00:42
 * purpose: The pattern we want in Project: Top-K contrast sequential pattern 
 */
public class Pattern {
	
	public double posSup;
	public double negSup;
	public double cRatio;
	
	public ArrayList<String> posList;
	public HashMap<Integer, BitSet> posOccurrence; 
	public HashMap<Integer, BitSet> negOccurrence; 
	private final double posSize;
	private final double negSize;
	
	public Pattern(Element posE, Element negE, double posSize, double negSize) {
		/** positive side **/
		this.posList = new ArrayList<String>();
		posList.add(posE.getValue());
		this.posOccurrence = MyClone.INSTANCE.clone(posE.getpOccurrence());
		
		/** negative side **/
		if(negE != null)
			this.negOccurrence = MyClone.INSTANCE.clone(negE.getpOccurrence());
		else{
			this.negOccurrence = new HashMap<Integer, BitSet>();
		}
		
		this.posSize = posSize;
		this.negSize = negSize;
	
		/** support update **/
		supportUpdate();
	}

	/** the method is only used in clone method **/
	private Pattern(double posSize, double negSize){
		this.posSize = posSize;
		this.negSize = negSize;
		
		this.posSup = 0;
		this.negSup = 0;
		this.cRatio = 0;		
	}
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		
		sb.append("Value: [");
		for(String e:posList){
			sb.append(e+" ");
		}
		sb.append("]\n");
		
		sb.append(" CRatio: "+ cRatio);
		sb.append(" posSup: " + posSup);
		sb.append(" negSup: " + negSup);
		
		return sb.toString();
	}
	
	public void printMore(){
		StringBuffer sb = new StringBuffer();
		
		sb.append("*Value: [");
		for(String e:posList){
			sb.append(e+" ");
		}
		sb.append("]\n");
		
		sb.append(" CRatio: "+ cRatio);
		sb.append(" posSup: " + posSup);
		sb.append(" negSup: " + negSup);
		
		System.out.println(sb);
		System.out.println("Positive:");
		for(Integer key:posOccurrence.keySet()){
			System.out.println(" "+key+" "+posOccurrence.get(key));
		}
		System.out.println("Negative:");
		for(Integer key:negOccurrence.keySet()){
			System.out.println(" "+key+" "+negOccurrence.get(key));
		}
	}
	
	@Override
	public Pattern clone(){
		Pattern clone = new Pattern(this.posSize, this.negSize);
		clone.posSup = this.posSup;
		clone.negSup = this.negSup;
		clone.cRatio = this.cRatio;
		clone.posList = MyClone.INSTANCE.clone(this.posList);
		clone.posOccurrence = MyClone.INSTANCE.clone(this.posOccurrence);
		clone.negOccurrence = MyClone.INSTANCE.clone(this.negOccurrence);
		
		return clone;
	}

	public void addNewElement(Element posE, Element negE, int minGap, int maxGap) {
//		System.out.println("***************");
		/** update the positive side **/
//		System.out.println("pos~~");
		posList.add(posE.getValue());
		addNewElement(posE, minGap, maxGap, posOccurrence);
		
		/** pruning in AddNewElement **/
		if(posOccurrence.size() == 0){
			posSup = 0;
			cRatio = Double.MIN_VALUE;
//			printMore();
//			System.out.println("pruning in AddNewElement ");
			return;
		}
			
		/** update the negative side **/
//		System.out.println("neg~~");
		if(negE != null)
			addNewElement(negE, minGap, maxGap, negOccurrence);
		else{
			negOccurrence.clear();
		}
		
		/** update the support information **/
		supportUpdate();
		
		//TODO print 
//		printMore();
	}
	
	private void addNewElement(Element nextE, int minGap, int maxGap, HashMap<Integer, BitSet> occurrence) {
		
		/** combine the occurrence of current and nextE **/
		HashSet<Integer> removeSet = new HashSet<Integer>();
 		for(Integer key: occurrence.keySet()){
			if(nextE.getpOccurrence().containsKey(key)){
				BitSet mask = new BitSet();
				BitSet cOccurrence = occurrence.get(key);
				for (int i = cOccurrence.nextSetBit(0); i >= 0; i = cOccurrence.nextSetBit(i+1)) {
					int fromIndex = i + minGap + 1;
					int toIndex = i + maxGap + 2;
				    mask.set(fromIndex, toIndex, true);
				 }
				
				mask.and(nextE.getpOccurrence().get(key));
				if(mask.cardinality() > 0)
					occurrence.put(key, mask);
				else
					removeSet.add(key);
				
//				System.out.println("  "+key+" from: "+ cOccurrence +" " + nextE.getpOccurrence().get(key) + " to "+ mask);
			}else{
				removeSet.add(key);
			}
		}
 		
 		/** remove empty BitSet and its key in the hashMap **/
 		for(Integer key: removeSet){
 			occurrence.remove(key);
 		}
	}

	private void supportUpdate() {
//		this.posSup = posOccurrence.size();
//		this.negSup = negOccurrence.size();
		this.posSup = posOccurrence.size() / posSize;
		this.negSup = negOccurrence.size() / negSize;
		this.cRatio = posSup - negSup;
	}
}
