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

### Intergrate SNVs
'geneIntegrate.py' could integrate the SNVs into the gene lines, you can find the detail in the Supplementary material Chapter 1.
The inputs are:  
  \-  genefile     #  gene information file  
  \-  openfile     #  original genotype data, contains the SNV information  
  \-  chr = '1'    #  specify the chromosome  
  \-  thre = 0.005 #  Threshold of rare SNPs  
The output is:  
\-  outfile   #  the output is the intergrated gene file  

### Filter genes
'geneFilter.py' could remove the genes with cMAF = 0.
The input is:  
  \-  openfile  #  the integrated gene file  
The output is:  
  \-  outfile  #  the filtered gene file
  
### Calculate cLD
'cld.py' could calculate the cLD matrix given the filtered gene file.
The input is:   
  \-  openfile  #  the filtered gene data from filter  
The output is:   
  \-  outfile  #  output is the cLD file without Ensemble ID  
    
'namedcld.py' could add gene names to the cLD result.  
The input is:   
  \-  openfile  #  cLD file from cLD.py  
  \-  genenamefile #  gene information list, you can find the example in the demo  
The outputs are:     
  \-  outfile  #  output is the cLD file with Gene Esmbel ID  

### Gene distance
'genemid.py' and 'genedistance.py' are used to calculate the gene distcance between gene pairs.
In genemid.py, the input is:  
  \-  openfile #  named cLD file  
The output is:  
  \-  outfile  #  gene mid point list  

In genedistance.py, the inputs are:  
  \-  openfile  #  named cLD file
  \-  openfile2   #  gene mid point file
The outputs are:  
  \-  outfile  #  gene distance file
  \-  outfile2   #  distance sequence file, a string
### Hi-C interaction transfer

### Separate gene pairs into different groups

### Run MH test & Fisher's Exact test

### Bootstrap Methods ()


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
