package gcp;

import gcp.impl.BaselineGCP;
import gcp.impl.KiGCP;

public class GenerateCandidatePatternFactory {

	public static GenerateCandidatePattern getByName(String gcpName) {
		if(gcpName.equals("KiGCP")){
			return new KiGCP();
		}		
		else if(gcpName.equals("BaselineGCP")){
			return new BaselineGCP();
		}
		else{
			System.err.println("Unkonwn generate candidate pattern method! ");
			System.exit(-1);
			return null;
		}
	}

}
