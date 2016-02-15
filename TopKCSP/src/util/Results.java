package util;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * author: Hao 
 * date:2015年3月6日
 * time:下午3:29:59
 * purpose: Store the final results, the Top-k contrast pattern 
 */
public class Results {
	private ArrayList<Pattern> pList;
	public final int K;
	private double threshold;
	
	public Results(int K) {
		this.pList = new ArrayList<Pattern>();
		this.K = K;
		this.threshold = Double.MIN_VALUE;
	}
	
	public boolean addPattern(Pattern newP){
		boolean flag = false;
		
		if(pList.size() < K){
			/** the number of pattern existed is less than k **/	
			pList.add(newP);
			sort();
			flag = true;
		}else{
			/*** count the number of patterns which has the same threshold of k-th pattern in set ***/
			if(newP.cRatio == threshold){
				Pattern oldP = pList.get(0);
				/*** if two pattern has same cRatio, we keep the shorter one
				 * and if both condition is same, we keep the one we found earlier ***/
				if(oldP.posList.size() > newP.posList.size()){
					pList.set(0, newP);
					sort();
				}
				
				/*** one new pattern with same cRatio ***/
			}
			else if(newP.cRatio > threshold){
				/** the contract ratio of p is larger than threshold, add it into pList **/
				pList.set(0, newP);
				/** then resort the list **/
				sort();
				flag = true;	
				threshold = pList.get(0).cRatio;
				
			}
			/** else: the contrast ratio is less than the threshold, we do not need add it into list **/	
		}
		
		return flag;
	}

	/** sort the pList, and find the minimum contrast value **/
	private void sort() {
		/** sort the list, ascend **/
		pList.sort(new Comparator<Pattern>() {

			@Override
			public int compare(Pattern o1, Pattern o2) {
				if(o1.cRatio < o2.cRatio)
					return -1;
				else if(o1.cRatio > o2.cRatio)
					return 1;
				else{
					if(o1.posList.size() <= o2.posList.size())
						return 1;
					else
						return -1;
				}
			}
		});
		/** get the minimum value(threshold) **/
		this.threshold = pList.get(0).cRatio;
	}

	public ArrayList<Pattern> getpList() {
		return pList;
	}
	
	public int getSize(){
		return pList.size();
	}

	public double getThreshold() {
		return threshold;
	}
	
	public void setThreshold(double d){
		this.threshold = d;
	}
	
	public boolean isFull(){
		return pList.size() == K;
	}

}
