package ki;

import tools.TimeRecord;
import tools.Verbase;
import util.Alphabet;
import util.Results;
import util.Sequences;

/**
 * author: Hao 
 * date:Dec 1, 2015
 * time:3:56:17 PM
 * purpose:
 */
public class TopkItemset {
	/**
	 * results: the results entry ; static Class
	 * sequences: the data sets
	 * parameter: the parameters entry ; static Class
	 */
	private Sequences sequences;
	
	public TopkItemset(Sequences sequences) {
		this.sequences = sequences;
	}

	/**
	 * mine the top-k distinguishing pattern 
	 **/
	public void mine() {
		
		/** generate candidate elements **/
		Verbase.verbaseAtLevel(8, TimeRecord.onceRecordAndPrint("Generate Elements"));
		GenerateCandidateElement gce = new GenerateCandidateElement(sequences);
		gce.generateCE();
		Verbase.verbaseAtLevel(8, TimeRecord.onceRecordAndPrint("Generate Elements"));
		Alphabet alphabet = gce.getAlphabet();
		
		/** generate patterns **/
		Verbase.verbaseAtLevel(8, TimeRecord.onceRecordAndPrint("Generate Patterns"));
		GenerateCandidatePattern gcp = new GenerateCandidatePattern(alphabet);
		gcp.generateCP();
		Verbase.verbaseAtLevel(8, TimeRecord.onceRecordAndPrint("Generate Patterns"));
		
		/** merge results **/
		Verbase.verbaseAtLevel(8, TimeRecord.onceRecordAndPrint("Merge Patterns"));
		Results.merge();
		Verbase.verbaseAtLevel(8, TimeRecord.onceRecordAndPrint("Merge Patterns"));
	}

	/************************************************
	 * Getter and Setter
	 ************************************************/
	public Sequences getSequences() {
		return sequences;
	}

	public void setSequences(Sequences sequences) {
		this.sequences = sequences;
	}
	
}
