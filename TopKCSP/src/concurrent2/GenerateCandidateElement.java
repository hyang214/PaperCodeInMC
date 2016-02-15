package concurrent2;

import java.util.ArrayList;
import java.util.HashMap;

import util.Element;

/**
 * author: Hao 
 * date:2015年3月5日
 * time:下午3:34:06
 * purpose: Generate candidate elements and collect their occurrences information 
 *          and also collect the length of sequences in data set
 */
public enum GenerateCandidateElement {
	INSTANCE;
	public HashMap<String, Element> genCE(ArrayList<String> dataset){
		HashMap<String, Element> eMap = new HashMap<String, Element>();
		ArrayList<Integer> lenList = new ArrayList<Integer>();
		double sum = 0;
		
		/** collect the position occurrence information **/
		for(int seqId = 0 ; seqId < dataset.size() ; seqId ++){
			String[] tmp = dataset.get(seqId).split(",");
			for(int position = 0 ; position < tmp.length ; position ++){
				String value = tmp[position];
				Element e = eMap.get(value);
				if(e == null){
					e = new Element(value);
					eMap.put(value, e);
				}
				
				e.addPOccurrence(seqId, position);
			}
			
			/** collect the length information **/
			lenList.add(tmp.length);
			
			sum += tmp.length;
		}
		
		/** update the p of all elements **/
		for(String key: eMap.keySet()){
			Element e = eMap.get(key);
			e.finalCheck(sum);
		}
		
//		getDSInfo(dataset,eMap,lenList);
		
		return eMap;
	}
	
	public void getDSInfo(ArrayList<String> dataset, HashMap<String, Element> eMap, ArrayList<Integer> lenList){
		System.out.println("DS info: "+ eMap.size());
		System.out.println(dataset.size());
		int min = Integer.MAX_VALUE;
		int max = Integer.MIN_VALUE;
		double avg = 0;
		for(Integer i: lenList){
			if(i < min)
				min = i;
			if(i > max)
				max = i;
			avg += i;
		}
		avg /= lenList.size();
		
		System.out.println("min: "+min);
		System.out.println("max: "+max);
		System.out.println("avg: "+avg);	}
}
