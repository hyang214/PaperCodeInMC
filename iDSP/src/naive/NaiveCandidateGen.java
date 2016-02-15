package naive;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import util.ItemMap;
import util.Node;
import util.Parameter;
import util.Pattern;
import util.PatternNode;
import util.ResultSet;

public class NaiveCandidateGen {

	private NodeMap index;
	public ArrayList<Pattern> posResults = new ArrayList<Pattern>();
	public ArrayList<Pattern> negResults = new ArrayList<Pattern>();
	private ItemMap itemMap;
	
	public NaiveCandidateGen(NodeMap index, ItemMap itemMap) {
		this.index = index;
		this.itemMap = itemMap;
	}
	
	public void generator(Parameter parameter) {	
		for(Node node:index.getNodeList()){
			if(!posBelowAlpha(node, parameter)){
				PatternNode pn = new PatternNode(node);
				Pattern p = new Pattern(pn, itemMap, parameter);
				Candidate_Gen_Pos(p, 0, parameter);
			}
		}
		
		posResults = minimum2(posResults);
		posResults = collectGenerators(posResults);
		
	}

	private ArrayList<Pattern> collectGenerators(ArrayList<Pattern> resultSet) {
		for(Pattern p:resultSet){
			for(PatternNode pn: p.elementList){
				for(Node node:index.getNodeList()){
					if(node.closed.equals(pn.closed.closed))
						continue;
					
					/* e is sub-element of element, so e can be a generator of element */
					BitSet generator = (BitSet)node.closed.clone();
					generator.and(pn.closed.closed);
					if(generator.cardinality() > 0 && generator.equals(node.closed))
					{
						pn.addGenerator(generator);
					}
				}
			}
		}
		return resultSet;
	}

	private void init(Parameter parameter, ArrayList<Pattern> posCloseds, ArrayList<Pattern> negCloseds) {
		for(Node node:index.getNodeList()){
			if(!posBelowAlpha(node, parameter)){
				PatternNode pn = new PatternNode(node);
				Pattern p = new Pattern(pn, itemMap, parameter);
				posCloseds.add(p);
			}
		}
	}

	private void Candidate_Gen_Pos(Pattern pattern, int layer, Parameter parameter)
	{
		if (pattern.posSeqBitSet.cardinality() < parameter.alpha)
			return;
		
		/* if this pattern is already a pattern, add it into the patternMap*/
		if ( pattern.negSeqBitSet.cardinality() <= parameter.beta)
		{	
			//pattern.posSeqBitSet.cardinality() >= parameter.alpha &&
//			checkResult(pattern);
			posResults.add(pattern);
			return;
		}

		/* else continue grow pattern: get value of the latest element of this pattern,  get this element in posIndex*/
		for(Node node:index.getNodeList()){
			Pattern p = pattern.clone();
			if(p.addElement(new PatternNode(node), parameter)){
				Candidate_Gen_Pos(p, layer+1, parameter);
			}
		}
		
		return;
	}
	

	private void Candidate_Gen_Neg(Pattern pattern, int layer, Parameter parameter)
	{
		if (pattern.negSeqBitSet.cardinality() < parameter.alpha)
			return;
		
//		System.out.println(layer+" "+pattern.toString());
		/* if this pattern is already a pattern, add it into the patternMap*/
		if (pattern.posSeqBitSet.cardinality() <= parameter.beta)
		{	
			//pattern.negSeqBitSet.cardinality() >= parameter.alpha && 
//			checkResult(pattern);
//			System.out.println("New Pattern: " + pattern.toString());
			this.negResults.add(pattern);
			return;
		}

		/* else continue grow pattern: get value of the latest element of this pattern,  get this element in posIndex*/
		for(Node node:index.getNodeList()){
			Pattern p = pattern.clone();
			if(p.addElement(new PatternNode(node), parameter)){
				Candidate_Gen_Neg(p, layer+1, parameter);
			}
		}
		
		return;
	}
	
	private ArrayList<Pattern> minimum(ArrayList<Pattern> resultsSet) {
		
		Set<Pattern> remove = new HashSet<Pattern>();
		
		for(int i = 0 ; i <  resultsSet.size() ; i++)
		{
			for(int j = i+1 ; j <  resultsSet.size() ; j++)
			{	
				Pattern p1 = resultsSet.get(i);
				Pattern p2 = resultsSet.get(j);
				
				if(p1.getSize() == p2.getSize())
					continue;
				
				/* a is the shorter pattern
				 * b is the longer pattern
				 * */
				Pattern a = p1.getSize() < p2.getSize() ? p1:p2;
				Pattern b = p1.getSize() > p2.getSize() ? p1:p2;
				
				int check = 0;
				
				for(int k = 0 ; k < b.getSize() ; k ++){
					if(a.getElementList().get(check).valueEquals(b.getElement(k))){
						check++;
						if(check == a.getSize()){
							remove.add(b);
							break;
						}
					}
				}
				
			}
		}
 
		resultsSet.removeAll(remove);
		
		return resultsSet;
	}

	/* shorter is minimum and only consider continue */
	private ArrayList<Pattern> minimum2(ArrayList<Pattern> resultsSet) {
		
		Set<Pattern> remove = new HashSet<Pattern>();
		
		for(int i = 0 ; i <  resultsSet.size() ; i++)
		{
			for(int j = 0 ; j <  resultsSet.size() ; j++)
			{	
				Pattern p1 = resultsSet.get(i);
				Pattern p2 = resultsSet.get(j);
				
				if(p1.getSize() == p2.getSize())
					continue;
				
				/* a is the shorter pattern
				 * b is the longer pattern
				 * */
				
				Pattern a = p1.getSize() < p2.getSize() ? p1:p2;
				Pattern b = p1.getSize() > p2.getSize() ? p1:p2;
				
				boolean flag = false;
				
				for(int k = 0 ; k < b.getSize() ; k ++){
					if(a.getElementList().get(0).valueEquals(b.getElement(k))){
						flag = true;
						if(b.getSize() - k < a.getSize()){
							flag = false;
							break;
						}
						System.out.print(" "+k);	
						for(int l = 1;l < a.getSize() ; l++){
							if(!a.getElementList().get(l).valueEquals(b.getElement(k+l))){
								flag = false;
							}
						}
					}
				}
				if(flag == true){
//					System.out.println("Minimum:  ");
//					p1.itemMap = itemMap;
//					p2.itemMap = itemMap;
//					p1.print();
//					p2.print();
					remove.add(b);
				}	
			}
		}
 
		resultsSet.removeAll(remove);
		
		return resultsSet;
	}
	
	private boolean posBelowAlpha(Node f, Parameter parameter)
	{
		BitSet b = (BitSet)f.seqIdSet.clone();
		b.and(parameter.posSeqId);
		return (b.cardinality() < parameter.alpha)? true : false;
	}
	
	private boolean negBelowAlpha(Node f, Parameter parameter)
	{
		BitSet b = (BitSet)f.seqIdSet.clone();
		b.and(parameter.negSeqId);
		return (b.cardinality() < parameter.alpha)? true : false;
	}
}
