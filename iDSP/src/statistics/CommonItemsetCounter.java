package statistics;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;

public class CommonItemsetCounter {
	public static void main(String[] args) {
		ArrayList<String> dbList = new ArrayList<>();
		ArrayList<String> dmList = new ArrayList<>();
		ArrayList<String> irList = new ArrayList<>();
		
		HashSet<String> dbAlphbet = new HashSet<String>(); 
		HashSet<String> dmAlphbet = new HashSet<String>(); 
		HashSet<String> irAlphbet = new HashSet<String>(); 
		
		HashSet<String> dbOwnAlphbet = new HashSet<String>(); 
		HashSet<String> dmOwnAlphbet = new HashSet<String>(); 
		HashSet<String> irOwnAlphbet = new HashSet<String>(); 
		/* read sequences from file	 */
		try
		{
			File db = new File("./dblp3/DB100.txt");
			File dm = new File("./dblp3/DM100.txt");
			File ir = new File("./dblp3/IR100.txt");
			Scanner sc = new Scanner(db);
			while(sc.hasNext())
				dbList.add(sc.nextLine());
			sc.close();
			
			sc = new Scanner(dm);
			while(sc.hasNext())
				dmList.add(sc.nextLine());
			sc.close();
			
			sc = new Scanner(ir);
			while(sc.hasNext())
				irList.add(sc.nextLine());
			sc.close();
			
			for(String s:dbList){
				String[] list = s.split(";");
				for(String e:list){
					String[] eL = e.split(",");
					for(String ee:eL){
						dbAlphbet.add(ee);
					}
				}
			}
			
			for(String s:dmList){
				String[] list = s.split(";");
				for(String e:list){
					String[] eL = e.split(",");
					for(String ee:eL){
						dmAlphbet.add(ee);
					}
				}
			}
			
			for(String s:irList){
				String[] list = s.split(";");
				for(String e:list){
					String[] eL = e.split(",");
					for(String ee:eL){
						irAlphbet.add(ee);
					}
				}
			}
			
			System.out.println("DB: "+dbAlphbet.size());
			System.out.println("DM: "+dmAlphbet.size());
			System.out.println("IR: "+irAlphbet.size());
			
			System.out.println("DB own:"+spec(dbAlphbet, dmAlphbet, irAlphbet));
			System.out.println("DM own:"+spec(dmAlphbet, dbAlphbet, irAlphbet));
			System.out.println("IR own:"+spec(irAlphbet, dmAlphbet, dbAlphbet));
			System.out.println("common DB and DM:"+common(dbAlphbet, dmAlphbet));
			System.out.println("common DB and IR:"+common(dbAlphbet, irAlphbet));
			System.out.println("common DM and IR:"+common(dmAlphbet, irAlphbet));
			System.out.println("common DB, DM and IR:"+common(dbAlphbet, dmAlphbet, irAlphbet));
			System.out.println("DB (DM):"+(dbAlphbet.size() - common(dbAlphbet, dmAlphbet)));
			System.out.println("DM (DB):"+(dmAlphbet.size() - common(dmAlphbet, dbAlphbet)));
			System.out.println("DB (IR):"+(dbAlphbet.size() - common(dbAlphbet, irAlphbet)));
			System.out.println("IR (DB):"+(irAlphbet.size() - common(irAlphbet, dbAlphbet)));
			System.out.println("DM (IR):"+(dmAlphbet.size() - common(dmAlphbet, irAlphbet)));
			System.out.println("IR (DM):"+(irAlphbet.size() - common(irAlphbet, dmAlphbet)));
			ownSupportCheck(dbAlphbet, dmAlphbet, dbList, dmList);
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public static int common(HashSet<String> a,HashSet<String> b){
		int count = 0;
		for(String s1:a){
			for(String s2:b){
				if(s1.equals(s2))
					count++;
			}
		}
		return count;
	}
	
	public static void ownSupportCheck(HashSet<String> a,HashSet<String> b, ArrayList<String> aL, ArrayList<String> bL){
		
		HashSet<String> com = new HashSet<String>();
		HashMap<String,Integer> aOwn = new HashMap<String,Integer>();
		HashMap<String,Integer> bOwn = new HashMap<String,Integer>();
		for(String s1:a){
			if(b.contains(s1))
				com.add(s1);
		}
		
		for(String as:a){
			if(!com.contains(as)){
				int count = 0;
				for(String s:aL){
					if(s.contains(as))
						count++;
				}
				aOwn.put(as, count);
			}
		}
		
		double sum = 0;
		for(String key:aOwn.keySet()){
			sum += aOwn.get(key);
			System.out.print(aOwn.get(key)+",");
		}
		System.out.println();
		double avg = sum / aOwn.size();
		
		int highest = 0;
		for(String key:aOwn.keySet()){
			if(aOwn.get(key) > highest)
				highest = aOwn.get(key);
		}
//		System.out.println("   Highest:"+highest);
//		System.out.println("   Own A:"+avg);
	
		for(String as:b){
			if(!com.contains(as)){
				int count = 0;
				for(String s:bL){
					if(s.contains(as))
						count++;
				}
				bOwn.put(as, count);
			}
		}
		
		sum = 0;
		for(String key:bOwn.keySet()){
			sum += bOwn.get(key);
			System.out.print(bOwn.get(key)+",");
		}
		System.out.println();
		avg = sum / bOwn.size();
		
//		System.out.println("   Own B:"+avg);
	}
	
	public static int common(HashSet<String> a, HashSet<String> b, HashSet<String> c){
		int count = 0;
		for(String s1:a){
			for(String s2:b){
				if(s1.equals(s2) && c.contains(s1))
					count++;
			}
		}
		return count;
	}
	
	public static int spec(HashSet<String> a, HashSet<String> b, HashSet<String> c){
		int count = 0;
		for(String s1:a){
			if(!b.contains(s1) && !c.contains(s1))
				count++;
		}
		return count;
	}
	
	
}
