package gcp.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import gcp.GenerateCandidatePattern;
import util.Element;
import util.Pattern;
import util.PeerKeyComparator;
import util.Results;

/**
 * author: Hao 
 * date:Jan 21, 2016
 * time:3:59:30 PM
 * purpose:
 */
public class KiGCP extends GenerateCandidatePattern{
	
	private List<Element> ceList;
	private PeerKeyComparator pkc;
	
	public KiGCP() {
		this.pkc = new PeerKeyComparator();
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
		/** filter all element with k, and generate length-1 pattern with all candidate element **/
		List<Pattern> pList = filter(ceList);
		
		/** use set enumeration tree search the candidate space by depth-first method **/
		for(Pattern p : pList){
			setEnumerationTree(p);
		}
	}
	
	/**
	 * use set enumeration tree search the candidate space by depth-first method
	 */
	private void setEnumerationTree(Pattern pattern) {
		/** append a new element **/
		Set<Element> ies = new HashSet<Element>();
		for(Element next : ceList){
			/** pruning rule: check element max cRatio **/
			if(impossibleElementPruning(next)){
				Pattern newPattern = pattern.clone();
				if(newPattern.addElement(next)){
					/** check newPattern is a pattern or not **/
					if(patternCheck(newPattern)){
						/** newPattern is a pattern! **/
						Results.addResult(newPattern);
						
						/** continue the search process in this branch **/
						setEnumerationTree(newPattern);
					}
				}else{
					/** pruning rule: p' = pattern + next cannot be a candidate pattern 
					 * since the max possible cRatio is less than threshold **/
				}
			}else{
				ies.add(next);
			}
		}
		ceList.removeAll(ies);
	}

	/** 
	 * filter all element with k, and generate length-1 pattern with all candidate element 
	 * **/
	private List<Pattern> filter(List<Element> ceList2) {
		List<Pattern> pList = new ArrayList<Pattern>();
		Set<Element> ies = new HashSet<>();
		for(Element e : ceList){
			if(impossibleElementPruning(e)){
				Pattern pattern = new Pattern(e);
				if(patternCheck(pattern)){
					/** pattern is a pattern  **/
					Results.addResult(pattern);
				}	
				pList.add(pattern);
			}else{
				/** pruning rule: if the cRatio of an element is less than threshold, all 
				 * patterns contains it cannot be a pattern, so we do not use this element 
				 * any more, just remove it from ceList **/
				ies.add(e);
			}
		}
		ceList.removeAll(ies);
		return pList;
	}
	
	/**
	 * check pattern 
	 */
	private boolean patternCheck(Pattern pattern){
		if(pattern.getcRatio() <= 0 )
			return false;
		return pkc.compare(pattern.getPeerKey(), Results.pkThreshold) < 0 ? false : true;
	}

	/** 
	 * impossible element pruning
	 */
	private boolean impossibleElementPruning(Element e){
		if(e.getPosSup() < Results.pkThreshold.getcRatio()){
			return false;
		}else{
			return true;
		}
	}

}
