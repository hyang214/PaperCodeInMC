package idspV2;

import java.io.FileNotFoundException;

import util.Combination;
import util.FasterElementMap;
import util.NaiveCounter;
import util.Parameter;
import util.Pattern;
import util.Sequences;

public class IDSPMinerV2 {

	public static void main(String args[]) throws FileNotFoundException
	{	
		/* set parameter */ 
		Parameter parameter = new Parameter();
//		Parameter parameter = new Parameter(dATASETSIZE, mAXLENGTH, tHRESHOLD_IN_POS, gMIN, gMAX, tHRESHOLD_IN_NEG)
		System.out.println("iDSP-Miner "+parameter.printParameter());
		
		/* read data sets */
		Sequences sequences = new Sequences(parameter);
		
		long start = System.currentTimeMillis();
		
		/* generate candidate elements */
		CandidateElementV3 indexer = new CandidateElementV3(parameter, sequences);
		/* remove impossible elements in Start-set and Follow-sets */
//		indexer.removeImpossible();
		
		/* get the candidate elements */
		Combination combination = indexer.getIndex();
//		index.printQuickList();
//		System.out.println("Pos ******");
//		combination.pos.print();
//		System.out.println("Neg ******");
//		combination.neg.print();
		System.out.println("Alphabet: "+combination.pos.itemMap.getSize());
		combination.pos.printStati();
		combination.neg.printStati();

		long end = System.currentTimeMillis();
		System.out.println("Time Cost of Indexer Construction: " + (end - start) + " ms.");
		
		/* generate candidate pattern */
		CandidatePatternV2 candidateGen = new CandidatePatternV2(combination);
		candidateGen.generate(parameter);
		
		long end2 = System.currentTimeMillis();
		System.out.println("Time of generate patterns: "+(end2 - end) + " ms.");
		System.out.println("Total Time cost: "+(end2 - start) + " ms.");
		
		System.gc();
		System.out.println("Menory: "+ (Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory()));
		
		/* print the results */
		System.out.println("--- Results: ----------------------------------------------");
		System.out.println("Pos ******************* "+candidateGen.rSet.posResults.size());
		for(Pattern p : candidateGen.rSet.posResults)
		{
			p.itemMap = combination.pos.itemMap;
			p.print();
//			p.print3();
//			p.print4();
		}
//		System.out.println("Merged Size:"+ candidateGen.rSet.posResults.size());
//		System.out.println("Naive Size:"+NaiveCounter.INSTANCE.count(candidateGen.rSet));

	}
}
