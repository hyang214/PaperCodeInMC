package len1;

import java.util.ArrayList;
import java.util.HashMap;

import util.Element;
import util.GetDataByPath;
import util.Results;

/**
 * author: Hao 
 * date:2015年3月19日
 * time:下午2:57:31
 * purpose:
 */
public class TopKCSP {

	private int minGap;
	private int maxGap;
	private int K;
	private Results results;
	
	public TopKCSP(int minGap, int maxGap, int K, String posPath, String negPath, boolean a, boolean baseline) {
		this.minGap = minGap;
		this.maxGap = maxGap;
		this.K = K;
		try {
			ArrayList<String> pos = GetDataByPath.INSTANCE.getData(posPath);
			ArrayList<String> neg = GetDataByPath.INSTANCE.getData(negPath);
			
			long start = System.currentTimeMillis();
			/** run this algorithm **/
			this.results = run(pos, neg, a, baseline);
			long end = System.currentTimeMillis();
			System.out.println("Time cost: "+ (end-start));
		} catch (Exception e) {
			System.out.println("Error in data set file!");
			e.printStackTrace();
		}
	}

	private Results run(ArrayList<String> pos, ArrayList<String> neg, boolean a, boolean baseline) {
		/** generate candidate elements **/
		HashMap<String, Element> posMap = GenerateCandidateElement.INSTANCE.genCE(pos);
		HashMap<String, Element> negMap = GenerateCandidateElement.INSTANCE.genCE(neg);
//		HashMap<String, Element> posMap = GenerateGeneCandidateElement.INSTANCE.genCE(pos);
//		HashMap<String, Element> negMap = GenerateGeneCandidateElement.INSTANCE.genCE(neg);
		
//		print(posMap);
//		print(negMap);
		
		/** generate candidate pattern **/
		GenerateCandidatePattern gcp = new GenerateCandidatePattern(posMap, negMap, K, minGap, maxGap, pos.size(), neg.size(), baseline);
		if(a == false)
			gcp.genCP();
		else
			gcp.genCP2();
		results = gcp.getResults();
		
		return results;
	}

	public Results getResults() {
		return results;
	}

	private void print(HashMap<String, Element> eMap) {
		/** print the information of elements **/
		for(String key:eMap.keySet()){
			eMap.get(key).printInfo();
		}
	}
}
