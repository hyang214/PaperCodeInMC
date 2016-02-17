package setImprove3;

import java.util.List;
import java.util.Map;

public class MainFunction {
	public static final double SUPP = 0.9;
	public static int frequency;
	
	public static void main(String[] args) throws Exception {
		long startTime = System.currentTimeMillis();
		String filePath = "input.txt";
		Map<String,Map<Integer,List<Integer>>> hashMap = new HandleFile().readText(filePath);
		double fre = SUPP * HandleFile.lineNumber;
		frequency = (int)fre;
		
		GeneratePattern a = new GeneratePattern();
		do {
			if(a.stack.empty()) {
				a.geneOneItem(hashMap);
			}
			else {
				if(a.stack.peek().get(0).pattern.length() == 1) {
					a.geneOtherPattern(a.stack.pop().get(0),hashMap);
				}
				else {
					a.geneOtherPattern(a.stack.pop(),hashMap);
				}
			}
		} while(!a.stack.empty());
		
		long endTime = System.currentTimeMillis();
		System.out.println("Time: " + (endTime-startTime));
	}
}
