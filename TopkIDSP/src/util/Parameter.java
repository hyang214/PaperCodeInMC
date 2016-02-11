package util;

public class Parameter 
{
	/**
	 * K: the number of patterns need by customer
	 * minGap: the minimum gap 
	 * maxGap: the maximum gap 
	 * posFilePath: the path of positive data set file
	 * negFilePath: the path of negative data set file
	 */
	public static int K;
	public static int minGap;
	public static int maxGap;
	public static String posFilePath;
	public static String negFilePath;
	public static double posSize;
	public static double negSize;
	public static String itemSeparator;
	public static String elementSeparator;
	public static final int POSITIVE = 0;
	public static final int NEGATIVE = 1;
	
	public static void initialize(String sPosFilePath, String sNegFilePath, int sK, int sMinGap, int sMaxGap,
			String sItemSeparator, String sElementSeparator)
	{
		posFilePath = sPosFilePath;
		negFilePath = sNegFilePath;
		K = sK;
		minGap = sMinGap;
		maxGap = sMaxGap;
		posSize = 0;
		negSize = 0;
		itemSeparator = sItemSeparator;
		elementSeparator = sElementSeparator;
	}
	
	public static String staticToString() {
		String tmp1 = posFilePath.replace("/", "%");
		String tmp2 = negFilePath.replace("/", "%");
		String s = "K " + K + " maxGap "+ maxGap + " minGap " + minGap + " " + tmp1 + " " + tmp2;
		return s;
	}
}
