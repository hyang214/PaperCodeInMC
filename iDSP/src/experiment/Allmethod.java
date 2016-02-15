package experiment;

import idspV2.CandidateElementV3;
import idspV2.CandidatePatternV2;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Scanner;

import naive.NaiveCandidateGen;
import naive.NaiveIndexer;
import util.Combination;
import util.NaiveCounter;
import util.Parameter;
import util.Pattern;
import util.Sequences;

public class Allmethod {
	public static void main(String[] args) {
		try {
			Scanner plan = new Scanner(new File("./src/experiment/exp in figure 4 a.txt"));

			while (plan.hasNext()) {

				String one = plan.nextLine();
				String[] parameters = one.split(" ");
				int alpha = Integer.valueOf(parameters[0]);
				int beta = Integer.valueOf(parameters[1]);
				int maxGap = Integer.valueOf(parameters[2]);
				int minGap = Integer.valueOf(parameters[3]);
				String pos = parameters[4];
				String neg = parameters[5];
				String dic = parameters[6];
				
				/* set parameter */ 
				Parameter parameter = new Parameter();
				parameter.alpha = alpha;
				parameter.beta = beta;
				parameter.maxGap = maxGap;
				parameter.minGap = minGap;
				parameter.posFileName = pos;
				parameter.negFileName = neg;
				
				/* methods */
//				iDSP(parameter, "./results/gamelog2/"+dic+"/iDSP ");
				iDSP(parameter, "./results/exp/"+dic+"/iDSP ");
				naive(parameter, "./results/exp/"+dic+"/Naive ");
			}
			plan.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static void naive(Parameter parameter, String dic) throws FileNotFoundException {
		/* out file name */
		String outName = dic + parameter.printParameter()+" .txt";
		
		File outFile = new File(outName);
		PrintStream sysout = System.out;
		PrintStream out = new PrintStream(outFile);
		System.setOut(out);
		
		/* set parameter */
		System.out.println("Naive "+parameter.printParameter());
		
		/* read data set in */
		Sequences sequences = new Sequences(parameter);
		
		long start = System.currentTimeMillis();
		
		/* generate candidate elements*/
		NaiveIndexer ib = new NaiveIndexer(parameter, sequences);
		ib.removeImpossible();
		long end = System.currentTimeMillis();
		System.out.println("Time Cost of Indexer Construction: " + (end - start) + " ms.");
		
		
		/* generate patterns*/
		NaiveCandidateGen nc = new NaiveCandidateGen(ib.nodeMap, ib.itemMap);
		nc.generator(parameter);

		long end2 = System.currentTimeMillis();
		
		System.gc();
		System.out.println("Menory: "+ (Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory()));
		

		System.out.println("Total Time cost: "+(end2 - start) + " ms.");
		
		System.out.println("--- Results: ----------------------------------------------");
		System.out.println("Pos ******************* "+nc.posResults.size());
		for(Pattern p : nc.posResults)
		{
			p.itemMap = ib.itemMap;
			p.print();
		}
		
		System.setOut(sysout);
		out.close();
		System.out.println("Naive Finished: "+outName);
	}

	/* iDSP */
	public static void iDSP(Parameter parameter, String dic) throws FileNotFoundException{
			/* out file name */
			String outName = dic	 + parameter.printParameter()+" .txt";
			
			File outFile = new File(outName);
			PrintStream sysout = System.out;
			PrintStream out = new PrintStream(outFile);
			System.setOut(out);
			
			System.out.println("iDSP-Miner "+parameter.printParameter());
			
			/* read data sets */
			Sequences sequences = new Sequences(parameter);
			
			long start = System.currentTimeMillis();
			
			/* generate candidate elements */
			CandidateElementV3 indexer = new CandidateElementV3(parameter, sequences);
			/* remove impossible elements in Start-set and Follow-sets */
//			indexer.removeImpossible();
			
			/* get the candidate elements */
			Combination combination = indexer.getIndex();
//			index.printQuickList();
//			System.out.println("Pos ******");
//			combination.pos.print();
			System.out.println("Alphabet: "+combination.pos.itemMap.getSize());
			combination.pos.printStati();

			long end = System.currentTimeMillis();
			System.out.println("Time Cost of Indexer Construction: " + (end - start) + " ms.");
			
			/* generate candidate pattern */
			CandidatePatternV2 candidateGen = new CandidatePatternV2(combination);
			candidateGen.generate(parameter);
			
			long end2 = System.currentTimeMillis();
			System.out.println("Time of generate patterns: "+(end2 - end) + " ms.");
			System.out.println("Total Time cost: "+(end2 - start) + " ms.");
			
//			System.gc();
//			System.out.println("Menory: "+ (Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory()));
			
			/* print the results */
			System.out.println("--- Results: ----------------------------------------------");
			System.out.println("Patterns Size: "+candidateGen.rSet.posResults.size());
			for(Pattern p : candidateGen.rSet.posResults)
			{
				p.itemMap = combination.pos.itemMap;
				p.print();
//				p.print3();
			}
//			System.out.println("Naive Size: "+NaiveCounter.INSTANCE.count(candidateGen.rSet));
			System.setOut(sysout);
			out.close();
			System.out.println("iDSP Finished: "+outName);
	}
}
