package util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import util.Pattern;

public class ResultSet {

	public ArrayList<Pattern> posResults;
	public HashMap<Integer, ArrayList<Pattern>> posLengthMap;
	
	public ResultSet(){
		this.posResults = new ArrayList<Pattern>();
		this.posLengthMap = new HashMap<Integer, ArrayList<Pattern>>();
	}
	
	public void addPosPattern(Pattern p){
		this.posResults.add(p);
		
		for(PatternNode pn:p.elementList){
			pn.generatorSet = pn.closed.generators;
		}

		/* generate a map to reduce the time cost of minimum test and merge operation */
		ArrayList<Pattern> set = posLengthMap.get(p.getSize());
		if(set == null){
			set = new ArrayList<Pattern>();
			posLengthMap.put(p.getSize(), set);
		}
		set.add(p);
	}
	
}
