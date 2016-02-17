package setImprove3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class JudgeClosed {
	public boolean isClosed(Attribute gap,Attribute base,int sup) {//a judgment about if this pattern is closed
		HashMap<String,HashMap<Integer,List<Integer>>> closedjudgemap = new HashMap<String,HashMap<Integer,List<Integer>>>();
		HashMap<Integer,List<Integer>> linehashmap;
		List<Integer> wardlist;
		Set<Integer> set = new TreeSet<Integer>();//scan every list of the last pattern
		set.addAll(base.location.keySet());//筛选所有满足gap的位置
		Iterator<Integer> iterator = set.iterator();
		while(iterator.hasNext()) {
			Integer rowNumber = iterator.next();
			for(int i = 0;i < base.location.get(rowNumber).size();i++) {
				if((base.location.get(rowNumber).get(i).get(2) >= gap.lowerBound) && (base.location.get(rowNumber).get(i).get(3) <= gap.upperBound)) {
					for(int j = gap.lowerBound;j <= gap.upperBound;j++) {				
						int leftlength = base.location.get(rowNumber).get(i).get(0) - j - 1;
						if(leftlength >= 0) {
							String backwarditem = HandleFile.readlistlist.get(rowNumber).get(leftlength);
						    if (!closedjudgemap.containsKey(backwarditem)) {
						    	wardlist = new ArrayList<Integer>();
						    	wardlist.add(0,1);
						    	wardlist.add(1,0);
						    	linehashmap = new HashMap<Integer,List<Integer>>();
						    	linehashmap.put(rowNumber,wardlist);
						    	closedjudgemap.put(backwarditem,linehashmap);
						    }
						    else {
						    	if(!closedjudgemap.get(backwarditem).containsKey(rowNumber)) {
						    		wardlist = new ArrayList<Integer>();
							    	wardlist.add(0,1);
							    	wardlist.add(1,0);
							    	closedjudgemap.get(backwarditem).put(rowNumber,wardlist);
						    	}
						    	else {
						    		if(closedjudgemap.get(backwarditem).get(rowNumber).get(0) == 0) {
						    			closedjudgemap.get(backwarditem).get(rowNumber).set(0,1);
						    		}
						    	}
						    }
						}
						
						int rightlength = base.location.get(rowNumber).get(i).get(1) + j + 1;
						if(rightlength <= HandleFile.readlistlist.get(rowNumber).size() - 1) {
						    String forwarditem = HandleFile.readlistlist.get(rowNumber).get(rightlength);
						    if (!closedjudgemap.containsKey(forwarditem)) {
						    	wardlist = new ArrayList<Integer>();
						    	wardlist.add(0,0);
						    	wardlist.add(1,1);
						    	linehashmap = new HashMap<Integer,List<Integer>>();
						    	linehashmap.put(rowNumber,wardlist);
						    	closedjudgemap.put(forwarditem,linehashmap);
						    }
						    else {
						    	if(!closedjudgemap.get(forwarditem).containsKey(rowNumber)) {
						    		wardlist = new ArrayList<Integer>();
							    	wardlist.add(0,0);
							    	wardlist.add(1,1);
							    	closedjudgemap.get(forwarditem).put(rowNumber,wardlist);
						    	}
						    	else {
						    		if(closedjudgemap.get(forwarditem).get(rowNumber).get(1) == 0) {
						    			closedjudgemap.get(forwarditem).get(rowNumber).set(1,1);
						    		}
						    	}					    	
						    }
						}
					}
				}
				/*else {
					if((base.location.get(rowNumber).get(i).get(2) == gap.lowerBound) && (base.location.get(rowNumber).get(i).get(3) < gap.upperBound)) {
						int leftlength = base.location.get(rowNumber).get(i).get(0) - gap.upperBound - 1;
						if(leftlength >= 0) {
							String backwarditem = HandleFile.readlistlist.get(rowNumber).get(leftlength);
						    if (!closedjudgemap.containsKey(backwarditem)) {
						    	wardlist = new ArrayList<Integer>();
						    	wardlist.add(0,1);
						    	wardlist.add(1,0);
						    	linehashmap = new HashMap<Integer,List<Integer>>();
						    	linehashmap.put(rowNumber,wardlist);
						    	closedjudgemap.put(backwarditem,linehashmap);
						    }
						    else {
						    	if(!closedjudgemap.get(backwarditem).containsKey(rowNumber)) {
						    		wardlist = new ArrayList<Integer>();
							    	wardlist.add(0,1);
							    	wardlist.add(1,0);
							    	closedjudgemap.get(backwarditem).put(rowNumber,wardlist);
						    	}
						    	else {
						    		if(closedjudgemap.get(backwarditem).get(rowNumber).get(0) == 0) {
						    			closedjudgemap.get(backwarditem).get(rowNumber).set(0,1);
						    		}
						    	}
						    }
						}
						int rightlength = base.location.get(rowNumber).get(i).get(1) + gap.upperBound + 1;
						if(rightlength <= HandleFile.readlistlist.get(rowNumber).size() - 1) {
						    String forwarditem = HandleFile.readlistlist.get(rowNumber).get(rightlength);
						    if (!closedjudgemap.containsKey(forwarditem)) {
						    	wardlist = new ArrayList<Integer>();
						    	wardlist.add(0,0);
						    	wardlist.add(1,1);
						    	linehashmap = new HashMap<Integer,List<Integer>>();
						    	linehashmap.put(rowNumber,wardlist);
						    	closedjudgemap.put(forwarditem,linehashmap);
						    }
						    else {
						    	if(!closedjudgemap.get(forwarditem).containsKey(rowNumber)) {
						    		wardlist = new ArrayList<Integer>();
							    	wardlist.add(0,0);
							    	wardlist.add(1,1);
							    	closedjudgemap.get(forwarditem).put(rowNumber,wardlist);
						    	}
						    	else {
						    		if(closedjudgemap.get(forwarditem).get(rowNumber).get(1) == 0) {
						    			closedjudgemap.get(forwarditem).get(rowNumber).set(1,1);
						    		}
						    	}					    	
						    }
						}
					}
					else {
						if((base.location.get(rowNumber).get(i).get(2) > gap.lowerBound) && (base.location.get(rowNumber).get(i).get(3) == gap.upperBound)) {
							int leftlength = base.location.get(rowNumber).get(i).get(0) - gap.lowerBound - 1;
							if(leftlength >= 0) {
								String backwarditem = HandleFile.readlistlist.get(rowNumber).get(leftlength);
							    if (!closedjudgemap.containsKey(backwarditem)) {
							    	wardlist = new ArrayList<Integer>();
							    	wardlist.add(0,1);
							    	wardlist.add(1,0);
							    	linehashmap = new HashMap<Integer,List<Integer>>();
							    	linehashmap.put(rowNumber,wardlist);
							    	closedjudgemap.put(backwarditem,linehashmap);
							    }
							    else {
							    	if(!closedjudgemap.get(backwarditem).containsKey(rowNumber)) {
							    		wardlist = new ArrayList<Integer>();
								    	wardlist.add(0,1);
								    	wardlist.add(1,0);
								    	closedjudgemap.get(backwarditem).put(rowNumber,wardlist);
							    	}
							    	else {
							    		if(closedjudgemap.get(backwarditem).get(rowNumber).get(0) == 0) {
							    			closedjudgemap.get(backwarditem).get(rowNumber).set(0,1);
							    		}
							    	}
							    }
							}
							int rightlength = base.location.get(rowNumber).get(i).get(1) + gap.lowerBound + 1;
							if(rightlength <= HandleFile.readlistlist.get(rowNumber).size() - 1) {
							    String forwarditem = HandleFile.readlistlist.get(rowNumber).get(rightlength);
							    if (!closedjudgemap.containsKey(forwarditem)) {
							    	wardlist = new ArrayList<Integer>();
							    	wardlist.add(0,0);
							    	wardlist.add(1,1);
							    	linehashmap = new HashMap<Integer,List<Integer>>();
							    	linehashmap.put(rowNumber,wardlist);
							    	closedjudgemap.put(forwarditem,linehashmap);
							    }
							    else {
							    	if(!closedjudgemap.get(forwarditem).containsKey(rowNumber)) {
							    		wardlist = new ArrayList<Integer>();
								    	wardlist.add(0,0);
								    	wardlist.add(1,1);
								    	closedjudgemap.get(forwarditem).put(rowNumber,wardlist);
							    	}
							    	else {
							    		if(closedjudgemap.get(forwarditem).get(rowNumber).get(1) == 0) {
							    			closedjudgemap.get(forwarditem).get(rowNumber).set(1,1);
							    		}
							    	}					    	
							    }
							}
						}
					}
				}*/
			}
		}
		
		Integer sum1 = 0;
		Integer sum2 = 0;
		Iterator<String> iteclosedjudge1 = closedjudgemap.keySet().iterator();
		while(iteclosedjudge1.hasNext()) {
			String lritem1 = iteclosedjudge1.next();
			Iterator<Integer> iteclosedjudge2 = closedjudgemap.get(lritem1).keySet().iterator();
			while(iteclosedjudge2.hasNext()) {
				Integer lritem2 = iteclosedjudge2.next();
				sum1 = sum1 + closedjudgemap.get(lritem1).get(lritem2).get(0);
				sum2 = sum2 + closedjudgemap.get(lritem1).get(lritem2).get(1);	
			}
			if((sum1 == sup) || (sum2 == sup)) {
				return false;
			}
			sum1 = 0;
			sum2 = 0;
		}
		return true;
	}
}
