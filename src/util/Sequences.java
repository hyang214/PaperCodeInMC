package util;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class Sequences 
{
	public ArrayList<String> posInstances = new ArrayList<String>();
	public ArrayList<String> negInstances = new ArrayList<String>();

	public Sequences()
	{
		/** read sequences from file **/
		try
		{
			/** positive data set **/
			File file = new File(Parameter.posFileName);
			Scanner sc = new Scanner(file);
			while(sc.hasNext())
				posInstances.add(sc.nextLine());
			sc.close();
			Parameter.posSeqId.set(0, this.posInstances.size(), true);
			Parameter.posSize = this.posInstances.size();

			/** negative data set **/
			file = new File(Parameter.negFileName);
			sc = new Scanner(file);
			while(sc.hasNext())
				negInstances.add(sc.nextLine());
			sc.close();
			Parameter.negSeqId.set(0, this.negInstances.size(), true);
			Parameter.negSize = this.negInstances.size();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * get the instances by DATASET
	 */
	public ArrayList<String> getInstanceByDATASET(int DATASET){
		if(DATASET == Parameter.POSITIVE)
			return posInstances;
		else if(DATASET == Parameter.NEGATIVE)
			return negInstances;
		else{
			//TODO error: unknown data set
			return null;
		}
	}

	public void setSequences(ArrayList<String> sequences) 
	{
		this.posInstances = sequences;
	}
	
	/**
	 * translate sequences into string
	 */
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("Positive data set:\n");
		for(String s : posInstances)
			sb.append(" "+s+"\n");
		sb.append("Negative data set:\n");
		for(String s : negInstances)
			sb.append(" "+s+"\n");
		
		return sb.toString();
	}
	
}
