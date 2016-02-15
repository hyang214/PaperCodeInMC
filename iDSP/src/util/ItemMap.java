package util;

import java.util.ArrayList;
import java.util.HashMap;

public class ItemMap {

	private ArrayList<String> itemLsit = new ArrayList<String>();
	private HashMap<String,Integer> map = new HashMap<String,Integer>();
	
	public void add(String iten)
	{
		map.put(iten, itemLsit.size());
		itemLsit.add(iten);
	}
	
	/* return the index of the item in the itemList by the values of item */
	public int getPosition(String item)
	{
		if(map.containsKey(item))
			return map.get(item);
		else
			return -1;
	}
	
	/* get the values of item in the itemList by the position of item */
	public String getValue(int position)
	{
		return itemLsit.get(position);
	}
	
	//get the size of the map
	public int getSize()
	{
		return itemLsit.size();
	}
	
	public String toString()
	{
		String result = "";
		int count = 0;
		for(String s:itemLsit){
			result += count+":"+s+" ";
			count++;
		}
		return result;
	}
}
