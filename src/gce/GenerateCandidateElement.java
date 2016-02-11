package gce;

import java.util.List;

import util.Alphabet;
import util.Element;
import util.Sequences;

public abstract class GenerateCandidateElement {
	
	/**
	 * get data sets
	 */
	public abstract void setDataset(Sequences sequences);
	
	/**
	 * generate candidate element
	 */
	public abstract void generateCE();
	
	/**
	 * get candidate element list 
	 */
	public abstract List<Element> getCEList();
	
	/**
	 * get the alphabet 
	 */
	public abstract Alphabet getAlphabet();
}
