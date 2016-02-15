package statistics;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class NaivePatternStati {
	public static void main(String[] args) {
		try {
			File dic = new File("./experiment/dblp3 v3");
			System.out.println("Method,File A,File B,Alpha,Beta,maxGap,minGap,Length of gap,"
					+ "# element instances,# total successor element instances,# avg successor element instances,"
					+ "Time cost of candidate elements,Time cost of candidate patterns,Time cost of total,"
					+ "# patterns in A,# patterns in B,# patterns in both,"
					+ "Avg length of patterns in A,Max length of patterns in A,Avg length of patterns in B,Max length of patterns in B,Avg length of patterns in both,"
					+ "Avg size of elements in patterns in A,Maxg size of elements in patterns in B,Avg size of elements in patterns in B,Max size of elements in patterns in B,Avg size of elements in patterns in both");
//			System.out.println(",,,,,,,,,,,,,,,,,,,,,,,");
			File[] groupFiles = dic.listFiles();
			for(File group:groupFiles){
				File[] types = group.listFiles();
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
							case PATTERN:
								patternList.add(tmp);
								break;
							default:
								break;
							}
						}
						sc.close();
						
						TimeCostofPattern = TimeCostofTotal - TimeCostofIndexer;
						if( SizeofStart != 0)
							AVGSizeofFollowSet = (double)SizeofFollow / SizeofStart;
						else
							AVGSizeofFollowSet = 0;
						
						/* information about size of elements and length of patterns */
						SizeofBothResults = SizeofPosResults+SizeofNegResults;
						
						/* results of pos */
						for(int i = 0 ; i < SizeofPosResults ; i ++){
							String pattern = patternList.get(i);
							String[] elements = pattern.split("> ");
							TotalLengthofPos += elements.length-1;
							if(elements.length-1 > maxLengthofPos)
								maxLengthofPos = elements.length-1;
							
							for(int j = 0 ; j < elements.length - 1 ; j ++){
								String[] content = elements[j].split(":");
								String[] itemset = content[1].split(",");
								TotalSizeofPosElements += itemset.length-1;
								if(itemset.length-1 > maxSizeofPosElement)
									maxSizeofPosElement = itemset.length-1;
							}
							
//							for(String element:elements){
//								String[] content = element.split(":");
//								String[] itemset = content[1].split(",");
//								TotalSizeofPosElements += itemset.length-1;
//							}
						}
						if( SizeofPosResults != 0)
							AVGLengthofPos = TotalLengthofPos / SizeofPosResults;
						else
							AVGLengthofPos = 0;
						if( TotalLengthofPos != 0)
							AVGSizeofPosElements = TotalSizeofPosElements / TotalLengthofPos;
						else
							AVGSizeofPosElements = 0;
						/* results of neg */
						for(int i = SizeofPosResults ; i < SizeofBothResults ; i ++){
							String pattern = patternList.get(i);
							String[] elements = pattern.split("> ");
							TotalLengthofNeg += elements.length-1;
							if(elements.length-1 > maxLengthofNeg)
								maxLengthofNeg = elements.length-1;
							
							for(int j = 0 ; j < elements.length - 1 ; j ++){
								String[] content = elements[j].split(":");
								String[] itemset = content[1].split(",");
								TotalSizeofNegElements += itemset.length-1;
								if(itemset.length-1 > maxSizeofNegElement)
									maxSizeofNegElement = itemset.length-1;
							}
							
//							for(String element:elements){
//								String[] content = element.split(":");
//								String[] itemset = content[1].split(",");
//								TotalSizeofNegElements += itemset.length-1;
//							}
						}
						if( SizeofNegResults != 0)
							AVGLengthofNeg = TotalLengthofNeg / SizeofNegResults;
						else
							AVGLengthofNeg = 0;
						if( TotalLengthofNeg != 0)
							AVGSizeofNegElements = TotalSizeofNegElements / TotalLengthofNeg;
						else
							AVGSizeofNegElements = 0;
						/* reuslts of both*/
						double TotalLengthofBoth = TotalLengthofPos+TotalLengthofNeg;
						if( SizeofBothResults != 0)
							AVGLengthofBoth = TotalLengthofBoth / SizeofBothResults;
						else
							AVGLengthofBoth = 0;
						double TotalSizeofBothElements = TotalSizeofPosElements+TotalSizeofNegElements;
						if( TotalLengthofBoth != 0)
							AVGSizehofBothElements = TotalSizeofBothElements / TotalLengthofBoth;
						else
							AVGSizehofBothElements = 0;
						
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
						
						/* print information */
						System.out.println(method+","+posFile+","+negFile+","+alpha+","+beta+","+maxGap+","+minGap+","+LengthOfGap+","+
								SizeofStart+","+SizeofFollow+","+AVGSizeofFollowSet+","+
								TimeCostofIndexer+","+TimeCostofPattern+","+TimeCostofTotal+","+
								SizeofPosResults+","+SizeofNegResults+","+SizeofBothResults+","+
								AVGLengthofPos+","+maxLengthofPos+","+AVGLengthofNeg+","+maxLengthofNeg+","+AVGLengthofBoth+","+
								AVGSizeofPosElements+","+maxSizeofPosElement+","+AVGSizeofNegElements+","+maxSizeofNegElement+","+AVGSizehofBothElements);
					}
					System.out.println(",,,,,,,,,,,,,,,,,,,,,,,");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
