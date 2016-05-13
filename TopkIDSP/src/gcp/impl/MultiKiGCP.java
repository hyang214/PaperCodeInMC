package gcp.impl;

import gcp.GenerateCandidatePattern;

import java.util.ArrayList;
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
 * date:Jan 21, 2016
 * time:3:59:30 PM
 * purpose:
 */
public class MultiKiGCP extends GenerateCandidatePattern{
	
	private int threadNumber; 
	private List<Element> ceList;
	private PeerKeyComparator pkc;
	private Set<Element> forbidden;
	private ThreadCount tc;
	
	public MultiKiGCP(int threadNumber) {
		this.threadNumber = threadNumber; 
		this.pkc = new PeerKeyComparator();
		this.forbidden = new HashSet<>();
		this.tc = new ThreadCount(threadNumber);
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
		
		
		for(int i = 0 ; i < threadNumber ; i ++){
			GCPThread gcpThread = new GCPThread(ceList,pkc,forbidden,pList,tc);
			gcpThread.start();	
		}
		
		try {
			while(true){
				synchronized (tc) {
					if(tc.count == 0)
						break;
				}
				Thread.sleep(1); 
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Results.merge();
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
					/** newPattern is a pattern! **/
					Results.addResult(pattern);
				}
				/** check: should we search the super space or not **/
				if(searchCheck(pattern)){						
					/** continue the search process in this branch **/
					pList.add(pattern);
				}
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

