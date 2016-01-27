package tools;

import java.util.HashMap;

/**
 * author: Hao 
 * date:Dec 30, 2015
 * time:5:25:09 PM
 * purpose: Use to record the time cost of some action 
 */
public class TimeRecord {
	public static HashMap<String, Long> onceRecord = new HashMap<String, Long>();
	public static HashMap<String, Long> allRecord = new HashMap<String, Long>();
	
	/**********************************************************
	 * Once record
	 **********************************************************/
	
	/**
	 * record the time cost of action and print  
	 * @param action: the name of action
	 * @return
	 *  if action has already registered, return the time cost of action and remove this action
	 *  else if action has not registered, register this action
	 */
	public static String onceRecordAndPrint(String action){
		if(onceRecord.containsKey(action)){
			/** register the action **/
			long start = onceRecord.get(action);
			long end = System.currentTimeMillis();
			onceRecord.remove(action);
			String output = "Action: " + action +" Ended! Time cost is " + (end - start) + " ms.";
			return output;
		}
		else{
			/** compute the time cost of action **/
			onceRecord.put(action, System.currentTimeMillis());
			String output = "Action: " + action +" started !";
			return output;
		}
	}
	
	/**
	 * record the time cost of action and return 
	 * @param action: the name of action
	 * @return
	 *  if action has already registered, return the time cost of action and remove this action
	 *  else if action has not registered, register this action
	 */
	public static long onceRecordAndReturn(String action){
		if(onceRecord.containsKey(action)){
			/** register the action **/
			long start = onceRecord.get(action);
			long end = System.currentTimeMillis();
			onceRecord.remove(action);
			return end - start;
		}
		else{
			/** compute the time cost of action **/
			onceRecord.put(action, System.currentTimeMillis());
			return Long.MIN_VALUE;
		}
	}	
	
	/**********************************************************
	 * All record
	 **********************************************************/	
	
}
