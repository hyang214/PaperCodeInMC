package main;

import java.io.PrintStream;
import java.util.List;

import miner.Miner;
import miner.MinerImpl;
import tools.Verbase;
import util.NaivePattern;
import util.NaiveResults;
import util.Parameter;
import util.Sequences;

/**
 * author: Hao 
 * date:Feb 29, 2016
 * time:4:25:26 PM
 * purpose:
 */
public class NaiveMain {
	public static void main(String[] args) {
		/**
		 * @posFilePath: the path of positive data set
		 * @negFilePath: the path of negative data set 
		 * @K: the number of pattern user need
		 * @min: the minimum gap between the any continuous elements in one pattern 
		 * @max: the maximum gap between the any continuous elements in one pattern
		 */
		String posFilePath = "./data/test/sr female.txt";// "./data/dblp/DB.txt";// "./data/test/a.txt";// 
		String negFilePath = "./data/test/sr male.txt";// "./data/dblp/DM.txt";//"./data/test/b.txt";// 
		int K = 10;
		int min = 0;
		int max = 5;
		String itemSeparator = ",";
		String elementSeparator = ";";
		PrintStream output = System.out;
		System.setOut(output);
		
		/** initialize the parameters **/
		Parameter.initialize(posFilePath, negFilePath, K, min, max, itemSeparator, elementSeparator);
		Verbase.verbaseAtLevel(1, Parameter.staticToString());
		
		/** read sequence data from disk **/
		Sequences sequences = new Sequences();
		Verbase.verbaseAtLevel(1, sequences.toString());
		
		/** baseline **/
		System.out.println("Baseline **********************");
			Miner baselineMiner = new MinerImpl(sequences, "BaselineGCE", "BaselineGCP");
		baselineMiner.mine();
		/** print results **/
		printResults();
	}
	
	private static void printResults() {
		System.out.println("Print results:");
		for(NaivePattern np : NaiveResults.topK){
			System.out.println(np);
		}
	}
}
