package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;

import ki.TopkItemset;
import tools.TimeRecord;
import tools.Verbase;
import util.Parameter;
import util.PeerKey;
import util.Results;
import util.Sequences;

/**
 * author: Hao 
 * date:Dec 1, 2015
 * time:3:53:08 PM
 * purpose:
 */
public class Main {
	
	public static void main(String[] args) throws FileNotFoundException {
		/**
		 * @posFileName: the path of positive data set
		 * @negFileName: the path of negative data set 
		 * @k: the number of pattern user want
		 * @min: the minimum gap between the any continuous elements in one pattern 
		 * @max: the maximum gap between the any continuous elements in one pattern
		 */
		String posFileName = "";
		String negFileName = "";
		int K = 10;
		int min = 0;
		int max = 2;
		String itemSeparator = ",";
		String elementSeparator = ";";
		PrintStream output = System.out;
		if(args.length == 6){
			posFileName = args[0];
			negFileName = args[1];
			K = Integer.valueOf(args[2]);
			min = Integer.valueOf(args[3]);
			max = Integer.valueOf(args[4]);
			output = new PrintStream(new File(args[5]));
		}
		System.setOut(output);
		
		/** initialize the parameters **/
		Verbase.verbaseAtLevel(8, TimeRecord.onceRecordAndPrint("Parameter Inital"));
		Parameter.initialize(posFileName, negFileName, K, min, max, itemSeparator, elementSeparator);
		Verbase.verbaseAtLevel(8, TimeRecord.onceRecordAndPrint("Parameter Inital"));
		
		/** read sequence data from disk **/
		Verbase.verbaseAtLevel(8, TimeRecord.onceRecordAndPrint("Read Data"));
		Sequences sequences = new Sequences();
		Verbase.verbaseAtLevel(8, TimeRecord.onceRecordAndPrint("Read Data"));
		
		/** mine patterns **/
		Verbase.verbaseAtLevel(8, TimeRecord.onceRecordAndPrint("Mine pattern"));
		TopkItemset ti = new TopkItemset(sequences);
		ti.mine();
		Verbase.verbaseAtLevel(8, TimeRecord.onceRecordAndPrint("Mine pattern"));
		
		/** print results **/
		printResults(ti);
	}

	private static void printResults(TopkItemset ti) {
		for(PeerKey pk : Results.peerStore.keySet()){
			System.out.println(pk.toString());
			System.out.println(Results.peerStore.get(pk).toString());
		}
	}
}
