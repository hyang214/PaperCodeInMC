package util;

import java.util.BitSet;


public class Parameter 
{
	public static int posDatasetSize;
	public static int negDatasetSize;
	public static int K;
	public static int minGap;
	public static int maxGap;
	public static String posFileName;
	public static String negFileName;
	public static BitSet posSeqId;
	public static BitSet negSeqId;
	public static double posSize;
	public static double negSize;
	public static String itemSeparator;
	public static String elementSeparator;
	public static final int POSITIVE = 0;
	public static final int NEGATIVE = 1;
	
	public static void initialize(String sPosFileName, String sNegFileName, int sK, int sMinGap, int sMaxGap,
			String sItemSeparator, String sElementSeparator)
	{
		posFileName = sPosFileName;
		negFileName = sNegFileName;
		K = sK;
		minGap = sMinGap;
		maxGap = sMaxGap;
		posSeqId = new BitSet();
		negSeqId = new BitSet();
		posSize = 0;
		negSize = 0;
		itemSeparator = sItemSeparator;
		elementSeparator = sElementSeparator;
	}
	
	public String printParameter(){
		String tmp1 = posFileName.replace("/", "");
		String tmp2 = negFileName.replace("/", "");
		String s = "K "+K+" maxGap "+maxGap+" minGap "+minGap +" " + tmp1 +" " + tmp2;
		return s;
	}
}
