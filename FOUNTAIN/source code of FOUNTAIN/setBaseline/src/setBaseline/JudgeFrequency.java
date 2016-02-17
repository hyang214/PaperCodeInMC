package setBaseline;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class JudgeFrequency {
	public Attribute Judgement(Map<String,List<Attribute>> allGap,Attribute base) {
		String currentPattern = "";
		Map<Integer,List<Integer>> candidateGap = getGapSetNaive();
		List<Attribute> gapList = new ArrayList<Attribute>();
		Iterator<String> iterator1 = allGap.keySet().iterator();
		while(iterator1.hasNext()) {
			currentPattern = iterator1.next();
			gapList = allGap.get(currentPattern);
		}		

		Map<Integer,Integer> index = new HashMap<Integer,Integer>();
		Iterator<Integer> iterator2 = candidateGap.keySet().iterator();
		while(iterator2.hasNext()) {
			Integer candidateGapSetKey = iterator2.next();
			index.put(candidateGapSetKey,0);
		}
		Attribute selectedGap = new Attribute(-1,-1);
			
		while(index != null && index.size() != 0) {
			selectedGap = findMinCompactGap(candidateGap,gapList,index);
			if(selectedGap.upperBound - selectedGap.lowerBound + 1 <= MainFunction.maxiamlgapwidth) {
				if(selectedGap.occur.cardinality() >= MainFunction.frequency) {
					if(new JudgeClosed().isClosed(selectedGap,base,selectedGap.occur.cardinality())) {					
						//new ReadFile().outputResult(currentPattern,selectedGap);
						System.out.println(currentPattern + " " + "[ " + selectedGap.lowerBound +", "+ selectedGap.upperBound+ " ]"); 					
					}
					break;
				}
			}
			else {
				selectedGap = new Attribute(-1,-1);
			}
		}
		return selectedGap;
	}
	
	public Attribute Judgement(Map<String,List<Attribute>> allGap,Attribute subGap,Attribute base) {
		String currentPattern = "";
		Map<Integer,List<Integer>> candidateGap = getGapSetNaive();
		List<Attribute> gapList = new ArrayList<Attribute>();
		Iterator<String> iterator1 = allGap.keySet().iterator();
		while(iterator1.hasNext()) {
			currentPattern = iterator1.next();
			gapList = allGap.get(currentPattern);
		}		

		Map<Integer,Integer> index = new HashMap<Integer,Integer>();
		Iterator<Integer> iterator2 = candidateGap.keySet().iterator();
		while(iterator2.hasNext()) {
			Integer candidateGapSetKey = iterator2.next();
			index.put(candidateGapSetKey,0);
		}
		Attribute selectedGap = new Attribute(-1,-1);
			
		while(index != null && index.size() != 0) {
			initialiseIndex(candidateGap,index,subGap);
			selectedGap = findMinCompactGap(candidateGap,gapList,index);
			if(selectedGap.upperBound - selectedGap.lowerBound + 1 <= MainFunction.maxiamlgapwidth) {
				if((selectedGap.occur.cardinality() >= MainFunction.frequency) && (selectedGap.upperBound - selectedGap.lowerBound + 1 <= 10)) {
					/*if((currentPattern.equals("DDDS")) || (currentPattern.equals("BDDDS"))) {
						System.out.println("************************************");
						System.out.println(selectedGap.occur.cardinality());
						System.out.println("************************************");
					}*/
					if(new JudgeClosed().isClosed(selectedGap,base,selectedGap.occur.cardinality())) {
						//new HandleFile().outputResult(currentPattern,selectedGap);
						System.out.println(currentPattern + " " + "[ " + selectedGap.lowerBound +", "+ selectedGap.upperBound+ " ]"); 
					}
					break;
				}
			}
			else {
				selectedGap = new Attribute(-1,-1);
			}
		}
		return selectedGap;
	}
	
	public Map<Integer,List<Integer>> getGapSetNaive() {
		Map<Integer,List<Integer>> candidateGap = new TreeMap<Integer,List<Integer>>();
		for(int j = 0;j <= HandleFile.maxLength - 2;j++)
		{
			List<Integer> right = new ArrayList<Integer>();
			for(int i = j;i < j + HandleFile.maxLength ;i++)
			{
				if(i <= HandleFile.maxLength - 2)
				{
					right.add(i);
				}
				else
				{
					break;
				}				
			}
			candidateGap.put(j,right);
			
		}
		return candidateGap;
	}
	
	/*public void getCandidateGapSet(Map<Integer,List<Integer>> candidateGapSet,Set<Attribute> set) {
		Iterator<Attribute> iterator3 = set.iterator();
		while(iterator3.hasNext()) {
			Attribute gap = iterator3.next();
			if(!candidateGapSet.containsKey(gap.lowerBound)) {
				List<Integer> upperList = new ArrayList<Integer>();
				upperList.add(gap.upperBound);
				candidateGapSet.put(gap.lowerBound,upperList);
			}
			else {
				if(!candidateGapSet.get(gap.lowerBound).contains(gap.upperBound)) {
					Integer i = 0;
					for(;i < candidateGapSet.get(gap.lowerBound).size();i++) {
						if(gap.upperBound < candidateGapSet.get(gap.lowerBound).get(i)) {
							candidateGapSet.get(gap.lowerBound).add(i,gap.upperBound);
							break;
						}
					}
					if(i == candidateGapSet.get(gap.lowerBound).size()) {
						candidateGapSet.get(gap.lowerBound).add(gap.upperBound);
					}
				}
			}
			Iterator<Integer> iterator4 = candidateGapSet.keySet().iterator();
			while(iterator4.hasNext()) {
				 Integer leftKey = iterator4.next();
				 if((gap.lowerBound > leftKey) && (gap.upperBound > candidateGapSet.get(leftKey).get(0)) && (!candidateGapSet.get(leftKey).contains(gap.upperBound))) {
					 Integer j = 0;
					 for(;j < candidateGapSet.get(leftKey).size();j++) {
						 if(gap.upperBound < candidateGapSet.get(leftKey).get(j)) {
							 candidateGapSet.get(leftKey).add(j,gap.upperBound);
							 break;
						 }
					 }
					 if(j == candidateGapSet.get(leftKey).size()) {
						 candidateGapSet.get(leftKey).add(gap.upperBound);
					 }
				 }
			}
		}
	}*/
	
	public Attribute findMinCompactGap(Map<Integer,List<Integer>> candidateGapSet,List<Attribute> list,Map<Integer,Integer> gapIndex) {
		Attribute minGap = new Attribute(0,32767,HandleFile.lineNumber);
		Iterator<Integer> iterator7 = gapIndex.keySet().iterator();
		while(iterator7.hasNext()) {
			Integer left = iterator7.next();
			Integer candidateGapSetIndex = gapIndex.get(left);
			Attribute currentGap = new Attribute(left,candidateGapSet.get(left).get(candidateGapSetIndex),HandleFile.lineNumber);
			if((currentGap.upperBound - currentGap.lowerBound < minGap.upperBound - minGap.lowerBound) || (currentGap.upperBound - currentGap.lowerBound == minGap.upperBound - minGap.lowerBound && currentGap.lowerBound < minGap.lowerBound)) {
				minGap = currentGap;
			}
		}
		
		minGap = combineGap(minGap,list);
		
		if(gapIndex.get(minGap.lowerBound) != candidateGapSet.get(minGap.lowerBound).size() - 1) {
			Integer newindex = gapIndex.get(minGap.lowerBound) + 1;
			gapIndex.put(minGap.lowerBound,newindex);
		}
		else {
			gapIndex.remove(minGap.lowerBound);
		}
		return minGap;
	}
	
	public void initialiseIndex(Map<Integer,List<Integer>> candidateGapSet,Map<Integer,Integer> gapIndex,Attribute subGap) {		
		List<Integer> delete = new ArrayList<Integer>();
		Iterator<Integer> iterator5 = gapIndex.keySet().iterator();
		while(iterator5.hasNext()) {
			Integer left = iterator5.next();
			int flag = 0;
			do {
				Integer candidateGapSetIndex = gapIndex.get(left);
				Attribute currentGap = new Attribute(left,candidateGapSet.get(left).get(candidateGapSetIndex),HandleFile.lineNumber);		
				if((currentGap.upperBound - currentGap.lowerBound < subGap.upperBound - subGap.lowerBound) || (currentGap.upperBound - currentGap.lowerBound == subGap.upperBound - subGap.lowerBound && currentGap.lowerBound < subGap.lowerBound)) {
					if(gapIndex.get(left) != candidateGapSet.get(left).size() - 1) {
						Integer newindex2 = gapIndex.get(left) + 1;
						gapIndex.put(left,newindex2);
					}
					else {
						delete.add(left);
						flag = 1;
					}
				}
				else {
					flag = 1;
				}
			} while(flag == 0);
		}
		
		if(!delete.isEmpty()) {
			Iterator<Integer> iterator6 = delete.iterator();
			while(iterator6.hasNext()) {
				Integer deleteIndex = iterator6.next();
				gapIndex.remove(deleteIndex);
			}
		}
	}
	
	public Attribute combineGap(Attribute candidateGap,List<Attribute> list) {
		Iterator<Attribute> iterator8 = list.iterator();
		while(iterator8.hasNext()) {
			Attribute originalGap = iterator8.next();
			if((originalGap.lowerBound >= candidateGap.lowerBound) && (originalGap.upperBound <= candidateGap.upperBound)) {
				candidateGap.occur.or(originalGap.occur);
			}
		}			
		return candidateGap;
	}
}
