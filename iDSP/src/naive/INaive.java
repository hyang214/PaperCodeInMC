package naive;

import java.io.FileNotFoundException;
import java.util.HashSet;

import util.Parameter;
import util.Pattern;
import util.Sequences;

public class INaive {

	public static void main(String args[]) throws FileNotFoundException {
		/* set parameter */
		Parameter parameter = new Parameter();
		System.out.println("Naive "+parameter.printParameter());
		
		/* read data set in */
		Sequences sequences = new Sequences(parameter);
		
		long start = System.currentTimeMillis();
		
		/* generate candidate elements*/
		NaiveIndexer ib = new NaiveIndexer(parameter, sequences);
		ib.removeImpossible();
		System.out.println("Size of eq: "+ib.nodeMap.getNodeList().size());
//		ib.printIndexerBaseline();
		long endI = System.currentTimeMillis();
		System.out.println("Index: "+(endI - start) + " ms.");
		
		/* generate patterns*/
		NaiveCandidateGen nc = new NaiveCandidateGen(ib.nodeMap, ib.itemMap);
		nc.generator(parameter);
		
		long end = System.currentTimeMillis();
		System.out.println("Gen: "+(end - endI) + " ms.");
			
		System.gc();
		System.out.println("Menory: "+ (Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory()));
		
		System.out.println("Total Time cost: "+(end - start) + " ms.");
		
		System.out.println("--- Results: ----------------------------------------------");
		System.out.println("Pos ******************* "+nc.posResults.size());
		for(Pattern p : nc.posResults)
		{
			p.itemMap = ib.itemMap;
			p.print();
		}
//		System.out.println("Neg ******************* "+nc.negResults.size());
//		for(Pattern p : nc.negResults)
//		{
//			p.itemMap = ib.itemMap;
//			p.print();
//		}
	}
}
