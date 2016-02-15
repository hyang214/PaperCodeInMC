package util;

import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * author: Hao 
 * date:2015年7月18日
 * time:下午4:29:48
 * purpose:
 */
public class GenedataFormateHelper {
	public static void main(String[] args) {
		try {
			ArrayList<String> list = new ArrayList<String>();
			
			String pathname = "./data/genes/eColi/eColi_B.txt";
			Scanner sc = new Scanner(new File(pathname));
			while(sc.hasNext()){
				String tmp = sc.nextLine();
				String[] tmps = tmp.split("");
				StringBuffer sb = new StringBuffer();
				for(String s : tmps){
					sb.append(s+",");
				}
				sb.deleteCharAt(sb.length()-1);
				list.add(sb.toString());
			}
			sc.close();
			
			PrintStream ps = new PrintStream(new File("./data/genes/eColi/eColi_B_f.txt"));
			System.setOut(ps);
			for(String str: list){
				System.out.println(str);
			}
			ps.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
