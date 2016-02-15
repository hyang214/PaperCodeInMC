package statistics;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class FinalPatternStati {
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
					+ "# itemset-DSPs (CR), # itemset-DSPs,"
					+ "Avg length of patterns,Max length of patterns,"
					+ "Avg size of elements,Max size of elements,");
//			System.out.println(",,,,,,,,,,,,,,,,,,,,,,,");
			File[] groupFiles = dic.listFiles();
				for(File type : groupFiles){
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
						int naive = 0;
						
						/* store patterns */
						ArrayList<String> patternList = new ArrayList<String>();
						
						Scanner sc = new Scanner(result);
						while (sc.hasNext()) {
							String tmp = sc.nextLine();
//							System.out.println(tmp);
//							System.out.println(InfoType.DEFAULT.judge(tmp));
							
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
								AVGSizeofPosElements = Double.valueOf(tmp7[1]);
								break;
							case MAX_SIZE_ELEMENT:
								String[] tmp8 = tmp.split(":");
								maxSizeofPosElement = Integer.valueOf(tmp8[1]);
								break;
							case AVG_LENGTH_PATTERN:
								String[] tmp9 = tmp.split(":");
								AVGLengthofPos = Double.valueOf(tmp9[1]);
								break;
							case MAX_LENGTH_PATTERN:
								String[] tmp10 = tmp.split(":");
//								System.out.println(tmp10[1]);
								maxLengthofPos = Integer.valueOf(tmp10[1]);
								break;
							case NAIVE_SIZE:
								String[] tmp11 = tmp.split(": ");
//								System.out.println(tmp11[1]);
								naive = Integer.valueOf(tmp11[1]);
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
//						System.out.println("File A,File B,Alpha,Beta,maxGap,minGap,"
//								+ "# itemset-DSPs (CR), # itemset-DSPs"
//								+ "Avg length of patterns,Max length of patterns,"
//								+ "Avg size of elements,Max size of elements,");
						/* print information */
						System.out.println(posFile+","+negFile+","+alpha+","+beta+","+maxGap+","+minGap+","+
								SizeofPosResults+","+naive+","+
								AVGLengthofPos+","+maxLengthofPos+","+
								AVGSizeofPosElements+","+maxSizeofPosElement);
					}
					System.out.println(",,,,,,,,,,,,,,,,,,,,,,,");
				}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}

