package util;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class FasterElementMap {

	public HashMap<BitSet,FasterElement> map = new HashMap<BitSet,FasterElement>();
	private ArrayList<FasterElement> elementList = new ArrayList<FasterElement>();
	public ItemMap itemMap;
	
	public HashMap<Integer, ArrayList<FasterElement>> quickList = new HashMap<Integer, ArrayList<FasterElement>>();
	/* updateSet and only store the nature elements */
	/* updateSet is used since V5*/
	public HashSet<FasterElement> updateSet = new HashSet<FasterElement>();
	/* updateQuickList is used since V7 */
	public HashMap<Integer, ArrayList<FasterElement>> updateQuickList = new HashMap<Integer, ArrayList<FasterElement>>();
	
	public FasterElementMap(){
		
	}
	
	public void buildQuickList(FasterElement e)
	{
		BitSet bitSet = e.node.closed;
		for (int i = bitSet.nextSetBit(0); i >= 0; i = bitSet.nextSetBit(i+1)) {
			ArrayList<FasterElement> aList = quickList.get(i);
			if(aList == null)
			{
				aList = new ArrayList<FasterElement>();
				quickList.put(i, aList);
			}
			aList.add(e);
		 }
	}
	
	public void buildUpdateQuickList(FasterElement e)
	{
		BitSet bitSet = e.node.closed;
		for (int i = bitSet.nextSetBit(0); i >= 0; i = bitSet.nextSetBit(i+1)) {
			ArrayList<FasterElement> aList = updateQuickList.get(i);
			if(aList == null)
			{
				aList = new ArrayList<FasterElement>();
				updateQuickList.put(i, aList);
			}
			aList.add(e);
		 }
	}
	
	public void printQuickList()
	{
		Set<Integer> keys = quickList.keySet();
		for(Integer key :keys){
			System.out.println(itemMap.getValue(key)+":");
			ArrayList<FasterElement> list = quickList.get(key);
			for(FasterElement e:list){
				e.setItemMap(itemMap);
				e.print();
			}
		}
	}
	
	public void add(BitSet bitSet, int sid, int pid){
		
		//add element into list
		FasterElement e = new FasterElement(bitSet,sid,pid);
		map.put(bitSet, e);
		elementList.add(e);
		
		/* add to quickList*/
		buildQuickList(e);
		
		/* only these element can use to update other elements */
		updateSet.add(e);
		/* used for quick generate common start-elements */
		buildUpdateQuickList(e);
	}
	
	public void add(FasterElement e){
		map.put(e.node.closed, e);
		elementList.add(e);
		
		/* add to quickList*/
		buildQuickList(e);
	}
	
	public int getSize(){
		return map.size();
	}
	
	public boolean containsElement(BitSet bitSet){
		if(map.containsKey(bitSet))
			return true;
		else
			return false;
	}
	
	public FasterElement getElement(BitSet bitSet)
	{
		if(map.containsKey(bitSet))
			return map.get(bitSet);
		else
			return null;
	}
	
	public void print(){
		System.out.println("Element Instance: ***********");
		for(BitSet bs : map.keySet()){
			FasterElement e = map.get(bs);
			e.setItemMap(itemMap);
			e.print();
		}
	}
	
	public ArrayList<FasterElement> getElementList()
	{
		return elementList;
	}

	public void setItemMap(ItemMap itemMap) 
	{
		this.itemMap = itemMap;
	}
	
	public void remove(FasterElement e){
		map.remove(e.node.closed);
		elementList.remove(e);
	}

	public void remove(HashSet<FasterElement> removeSet) {
		
		for(FasterElement e: removeSet){
			remove(e);
		}
		/* remove quicklist */
		for(Integer key : quickList.keySet()){
			ArrayList<FasterElement> ql = quickList.get(key);
			ql.removeAll(removeSet);
		}
	}
	
	public void printStati(){
		System.out.println("Size of Start-elements:" + elementList.size());
		int fc = 0;
		double size = 0;
		for(FasterElement e:elementList){
			fc += e.followSetSize();
			size += e.node.closed.cardinality();
		}
		System.out.println("Avg size of elements:"+size/elementList.size());
		System.out.println("Size of Follow-elements:" + fc);
		double fcd = fc;
		System.out.println("AVG size of follow-set:"+fcd/elementList.size());
	}
	
//	public ElementMap clone(){
//		ElementMap clone = new ElementMap();
//		for(int i = 0 ;i < this.elementBitLsit.size() ; i ++){
//			BitSet c = (BitSet)this.elementBitLsit.get(i).clone();
//			clone.elementBitLsit.add(c);
//		}
//		clone.map = HashMapClone.INSTANCE.hashMapClone3(this.map);
//		for(int i = 0 ;i < this.elementList.size() ; i ++){
//			Element c = this.elementList.get(i).clone();
//			clone.elementList.add(c);
//		}
//		return clone;
//	}
}
