package naive;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;

import util.BitSetToItemSet;
import util.ItemMap;
import util.Node;

public class NodeMap {

//	private ArrayList<BitSet> nodeBitOLsit = new ArrayList<BitSet>();
	private HashMap<BitSet,Node> map = new HashMap<BitSet,Node>();
	private ArrayList<Node> nodeList = new ArrayList<Node>();
	public HashMap<Integer, ArrayList<Node>> quickList = new HashMap<Integer, ArrayList<Node>>();
	public ItemMap itemMap;
	
	public void add(Node node){
		//generate index information 
		map.put(node.closed,node);
		buildQuickList(node);
		nodeList.add(node);	
	}

	public boolean contain(BitSet bitSet){
		if(map.containsKey(bitSet))
			return true;
		else
			return false;
	}
	
	public Node getNode(BitSet bs){
		if(map.containsKey(bs))
			return map.get(bs);
		else
			return null;
	}
	
	public void print(){
		for(Node e : nodeList){
			BitSet sequenceIdSet = e.seqIdSet;
			HashMap<Integer,BitSet> positionSet = e.positionSet;
			
			System.out.print(" "+BitSetToItemSet.INSTANCE.bitSetToItemSet(e.closed, itemMap));
			System.out.print(" sup:"+e.seqIdSet.cardinality());
			System.out.print(" instances:{");
			//print the sequenceIds and the position info
			for(int j = 0 ; j < sequenceIdSet.length() ; j ++){
				// if this item set has occurred in j-th sequence
				if(sequenceIdSet.get(j)){
					//print j and the position info of this item set in this sequence
					System.out.print("<"+j+":"+positionSet.get(Integer.valueOf(j)).toString()+"> ");
				}
			}
			System.out.println("}");
		}
	}
	
	public ArrayList<Node> getNodeList()
	{
		return nodeList;
	}
	
	private void buildQuickList(Node node)
	{
		BitSet bitSet = node.closed;
		for (int i = bitSet.nextSetBit(0); i >= 0; i = bitSet.nextSetBit(i+1)) {
			ArrayList<Node> aList = quickList.get(i);
			if(aList == null)
			{
				aList = new ArrayList<Node>();
				quickList.put(i, aList);
			}
			aList.add(node);
		 }
	}

	public void remove(Node e) {
		map.remove(e.closed);
		nodeList.remove(e);
//		removeQuickList(e);
	}

//	private void removeQuickList(Node e) {
//		BitSet bitSet = e.value;
//		for (int i = bitSet.nextSetBit(0); i >= 0; i = bitSet.nextSetBit(i+1)) {
//			ArrayList<Node> aList = quickList.get(i);
//			if(aList.contains(o))
//			{
//				aList = new ArrayList<Node>();
//				quickList.put(i, aList);
//			}
//			aList.add(node);
//		 }
//	}
}
