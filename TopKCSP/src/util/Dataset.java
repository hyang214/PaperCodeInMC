package util;

import java.util.ArrayList;
/**
 * author: Hao 
 * date:2015年3月4日
 * time:下午5:26:26
 * purpose: Package the data set and some information about it.
 */
public class Dataset {
	private ArrayList<ArrayList<String>> dataset;
	private int maxLength;
	private int minLength;
	
	public ArrayList<ArrayList<String>> getDataset() {
		return dataset;
	}
	public int getMaxLength() {
		return maxLength;
	}
	public int getMinLength() {
		return minLength;
	}
}
