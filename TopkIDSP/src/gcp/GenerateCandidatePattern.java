package gcp;

import java.util.List;

import util.Element;

public abstract class GenerateCandidatePattern {
	/**
	 * generate candidate pattern
	 */
	public abstract void generateCP();
	
	/**
	 * set the list of elements
	 */
	public abstract void setCEList(List<Element> ceList);
}
