package tools;

import java.util.HashMap;
import java.util.Map.Entry;

/**
 * author: Hao 
 * date:Jan 28, 2016
 * time:2:03:42 PM
 * purpose:
 */
public class Printer {
	public static void printMap(HashMap map){
		for(Object e: map.entrySet()){
			Entry ee = (Entry)e;
			System.out.println("key: " + ee.getKey().toString() + " Value: " + ee.getValue().toString());
		}
	}
}
