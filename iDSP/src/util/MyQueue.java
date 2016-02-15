package util;

import java.util.ArrayList;

public class MyQueue {

	public ArrayList<FasterElement> queue;
	private int size;
	
	public MyQueue(int maxGap) {
		this.size = maxGap+1;
		queue = new ArrayList<FasterElement>();
	}
	
	public void add(FasterElement fe){
		if(queue.size() >= size){
			queue.remove(0);
		}
		queue.add(fe);
	}
	
	public FasterElement getFrontFasterElement(int gap){
		int index = queue.size()-1-gap;
		if( index >= 0)
			return queue.get(index);
		else
			return null;
	}
	
	public void clear(){
		queue.clear();
	}
}
