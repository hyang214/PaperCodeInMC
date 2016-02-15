//package naive;
//
//import java.util.ArrayList;
//import java.util.BitSet;
//import java.util.HashMap;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Set;
//import java.util.Vector;
//
//import dke.scu.edu.gdsp.impl.Element;
//import dke.scu.edu.gdsp.impl.Node;
//import dke.scu.edu.gdsp.impl.Pattern;
//import dke.scu.edu.gdsp.util.ItemMap;
//import dke.scu.edu.gdsp.util.Parameter;
//import dke.scu.edu.gdsp.util.PatternNode;
//
//public class NaiveCandidateGenInBoth
//{
//
//	private NodeMap index;
//	public ArrayList<Pattern> posResults = new ArrayList<Pattern>();
//	public ArrayList<Pattern> negResults = new ArrayList<Pattern>();
//	private ItemMap itemMap;
//
//	public NaiveCandidateGenInBoth(NodeMap index)
//	{
//		this.index = index;
//		this.itemMap = index.itemMap;
//	}
//
//
//	public void generator(Parameter parameter)
//	{
//		/* choose an element in the elmementMap as 1-length Pattern and grow */
//		long start = System.currentTimeMillis();
////		for(Pattern p:init(parameter))
//		ArrayList<Pattern> posCloseds = new ArrayList<Pattern>();
//		ArrayList<Pattern> negCloseds = new ArrayList<Pattern>();
//		naiveInit(parameter, posCloseds, negCloseds);
//		for(Pattern p:posCloseds)
//		{
//			Candidate_Gen_Pos(p, 0, parameter);
//		}
//		for(Pattern p:negCloseds)
//		{
//			Candidate_Gen_Neg(p, 0, parameter);
//		}
//		long end = System.currentTimeMillis();
//		System.out.println("Time Cost of Candidate Generate:"+(end-start));
//		
////		System.out.println("Pos *******************************************");
////		for(Pattern p : posResults)
////		{
////			p.itemMap = itemMap;
////			p.print();
////		}
////		System.out.println("Neg *******************************************");
////		for(Pattern p : negResults)
////		{
////			p.itemMap = itemMap;
////			p.print();
////		}
//
////		long start,end;
//		start = System.currentTimeMillis();
//		
////		System.out.println("Merged******************************************");
//		
//		posResults = merge(posResults);
//		negResults = merge(negResults);
//		end = System.currentTimeMillis();
//		System.out.println("Time Cost of Merge candidate:"+(end-start));
////		results = merge2(results);
//		
////		System.out.println("Pos *******************************************");
////		for(Pattern p : posResults)
////		{
////			p.itemMap = itemMap;
////			p.print();
////		}
////		System.out.println("Neg *******************************************");
////		for(Pattern p : negResults)
////		{
////			p.itemMap = itemMap;
////			p.print();
////		}
//		start = System.currentTimeMillis();
//		
//		posResults = minimum(posResults);
//		negResults = minimum(negResults);
//		
//		end = System.currentTimeMillis();
//		System.out.println("Time Cost of Minimum test: "+(end-start));
////		
////		start = System.currentTimeMillis();
////		System.out.println("Redundancy Time Cost: "+(start-end));
//	}
//
//	private void naiveInit(Parameter parameter, ArrayList<Pattern> posCloseds, ArrayList<Pattern> negCloseds) {
//		List<Node> posClosedNode = new ArrayList<Node>();
//		List<Node> negClosedNode = new ArrayList<Node>();
//		
//		for(Node node:index.getNodeList()){
//			if(posBelowAlpha(node, parameter)){
//				PatternNode pn = new PatternNode(node);
//				
//			}
//			
//		}
//	}
//
//
//	private ArrayList<Pattern> minimum(ArrayList<Pattern> resultsSet) {
//		
//		Set<Pattern> remove = new HashSet<Pattern>();
//		
//		for(int i = 0 ; i <  resultsSet.size() ; i++)
//		{
//			for(int j = i+1 ; j <  resultsSet.size() ; j++)
//			{	
//				Pattern p1 = resultsSet.get(i);
//				Pattern p2 = resultsSet.get(j);
//				
//				if(p1.getSize() == p2.getSize())
//					continue;
//				
//				/* a is the shorter pattern
//				 * b is the longer pattern
//				 * */
//				Pattern a = p1.getSize() < p2.getSize() ? p1:p2;
//				Pattern b = p1.getSize() > p2.getSize() ? p1:p2;
//				
//				int check = 0;
//				
//				for(int k = 0 ; k < b.getSize() ; k ++){
//					if(a.getElementList().get(check).valueEquals(b.getElement(k))){
//						check++;
//					}
//				}
//				if(check == a.getSize())
//					remove.add(b);
//			}
//		}
//		
////		System.out.println("****************** minimum **************************");
////		for(Pattern p : remove)
////		{
////			p.itemMap = this.itemMap;
////			p.print();
////		}
// 
//		resultsSet.removeAll(remove);
//		
//		return resultsSet;
//	}
//
//
//	private ArrayList<Pattern> redundancy(ArrayList<Pattern> resultsSet)
//	{
//		Set<Pattern> remove = new HashSet<Pattern>();
//		for(int i = 0 ; i <  resultsSet.size() ; i++)
//		{
//			for(int j = i+1 ; j <  resultsSet.size() ; j++)
//			{	
//				Pattern p1 = resultsSet.get(i);
//				Pattern p2 = resultsSet.get(j);
//				
//				if(p1.getSize() != p2.getSize())
//					continue;
//				
//				Pattern r = patternRedundancy(p1, p2);
//				if(r != null)
//					remove.add(r);
//			}
//		}
//		
////		System.out.println("****************** redundancy **************************");
////		for(Pattern p : remove)
////		{
////			p.itemMap = this.itemMap;
////			p.print();
////		}
// 
//		resultsSet.removeAll(remove);
//		
//		return resultsSet;
//	}
//
//	/* merge pattern method 2*/
//	private ArrayList<Pattern> merge2(ArrayList<Pattern> patternSet)
//	{
//		ArrayList<Pattern> results = new ArrayList<Pattern>();
//		HashMap<Integer,ArrayList<Pattern>> groups = groupByLength(patternSet);
//		Set<Integer> keys = groups.keySet();
//		for(Integer i : keys)
//		{
//			results.addAll(merge(groups.get(i)));
//		}
//		return results;
//	}
//	
//	/* group pattern by the length */
//	private HashMap<Integer,ArrayList<Pattern>> groupByLength(ArrayList<Pattern> patternSet)
//	{
//		HashMap<Integer,ArrayList<Pattern>> groups = new HashMap<Integer,ArrayList<Pattern>>();
//		for(Pattern p:patternSet)
//		{
//			int i = p.getSize();
//			ArrayList<Pattern> list = groups.get(i);
//			if(list == null){
//				list = new ArrayList<Pattern>();
//				groups.put(i, list);
//			}
//			list.add(p);
//		}
//		return groups;
//	}
//	
//	/* merge pattern*/
//	private ArrayList<Pattern> merge(ArrayList<Pattern> patternSet)
//	{
//		Set<Pattern> mergedSet = new HashSet<Pattern>();
//		Set<Pattern> used = new HashSet<Pattern>();
//		for(int i = 0 ; i <  patternSet.size() ; i++)
//		{
//			Pattern p1 = patternSet.get(i).clone();
//			boolean changing = true;
//			while(changing)
//			{
//				changing = false;
//				for(int j = 0 ; j <  patternSet.size() ; j++)
//				{
//					Pattern p2 = patternSet.get(j);
//					
//					if(p1.getSize() != p2.getSize())
//						continue;
//					
//					Pattern changedP  = patternMerge(p1, p2);
//					if(changedP != null)
//					{
//						used.add(patternSet.get(i));
//						used.add(patternSet.get(j));
//						changing = true;
//						mergedSet.add(changedP);
//					}
//				}
//			}
//		}
//				
//		patternSet.removeAll(used);
//		
////		System.out.println("******************no change patterns******************");
////		for(Pattern p : patternSet)
////		{
////			p.itemMap = itemMap;
////			p.print();
////		}
////		System.out.println("");
//		
//		ArrayList<Pattern> next = new ArrayList<Pattern>(mergedSet);
//		next.addAll(patternSet);
//		 
//		if(used.size() != 0)
//			patternSet = merge(next);
//		return patternSet;
//	}
//
//
//	private void Candidate_Gen_Pos(Pattern pattern, int layer, Parameter parameter)
//	{
//		if (pattern.posSeqBitSet.cardinality() < parameter.alpha)
//			return;
//		
//		/* if this pattern is already a pattern, add it into the patternMap*/
//		if ( pattern.negSeqBitSet.cardinality() <= parameter.beta)
//		{	
//			//pattern.posSeqBitSet.cardinality() >= parameter.alpha &&
////			checkResult(pattern);
//			this.posResults.add(pattern);
//			return;
//		}
//
//		/* else continue grow pattern: get value of the latest element of this pattern,  get this element in posIndex*/
//		List<Pattern> candidates = generateAndClosed_Pos(pattern, parameter);
//		if(candidates == null)
//		{
//			System.out.println("null at this");
//			return;
//		}
//		for(Pattern p:candidates)
//			Candidate_Gen_Pos(p, layer+1, parameter);
//		
//		return;
//	}
//	
//	private void Candidate_Gen_Neg(Pattern pattern, int layer, Parameter parameter)
//	{
//		if (pattern.negSeqBitSet.cardinality() < parameter.alpha)
//			return;
//		
////		System.out.println(layer+" "+pattern.toString());
//		/* if this pattern is already a pattern, add it into the patternMap*/
//		if (pattern.posSeqBitSet.cardinality() <= parameter.beta)
//		{	
//			//pattern.negSeqBitSet.cardinality() >= parameter.alpha && 
////			checkResult(pattern);
//			this.negResults.add(pattern);
//			return;
//		}
//
//		/* else continue grow pattern: get value of the latest element of this pattern,  get this element in posIndex*/
//		List<Pattern> candidates = generateAndClosed_Neg(pattern, parameter);
//		if(candidates == null)
//		{
//			System.out.println("null at this");
//			return;
//		}
//		for(Pattern p:candidates)
//			Candidate_Gen_Neg(p, layer+1, parameter);
//		
//		return;
//	}
//
//	private void checkResult(Pattern pattern) 
//	{
//		Vector<Integer> superIds = new Vector<Integer>();
//		for(int i=0; i<this.posResults.size(); i++)
//		{	
//			BitSet cP = new BitSet();
//			cP.set(0, pattern.elementList.size(), true);
//			BitSet cR = new BitSet();
//			cR.set(0, this.posResults.get(i).elementList.size(), true);
//			
//			for(int j=0; j<pattern.elementList.size(); j++)
//			{
//				for(int k=0; k<this.posResults.get(i).elementList.size(); k++)
//				{
//					if(cR.get(k)==false)
//						continue;
//					if(pattern.elementList.get(j).valueEquals(this.posResults.get(i).elementList.get(k)))
//					{
//						cP.clear(j);
//						cR.clear(k);
//						break;
//					}
//				}
//			}
//			if(cP.cardinality()==0)
//			{
//				/* cP is a subset of cR*/
//				superIds.add(new Integer(i));
//			}
//			if(cR.cardinality()==0)
//			{
//				return;
//			}
//
//		}
//		
//		for(int i=superIds.size()-1; i>=0; i--)
//		{
//			/*for there are two methods remove(Object obj), superIds contians Integer is also a type of Object, we need convert Integer to int*/
//			int removeId = (Integer)superIds.elementAt(i);
//			this.posResults.remove(removeId);
//		}
//		
//		this.posResults.add(pattern);
//	}
//
//	private boolean posBelowAlpha(Node f, Parameter parameter)
//	{
//		BitSet b = (BitSet)f.seqIdSet.clone();
//		b.and(parameter.posSeqId);
//		return (b.cardinality() < parameter.alpha)? true : false;
//	}
//	
//	private boolean negBelowAlpha(Node f, Parameter parameter)
//	{
//		BitSet b = (BitSet)f.seqIdSet.clone();
//		b.and(parameter.negSeqId);
//		return (b.cardinality() < parameter.alpha)? true : false;
//	}
//
//
//	public Pattern patternMerge(Pattern p1, Pattern p2)
//	{	
//		BitSet p1B = new BitSet();
//		p1B.set(0, p1.getSize(), true);
//		BitSet p2B = new BitSet();
//		p2B.set(0, p2.getSize(), true);
//		
//		/* only the patterns which has n-1 common patternNode and one different patternNode can merge*/
//		boolean oneDifference = false;
//		int k = 0;
//		for(int l = 0 ; l < p2.getSize() ; l ++)
//		{
//			int value = p1.getElement(k).compare(p2.getElement(l));
//			if(value == 0)
//			{
//				/*p1.getElement(k) has no relationship with p2.getElement(l)*/
//				break;
//			}
//			else if(value == 1)
//			{
//				/*p1.getElement(k) equals with p2.getElement(l)*/
//				p1B.set(k, false);
//				p2B.set(l, false);
//				k++;
//			}
//			else if(value == 2)
//			{
//				if(oneDifference)
//					break;
//				
//				/*p1.getElement(k) is sub item set of p2.getElement(l)*/
//				p1B.set(k, false);
//				oneDifference = true;
//				k++;
//			}
//			else
//			{
//				if(oneDifference)
//					break;
//				
//				/*p2.getElement(l) is sub item set of p1.getElement(k)*/
//				p2B.set(l, false);
//				oneDifference = true;
//				k++;
//			}
//		}
//		
//		if( p2B.cardinality() == 0 && p1B.cardinality() == 0)
//			return null;
//			
////		if(p1B.cardinality() == 0)
////		{
////			/* p1 merges with p2, keep p2*/
////			System.out.println("Merge:  ");
////			p1.print();
////			p2.print();
////			p2.mergePattern(p1);
////			System.out.println("To:");
////			p2.print();
////			return p2;
////		}
//		if(p2B.cardinality() == 0)
//		{
//			/* p2 merges with p1, keep p1*/
////			System.out.println("Merge:  ");
////			p1.print();
////			p2.print();
//			p1.mergePattern(p2);
////			System.out.println("To:");
////			p1.print();
//			return p1;
//		}
//
//		return null;
//	}
//	
//	public Pattern patternRedundancy(Pattern p1, Pattern p2)
//	{	
//		BitSet p1B = new BitSet();
//		p1B.set(0, p1.getSize(), true);
//		BitSet p2B = new BitSet();
//		p2B.set(0, p2.getSize(), true);
//		
//		/* only the patterns which has n-1 common patternNode and one different patternNode can merge*/
//		boolean oneDifference = false;
//		int k = 0;
//		for(int l = 0 ; l < p2.getSize() ; l ++)
//		{
//			int value = p1.getElement(k).compare2(p2.getElement(l));
//			if(value == 0)
//			{
//				/*p1.getElement(k) has no relationship with p2.getElement(l)*/
//				break;
//			}
//			else if(value == 1)
//			{
//				/*p1.getElement(k) equals with p2.getElement(l)*/
//				p1B.set(k, false);
//				p2B.set(l, false);
//				k++;
//			}
//			else if(value == 2)
//			{
//				if(oneDifference)
//					break;
//				
//				/*p1.getElement(k) is sub element set of p2.getElement(l)*/
//				p1B.set(k, false);
//				oneDifference = true;
//				k++;
//			}
//			else
//			{
//				if(oneDifference)
//					break;
//				
//				/*p2.getElement(l) is sub element set of p1.getElement(k)*/
//				p2B.set(l, false);
//				oneDifference = true;
//				k++;
//			}
//		}
//		
//		if( p2B.cardinality() == 0 && p1B.cardinality() == 0)
//			return null;
//			
//		if(p1B.cardinality() == 0)
//		{
//			/* p1 are redundancy*/
//			System.out.println("Redundancy:  ");
//			p1.print();
//			p2.print();
//			System.out.println("To:");
//			p1.print();
//			return p1;
//		}
//		if(p2B.cardinality() == 0)
//		{
//			/* p2 are redundancy*/
//			System.out.println("Redundancy:  ");
//			p1.print();
//			p2.print();
//			System.out.println("To:");
//			p2.print();
//			return p2;
//		}
//
//		return null;
//	}
//}
