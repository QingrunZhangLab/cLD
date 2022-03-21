# cLD Project
Linkage disequilibrium (LD) is a fundamental concept in genetics; critical for studying genetic associations and molecular evolution. However, LD measurements are only reliable for common genetic variants, leaving low-frequency variants unanalyzed. In this work, we introduce cumulative LD (cLD), a stable statistic that captures the rare-variant LD between genetic regions and opens the door for furthering biological knowledge using rare genetic variants. In application, we find cLD reveals an increased genetic association between genes in 3D chromatin interactions, a phenomenon recently reported negatively by calculating standard LD between common variants. Additionally, we show that cLD is higher between gene pairs reported in interaction databases, identifies unreported protein-protein interactions, and reveals interacting genes distinguishing case/control samples in association studies.  

The R & Python code for the cLD project and instructions on how to run it are included in this repository. Using our toy example you can verify the cLD value and the gene distance value easily, since we only have 5 genes and 6 individuals in it. If you want to go through the whole process, you may use demo example to run these codes.

## Data
In this analysis, you need to provide:  
  \-  the genotype file  
  \-  the gene information file  
  \-  the gene-gene interaction file  
In the paper, we are using the 1000 Genome dataset as the genotype file. The gene information file should contain the start point, end point and gene's Ensembl ID. The gene-gene interaction file should contain the gene-gene interaction in terms of genes' Ensembl ID. The example data is given.
If you want to run a the anaylsis on the Hi-C interactions, you'll need to use Hi-C_InteractionTransfer.py to transfer the interaction intervals into gene-gene interactions. 

## Procedure (Chapter 2 & 3)

### Integrate SNVs
'geneIntegrate.py' could integrate the SNVs into the gene lines, you can find the detail in the Supplementary material Chapter 1.
The inputs are:  
  \-  gene information file  
  \-  original genotype data, contains the SNV information  
  \-  specify the chromosome  
  \-  Threshold of rare SNVs  
The output is:  
\-  the intergrated gene file  

'geneFilter.py' could remove the genes with cMAF = 0.
The input is:  
  \-  the integrated gene file  
The output is:  
  \-  the filtered gene file
  
### Calculate cLD
'cld.py' could calculate the cLD matrix given the filtered gene file.
The input is:   
  \-  the filtered gene data from filter  
The output is:   
  \-  the cLD file without Ensemble ID  
    
'namedcld.py' could add gene names to the cLD result.  
The input is:   
  \-  cLD file from cLD.py  
  \-  gene information list, you can find the example in the demo  
The output is:     
  \-  output is the cLD file with Gene Esmbel ID  

### Gene distance
'genemid.py' and 'genedistance.py' are used to calculate the gene distcance between gene pairs.
In genemid.py, the input is:  
  \-  cLD file with Ensembl ID  
The output is:  
  \-  gene mid point list  

In genedistance.py, the inputs are:  
  \-  cLD file with Ensembl ID   
  \-  gene mid point file  
The outputs are:  
  \-  gene distance file  
  \-  distance sequence file, a string  
  
### Hi-C interaction transfer
'Hi-C_InteractionTransfer.py' could transfer the interaction intervals into gene-gene interactions.  
The input is:  
  \-  Hi-C interaction file  
  \-  cLD file with Ensembl ID  
  \-  specify the chromosome  
The output is:  
  \-  the gene-gene interactions in terms of Esmbel ID   
  
### Variance Comparison
'cldVar.py' calculate the variance of cLD based on the filtered gene file.
The input is:    
  \-  filtered gene file from filter  
'ldVar.py' estimate the variance of LD based of the original SNV file. 
The inputs are: 
  \-  SNP file  
  \-  specify the chromosome  
  \-  threshold of rare variant  
  \-  sample size  
The output of these two python files are the estimated variance. 
 
### Run MH test & Fisher's Exact test
'interactionDistGroup.py' could separate the cLD gene pairs into 13\*2 groups, 13 means 13 distance groups, 2 means with/without interactions.  
The inputs are:  
  \-  gene distance file   
  \-  cLD with gene esembl ID   
  \-  Gene interaction file, e.g. Hi-Cgeneint.txt   
