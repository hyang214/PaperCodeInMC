package stat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import main.GenerateCandidateElement;
import main.GenerateGeneCandidateElement;
import util.Element;
import util.GetDataByPath;

/**
 * author: Hao 
 * date:2015年3月23日
 * time:下午8:25:36
 * purpose:Get the information about the data set
 */
public class StatisticalHelper {
	public static void main(String[] args) {
		try {
//			String posPath = "./data/ADLs/Activity/A.txt";
//			String negPath = "./data/ADLs/Activity/B.txt";
//			String posPath = "./data/ADLs/Location/A_s.txt";
//			String negPath = "./data/ADLs/Location/B_s.txt";
			String posPath = "./data/bible/old v3.txt";
			String negPath = "./data/bible/new v3.txt";
			ArrayList<String> pos = GetDataByPath.INSTANCE.getData(posPath);
			ArrayList<String> neg = GetDataByPath.INSTANCE.getData(negPath);
			
			HashMap<String, Element> posMap = GenerateCandidateElement.INSTANCE.genCE(pos);
//			System.out.println(posMap.size());
			HashMap<String, Element> negMap = GenerateCandidateElement.INSTANCE.genCE(neg);
//			System.out.println(negMap.size());
			HashSet<String> all = new HashSet<String>();
//			all.addAll(posMap.keySet());
//			all.addAll(negMap.keySet());
//			System.out.println("All: "+all.size());
			

////			String posPath = "./data/genes/Cbi/Cbi_A.txt";
////			String negPath = "./data/genes/Cbi/Cbi_B.txt";
////			String posPath = "./data/genes/eColi/eColi_A.txt";
////			String negPath = "./data/genes/eColi/eColi_B.txt";
//			String posPath = "./data/genes/tatCD/tatCD_A.txt";
//			String negPath = "./data/genes/tatCD/tatCD_B.txt";
//			
//			ArrayList<String> pos = GetDataByPath.INSTANCE.getData(posPath);
//			ArrayList<String> neg = GetDataByPath.INSTANCE.getData(negPath);
//			
//			HashMap<String, Element> posMap = GenerateGeneCandidateElement.INSTANCE.genCE(pos);
//			HashMap<String, Element> negMap = GenerateGeneCandidateElement.INSTANCE.genCE(neg);
//			HashSet<String> all = new HashSet<String>();
//			all.addAll(posMap.keySet());
//			all.addAll(negMap.keySet());
//			System.out.println("All: "+all.size());
			
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
}
