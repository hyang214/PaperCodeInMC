package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;

import miner.Miner;
import miner.MinerImpl;
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
 * purpose: the enter of mining top-k itemset-based distinguishing pattern with gap constraint
 */
public class Main {
	
	public static void main(String[] args) throws FileNotFoundException {
		/**
		 * @posFilePath: the path of positive data set
		 * @negFilePath: the path of negative data set 
		 * @K: the number of pattern user need
		 * @min: the minimum gap between the any continuous elements in one pattern 
		 * @max: the maximum gap between the any continuous elements in one pattern
		 */
		String posFilePath = "./data/test/sr female.txt";// "./data/test/a.txt";// 
		String negFilePath = "./data/test/sr male.txt";// "./data/test/b.txt";// 
		int K = 5;
		int min = 0;
		int max = 2;
		String itemSeparator = ",";
		String elementSeparator = ";";
		PrintStream output = System.out;
		String gceName = "KiGCE";
		String gcpName = "KiGCP";
		if(args.length == 8){
			posFilePath = args[0];
			negFilePath = args[1];
			K = Integer.valueOf(args[2]);
			min = Integer.valueOf(args[3]);
			max = Integer.valueOf(args[4]);
			output = new PrintStream(new File(args[5]));
			gceName = args[6];
			gcpName = args[7];
		}
		System.setOut(output);
		
		/** initialize the parameters **/
		TimeRecord.allRecordAndReturn("Parameter Inital");
		Parameter.initialize(posFilePath, negFilePath, K, min, max, itemSeparator, elementSeparator);
		TimeRecord.allRecordAndReturn("Parameter Inital");
		Verbase.verbaseAtLevel(1, Parameter.staticToString());
		
		/** read sequence data from disk **/
		TimeRecord.allRecordAndReturn("Read Data");
		Sequences sequences = new Sequences();
		TimeRecord.allRecordAndReturn("Read Data");
		Verbase.verbaseAtLevel(1, sequences.toString());
		
		/** mine patterns **/
		TimeRecord.allRecordAndReturn("Mine pattern");
		Miner miner = new MinerImpl(sequences, gceName, gcpName);
		miner.mine();
		TimeRecord.allRecordAndReturn("Mine pattern");
		
		/** print the time cost of all action **/
		System.out.println(TimeRecord.allRecordToString());
		/** print results **/
		printResults();
	}

	private static void printResults() {
		System.out.println("Print results:");
		for(PeerKey pk : Results.peerStore.keySet()){
			System.out.println(pk.toString());
			System.out.println(Results.peerStore.get(pk).toString());
		}
	}
}
