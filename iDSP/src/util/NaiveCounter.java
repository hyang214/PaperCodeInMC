package util;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import naive.SubElementList;

public enum NaiveCounter {
	INSTANCE;
	public static int count(ResultSet rSet){
		int count = 0;
		
		Collections.sort(rSet.posResults, new Comparator<Pattern>() {

			@Override
			public int compare(Pattern o1, Pattern o2) {
				int c1 = o1.posSeqBitSet.cardinality() - o1.negSeqBitSet.cardinality();
				int c2 = o2.posSeqBitSet.cardinality() - o2.negSeqBitSet.cardinality();
				if(c1 > c2)
					return -1;
				else if(c1 < c2)
					return 1;
				return 0;
			}
		});
		
		
		double totalLength = 0;
		double totalSize = 0;
		int maxLength = Integer.MIN_VALUE;
		int maxSize = Integer.MIN_VALUE;
		
		for(Pattern p : rSet.posResults){
			
			if(p.elementList.size() > maxLength)
				maxLength = p.elementList.size();
			
			int sum = 1;
			p.print4();
			for(PatternNode pN:p.elementList){
				if(pN.closed.closed.cardinality() > maxSize)
					maxSize = pN.closed.closed.cardinality();
				
				ArrayList<BitSet> subList = SubElementList.INSTANCE.getSub(pN.closed.closed); 
				int type = subList.size();
				for(BitSet sub:subList){
//					System.out.println("Sub:"+sub);
					boolean covered = false;
					for(BitSet g:pN.generatorSet){
						BitSet tmp = (BitSet)g.clone();
						tmp.and(sub);
						if(tmp.equals(sub)){
//							System.out.println("    tmp:"+tmp);
							covered = true;
						}
					}
					if(covered){
//						System.out.println("Covered");
						type--;
					}
					else{
						System.out.println(sub+" "+sub.cardinality());
						totalSize += sub.cardinality();
					}
				}
				sum *= type;
			}
			totalLength += sum*p.elementList.size();
			System.out.println("      Type: "+sum);
			count += sum;
		}
		
		double avgSize = totalSize / totalLength;
		double avgLength = totalLength / count;
		
		System.out.println("Avg size:"+avgSize);
		System.out.println("Avg length:"+avgLength);
		System.out.println("Max size:"+maxSize);
		System.out.println("Max length:"+maxLength);
		
		return count;
	}
}
