package util;

import java.util.BitSet;

public class NewNode {
	
	public Node node;
	public BitSet seqIds;

	public NewNode(Node node) {
		this.node = node;
		this.seqIds = new BitSet();
	}

	public NewNode clone(){
		NewNode clone = new NewNode(this.node);
		clone.seqIds = (BitSet)this.seqIds.clone();
		return clone;
	}
}
