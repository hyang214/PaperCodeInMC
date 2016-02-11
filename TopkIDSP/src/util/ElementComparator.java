package util;

import java.util.Comparator;

/**
 * author: Hao 
 * date:Jan 22, 2016
 * time:3:38:33 PM
 * purpose:
 */
public class ElementComparator implements Comparator<Element>{

	/**
	 * order the element by cRatio desc, posSup desc
	 */
	@Override
	public int compare(Element o1, Element o2) {
		if(o1.getcRatio() > o2.getcRatio()){
			return -1;
		}else if(o1.getcRatio() < o2.getcRatio()){
			return 1;
		}else{
			if(o1.getPosSup() > o2.getPosSup()){
				return -1;
			}else if(o1.getPosSup() < o2.getPosSup()){
				return 1;
			}else{
				return 0;
			}
		}
	}

}
