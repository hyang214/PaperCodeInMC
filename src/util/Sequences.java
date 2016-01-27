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
	
	
	public boolean hasNext()
	{
		boolean flag = true;
//		if(point >= sequences.size())
			flag = false;
		return flag;
	}

	public void setSequences(ArrayList<String> sequences) 
	{
		this.posInstances = sequences;
//		this.point = sequences.size();
	}
	
	public void printSequences()
	{
		for(String s : posInstances)
			System.out.println(s);
	}
	
}
