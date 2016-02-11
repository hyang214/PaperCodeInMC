# PaperCodeInMC
All code of the papers which is published in my master career are in this repository. There are:
+ Mining Itemset-based Distinguishing Sequential Patterns with Gap Constraint
+ Mining Top-k Distinguishing Sequential Patterns with Gap Constraint
+ Mining Itemset-based Top-k Distinguishing Sequential Patterns with Gap Constraint
+ Mining Frequent Closed Sequential Patterns with Non-user-defined Gap Constraints

Attention: 
+ The code of iDSP-Miner, kDSP-Miner and FOUNTAIN have not been uploaded.
+ The code of kiDSP-Miner is still under review and modify.

## 1. iDSP-Miner

+ <b>Abstract</b>: Mining contrast sequential patterns, which are sequential patterns that characterize a given sequence class and distinguish that class from another given sequence class, has a wide range of applications including medical informatics, computational finance and consumer behavior analysis. In previous studies on contrast sequential pattern mining, each element in a sequence is a single item or symbol. This paper considers a more general case where each element in a sequence is a set of items. The associated contrast sequential patterns will be called itemset-based distinguishing sequential patterns (itemset-DSP). After discussing the challenges on mining itemset-DSP, we present iDSP-Miner, a mining method with various pruning techniques, for mining itemset-DSPs that satisfy given support and gap constraint. In this study, we also propose a concise border-like representation (with exclusive bounds) for sets of similar itemset-DSPs and use that representation to improve efficiency of our proposed algorithm. Our empirical study using both real data and synthetic data demonstrates that iDSP-Miner is effective and efficient.
+ <b>Bible</b>: <b>Hao Yang</b>, Lei Duan, Guozhu Dong, Jyrki Nummenmaa, Changjie Tang, Xiaosong Li: Mining Itemset-based Distinguishing Sequential Patterns with Gap Constraint. DASFAA (1) 2015: 39-54
+ <b>Download</b>: [Mining Itemset-based Distinguishing Sequential Patterns with Gap Constraint](http://link.springer.com/chapter/10.1007%2F978-3-319-18120-2_3)


## 2. kDSP-Miner

+ <b>Abstract</b>: Distinguishing sequential pattern can be used to present the difference between data sets, and thus has wide applications, such as commodity recommendation, user behavior analysis and power supply predication. Previous algorithms on mining distinguishing sequential patterns ask users to set both positive and negative support thresholds. Without sufficient prior knowledge of data sets, it is difficult for users to set the appropriate support thresholds, resulting in missing some significant contrast patterns. To deal with this problem, an algorithm, called kDSP-miner (top-k distinguishing sequential patterns with gap constraint miner), for mining top-k distinguishing sequential patterns satisfying the gap constraint is proposed. Instead of setting the contrast thresholds directly, a user-friendly parameter, which indicates the expected number of top distinguishing patterns to be discovered, is introduced in kDSP-miner. It makes kDSP-miner easy to use, and its mining result more comprehensible. In order to improve the efficiency of kDSP-miner, several pruning strategies and a heuristic strategy are designed. Furthermore, a multi-thread version of kDSP-miner is designed to enhance its applicability in dealing with the sequences with high dimensional set of elements. Experiments on real world data sets demonstrate that the proposed algorithm is effective and efficient.
+ <b>Bible</b>: <b>Hao Yang</b>, Lei Duan, Bing Hu, Song Deng, Wentao Wang, Qin Pan: Mining Top-k Distinguishing Sequential Patterns with Gap Constraint. Journal of Software, 2015, 26(11):2994-3009
+ <b>Download</b>: [Mining Top-k Distinguishing Sequential Patterns with Gap Constraint](http://www.jos.org.cn/ch/reader/view_abstract.aspx?file_no=4906&flag=1)


## 3. kiDSP-Miner

+ <b>TODO</b>


## 4. FOUNTAIN

+ <b>Abstract</b>: Frequent closed sequential pattern mining plays an important role in sequence data mining and has a wide range of applications in real life, such as protein sequence analysis, financial data investigation, and user behavior prediction. In previous studies, a user predefined gap constraint is considered in frequent closed sequential pattern mining as a parameter. However, it is difficult for users, who are lacking sufficient priori knowledge, to set suitable gap constraints. Furthermore, different gap constraints may lead to different results, and some useful patterns may be missed if the gap constraint is chosen inappropriately. To deal with this, we present a novel problem of mining frequent closed sequential patterns with non-user-defined gap constraints. In addition, we propose an efficient algorithm to find the frequent closed sequential patterns with the most suitable gap constraints. Our empirical study on protein data sets demonstrates that our algorithm is effective and efficient.
+ <b>Bible</b>: Wentao Wang, Lei Duan, Jyrki Nummenmaa, Song Deng, Zhongqi Li, <b>Hao Yang</b>, Changjie Tang: Mining Frequent Closed Sequential Patterns with Non-user-defined Gap Constraints. ADMA 2014: 57-70
+ <b>Download</b>: [Mining Frequent Closed Sequential Patterns with Non-user-defined Gap Constraints](http://link.springer.com/chapter/10.1007%2F978-3-319-14717-8_5)



