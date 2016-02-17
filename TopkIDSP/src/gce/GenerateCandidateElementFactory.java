package gce;

import gce.impl.BaselineGCE;
import gce.impl.KiGCE;

public enum GenerateCandidateElementFactory {
	INSTANCE;
	
	public GenerateCandidateElement getByName(String gceName) {
		if(gceName.equals("KiGCE")){
			return new KiGCE();
		}
		else if(gceName.equals("BaselineGCE")){
			return new BaselineGCE();
		}
		else{
			System.err.println("Unkonwn generate candidate element method! ");
			System.exit(-1);
			return null;
		}
	}

}
