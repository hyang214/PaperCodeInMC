package gcp.impl;

import gcp.GenerateCandidatePattern;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import util.Element;
import util.NaivePattern;
import util.NaiveResults;
import util.PeerKeyComparator;

public class BaselineGCP extends GenerateCandidatePattern {
	private List<Element> ceList;
	private PeerKeyComparator pkc;
	private Set<Element> forbidden;
	
	public BaselineGCP() {
		this.pkc = new PeerKeyComparator();
		this.forbidden = new HashSet<>();
	}

	@Override
	public void setCEList(List<Element> ceList) {
		this.ceList = ceList;
	}

	/** 
	 * generate candidate pattern
	 */
	@Override
	public void generateCP() {
		/** set enumeration tree **/
		setEnumerationTree(null);
	}
	
	/**
	 * use set enumeration tree search the candidate space by depth-first method
	 */
	private void setEnumerationTree(NaivePattern pattern) {
		/** append a new element **/
		for(Element next : ceList){
			/** skip elements in 'forbidden' **/
			if(forbidden.contains(next)){
				continue;
			}
			if( pattern == null){
				int a = 1;
				a = a + 1;
			}
			else{
				if(pattern.getLength() == 1){
					int a = 1;
					a = a + 1;
				}else if(pattern.getLength() == 2){
					int a = 1;
					a = a + 1;
				}else if(pattern.getLength() == 3){
					int a = 1;
					a = a + 1;
				}else if(pattern.getLength() == 4){
					int a = 1;
					a = a + 1;
				}
			}
			
			/** pruning rule: check element max cRatio **/
			if(impossibleElementPruning(next)){
				if(pattern != null){
					NaivePattern newPattern = pattern.clone();
					if(newPattern.addElement(next)){
						/** check newPattern is a pattern or not **/
						if(patternCheck(newPattern)){
							/** newPattern is a pattern! **/
							NaiveResults.addResult(newPattern);
						}
						
						/** check: should we search the super space or not **/
						if(searchCheck(newPattern)){						
							/** continue the search process in this branch **/
							setEnumerationTree(newPattern);
						}
					}else{
						/** pruning rule: p' = pattern + next cannot be a candidate pattern 
						 * since the max possible cRatio is less than threshold **/
					}					
				}else{
					NaivePattern newPattern = new NaivePattern(next);
					/** check: newPattern is a pattern or not **/
					if(patternCheck(newPattern)){
						/** newPattern is a pattern! **/
						NaiveResults.addResult(newPattern);
					}
					
					/** check: should we search the super space or not **/
					if(searchCheck(newPattern)){						
						/** continue the search process in this branch **/
						setEnumerationTree(newPattern);
					}
				}
			}else{
				forbidden.add(next);
			}
		}
	}
	
	/**
	 * check pattern 
	 */
	private boolean patternCheck(NaivePattern pattern){
		if(pattern.getPosSup() <= 0 )
			return false;
		return pkc.compare(pattern.getPeerKey(), NaiveResults.pkThreshold) < 0 ? false : true; 
	}
	
	/**
	 * search the super-pattern or not
	 */
	private boolean searchCheck(NaivePattern pattern){
		return pattern.getPosSup() >= NaiveResults.pkThreshold.getcRatio();
	}

	/** 
	 * impossible element pruning
	 */
	private boolean impossibleElementPruning(Element e){
		if(e.getPosSup() < NaiveResults.pkThreshold.getcRatio()){
			return false;
		}else{
			return true;
		}
	}
}
