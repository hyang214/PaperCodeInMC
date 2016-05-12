package main;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import miner.Miner;
import miner.MinerImpl;
import pp.PostProcess;
import pp.PostProcessFactory;
import tools.Verbase;
import util.NaivePattern;
import util.NaiveResults;
import util.Parameter;
import util.PeerPattern;
import util.Results;
import util.Sequences;

/**
 * author: Hao 
 * date:Feb 23, 2016
 * time:2:37:02 PM
 * purpose:
 */
public class ValidationMain {
	public static void main(String[] args) throws FileNotFoundException {
		/**
		 * @posFilePath: the path of positive data set
		 * @negFilePath: the path of negative data set 
		 * @K: the number of pattern user need
		 * @min: the minimum gap between the any continuous elements in one pattern 
		 * @max: the maximum gap between the any continuous elements in one pattern
		 */
		String posFilePath = "./data/dblp/DB.txt";//"./data/test/sr female.txt";//  "./data/test/a.txt";// 
		String negFilePath = "./data/dblp/DM.txt";//"./data/test/sr male.txt";// "./data/test/b.txt";// 
		int K = 5;
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
		
		/** ki **/
		System.out.println("KI **********************");
		Miner kiMiner = new MinerImpl(sequences, "KiGCE", "KiGCP");
		kiMiner.mine();
		/** post process of result **/
		@SuppressWarnings("unchecked")
		PostProcess<PeerPattern, NaivePattern> pp = PostProcessFactory.INSTANCE.getByName("KiPP");
		pp.inputResult(Results.peerStore.values());
		List<NaivePattern> kiList = new ArrayList<>(pp.outputResult()); 
		/** print results **/
		pp.print();
		
		/** baseline **/
		System.out.println("Baseline **********************");
		Parameter.K = kiList.size();
		Miner baselineMiner = new MinerImpl(sequences, "BaselineGCE", "BaselineGCP");
		baselineMiner.mine();
		/** print results **/
		printResults();
		List<NaivePattern> blList = NaiveResults.topK;
		
		/** compare results from two algorithms **/
		Set<NaivePattern> matched = new HashSet<>();
		for(NaivePattern kiP : kiList){
			for(NaivePattern blP : blList){
				if(kiP.equals(blP)){
					matched.add(blP);
				}
			}
		}
		
		System.out.println("Matched: " + matched.size() + " / " + kiList.size());
		System.out.println("unmatched:");
		System.out.println("	In ki:");
		for(NaivePattern kiP : kiList){
			boolean contained = false;
			for(NaivePattern match : matched){
				if(match.equals(kiP)){
					contained = true;
				}
			}
			if(!contained)
				System.out.println("		" + kiP);
		}
		System.out.println("	In bl:");
		for(NaivePattern kiP : blList){
			boolean contained = false;
			for(NaivePattern match : matched){
				if(match.equals(kiP)){
					contained = true;
				}
			}
			if(!contained)
				System.out.println("		" + kiP);
		}
	}
	
	private static void printResults() {
		System.out.println("Print results:");
		for(NaivePattern np : NaiveResults.topK){
			System.out.println(np);
		}
	}
}
