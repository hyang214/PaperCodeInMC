package util;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.Set;

public class FasterElement 
{
	public Node node;
	/* the set of elements that can be appended to seqElement satisfying gap constraint */
	public HashMap<BitSet,NewNode> followMap = new HashMap<BitSet, NewNode>();   
	private ItemMap itemMap;
	
	/* status flag helper */
	public boolean generatedBySearchFE = false;
	public boolean updateFollowSetFlag = false;
	
	//
	public FasterElement(BitSet value, int sid, int pid)
	{
		// construct indexNode
		this.node = new Node(value, sid, pid);
	}
	
	public FasterElement(Node indexNode)
	{
		this.node = indexNode.clone();
		generatedBySearchFE = true;
	}
	
	public FasterElement(Node node, ItemMap itemMap) 
	{
		this.node = node.clone();
		this.itemMap = itemMap;
	}

	public FasterElement() {
		/* only used in V9, generate a clone for updateQuickList */
	}

	public void addFollowSet(NewNode fn)
	{
		followMap.put(fn.node.closed, fn);
//		fNList.add(indexNode);
	}
	
	public boolean containsFollow(BitSet bitSet)
	{
//		System.out.println(followSet.containsKey(bitSet));
		return followMap.containsKey(bitSet);
	}
	
	public NewNode getFollowNode(BitSet bitSet)
	{
		return followMap.get(bitSet);
	}
	
	public int followSetSize()
	{
		return followMap.size();
	}
	
	public void print()
	{
		System.out.print("[{");
		for(BitSet ge:node.generators){
			System.out.print(BitSetToItemSet.INSTANCE.bitSetToItemSet(ge, itemMap)+", ");
		}
		System.out.print("}, "+BitSetToItemSet.INSTANCE.bitSetToItemSet(node.closed, itemMap)+"]");
		BitSet sequenceIdSet = node.seqIdSet;
		HashMap<Integer,BitSet> positionSet = node.positionSet;
		System.out.print(" sup:"+node.seqIdSet.cardinality());
		System.out.print(" instances:{");
		//print the sequenceIds and the position info
		for(int j = 0 ; j < sequenceIdSet.length() ; j ++){
			// if this item set has occurred in j-th sequence
			if(sequenceIdSet.get(j)){
				//print j and the position info of this item set in this sequence
//				System.out.print("<"+j+"> ");
				System.out.print("<"+j+":"+positionSet.get(Integer.valueOf(j)).toString()+"> ");
			}
		}
		System.out.println("}");
		System.out.println("   followSet.size: "+followMap.size());
		printFollow();
	}
	
	public void print2(){
		System.out.print(node.closed.toString());
		BitSet sequenceIdSet = node.seqIdSet;
		HashMap<Integer,BitSet> positionSet = node.positionSet;
		System.out.print("\\{");
		//print the sequenceIds and the position info
		for(int j = 0 ; j < sequenceIdSet.length() ; j ++){
			// if this item set has occurred in j-th sequence
			if(sequenceIdSet.get(j)){
				//print j and the position info of this item set in this sequence
				System.out.print(j+":"+positionSet.get(Integer.valueOf(j)).toString()+" ");
			}
		}
		System.out.println("\\}");
		printFollow2();
	}
	
	public void printFollow(){
		Set<BitSet> h = followMap.keySet();
//		System.out.println("followNode print");
		for(BitSet i:h){
			System.out.print("   "+BitSetToItemSet.INSTANCE.bitSetToItemSet(i, itemMap));
			NewNode tempF = followMap.get(i);
			BitSet sequenceIdSet = tempF.seqIds;
			HashMap<Integer,BitSet> positionSet = tempF.node.positionSet;
			System.out.print(" sup:"+tempF.seqIds.cardinality()+" {");
			//print the sequenceIds and the position info
			for(int j = 0 ; j < sequenceIdSet.length() ; j ++){
				// if this item set has occurred in j-th sequence
				if(sequenceIdSet.get(j)){
					//print j and the position info of this item set in this sequence
					System.out.print("<"+j+"> ");
//					System.out.print("<"+j+":"+positionSet.get(Integer.valueOf(j)).toString()+"> ");
				}
			}
			System.out.println("}");
		}
		System.out.println();
	}

	public void printFollow2(){
		Set<BitSet> h = followMap.keySet();
		for(BitSet i:h){
			System.out.print("   -"+i.toString()+"{");
			NewNode tempF = followMap.get(i);
			BitSet sequenceIdSet = tempF.node.seqIdSet;
			HashMap<Integer,BitSet> positionSet = tempF.node.positionSet;
			//print the sequenceIds and the position info
			for(int j = 0 ; j < sequenceIdSet.length() ; j ++){
				// if this item set has occurred in j-th sequence
				if(sequenceIdSet.get(j)){
					//print j and the position info of this item set in this sequence
					System.out.print(j+":"+positionSet.get(Integer.valueOf(j)).toString()+" ");
				}
			}
			System.out.println("}");
		}
		System.out.println();
	}
	
	public HashMap<BitSet, NewNode> getFollowSet() 
	{
		return followMap;
	}

	public void setItemMap(ItemMap itemMap) 
	{
		this.itemMap = itemMap;
	}
	
	/* only want keep a original version of follow set for nature elements in order to update elements */
	public FasterElement cloneForUpdate(){
		FasterElement e = new FasterElement();
		e.node = this.node;
		e.followMap = (HashMap<BitSet,NewNode>)this.followMap.clone(); 
		e.itemMap = this.itemMap;
		return e;
	}
	
	public ArrayList<NewNode> getFollowNodeList(){
		ArrayList<NewNode> fNList = new ArrayList<NewNode>();
		Set<BitSet> keySet = followMap.keySet();
		for(BitSet b : keySet){
			fNList.add(followMap.get(b));
		}
		return fNList;
	}
	
	public String toString(){
		StringBuffer sb = new StringBuffer();
		sb.append(BitSetToItemSet.INSTANCE.bitSetToItemSet(node.closed, itemMap));
//		sb.append("{");
//		BitSet sequenceIdSet = indexNode.sequenceIdSet;
//		HashMap<Integer,BitSet> positionSet = indexNode.positionSet;
//		//print the sequenceIds and the position info
//		for(int j = 0 ; j < sequenceIdSet.length() ; j ++){
//			// if this item set has occurred in j-th sequence
//			if(sequenceIdSet.get(j)){
//				//print j and the position info of this item set in this sequence
//				sb.append(j+":"+positionSet.get(Integer.valueOf(j)).toString()+" ");
//			}
//		}
//		sb.append("}");
		return new String(sb);
	}
}
