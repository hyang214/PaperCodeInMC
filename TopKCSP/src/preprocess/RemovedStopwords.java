package preprocess;

import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;

/**
 * author: Hao 
 * date:2015年5月18日
 * time:上午10:49:34
 * purpose: Remove the stop words from data sets
 */
public class RemovedStopwords {
	public static void main(String[] args) {
		HashSet<String> stopWords = stopWords("./data/bible/stopwords.txt");
		translate("./data/bible/new v2.txt", "./data/bible/new v3.txt", stopWords);
		translate("./data/bible/old v2.txt", "./data/bible/old v3.txt", stopWords);
	}
	
	private static void translate(String path, String outPath, HashSet<String> stopWords){
		ArrayList<String> list = new ArrayList<>();
		
		try {
			Scanner sc = new Scanner(new File(path));
			while(sc.hasNext()){
				String tmp = sc.nextLine();
				String[] tmps = tmp.split("  ");
				if(tmps.length == 1)
					continue;
				String line = tmps[1];
				
				line = line.toLowerCase();
				
				String[] ttmps = line.split(" ");
				StringBuffer sb = new StringBuffer();
				for(String s:ttmps){
					if(!stopWords.contains(s))
						sb.append(s+",");
				}
				list.add(sb.toString());
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
	
	private static HashSet<String> stopWords(String path){
		HashSet<String> stopWords = new HashSet<>();
		try {
			Scanner sc = new Scanner(new File(path));
			while(sc.hasNext()){
				String tmp = sc.nextLine();	
				stopWords.add(tmp);
			}
			sc.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return stopWords;
	}
}
