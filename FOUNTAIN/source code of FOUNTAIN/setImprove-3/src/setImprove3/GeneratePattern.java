package setImprove3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.TreeSet;

public class GeneratePattern {
public Stack<List<Attribute>> stack = new Stack<List<Attribute>>();
	
	public void geneOneItem(Map<String,Map<Integer,List<Integer>>> itemHashMap) {
		String pattern = "";
		Iterator<String> iterator1 = itemHashMap.keySet().iterator();
		while(iterator1.hasNext()) {			
			pattern = iterator1.next();
			if(itemHashMap.get(pattern).size() >= MainFunction.frequency) {
				List<Attribute> information = new ArrayList<Attribute>();				
				Attribute baseInfor = new Attribute(pattern);
				
				Set<Integer> set = new TreeSet<Integer>();
				set.addAll(itemHashMap.get(pattern).keySet());
				Iterator<Integer> iterator2 = set.iterator();
				while(iterator2.hasNext()) {
					Integer rowNumber = iterator2.next();//current row number			
					List<List<Integer>> listList = new ArrayList<List<Integer>>();
					List<Integer> list;
					for(Integer i = 0;i < itemHashMap.get(pattern).get(rowNumber).size();i++) {
						list = new ArrayList<Integer>();
						list.add(0,itemHashMap.get(pattern).get(rowNumber).get(i));//the position of the item
						list.add(1,itemHashMap.get(pattern).get(rowNumber).get(i));
						list.add(2,-1);
						list.add(3,-1);
						listList.add(list);
					}			
					baseInfor.location.put(rowNumber,listList);
				}
				information.add(baseInfor);
				System.out.println(pattern);
				stack.push(information);
			}
			else {
				iterator1.remove();
			}
		}
	}
	
	public void geneOtherPattern(Attribute subPatternInfor,Map<String,Map<Integer,List<Integer>>> itemHashMap) {
		Iterator<String> iterator2 = itemHashMap.keySet().iterator();
		while(iterator2.hasNext()) {
			List<Attribute> information = new ArrayList<Attribute>();
			String lastItem = iterator2.next();
			String pattern = subPatternInfor.pattern + lastItem;
			Attribute baseInfor = new Attribute(pattern);
			List<List<Integer>> listList;
			
			Map<String,List<Attribute>> patternMap = new HashMap<String,List<Attribute>>();
			List<Attribute> attributeList = new ArrayList<Attribute>();
			Attribute patternGap;
						
			Set<Integer> set1 = new TreeSet<Integer>();//scan every list of the last pattern
			set1.addAll(subPatternInfor.location.keySet());
			Iterator<Integer> iterator3 = set1.iterator();
			while(iterator3.hasNext()) {
				Integer rowNumber = iterator3.next();//current row number
				if(itemHashMap.get(lastItem).containsKey(rowNumber)) {
					listList = new ArrayList<List<Integer>>();
					for(Integer i = 0;i < subPatternInfor.location.get(rowNumber).size();i++) {
						for(Integer j = 0;j < itemHashMap.get(lastItem).get(rowNumber).size();j++) {
							Integer diff = itemHashMap.get(lastItem).get(rowNumber).get(j) - subPatternInfor.location.get(rowNumber).get(i).get(1) - 1;
							int gapflag = 0;
							if(diff >= 0) {
								List<Integer> list = new ArrayList<Integer>();
								list.add(0,subPatternInfor.location.get(rowNumber).get(i).get(0));
								list.add(1,itemHashMap.get(lastItem).get(rowNumber).get(j));
								list.add(2,diff);
								list.add(3,diff);
								
								patternGap = new Attribute(diff,diff,HandleFile.lineNumber);
								patternGap.occur.set(rowNumber);		
								
								if(!baseInfor.location.containsKey(rowNumber)) {
									listList.add(list);
									baseInfor.location.put(rowNumber,listList);									
								}
								else {
									baseInfor.location.get(rowNumber).add(list);
								}
								
								Iterator<Attribute> iterator4 = attributeList.iterator();
								while(iterator4.hasNext()) {
									Attribute existedPattern = iterator4.next();
									if((patternGap.lowerBound == existedPattern.lowerBound) && (patternGap.upperBound == existedPattern.upperBound)) {
										gapflag = 1;
										existedPattern.occur.or(patternGap.occur);
										break;
									}
								}
								if(gapflag == 0) {
									attributeList.add(patternGap);									
								}								
							}								
						}
					}
				}
			}
			patternMap.put(pattern,attributeList);
			if(baseInfor.location.size() >= MainFunction.frequency) {		
				information.add(0,baseInfor);
				Attribute otherInfor = new JudgeFrequency().Judgement(patternMap,baseInfor);
				if(otherInfor.lowerBound != -1) {
					information.add(1,otherInfor);
					stack.push(information);
				}
			}
		}
	}
	
