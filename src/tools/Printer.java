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
	public static <K,V> void printMap(HashMap<K,V> map){
		for(Object e: map.entrySet()){
			if(e instanceof Entry<?,?>){
				@SuppressWarnings("unchecked")
				Entry<K,V> ee = (Entry<K,V>)e;
				System.out.println("key: " + ee.getKey().toString() + " Value: " + ee.getValue().toString());
			}
		}
	}
}
