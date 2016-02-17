package test;

import java.util.ArrayList;
import java.util.BitSet;

import tools.NaivePatternExtract;
import util.SubElementList;
import util.Value;

/**
 * author: Hao 
 * date:Feb 17, 2016
 * time:3:39:39 PM
 * purpose:
 */
public class NaivePatternExtractTest {
	public static void main(String[] args) {
		coveredMethodTest();
	}
	
	private static void coveredMethodTest(){
		BitSet closure = constructBitSet(new int[] {0,1,2,3});
		Value value = new Value(closure);
		value.addGenerator(constructBitSet(new int[] {0,1,2}));
		value.addGenerator(constructBitSet(new int[] {1,2,3}));
		value.addGenerator(constructBitSet(new int[] {0,2,3}));
		value.addGenerator(constructBitSet(new int[] {0,1,3}));
		
		ArrayList<BitSet> allSub = SubElementList.INSTANCE.getSub(constructBitSet(new int[] {0,1,2,3}));
		System.out.println(value.toBitSetString()+"\n");
		for(BitSet sub : allSub){
			System.out.println(sub + " is covered by value? " + NaivePatternExtract.INSTANCE.covered(sub, value));
		}
	}
	
	private static BitSet constructBitSet(int[] bs){
		BitSet tmp = new BitSet();
		for(int i : bs){
			tmp.set(i, true);
		}
		return tmp;
	}
}
