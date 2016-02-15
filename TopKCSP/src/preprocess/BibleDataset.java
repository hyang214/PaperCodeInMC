package preprocess;

import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * author: Hao 
 * date:2015年5月18日
 * time:上午10:11:44
 * purpose: Translate bible text into data set (Not use)
 */
@Deprecated
public class BibleDataset {
	public static void main(String[] args) {
		translate("./data/bible/new v2.txt", "./data/bible/new v3.txt");
		translate("./data/bible/old v2.txt", "./data/bible/old v3.txt");
	}
	
	private static void translate(String path, String outPath){
		ArrayList<String> list = new ArrayList<>();
		
		try {
			Scanner sc = new Scanner(new File(path));
			while(sc.hasNext()){
				String tmp = sc.nextLine();
				String[] tmps = tmp.split("  ");
				if(tmps.length == 1)
					continue;
				String line = tmps[1];
				line = line.replaceAll(" ", ",");
				line = line.toLowerCase();
				list.add(line);
			}
			sc.close();
			
			printInDisk(list, outPath);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static void printInDisk(ArrayList<String> list, String outPath){
		try {
			PrintStream out = new PrintStream(new File(outPath));
			System.setOut(out);
			for(String s : list){
				System.out.println(s);
			}
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
