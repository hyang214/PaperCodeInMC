package util;

import java.util.BitSet;


public class Parameter 
{
	public int posDatasetSize;
	public int negDatasetSize;
	public int alpha;
	public int beta;
	public int minGap;
	public int maxGap;
	public String posFileName;
	public String negFileName;
	public BitSet posSeqId;
	public BitSet negSeqId;

	public Parameter()
	{
		this.posFileName = "./syn/pos/syn_pos_30_4_30_50_.txt"; //"pos.txt"; //"posSeq.txt"; //"posScholar.txt"; //"posMerge.txt"; //"posCPages.txt"; //"posPages.txt"; //"posRE.txt"; //"posInput.txt"; //"posMerge.txt"; //"posScholarI.txt"; //  "D01.txt"; // "posSequences.txt"; // 
		this.negFileName = "./syn/neg/syn_neg_30_4_30_50_.txt"; //"neg.txt"; //"negSeq.txt"; //"negScholar.txt"; //"negMerge.txt"; //"negCPages.txt"; //"negPages.txt"; //"negRE.txt"; //"negInput.txt"; //"negMerge.txt"; //"negScholarI.txt"; //  "D02.txt"; // "negSequences.txt"; // 

//		this.posFileName = "./dblp/database.txt";
//		this.negFileName = "./dblp/datamining.txt";
		
//		this.posFileName = "./used/posSeq.txt"; 
//		this.negFileName = "./used/negSeq.txt"; 
		
		this.posFileName = "./re/sr female.txt"; 
		this.negFileName = "./re/sr male.txt"; 
		
//		this.posFileName = "./dblp3/size/DB40.txt"; 
//		this.negFileName = "./dblp3/size/DM40.txt"; 
		
//		this.posFileName = "./dblp3/DB100.txt";
//		this.posFileName = "./dblp3/DM100.txt";
//		this.posFileName = "./dblp3/IR100.txt";
//		this.negFileName = "./dblp3/DM100.txt";
//		this.negFileName = "./dblp3/IR100.txt";
//		this.negFileName = "./dblp3/DB100.txt";
		

//		this.posFileName = "./dblp4/DB.txt";
		this.posFileName = "./dblp4/DM.txt";
//		this.posFileName = "./dblp4/IR.txt";
		
//		this.negFileName = "./dblp4/DB.txt";
//		this.negFileName = "./dblp4/DM.txt";
		this.negFileName = "./dblp4/IR.txt";
		
//		this.negFileName = "./dblp4/DBDM.txt";
//		this.negFileName = "./dblp4/DMIR.txt";
//		this.negFileName = "./dblp4/DBIR.txt";
		
//		this.posFileName = "./re/posMerge.txt";
//		this.negFileName = "./re/empty.txt";
//		this.posFileName = "./re/a.txt";
//		this.negFileName = "./re/b.txt";
//		this.posFileName = "./re/posSeq.txt";
//		this.negFileName = "./re/negSeq.txt";
		
//		this.posFileName = "./syn2/pos/syn_pos_10_4_10_50_.txt"; 
//		this.negFileName = "./syn2/neg/syn_neg_10_4_10_50_.txt"; 
		
//		this.posFileName = "./gamelog/c pos.txt"; 
//		this.negFileName = "./gamelog/c neg.txt"; 
//		this.posFileName = "./gamelog/sc pos pro.txt"; 
//		this.negFileName = "./gamelog/sc neg pro.txt"; 
//		this.posFileName = "./gamelog/sc pos.txt"; 
//		this.negFileName = "./gamelog/sc neg.txt"; 
		
//		this.posFileName = "./gamelog/data6/pos1.txt"; 
//		this.negFileName = "./gamelog/data6/neg1.txt"; 
		

		this.posFileName = "./ecoli/eColi_A_f.txt";	
		this.negFileName = "./ecoli/eColi_B_f.txt";	
		
		this.alpha = 40;
		this.beta = 20;                             
		
		this.maxGap = 5;
		this.minGap = 0;		
		
		this.posSeqId = new BitSet();
		this.negSeqId = new BitSet();

	}
	
	public Parameter(String posFileName, String negFileName, int posThreshold, int negThreshold, int maxGap, int minGap, BitSet posDataSetId, BitSet negDataSetId)
	{
		this.posFileName = posFileName;
		this.negFileName = negFileName;
		this.alpha = posThreshold;
		this.beta = negThreshold;
		this.maxGap = maxGap;
		this.minGap = minGap;
		this.posSeqId = posDataSetId;
		this.negSeqId = negDataSetId;
	}
	
	public String printParameter(){
		String tmp1 = posFileName.replace("/", "");
		String tmp2 = negFileName.replace("/", "");
		String s = "alpha "+alpha+" beta "+beta+" maxGap "+maxGap+" minGap "+minGap +" " + tmp1 +" " + tmp2;
		return s;
	}
}
