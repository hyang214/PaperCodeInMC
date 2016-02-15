package statistics;

import java.io.File;
import java.util.HashSet;
import java.util.Scanner;

/**
 * author: Hao 
 * date:Oct 31, 2015
 * time:9:13:17 PM
 * purpose:
 */
public class RawTest {
	public static void main(String[] args) {
		try {
			Scanner sc = new Scanner(new File("./dblp4/DM.txt"));
			String tmp = "";
			int max = Integer.MIN_VALUE;
			while(sc.hasNext()){
				String[] itemsets = sc.nextLine().split(";");
				for(String itemset : itemsets){
					String[] items = itemset.split(",");
					HashSet<String> set = new HashSet<String>();
					for(String item : items){
						set.add(item);
					}
					if(set.size() > max){
						max = items.length;
						tmp = itemset;
					}
				}
			}
			System.out.println(tmp);
			System.out.println(max);
			sc.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
