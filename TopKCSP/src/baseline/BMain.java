package baseline;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;

import util.Pattern;
import util.Results;

/**
 * author: Hao 
 * date:2015年3月19日
 * time:下午2:57:16
 * purpose: The enter class of Top-K Contrast Sequential Pattern mining
 */
public class BMain {
	public static void main(String[] args) {
		int minGap = 0;
		int maxGap = 2;
		int K = 10;
		final boolean turn = false;
		final boolean a = false;
		final boolean baseline = false;
		

//		String posPath = "./data/ADLs/Activity/A.txt";
//		String negPath = "./data/ADLs/Activity/B.txt";
//		redirect("./results/ADLs/Activity/baseline/", minGap, maxGap, K, posPath, negPath,turn,a,baseline,"");
		
//		String posPath = "./data/ADLs/Location/A_s.txt";
//		String negPath = "./data/ADLs/Location/B_s.txt";
//		redirect("./results/ADLs/Location/baseline/", minGap, maxGap, K, posPath, negPath,turn,a,baseline,"");
		
//		String posPath = "./data/genes/eColi/eColi_A.txt";
//		String negPath = "./data/genes/eColi/eColi_B.txt";
//		redirect("./results/genes/eColi/baseline/", minGap, maxGap, K, posPath, negPath,turn,a,baseline,"");
		
//		String posPath = "./data/bible/old v3.txt";
//		String negPath = "./data/bible/new v3.txt";
//		redirect("./results/bible/bible/baseline/", minGap, maxGap, K, posPath, negPath,turn,a,baseline,"");

		String X = " x20";
		String posPath = "./data/ADLs/ActivityX/A X20.txt";
		String negPath = "./data/ADLs/ActivityX/B X20.txt";
		redirect("./results/ADLs/ActivityX/baseline/", minGap, maxGap, K, posPath, negPath,turn,a,baseline,X);
		
//		String X = " x5";
//		String posPath = "./data/ADLs/LocationX/A_s X5.txt";
//		String negPath = "./data/ADLs/LocationX/B_s X5.txt";
//		redirect("./results/ADLs/LocationX/baseline/", minGap, maxGap, K, posPath, negPath,turn,a,baseline,X);
		
//		String X = " x10";
//		String posPath = "./data/genes/eColiX/eColi_A X10.txt";
//		String negPath = "./data/genes/eColiX/eColi_B X10.txt";
//		redirect("./results/genes/eColiX/baseline/", minGap, maxGap, K, posPath, negPath,turn,a,baseline,X);
		
		
//		String X = " x50";
//		String posPath = "./data/ADLs/ActivityX/A X50.txt";
//		String negPath = "./data/ADLs/ActivityX/B X50.txt";
		
//		String X = " x10";
//		String posPath = "./data/genes/eColiX/eColi_A X10.txt";
//		String negPath = "./data/genes/eColiX/eColi_B X10.txt";
		
//		String posPath = "./data/ADLs/Activity/A.txt";
//		String negPath = "./data/ADLs/Activity/B.txt";
		
//		String posPath = "./data/ADLs/Location/A_s.txt";
//		String negPath = "./data/ADLs/Location/B_s.txt";

//		String posPath = "./data/test/s";
//		String negPath = "./data/test/t";
		
//		String posPath = "./data/genes/test/a";
//		String negPath = "./data/genes/test/b";
		
//		String posPath = "./data/ADLs/Location/B_s.txt";
//		String negPath = "./data/ADLs/Location/A_s.txt";
		
//		String posPath = "./data/genes/Cbi/Cbi_A.txt";
//		String negPath = "./data/genes/Cbi/Cbi_B.txt";
//		String posPath = "./data/genes/eColi/eColi_A.txt";
//		String negPath = "./data/genes/eColi/eColi_B.txt";
//		String posPath = "./data/genes/tatCD/tatCD_A.txt";
//		String negPath = "./data/genes/tatCD/tatCD_B.txt";
		
//		redirect("./results/ADLs/ActivityX/", minGap, maxGap, K, posPath, negPath,turn,a,baseline, X);
//		redirect("./results/ADLs/Activity/", minGap, maxGap, K, posPath, negPath,turn,a,baseline, "");
//		redirect("./results/ADLs/Location/", minGap, maxGap, K, posPath, negPath,turn,a,baseline, "");
//		redirect("./results/genes/eColi/", minGap, maxGap, K, posPath, negPath,turn,a,baseline);
//		redirect("./results/genes/eColiX/", minGap, maxGap, K, posPath, negPath,turn,a,baseline, X);
		
		printParameters(minGap, maxGap, K, posPath, negPath, a, baseline);
		
		Baseline bl = new Baseline(minGap, maxGap, K, posPath, negPath,a, baseline);
		Results results = bl.getResults();
		printPattern(results);
		
		
	}

	private static void printPattern(Results results) {
		double min = results.getpList().get(0).cRatio;
		double max = results.getpList().get(results.getpList().size()-1).cRatio;
		double sum = 0;
		for(Pattern p: results.getpList()){
			sum += p.cRatio;
			System.out.println(p.toString());
		}
		double avg = sum/results.getpList().size();
		System.out.println("min: "+min);
		System.out.println("max: "+max);
		System.out.println("avg: "+avg);
	}

	private static void redirect(String path, int minGap, int maxGap, int K, String posPath, String negPath, boolean turn, boolean a, boolean baseline, String x) {
		
		try {
			String pathname = "";
			pathname = path+"TopK Baseline ["+minGap+","+maxGap+"] "+K+" "+turn+" "+x+" .txt";
			
			PrintStream re = new PrintStream(new File(pathname));
			System.setOut(re);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void printParameters(int minGap, int maxGap, int K, String posPath, String negPath, boolean a, boolean baseline) {
		if(baseline)
			System.out.println("Method: Baseline");
		else{
			if(a)
				System.out.println("Method: TopK CSP-A");
			else
				System.out.println("Method: TopK CSP");
		}
		System.out.println("Parameters: ");
		System.out.println(" gap: [" +minGap+", "+maxGap+"]");
		System.out.println(" K: "+K);
		System.out.println(" pos: "+posPath);
		System.out.println(" neg: "+negPath);
		System.out.println("run ***************************");
	}
}
