package setBaseline;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HandleFile {
	public static int lineNumber = 0;
	public static List<List<String>> readlistlist;
	public static List<String> readlist;
	public static int maxLength = 0;
	
	public Map<String,Map<Integer,List<Integer>>> readText(String filePath) throws Exception {
		Map<String,Map<Integer,List<Integer>>> hashMap = new HashMap<String,Map<Integer,List<Integer>>>();	
		Map<Integer,List<Integer>> map;
		FileReader fr = new FileReader(filePath);
		BufferedReader br = new BufferedReader(fr);
		String s = null;
		readlistlist = new ArrayList<List<String>>();
		while((s = br.readLine()) != null) {
			if(s.length() > maxLength)
			{
				maxLength = s.length();
			}
			readlist = new ArrayList<String>();
			for(int i = 0;i < s.length();i++) {	
				String item = s.charAt(i) + "";
				if(!hashMap.containsKey(item)) {//the first appearance of this item
					List<Integer> list = new ArrayList<Integer>();
					list.add(i);
					map = new HashMap<Integer,List<Integer>>();
					map.put(lineNumber,list);
					hashMap.put(item,map);
				}								
				else {
					if(!hashMap.get(item).containsKey(lineNumber)) {//appearance in another sequence 
						List<Integer> list = new ArrayList<Integer>();
						list.add(i);
						map = new HashMap<Integer,List<Integer>>();
						hashMap.get(item).put(lineNumber,list);							
					}
					else {
						hashMap.get(item).get(lineNumber).add(i);
					}							
				}
				readlist.add(item);
			}
			readlistlist.add(readlist);	
			lineNumber++;
		}
		br.close();
		return hashMap;
	}
}
