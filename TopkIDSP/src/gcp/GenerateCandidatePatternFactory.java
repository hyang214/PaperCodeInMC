package gcp;

import gcp.impl.BaselineGCP;
import gcp.impl.KiGCP;
import gcp.impl.MultiKiGCP;

public enum GenerateCandidatePatternFactory {
	INTSANCE;
	
	public GenerateCandidatePattern getByName(String gcpName) {
		if(gcpName.equals("KiGCP")){
			return new KiGCP();
		}		
		else if(gcpName.equals("BaselineGCP")){
			return new BaselineGCP();
		}
		else if(gcpName.contains("MultiKi-")){
			int threadNumber = Integer.valueOf(gcpName.split("-")[1]);
			return new MultiKiGCP(threadNumber);
		}
		else{
			System.err.println("Unkonwn generate candidate pattern method! ");
			System.exit(-1);
			return null;
		}
	}

}
