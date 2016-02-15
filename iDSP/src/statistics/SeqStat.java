package statistics;

import java.util.HashSet;
import java.util.function.Consumer;

import util.Parameter;
import util.Sequences;

public class SeqStat {
	
	public static void main(String[] args) {
		Parameter parameter = new Parameter();
		System.out.println(parameter.posFileName);
		Sequences sequences = new Sequences(parameter);
		HashSet<String> alphbet = new HashSet<String>(); 
		double elementCount = 0;
		double seqCount = 0;
		double itemCount = 0;
		int maxL = Integer.MIN_VALUE;
		int minL = Integer.MAX_VALUE;
		int maxS = Integer.MIN_VALUE;
		int minS = Integer.MAX_VALUE;
		for(String s:sequences.instances){
			seqCount ++;
			String[] list = s.split(";");
			if(list.length < minL){
				minL = list.length;
			}
			if(list.length > maxL){
				maxL = list.length;
			}
			for(String e:list){
				elementCount ++;
				String[] eL = e.split(",");
				if(eL.length < minS){
					minS = eL.length;
				}
				if(eL.length > maxS){
					maxS = eL.length;
				}
				for(String ee:eL){
					itemCount ++;
					alphbet.add(ee);
				}
			}
		}
		System.out.println("Total Element:" + elementCount);
		System.out.println("Sequences Count: "+seqCount);
		System.out.println("Number of Item: "+itemCount);
		System.out.println("Size of Alphbet: "+alphbet.size());
		itemCount = itemCount / elementCount;
		elementCount = elementCount / seqCount;
		System.out.println("avgLen: "+elementCount);
		System.out.println("avgSize: "+itemCount);
		System.out.println("Max L:"+maxL);
		System.out.println("Min L:"+minL);
		System.out.println("Max S:"+maxS);
		System.out.println("Min S:"+minS);
//		for(String s: alphbet){
//			System.out.println(s);
//		}
	}
}
