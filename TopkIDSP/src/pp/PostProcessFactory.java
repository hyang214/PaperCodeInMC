package pp;

import pp.impl.KiPP;

/**
 * author: Hao 
 * date:Feb 17, 2016
 * time:4:35:33 PM
 * purpose:
 */
public enum PostProcessFactory {
	INSTANCE;
	
	@SuppressWarnings("rawtypes")
	public PostProcess getByName(String ppName){
		if(ppName.equals("KiPP")){
			return new KiPP();
		}else{
			return null;
		}
	}
}
