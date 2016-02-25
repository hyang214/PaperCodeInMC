package miner;

import gce.GenerateCandidateElement;
import gce.GenerateCandidateElementFactory;
import gcp.GenerateCandidatePattern;
import gcp.GenerateCandidatePatternFactory;
import tools.TimeRecord;
import util.Results;
import util.Sequences;

/**
 * author: Hao 
 * date:Dec 1, 2015
 * time:3:56:17 PM
 * purpose:
 */
public class MinerImpl extends Miner{
	/**
	 * results: the results entry ; static Class
	 * sequences: the data sets
	 * parameter: the parameters entry ; static Class
	 * gce: the method of generate candidate element
	 * gcp: the method of generate candidate pattern
	 */
	private GenerateCandidateElement gce;
	private GenerateCandidatePattern gcp;
	
	
	public MinerImpl(Sequences sequences, String gceName, String gcpName) {
		this.gce = GenerateCandidateElementFactory.INSTANCE.getByName(gceName);
		this.gce.setDataset(sequences);
		this.gcp = GenerateCandidatePatternFactory.INTSANCE.getByName(gcpName);
	}

	/**
	 * mine distinguishing pattern with gce and gcp
	 **/
	public void mine() {
		
		/** generate candidate elements **/
		gce.generateCE();
		
		/** generate patterns **/
		gcp.setCEList(gce.getCEList());
		gcp.generateCP();
		
	}

	/************************************************
	 * Getter and Setter
	 ************************************************/

	
}
