package setBaseline;

import java.util.BitSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Attribute implements Comparable<Object> {
	public String pattern;	
	public Map<Integer,List<List<Integer>>> location;
	public Integer lowerBound;
	public Integer upperBound;
	public BitSet occur;
	
	public Attribute() {
		
	}
	
	public Attribute(String pattern) {
		this.pattern = pattern;
		location = new HashMap<Integer,List<List<Integer>>>();
	}
	
	public Attribute(int lowerBound,int upperBound) {
		this.lowerBound = lowerBound;
		this.upperBound = upperBound;
	}
	
	public Attribute(int lowerBound,int upperBound,int length) {
		this.lowerBound = lowerBound;
 	    this.upperBound = upperBound;
 	    occur = new BitSet(length);
	}
	@Override
	public boolean equals(Object attribute1) {
		Attribute attribute = (Attribute) attribute1;
		if((this.lowerBound > attribute.lowerBound) || (this.lowerBound == attribute.lowerBound && this.upperBound > attribute.upperBound)){
			return false;
		}
		else {
			if((this.lowerBound < attribute.lowerBound) || (this.lowerBound == attribute.lowerBound && this.upperBound < attribute.upperBound)) {
				return false;
			}
			else {
				return true;
			}
		}
	}
	@Override
	public int hashCode() {
		int result = 17;  
	    result = result * 31 + lowerBound.hashCode();  
	    result = result * 31 + upperBound.hashCode();  
	    return result;
	}
	@Override
	public int compareTo(Object attribute1) {
		Attribute attribute = (Attribute) attribute1;
		if((this.lowerBound > attribute.lowerBound) || (this.lowerBound == attribute.lowerBound && this.upperBound > attribute.upperBound)) {
			return 1;
		}
		else {
			if((this.lowerBound < attribute.lowerBound) || (this.lowerBound == attribute.lowerBound && this.upperBound < attribute.upperBound)) {
				return -1;
			}
			else {
				return 0;
			}
		}
	}

}
