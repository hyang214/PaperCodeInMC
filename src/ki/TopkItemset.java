package ki;

import tools.TimeRecord;
import tools.Verbase;
import util.Alphabet;
import util.ItemMap;
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
		TimeRecord.allRecordAndReturn("Generate Elements");
		GenerateCandidateElement gce = new GenerateCandidateElement(sequences);
		gce.generateCE();
		TimeRecord.allRecordAndReturn("Generate Elements");
		Verbase.verbaseAtLevel(1, ItemMap.staticToString());
		Alphabet alphabet = gce.getAlphabet();
		Verbase.verbaseAtLevel(2, alphabet.toString());
		
		/** generate patterns **/
		TimeRecord.allRecordAndReturn("Generate Patterns");
		GenerateCandidatePattern gcp = new GenerateCandidatePattern(alphabet);
		gcp.generateCP();
		TimeRecord.allRecordAndReturn("Generate Patterns");
		
		/** merge results **/
		TimeRecord.allRecordAndReturn("Merge Patterns");
		Results.merge();
		TimeRecord.allRecordAndReturn("Merge Patterns");
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
