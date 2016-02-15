package util;

import java.util.HashSet;

public enum Minimize {
	INSTANCE;
	HashSet<Pattern> minSet = new HashSet<Pattern>();
	
	public HashSet<Pattern> minimize(HashSet<Pattern> patternSet){
		
		return minSet;
	}
}