	public void geneOtherPattern(List<Attribute> patternInformation,Map<String,Map<Integer,List<Integer>>> itemHashMap) {
		Iterator<String> iterator5 = itemHashMap.keySet().iterator();
		while(iterator5.hasNext()) {
			List<Attribute> information = new ArrayList<Attribute>();
			String lastItem = iterator5.next();
			String pattern = patternInformation.get(0).pattern + lastItem;
			Attribute baseInfor = new Attribute(pattern);
			List<List<Integer>> listList;
			
			Map<String,List<Attribute>> patternMap = new HashMap<String,List<Attribute>>();
			List<Attribute> attributeList = new ArrayList<Attribute>();
			Attribute patternGap;		
			
			Set<Integer> set2 = new TreeSet<Integer>();//scan every list of the last pattern
			set2.addAll(patternInformation.get(0).location.keySet());
			Iterator<Integer> iterator6 = set2.iterator();
			while(iterator6.hasNext()) {
				Integer rowNumber = iterator6.next();//current row number
				if(itemHashMap.get(lastItem).containsKey(rowNumber)) {
					listList = new ArrayList<List<Integer>>();
					for(Integer i = 0;i < patternInformation.get(0).location.get(rowNumber).size();i++) {
						for(Integer j = 0;j < itemHashMap.get(lastItem).get(rowNumber).size();j++) {
							Integer diff = itemHashMap.get(lastItem).get(rowNumber).get(j) - patternInformation.get(0).location.get(rowNumber).get(i).get(1) - 1;
							int gapflag = 0;
							if(diff >= 0) {
								List<Integer> list = new ArrayList<Integer>();
								list.add(0,patternInformation.get(0).location.get(rowNumber).get(i).get(0));
								list.add(1,itemHashMap.get(lastItem).get(rowNumber).get(j));
								Integer min = patternInformation.get(0).location.get(rowNumber).get(i).get(2);
								Integer max = patternInformation.get(0).location.get(rowNumber).get(i).get(3);
								if(diff >= min && diff <= max) {
									list.add(2,min);
									list.add(3,max);
									patternGap = new Attribute(min,max,HandleFile.lineNumber);
								}
								else {
									if(diff < min) {
										list.add(2,diff);
										list.add(3,max);
										patternGap = new Attribute(diff,max,HandleFile.lineNumber);
									}
									else {
										list.add(2,min);
										list.add(3,diff);
										patternGap = new Attribute(min,diff,HandleFile.lineNumber);
									}
								}	
								patternGap.occur.set(rowNumber);		
								
								if(!baseInfor.location.containsKey(rowNumber)) {
									listList.add(list);
									baseInfor.location.put(rowNumber,listList);									
								}
								else {
									baseInfor.location.get(rowNumber).add(list);
								}
								
								Iterator<Attribute> iterator7 = attributeList.iterator();
								while(iterator7.hasNext()) {
									Attribute existedPattern = iterator7.next();
									if((patternGap.lowerBound == existedPattern.lowerBound) && (patternGap.upperBound == existedPattern.upperBound)) {
										gapflag = 1;
										existedPattern.occur.or(patternGap.occur);
										break;
									}
								}
								if(gapflag == 0) {
									attributeList.add(patternGap);									
								}
							}								
						}
					}
				}
			}
			patternMap.put(pattern,attributeList);
			if(baseInfor.location.size() >= MainFunction.frequency) {
				information.add(0,baseInfor);
				Attribute otherInfor = new JudgeFrequency().Judgement(patternMap,patternInformation.get(1),baseInfor);
				if(otherInfor.lowerBound != -1) {
					information.add(1,otherInfor);
					stack.push(information);
				}
			}
		}
	}
}
