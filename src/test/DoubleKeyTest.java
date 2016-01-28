package test;

import java.util.HashMap;

import tools.Printer;

/**
 * author: Hao 
 * date:Jan 28, 2016
 * time:1:54:22 PM
 * purpose:
 */
public class DoubleKeyTest {
	public static void main(String[] args) {
		HashMap<Double, String> map = new HashMap<Double, String>();
		map.put(new Double((double)9/(double)10), "Test1");
		map.put(new Double((double)9/(double)10), "Test2");
		map.put(new Double((double)9/(double)10), "Test3");
		map.put(new Double((double)9/(double)11), "Test4");
		map.put(new Double((double)9/(double)12), "Test5");
		map.put(new Double((double)9/(double)12), "Test6");
		
		Printer.printMap(map);
	}
}
