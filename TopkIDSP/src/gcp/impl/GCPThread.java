package gcp.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import util.Element;
import util.Pattern;
import util.PeerKeyComparator;
import util.Results;
import util.ThreadCount;
/**
 * author: Hao 
 * date:May 13, 2016
 * time:8:32:55 PM
 * purpose:
 */
public class GCPThread extends Thread{
	
	private List<Element> ceList;
	private PeerKeyComparator pkc;
	private Set<Element> forbidden;
	private List<Pattern> pList;
	private ThreadCount tc;
	
	public GCPThread(List<Element> ceList, PeerKeyComparator pkc, Set<Element> forbidden, List<Pattern> pList, ThreadCount tc) {
		this.ceList = ceList;
		this.pkc = pkc;
		this.forbidden = forbidden;
		this.pList = pList;
		this.tc = tc;
	}
	
	@Override
	public void run() {
		
		while(true){
			Pattern p = null;
			synchronized (pList) {
				/** all jobs has been done **/
				if(pList.size() == 0)
					break;
				else{
					p = pList.get(0);
					pList.remove(0);
				}
			}
			if(p!=null)
				setEnumerationTree(p);
		}
		
		/**  the thread end **/
		synchronized (tc) {
			tc.count --;
		}
	}
	
	/**
	 * use set enumeration tree search the candidate space by depth-first method
	 */
	private void setEnumerationTree(Pattern pattern) {
		/** append a new element **/
		for(Element next : ceList){
			/** skip elements in 'forbidden' **/
			if(forbidden.contains(next)){
				continue;
			}
			
			/** pruning rule: check element max cRatio **/
			if(impossibleElementPruning(next)){
				Pattern newPattern = pattern.clone();
				if(newPattern.addElement(next)){
					if(patternCheck(newPattern)){
						/** newPattern is a pattern! **/
						synchronized(Results.class){
							Results.addResult(newPattern);
						}
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
				forbidden.add(next);
			}
		}
	}
	
	/**
	 * check pattern 
	 */
	private boolean patternCheck(Pattern pattern){
		if(pattern.getPosSup() <= 0 )
			return false;
		return pkc.compare(pattern.getPeerKey(), Results.pkThreshold) < 0 ? false : true; 
	}
	
	/**
	 * search the super-pattern or not
	 */
	private boolean searchCheck(Pattern pattern){
		return pattern.getPosSup() >= Results.pkThreshold.getcRatio();
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
