package statistics;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class PatternStati {
	public static void main(String[] args) {
		try {
			File dic = new File("./results/dblp9/");
//			System.out.println("Name,Method,File A,File B,Alpha,Beta,maxGap,minGap,Length of gap,"
//					//+ "N,ES,SL,SIZE,"
//					+ "# element instances,# total successor element instances,# avg successor element instances,"
//					+ "Time cost of candidate elements,Time cost of candidate patterns,Time cost of total,"
//					+ "# patterns ,"
//					+ "Avg length of patterns,Max length of patterns,"
//					+ "Avg size of elements in patterns,Max size of elements in patterns,");
			System.out.println("File A,File B,Alpha,Beta,maxGap,minGap,"
					+ "# patterns ,"
					+ "Avg length of patterns,Max length of patterns,"
					+ "Avg size of elements in patterns,Max size of elements in patterns,");
//			System.out.println(",,,,,,,,,,,,,,,,,,,,,,,");
			File[] groupFiles = dic.listFiles();
			for(File group:groupFiles){
				File[] types = group.listFiles();
				String groupName = group.getName();
				for(File type : types){
					File[] results = type.listFiles();
					for(File result : results){
						/* time cost */
						int TimeCostofIndexer = 0;
						int TimeCostofPattern = 0;
						int TimeCostofTotal = 0;
						/* size of candidate elements*/
						int SizeofStart = 0;
						int SizeofFollow = 0;
						double AVGSizeofFollowSet = 0;
						/* size of results patterns */
						int SizeofPosResults = 0;
						int SizeofNegResults = 0;
						int SizeofBothResults = 0;
						/* length of patterns */
						double TotalLengthofPos = 0;
						double TotalLengthofNeg = 0;
						int maxLengthofPos = 0;
						double AVGLengthofPos = 0;
						double AVGLengthofNeg = 0;
						int maxLengthofNeg = 0;
						double AVGLengthofBoth = 0;
						/* size of elements */
						double TotalSizeofPosElements = 0;
						double TotalSizeofNegElements = 0;
						int maxSizeofPosElement = 0;
						double AVGSizeofPosElements = 0;
						double AVGSizeofNegElements = 0;
						int maxSizeofNegElement = 0;
						double AVGSizehofBothElements = 0;
						
						/* store patterns */
						ArrayList<String> patternList = new ArrayList<String>();
						
						Scanner sc = new Scanner(result);
						while (sc.hasNext()) {
							String tmp = sc.nextLine();
							switch(InfoType.DEFAULT.judge(tmp)){
							case START_ELEMENT:
								String[] tmp1 = tmp.split(":");
								SizeofStart = Integer.valueOf(tmp1[1]);
								break;
							case FOLLOW_ELEMENT:
								String[] tmp2 = tmp.split(":");
								SizeofFollow = Integer.valueOf(tmp2[1]);
								break;
							case TIME_COST_OF_INDEXER:
								String[] tmp3 = tmp.split(":");
								TimeCostofIndexer = Integer.valueOf(tmp3[1].replace(" ms.", "").replace(" ", ""));
								break;
							case TIME_COST_OF_TOTAL:
								String[] tmp4 = tmp.split(": ");
								TimeCostofTotal = Integer.valueOf(tmp4[1].replace(" ms.", ""));
								break;
							case SIZE_OF_POS_PATTERN:
								String[] tmp5 = tmp.split(": ");
								SizeofPosResults = Integer.valueOf(tmp5[1]);
								break;
							case SIZE_OF_NEG_PATTERN:
								String[] tmp6 = tmp.split(": ");
								SizeofNegResults = Integer.valueOf(tmp6[1]);
								break;
							case AVG_SIZE_ELEMENT:
								String[] tmp7 = tmp.split(":");
								AVGSizeofPosElements = Integer.valueOf(tmp7[1]);
								break;
							case MAX_SIZE_ELEMENT:
								String[] tmp8 = tmp.split(":");
								maxSizeofPosElement = Integer.valueOf(tmp8[1]);
								break;
							case AVG_LENGTH_PATTERN:
								String[] tmp9 = tmp.split(":");
								AVGLengthofPos = Integer.valueOf(tmp9[1]);
								break;
							case MAX_LENGTH_PATTERN:
								String[] tmp10 = tmp.split(":");
								maxSizeofPosElement = Integer.valueOf(tmp10[1]);
								break;
							case NAIVE_SIZE:
								String[] tmp11 = tmp.split(": ");
								maxSizeofPosElement = Integer.valueOf(tmp11[1]);
								break;
							case PATTERN:
								patternList.add(tmp);
								break;
							default:
								break;
							}
						}
						sc.close();
						
						/* parameter information */
						String name = result.getName();
						String[] parameters = name.split(" ");
						String method = parameters[0];
						String alpha = parameters[2];
						String beta = parameters[4];
						int maxGap = Integer.valueOf(parameters[6]);
						int minGap = Integer.valueOf(parameters[8]);
						int LengthOfGap = maxGap - minGap;
						String posFile = parameters[9];
						String negFile = parameters[10];
//						String[] dataInfo = posFile.split("_");
//						int N = Integer.valueOf(dataInfo[2]);
//						int ES = Integer.valueOf(dataInfo[3]);
//						int SL = Integer.valueOf(dataInfo[4]);
//						int SIZE = Integer.valueOf(dataInfo[5]);
						
						
//						System.out.println("Name,Method,File A,File B,Alpha,Beta,maxGap,minGap,Length of gap,"
//								//+ "N,ES,SL,SIZE,"
//								+ "# element instances,# total successor element instances,# avg successor element instances,"
//								+ "Time cost of candidate elements,Time cost of candidate patterns,Time cost of total,"
//								+ "# patterns ,"
//								+ "Avg length of patterns,Max length of patterns,"
//								+ "Avg size of elements in patterns,Max size of elements in patterns,");
						System.out.println("File A,File B,Alpha,Beta,maxGap,minGap,"
								+ "# patterns ,"
								+ "Avg length of patterns,Max length of patterns,"
								+ "Avg size of elements in patterns,Max size of elements in patterns,");
						/* print information */
						System.out.println(posFile+","+negFile+","+alpha+","+beta+","+maxGap+","+minGap+","+
								SizeofPosResults+","+
								AVGLengthofPos+","+maxLengthofPos+","+
								AVGSizeofPosElements+","+maxSizeofPosElement+",");
					}
					System.out.println(",,,,,,,,,,,,,,,,,,,,,,,");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}

enum InfoType{
	START_ELEMENT,
	FOLLOW_ELEMENT,
	AVG_SIZE_OF_FOLLOW_SET,
	AVG_SIZE_ELEMENT,
	AVG_LENGTH_PATTERN,
	MAX_SIZE_ELEMENT,
	MAX_LENGTH_PATTERN,
	TIME_COST_OF_INDEXER,
	TIME_COST_OF_TOTAL,
	SIZE_OF_POS_PATTERN,
	SIZE_OF_NEG_PATTERN,
	SIZE_OF_TOTAL_PATTERN,
	NAIVE_SIZE,
	PATTERN,
	DEFAULT;
	
	public InfoType judge(String s){
		if(s.contains("Time Cost of Indexer Construction:"))
			return TIME_COST_OF_INDEXER;
		else if(s.contains("Total Time cost:"))
			return TIME_COST_OF_TOTAL;
		else if(s.contains("Size of Start-elements:"))
			return START_ELEMENT;
		else if(s.contains("Size of Follow-elements:"))
			return FOLLOW_ELEMENT;
		else if(s.contains("Patterns Size:"))
			return SIZE_OF_POS_PATTERN;
		else if(s.contains("Neg Size:"))
			return SIZE_OF_NEG_PATTERN;
		else if(s.contains("Total Size:"))
			return SIZE_OF_TOTAL_PATTERN;
		else if(s.contains("> "))
			return PATTERN;
		else if(s.contains("Avg size:"))
			return AVG_SIZE_ELEMENT;
		else if(s.contains("Avg length:"))
			return AVG_LENGTH_PATTERN;
		else if(s.contains("Max size:"))
			return MAX_SIZE_ELEMENT;
		else if(s.contains("Max length:"))
			return MAX_LENGTH_PATTERN;
		else if(s.contains("Naive Size:"))
			return NAIVE_SIZE;
		else
			return DEFAULT;
	}
}
