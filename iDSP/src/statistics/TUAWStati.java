package statistics;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Scanner;

public class TUAWStati {
	
	public static void main(String[] args) {
		
		try {
			/*
			 * bSet: the name set of bloggers, we have 34 bloggers.
			 * size_of_dataset: size of data set
			 * num_of_blogger: number of bloggers
			 * records: 35 bloggers, 
			 * 			3 years: 2004, 2005 and 2006, 
			 * 			12 month every year, 
			 * 			31 days every month.
			 */
			HashMap<String, Integer> bMap = new HashMap<String, Integer>();
			int size_of_dataset = 0;
			int num_of_blogger = 0;
			int[][][][] records = new int[34][3][12][31];
			
			
			File file = new File("./tuaw/processed_date.csv");// new File("./tuawDEMO.txt");//

			size_of_dataset = bloggerCounter(bMap, file); 
			num_of_blogger = bMap.size();
//			printMap(bMap);
			
			System.out.println("Size of Dataset: "+ size_of_dataset);
			System.out.println("Number of Bloggers: "+ num_of_blogger);
			
//			HashMap<String,HashMap<String,HashMap<String,Integer>>> map = new HashMap<String,HashMap<String,HashMap<String,Integer>>>();
			
			Scanner sc = new Scanner(file);
			
//			int count = 0;
			while(sc.hasNext()){
				String tmp = sc.nextLine();
				String[] attrs = tmp.split(",");
				
				/* Date */
				String date = attrs[1];
				int dateD = Integer.valueOf(attrs[1]);
				int year = dateD / 10000 - 2004;
				int month = (dateD % 10000) / 100 - 1;
				int day = (dateD % 100) - 1;
				
				/* Blogger */
				String blogger = attrs[2];
//				System.out.println(blogger+" "+bMap.get(blogger));
				records[bMap.get(blogger)][year][month][day]++;
//				System.out.println(count+" "+blogger+" : "+year+"~"+month+"~"+day);
//				count ++;
			}
			sc.close();
			
			printRecord(bMap,records);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	private static void printMap(HashMap<String, Integer> bMap) {
		for(String key: bMap.keySet()){
			System.out.println(key+" : " + bMap.get(key));
		}
	}


	private static void printRecord(HashMap<String, Integer> bMap, int[][][][] records) {
		System.out.println("Total*******************");
//		bloggerInTotal(bMap,records);
		System.out.println("Year*******************");
//		bloggerInYear(bMap,records);
		System.out.println("Month*******************");
		bloggerInMonth(bMap,records);
		System.out.println("Year dis*******************");
//		blogsInYear(records);
		System.out.println("Month dis*******************");
//		blogsInMonth(records);
	}

	private static void blogsInYear(int[][][][] records) {
		int[] count = {0,0,0};
		int c = 0;
		for(int b = 0 ; b < 34; b ++){
			for(int y = 0 ; y < 3 ; y++){
				for(int m = 0 ; m < 12; m ++){
					for(int d = 0 ; d < 31 ; d ++){
						count[y] += records[b][y][m][d];
					}
				}
			}
		}
		for(int i = 0 ; i < 3 ;i ++){
			c+=count[i];
			System.out.println((i+2004)+","+count[i]);
		}
		System.out.println("Sum:"+c);
	}
	
	private static void blogsInMonth(int[][][][] records) {
		int[] count = new int[36];
		for(int b = 0 ; b < 34; b ++){
			for(int y = 0 ; y < 3 ; y++){
				for(int m = 0 ; m < 12; m ++){
					for(int d = 0 ; d < 31 ; d ++){
						count[y*12+m] += records[b][y][m][d];
					}
				}
			}
		}
		for(int i = 0 ; i < 36 ;i ++){
			System.out.println((2004+i/12)+"."+(i%12+1)+","+count[i]);
		}
		int sum = 0 ;
		for(int i = 0 ; i < 36 ;i ++){
			sum += count[i];
		}
		System.out.println("Sum: "+sum);
	}


	private static void bloggerInTotal(HashMap<String, Integer> bMap, int[][][][] records) {
		int sum = 0;
		for(Entry<String,Integer> entry:bMap.entrySet()){
			String key = entry.getKey();
			int value = entry.getValue();
			int count = 0; 
			for(int y = 0 ; y < 3 ; y++){
				for(int m = 0 ; m < 12; m ++){
					for(int d = 0 ; d < 31 ; d ++){
						count += records[value][y][m][d];
					}
				}
			}
			sum+=count;
			System.out.println(key+","+count);
		}
		System.out.println("Sum: "+sum);
	}

	private static void bloggerInYear(HashMap<String, Integer> bMap, int[][][][] records) {
		int sum = 0;
		for(Entry<String,Integer> entry:bMap.entrySet()){
			String key = entry.getKey();
			int value = entry.getValue();
			System.out.print(key+",");
			for(int y = 0 ; y < 3 ; y++){
				int count = 0; 
				for(int m = 0 ; m < 12; m ++){
					for(int d = 0 ; d < 31 ; d ++){
						count += records[value][y][m][d];
					}
				}
				sum += count;
				System.out.print(count+",");
			}
			System.out.println();
		}
		System.out.println("Sum: "+sum);
	}

	private static void bloggerInMonth(HashMap<String, Integer> bMap, int[][][][] records) {
		int sum = 0;
		for(Entry<String,Integer> entry:bMap.entrySet()){
			String key = entry.getKey();
			int value = entry.getValue();
			System.out.print(key+",");
			for(int y = 0 ; y < 3 ; y++){
				for(int m = 0 ; m < 12; m ++){
					int count = 0; 
					for(int d = 0 ; d < 31 ; d ++){
						count += records[value][y][m][d];
					}
					sum += count;
					System.out.print(count+",");
				}
			}
			System.out.println();
		}
		System.out.println("Sum :" +sum);
	}

	

	private static int bloggerCounter(HashMap<String, Integer> bMap, File file) {
		int count = 0;
		try {
			Scanner sc = new Scanner(file);
			while(sc.hasNext()){
				count ++;
				String tmp = sc.nextLine();
				String[] attrs = tmp.split(",");
			
				/* Blogger */
				String blogger = attrs[2];
				
				if(bMap.containsKey(blogger))
					continue;
				bMap.put(blogger,bMap.size());
			}
			sc.close();
		} catch (Exception e) {
		}
		return count;
	}
	
	
}
