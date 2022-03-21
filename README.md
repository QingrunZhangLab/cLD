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
  \-  Threshold of rare SNPs  
The output is:  
\-  the intergrated gene file  

### Filter genes
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
*The input is:  
  \-  Input is the Hi-C interaction file  
  \-  cLD file with Ensembl ID  
  \-  specify the chromosome  
The output is:  
  \-  the gene-gene interactions in terms of Esmbel ID   
  
### Run MH test & Fisher's Exact test
'interactionDistGroup.py' could separate the cLD gene pairs into 13\*2 groups, g
openfile1 = open(r'/PATH/genedistance.txt','r')  #gene distance file
openfile2 = open(r'/PATH/namedcld.txt','r')  #namedcld file, the cLD value with gene esembl ID
openfile3 = open(r'/PATH/Hi-Cgeneint.txt','r') #Gene interaction file, e.g. Hi-Cgeneint.txt
outfile = '/PATH/interactionDistGroup.txt' #output is the triangule matrix. negtive value means with interaction, positive means no-interaction

'interactionDistseparate.py'
cldfile = open(r'/PATH/namedcld.txt','r')  # the named cld file
groupfile = open(r'/PATH/interactionDistGroup.txt','r')  #the upper triangule interaction-distance information file.
i0file = '/PATH/cldi0.txt' # output is the cld without interaction, this file contains 13 lines, each one represent a distance group.
i1file = '/PATH/cldi1.txt' # output is the cld with interaction, this file contains 13 lines, each one represent a distance group.

'tests.py'
thres = 0.5 #quantile, threshold of success
i0file = '/PATH/cldi0.txt' 
i1file = '/PATH/cldi1.txt'

### Bootstrap Methods ()
In the bootstrap parts. 

### Example Input Data


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