The output is:  
  \-  output is a matrix. negtive value means with interaction, positive means no-interaction   

'interactionDistseparate.py' and 'tests.py' could run the MH test and Fisher's exact test.  
The inputs are:    
  \-  cLD with gene esembl ID    
  \-  interaction-distance information matrix   
  \-  threshold of success (in quantile)  
 
The outputs are the cld without interaction, this file contains 13 lines, each one represent a distance group. Base on these outputs, 'tests.py' could provide the test statistics and p-values for the test.  

### Bootstrap Methods (Chapter 3)
'geneRandom.py' is used to sample a subset of genes from the filtered gene file.   
The inputs are:  
  \-  Filtered genefile  
  \-  sample size  
The output is a gene sample file.   

'ldBootstrap.py' and 'cldBootstrap' are used to run bootstrap algorithm.
#####For LD bootstrap:
The inputs are:  
  \-  gene sample  
  \-  threshold of rare variant  
  \-  Sample size of each iteration   
  \-  epoch  
The output is the bootstrap result file.  
#####For cLD bootstrap:
The inputs are:  
  \-  gene sample  
  \-  gene information list, you can find the example in the demo  
  \-  samplesize  
  \-  epoch  
The output is the cLD Bootstap result, each row represents a iteration.

'bootstrapGroups.py' and 'bootstrapResultSeparate.py' are used to separate gene pairs into several cMAF groups. You may read the detail in the Supplementary material.

### Example Input Data
#### Example SNV file
#CHROM  |  POS  |  ID  |  REF  |  ALT  |  QUAL  |  FILTER  |  INFO  |  FORMAT  | HG00096 |   HG00097  |  HG00099  |  HG00100  |  HG00101  |  HG00102 
--- | --- | --- | --- |--- | --- | --- | --- | --- |--- | --- |--- |--- | --- |--- 
1  |  22  |  rs367896724  |  A  |  AC  |  100   | PASS  |  AC=2130;AF=0.425319;AN=5008;NS=2504;DP=103152;EAS_AF=0.3363;AMR_AF=0.3602;AFR_AF=0.4909;EUR_AF=0.4056;SAS_AF=0.4949;AA=\|\|\|unknown(NO_COVERAGE);VT=INDEL  |  GT |  1\|0  |  0\|1 |   0\|1  |  1\|0  |  0\|0  |  1\|0 |
... | ... | ... | ... |... | ... | ... | ... | ... |... | ... |... |... | ... |... 
#### Example gene information file  
CHR | genename | Startpoint | Endpoint | Ensemble ID
--- | --- | --- | --- |--- 
chr1 | gene | 10 | 20 | Ensemble1
chr1 | gene | 15 | 24 | Ensemble2  
chr1 | gene | 20 | 29 | Ensemble3
chr1 | gene | 26 | 101 | Ensemble4
chr1 | gene | 99 | 150 | Ensemble5
... | ... | ... | ... |...
In the practice, we don't need the headline. 
#### Example Hi-C interaction file
Count | interval 1 (chr;lower;upper) | interval 2 (chr;lower;upper)
--- | --- | --- 
C0   |   1;0;10 | 1;19;25
C1   |   1;25;40 | 1;90;110
C2   |   1;2;13 | 1;20;24
... | ... | ... 
## Protein Docking (Chapter 4)
The softwares are  
1.	Protein docking software:  HDOCKlite-v1.1: http://huanglab.phys.hust.edu.cn/software/hdocklite/   
2.	Visualization software:   
Pymol 2.5.1: https://pymol.org/2/   
LigPlot+’s: https://www.ebi.ac.uk/thornton-srv/software/LigPlus/manual/manual.html  

### Data source and quality control  
plink="/path_to_plink/plink".

### Go and KEGG pathway analysis
Using ‘clusterProfiler’ R package, the code is 'Go and KEGG pathway analysis.R'
