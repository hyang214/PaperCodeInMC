package statistics;

import java.io.File;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Scanner;

public class TUAWDateProcessor {

	public static void main(String[] args) {
		try {
			File file = new File("./tuaw/processed.csv");// new File("./tuawDEMO.txt");//
			File out = new File("./tuaw/processed_date.csv");
			HashMap<String,String> map = new HashMap<String,String>();
			initmap(map);
			PrintStream ps = new PrintStream(out);
			System.setOut(ps);
			
			Scanner sc = new Scanner(file);
			while(sc.hasNext()){
				String tmp = sc.nextLine();
				process(tmp, map);
			}
			sc.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void process(String tmp, HashMap<String,String> map) {
		String[] attrs = tmp.split(",");
		if(attrs.length <= 2)
			return;
		
		for(int i = 0 ; i < attrs.length ; i ++){
			if(i != 1){
				System.out.print(attrs[i]+",");
			}
			else{
				/* Date */
				String date = attrs[1];
				String[] dateL = date.split(" ");
				int mon = Integer.valueOf(map.get(dateL[0]));
				int day = Integer.valueOf(dateL[1]);
				int year = Integer.valueOf(dateL[2]);
				int newDate = year*10000 + mon*100 + day;
				System.out.print(newDate+",");
			}
		}
		System.out.println();
	}

	private static void initmap(HashMap<String, String> map) {
		map.put("Jan","01");
		map.put("Feb","02");
		map.put("Mar","03");
		map.put("Apr","04");
		map.put("May","05");
		map.put("Jun","06");
		map.put("Jul","07");
		map.put("Aug","08");
		map.put("Sep","09");
		map.put("Oct","10");
		map.put("Nov","11");
		map.put("Dec","12");
	}
}
