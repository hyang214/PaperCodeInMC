package statistics;

import java.io.File;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Scanner;

public class SupportCheck {
	public static void main(String[] args) {
//		String check = "SIGMOD Conference;VLDB;IEEE Data Eng. Bull.;KDD;ICDM;SDM;SIGIR;TREC;CoRR;KDD,ICDM;VLDB,SIGMOD Conference;"
//				+ "KDD,CoRR;TREC,SIGIR;PAKDD;CIKM;Knowl. Inf. Syst.;SIGMOD Record;VLDB,SIGMOD Record;ICDE;"
//				+ "IEEE Trans. Knowl. Data Eng.;";
		String check = "VLDB;SIGMOD Record;VLDB,SIGMOD Record;IEEE Trans. Knowl. Data Eng.;CIKM;KDD;CIKM,KDD;"
				+ "ICDM;KDD,ICDM;SIGIR;TREC;TREC,SIGIR;CIKM,SIGIR;Inf. Retr.;";
		String file1 = "./dblp3/DB100.txt";
		String file2 = "./dblp3/DM100.txt";
		String file3 = "./dblp3/IR100.txt";
		System.out.println("DB ******");
		check(check, file1);
		System.out.println("DM ******");
		check(check, file2);
		System.out.println("IR ******");
		check(check, file3);
	}
	
	public static void check(String check, String fileName){
		/* read file */
		try {
			File file = new File(fileName);
			Scanner sc = new Scanner(file);
			ArrayList<String> list = new ArrayList<String>();
			while(sc.hasNext()){
				String tmp = sc.nextLine();
				list.add(tmp);
			}
			sc.close();
			String[] elements = check.split(";");
			/* for every check element */
			for(String element:elements){
				/* for every item in the element */
				String[] items = element.split(",");
				int count = 0;
				BitSet countBS = new BitSet();
				for(int i = 0 ; i < list.size() ; i++){
					/* for evey sequence in data set */
					String[] seqElements = list.get(i).split(";");
					for(String seqE:seqElements){
						/* for every element in sequence */
						boolean flag = true;
						/* test every item is in this element or not */
						int containsAllHelper = 0;
						for(String item:items){
							String[] sItems = seqE.split(",");
							for(String sItem:sItems){
								if(sItem.equals(item)){
									containsAllHelper++;
									break;
								}
							}
						}
						if(containsAllHelper == items.length){
							count++;
							countBS.set(i, true);
//							System.out.println("  "+seqE);
							break;
						}
					}
				}
				System.out.println(""+element+" sup: "+countBS.cardinality());
//				System.out.println("                 "+countBS);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
