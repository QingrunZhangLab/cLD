library(clusterProfiler)
library(stringr)
library(ggplot2)
library(topGO)
library(Rgraphviz)
library(pathview)
library(org.Hs.eg.db)
library(enrichplot)

args = commandArgs(trailingOnly=TRUE)
cutoff<-args[1]
outdir<-args[2]
filename<-paste("GeneName_Top",cutoff,".txt",sep="")
data<-read.table(filename,header=T)
DEG.gene_symbol = as.character(data$Gene_Name)
DEG.entrez_id = mapIds(x = org.Hs.eg.db,
                       keys = DEG.gene_symbol,
                       keytype = "SYMBOL",
                       column = "ENTREZID")
DEG.entrez_id = na.omit(DEG.entrez_id)

erich.go.BP = enrichGO(gene = DEG.entrez_id,
                       OrgDb = org.Hs.eg.db,
                       keyType = "ENTREZID",
                       ont = "BP",
                       pvalueCutoff = 0.5)
cluster_summary <- data.frame(erich.go.BP)
outfile_0=paste(outdir,"/GO_BP_Top",cutoff,".csv",sep="")
write.csv(cluster_summary , outfile_0, row.names=FALSE)
outfile_1 <-paste(outdir,"/bar_gene_BP_Top",cutoff,".pdf",sep="")
pdf(file=outfile_1)
barplot(erich.go.BP,showCategory=10,font.size=15)  + scale_y_discrete(labels=function(erich.go.BP) str_wrap(erich.go.BP, width=25))
dev.off()
outfile_2 <-paste(outdir,"/plot_gene_BP_Top",cutoff,".pdf",sep="")
pdf(file=outfile_2)
dotplot(erich.go.BP,font.size=15) + scale_y_discrete(labels=function(erich.go.BP) str_wrap(erich.go.BP, width=25))
dev.off()

erich.go.CC = enrichGO(gene = DEG.entrez_id,
                       OrgDb = org.Hs.eg.db,
                       keyType = "ENTREZID",
                       ont = "CC",
                       pvalueCutoff = 0.5)
cluster_summary_cc <- data.frame(erich.go.CC)
outfile_3=paste(outdir,"/GO_CC_Top",cutoff,".csv",sep="")
write.csv(cluster_summary_cc , outfile_3, row.names=FALSE)
outfile_4 <-paste(outdir,"/bar_gene_CC_Top",cutoff,".pdf",sep="")
pdf(file=outfile_4)
barplot(erich.go.CC,showCategory=10,font.size=15)  + scale_y_discrete(labels=function(erich.go.CC) str_wrap(erich.go.CC, width=25))
dev.off()
outfile_5 <-paste(outdir,"/plot_gene_CC_Top",cutoff,".pdf",sep="")
pdf(file=outfile_5)
dotplot(erich.go.CC,font.size=15) + scale_y_discrete(labels=function(erich.go.CC) str_wrap(erich.go.CC, width=25))
dev.off()

erich.go.MF = enrichGO(gene = DEG.entrez_id,
                       OrgDb = org.Hs.eg.db,
                       keyType = "ENTREZID",
                      ont = "MF",
                       pAdjustMethod = "BH",
                       pvalueCutoff = 1)
cluster_summary_mf <- data.frame(erich.go.MF)
outfile_6=paste(outdir,"/GO_MF_Top",cutoff,".csv",sep="")
write.csv(cluster_summary_mf , outfile_6, row.names=FALSE)
outfile_7 <-paste(outdir,"/bar_gene_Top",cutoff,".pdf",sep="")
pdf(file=outfile_7)
barplot(erich.go.MF,showCategory=10,font.size=15)  + scale_y_discrete(labels=function(erich.go.MF) str_wrap(erich.go.MF, width=25))
dev.off()
outfile_8 <-paste(outdir,"/plot_gene_MF_Top",cutoff,".pdf",sep="")
pdf(file=outfile_8)
dotplot(erich.go.MF,font.size=15) + scale_y_discrete(labels=function(erich.go.MF) str_wrap(erich.go.MF, width=25))
dev.off()

kk <- enrichKEGG(gene = DEG.entrez_id,
                 organism = 'hsa',
                 pvalueCutoff = 1
                 )
outfile_9 <-paste(outdir,"/kegg_bar_gene_Top",cutoff,".pdf",sep="")
pdf(file=outfile_9)
barplot(kk,showCategory=10,font.size=15)  + scale_y_discrete(labels=function(kk) str_wrap(kk, width=25))
dev.off()
outfile_10 <-paste(outdir,"/kegg_plot_gene_Top",cutoff,".pdf",sep="")
pdf(file=outfile_10)
dotplot(kk,font.size=15) + scale_y_discrete(labels=function(kk) str_wrap(kk, width=25))
dev.off()
