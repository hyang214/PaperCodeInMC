package util;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

/**
 * author: Hao 
 * date:2015年3月22日
 * time:上午11:57:57
 * purpose: Reorder the output order of HashMap
 */
public enum HashMapSortHelper {
	INSTANCE;
	public ArrayList<Element> reorder(HashMap<String, Element> eMap, HashMap<String, Element> negMap){
		ArrayList<Element> eList = new ArrayList<Element>(eMap.values());
		eList.sort(new Comparator<Element>() {
			@Override
			public int compare(Element o1, Element o2) {
				Element neg1 = negMap.get(o1.getValue());
				double negSup1 = 0;
				if(neg1 != null)
					negSup1 = neg1.sup;
				Element neg2 = negMap.get(o2.getValue());
				double negSup2 = 0;
				if(neg2 != null)
					negSup2 = neg2.sup;
				if(o1.sup - negSup1 > o2.sup - negSup2)
					return -1;
				else if(o1.sup - negSup1 < o2.sup - negSup2)
					return 1;
				else{
					if(o1.sup > o2.sup)
						return -1;
					else if(o1.sup < o2.sup)
						return 1;
					else
						return 0;
				}	
			}
		});
		return eList;
	}
}
