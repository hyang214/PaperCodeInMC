package preprocess;

import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;

/**
 * author: Hao 
 * date:2015年5月18日
 * time:上午9:32:41
 * purpose: Preprocess the bible text file only with letters
 */
public class BibleLetter {
	public static void main(String[] args) {
		String path = "./data/bible/bbe.txt";
		
		HashSet<String> charSet = charSet(path);
		
		translate("./data/bible/new.txt", "./data/bible/new v2.txt", charSet);
		translate("./data/bible/old.txt", "./data/bible/old v2.txt", charSet);
	}
	
	private static void translate(String path, String outPath, HashSet<String> charSet){
		ArrayList<String> list = new ArrayList<>(); 
		
		File file = new File(path);
		try {
			Scanner sc = new Scanner(file);
			while (sc.hasNext()) {
				String tmp = sc.nextLine();
				for(String s: charSet){
					tmp = tmp.replace(s, "");
				}
				list.add(tmp);
			}
			sc.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		printInDisk(list, outPath);
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
	
	private static HashSet<String> charSet(String path){
		HashSet<String> charSet = new HashSet<>();
		
		File file = new File(path);
		try {
			Scanner sc = new Scanner(file);
			while (sc.hasNext()) {
				String tmp = sc.nextLine();
				String[] tmps = tmp.split("");
				for(String s: tmps){
					if(!Character.isLetter(s.charAt(0))){
						charSet.add(s);
					}
				}
			}
			sc.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		charSet.remove(" ");
		
		for(String s: charSet){
			System.out.println("\""+s+"\"");
		}
		
		return charSet;
	}
}
