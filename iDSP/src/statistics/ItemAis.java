package statistics;

import java.io.File;
import java.util.Scanner;

public class ItemAis {
	public static void main(String[] args) {
		try {
			int[] alphaDis = new int[100];
			File file = new File("./tmp/alpha.csv");
			Scanner sc = new Scanner(file);
			while(sc.hasNext()){
				String[] vs = sc.nextLine().split(",");
				for(String v:vs){
					alphaDis[Integer.valueOf(v)]++;
				}
				int size = 0;
				for(int i = 0; i < 100 ; i ++){
					if(i % 5 == 0){
						System.out.print(size+",");
						size = 0;
					}else{
						size += alphaDis[i];
					}
				}
				System.out.println();
				alphaDis = new int[100];
			}
			sc.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
