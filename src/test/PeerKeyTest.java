package test;

import java.util.HashMap;

import tools.Printer;
import util.PeerKey;

/**
 * author: Hao 
 * date:Jan 28, 2016
 * time:2:36:05 PM
 * purpose:
 */
public class PeerKeyTest {
	public static void main(String[] args) {
		PeerKey pk1 = new PeerKey(new Double(0.198), new Double(0.113));
		PeerKey pk2 = new PeerKey(new Double(0.198), new Double(0.113));
		PeerKey pk3 = new PeerKey(new Double(0.238), new Double(0.113));
		PeerKey pk4 = new PeerKey(new Double(0.198), new Double(0.1001));
		PeerKey pk5 = new PeerKey(new Double(0.198), new Double(0.1001));
		PeerKey pk6 = new PeerKey(new Double(0.398), new Double(0.03));
		
		HashMap<PeerKey, String> map = new HashMap<>();
		map.put(pk1, "Test1");
		map.put(pk2, "Test2");
		map.put(pk3, "Test3");
		map.put(pk4, "Test4");
		map.put(pk5, "Test5");
		map.put(pk6, "Test6");
		
		Printer.printMap(map);
	}
}
