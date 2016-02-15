package util;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class Sequences 
{
	public ArrayList<String> instances = new ArrayList<String>();
//	private int point;
	
	public Sequences(Parameter parameter)
	{
		/* read sequences from file	 */
		try
		{
			File file = new File(parameter.posFileName);
			Scanner sc = new Scanner(file);
			while(sc.hasNext())
				instances.add(sc.nextLine());
			sc.close();
			parameter.posSeqId.set(0, this.instances.size(), true);
			
			file = new File(parameter.negFileName);
			sc = new Scanner(file);
			while(sc.hasNext())
				instances.add(sc.nextLine());
			sc.close();
			parameter.negSeqId.set(parameter.posSeqId.cardinality(), this.instances.size(), true);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
//		point = 0;
	}
	
//	public String nextLine()
//	{
//		if(hasNext())
//		{
////			String reS = this.sequences.get(this.point);
////			this.point++;
////			return reS;
//		}
//		else
//			return "";
//	}
	
	public boolean hasNext()
	{
		boolean flag = true;
//		if(point >= sequences.size())
			flag = false;
		return flag;
	}

	public void setSequences(ArrayList<String> sequences) 
	{
		this.instances = sequences;
//		this.point = sequences.size();
	}
	
	public void printSequences()
	{
		for(String s : instances)
			System.out.println(s);
	}
	
}
