package statistics;

import java.io.File;
import java.io.PrintStream;
import java.util.HashSet;
import java.util.Scanner;

public class TUAWPreprocessor {

	public static void main(String[] args) {
		try {
			File file = new File("./tuaw/posts.csv");// new File("./tuawDEMO.txt");//
			File out = new File("./tuaw/processed.csv");
			HashSet<String> set = new HashSet<String>();
			initSet(set);
			PrintStream ps = new PrintStream(out);
			System.setOut(ps);
			
			Scanner sc = new Scanner(file);
			while(sc.hasNext()){
				String tmp = sc.nextLine();
				process(tmp, set);
			}
			sc.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	private static void process(String tmp, HashSet<String> set) {
		String[] attrs = tmp.split(",");
		
		int pos = 1;
		for(int i = 1 ;i < 7; i ++){
			for(String s:set){
				if(attrs[i].contains(s)){
					pos = i;
				}
			}
		}
		String outS = tmp;
		for(int i = 1 ; i < pos ; i ++){
			outS = outS.replaceFirst(",", "");
		}
		System.out.println(outS);
	}
	

	private static void initSet(HashSet<String> set) {
		set.add("Jan");
		set.add("Feb");
		set.add("Mar");
		set.add("Apr");
		set.add("May");
		set.add("Jun");
		set.add("Jul");
		set.add("Aug");
		set.add("Sep");
		set.add("Oct");
		set.add("Nov");
		set.add("Dec");
	}
	
	
}
