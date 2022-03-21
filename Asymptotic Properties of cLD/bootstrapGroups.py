import time
import os
import pandas as pd
import numpy
import random

#This is separate the bootstrap result into different groups according to the cMAF. One can use the output to plot the density.


openfile = open(r'/PATH/SNPfile.txt','r') 
genesample = open(r'/PATH/genesample.txt.txt')
outfile = '/PATH/BootstrapGroups.txt'
chr = '1' #Chromosome
thre = 0.005     #threshold of rare variant
indsamplesize = 250 #For each sample
epoch = 300


with open(outfile,'w',newline='') as fdo:
    fdo.write('')


cut1 = 35 * 1000
cut2 = cut1 * 2
cut3 = cut2 * 2
cut4 = cut3 * 2
cut5 = cut4 * 2
cut6 = cut5 * 2
cut7 = cut6 * 2
cut8 = cut7 * 2
cut9 = cut8 * 2
cut10 = cut9 * 2
cut11 = cut10 * 2
cut12 = cut11 * 2
cut13 = cut12 * 2

def get_newline(line):
    line_list = line.split()
    np = len(line_list)
    rare = allrare(line_list,thre)
    if rare == 0:
        add_line = ['0|0']*n
        newline = [line_list[0],line_list[1]]
        print('YOUR PROGRAM IS WRONG!!!!!!')
        newline.extend(add_line)
    else:
        newline = [line_list[0],line_list[1]]
        for element in line_list[9:np]:
            if element == '0|0' or element == '0|0\n':
                newline.append('0|0')
            elif element == '1|0'or element == '1|0\n':
                newline.append('1|0')
            elif element == '0|1'or element == '0|1\n':
                newline.append('0|1')
            elif element == '1|1'or element == '1|1\n':
                newline.append('1|1')
    return(newline)

def allrare(line_list,thres):
    n2 = 0
    n0 = 0
    rare = 0
    for elem in line_list:
        if elem == '1|1'or elem == '1|1\n':
           n2 = n2 + 1
        elif elem == '0|0'or elem == '0|0\n':
               n0 = n0 + 1
    n1 = n - n0 - n2
    allelfq = (n1+2*n2)/(2*n)
    if allelfq < thres:
        rare = 1
    return(rare)


def choose_chr(openfile,chr):
    #skip the headline
    line = openfile.readline() 
    line_list = line.split()
    while line:
        if not line_list[0]==str(chr):    
            line = openfile.readline()
            line_list = line.split()
        else:
            print('choose over(in-func)',line_list[0])
            break
    return(line)

def p11count(sign1,sign2):
    count = 0
    if sign1 == '1|0' or sign1 == '1|0\n':
       if sign2 == '1|0'or sign2 == '1|0\n':
           count = count + 1
       if sign2 == '1|1'or sign2 == '1|1\n':
           count = count + 1
    if sign1 == '0|1' or sign1 == '0|1\n':
        if sign2 == '0|1'or sign2 == '0|1\n':
            count = count + 1
        if sign2 == '1|1'or sign2 == '1|1\n':
            count = count + 1
    if sign1 == '1|1' or sign1 == '1|1\n':
        if sign2 == '1|0'or sign2 == '1|0\n' or sign2 == '0|1'or sign2 == '0|1\n':
            count = count + 1
        if sign2 == '1|1'or sign2 == '1|1\n':
            count = count + 2
    return(count)

def p01count(sign1,sign2):
    count = 0
    if sign1 == '1|0' or sign1 == '1|0\n':
       if sign2 == '0|1'or sign2 == '0|1\n':
           count = count + 1
       if sign2 == '1|1'or sign2 == '1|1\n':
           count = count + 1
    if sign1 == '0|1' or sign1 == '0|1\n':
        if sign2 == '1|0'or sign2 == '1|0\n':
            count = count + 1
        if sign2 == '1|1'or sign2 == '1|1\n':
            count = count + 1
    if sign1 == '0|0' or sign1 == '0|0\n':
        if sign2 == '1|0'or sign2 == '1|0\n' or sign2 == '0|1'or sign2 == '0|1\n':
            count = count + 1
        if sign2 == '1|1'or sign2 == '1|1\n':
            count = count + 2
    return(count)

def p10count(sign1,sign2):
    count = 0
    if sign1 == '1|0' or sign1 == '1|0\n':
       if sign2 == '0|1'or sign2 == '0|1\n':
           count = count + 1
       if sign2 == '0|0'or sign2 == '0|0\n':
           count = count + 1
    if sign1 == '0|1' or sign1 == '0|1\n':
        if sign2 == '1|0'or sign2 == '1|0\n':
            count = count + 1
        if sign2 == '0|0'or sign2 == '0|0\n':
            count = count + 1
    if sign1 == '1|1' or sign1 == '1|1\n':
        if sign2 == '1|0'or sign2 == '1|0\n' or sign2 == '0|1'or sign2 == '0|1\n':
            count = count + 1
        if sign2 == '0|0'or sign2 == '0|0\n':
            count = count + 2
    return(count)


def p00count(sign1,sign2):
    count = 0
    if sign1 == '1|0' or sign1 == '1|0\n':
       if sign2 == '1|0'or sign2 == '1|0\n':
           count = count + 1
       if sign2 == '0|0'or sign2 == '0|0\n':
           count = count + 1
    if sign1 == '0|1' or sign1 == '0|1\n':
        if sign2 == '0|1'or sign2 == '0|1\n':
            count = count + 1
        if sign2 == '0|0'or sign2 == '0|0\n':
            count = count + 1
    if sign1 == '0|0' or sign1 == '0|0\n':
        if sign2 == '1|0'or sign2 == '1|0\n' or sign2 == '0|1'or sign2 == '0|1\n':
            count = count + 1
        if sign2 == '0|0'or sign2 == '0|0\n':
            count = count + 2
    return(count)

def cal_prop(line_list1,line_list2):
    count11 = 0
    count01 = 0
    count10 = 0
    count00 = 0
    lenindex = 0
    if not len(line_list2) == len(line_list1):
        print('WRONG LEN')
        print('Len line_list1 is',len(line_list1))
        print('len line_list2 is',len(line_list2))
        print('n+9 is',n+9)
        if not len(line_list2) == n+9:
            print('wrong line2 pos is',line_list2[1])
        if not len(line_list1) == n+9:
            print('wrong line1 pos is',line_list1[1])
        lenindex = 1
    if lenindex == 0:
        for i in sampleind:
            count11 = count11 + p11count(line_list1[i],line_list2[i])
            count01 = count01 + p01count(line_list1[i],line_list2[i])
            count10 = count10 + p10count(line_list1[i],line_list2[i])
            count00 = count00 + p00count(line_list1[i],line_list2[i])
    p11 = count11/(2*indsamplesize)
    p01 = count01/(2*indsamplesize)
    p10 = count10/(2*indsamplesize)
    p00 = count00/(2*indsamplesize)
    return(p11,p01,p10,p00)


def find_pos(openfile,num1,num2):
    global line
    global line_list
    openfile.seek(0,0)
    line = openfile.readline()
    while line:
        if line[0] == '#':
            line = openfile.readline() 
        else:
            break
    line_list = line.split()
    for i in range(0,num1):
        line = openfile.readline()
        line_list = line.split()
    while line:
        if allrare(line_list,thre) == 1:
            line1 = line
            break
        line = openfile.readline()
        line_list = line.split()
    for i in range(0,num2-100):#to avoid overrange
        line = openfile.readline()
        line_list = line.split()
    while line:
        if allrare(line_list,thre) == 1:
            line2 = line
            break
        if len(line) == 0:
            openfile.seek(0,0)
            while line:
                if line[0] == '#':
                    line = openfile.readline()
                else:
                    break
        line = openfile.readline()
        line_list = line.split()
    if line1 == line2:
        print('line1 [0] is',line1.split()[1])
        print('line2 [0] is',line2.split()[1])
    return(line1,line2)

def cldvar(plist,n):
    p1 = plist[0]
    #p2 = plist[1]
    #p3 = plist[2]
    p4 = plist[3]
    p2 = (plist[1] + plist[2])/2
    p3 = p2
    v1 = (n*(n-2*p1-p2-p3)*(n*p1-(p1+p2)*(p1+p3))*(n*p1*(p2+p3)+2*n*p2*p3-(p1+p2)*(p1+p3)*(p2+p3)))/(((p1+p2)**2)*((p1+p3)**2)*((-n+p1+p2)**2)*((-n+p1+p3)**2))
    v2 = (n*(p1**2)*((-n+p1+p2)**2)-n*(p3**2)*((p1+p2)**2))/(((p1+p2)**2)*(p1+p3)*((-n+p1+p2)**2)*(-n+p1+p3))
    v3 = (n*(p1**2)*((-n+p1+p3)**2)-n*(p2**2)*((p1+p3)**2))/(((p1+p3)**2)*(p1+p2)*((-n+p1+p3)**2)*(-n+p1+p2))
    v = numpy.mat([v1,v2,v3,0])
    #print('vector v is',v)
    p1 = p1/n
    p2 = p2/n
    p3 = p3/n
    p4 = p4/n
    #print('p are',p1,p2,p3,p4)
    matr = numpy.mat([[p1-p1**2,-p1*p2,-p1*p3,-p1*p4],
            [-p2*p1,p2-p2**2,-p2*p3,-p2*p4],
            [-p3*p1,-p3*p2,p3-p3**2,-p3*p4],
            [-p4*p1,-p4*p2,-p4*p3,p4-p4**2]])
    #print('matr is',matr)
    varres = v*matr*v.T
    return(n*float(varres))

def cldfunc(mlist,n):
    cld = 0
    m1 = float(mlist[0])
    m2 = float(mlist[1])
    m3 = float(mlist[2])
    m4 = float(mlist[3])
    cldnumer = (m1/n - (1/(n**2))*(m1+m2)*(m1+m3))**2
    clddeno = ((m1+m3)/n)*(1-((m1+m3)/n))*((m1+m2)/n)*(1-((m1+m2)/n))
    if not clddeno == 0:
        cld = cldnumer/clddeno
    return(cld)


def find_pos_oneline(openfile,genepos):
    openfile.seek(0,0)
    line = openfile.readline()
    line_list = line.split()
    while line:
        if line_list[1] ==genepos:
            break
        line = openfile.readline()
        line_list = line.split()
    return(1)

def groupld(snp1,snp2):
    snpnum1 = 0
    for i in range(0,len(snp1)):
        if not len(snp1[i]) == 0:
            snpnum1 = snpnum1 + 1
    snpnum2 = 0
    for i in range(0,len(snp2)):
        if not len(snp2[i]) == 0:
            snpnum2 = snpnum2 + 1
    ldcrtlist = []
    for i in range(0,snpnum1):
        for j in range(0,snpnum2):
            crtld = cal_prop(snp1[i],snp2[j])
            proplist = [2*indsamplesize*crtld[0],2*indsamplesize*crtld[1],2*indsamplesize*crtld[2],2*indsamplesize*crtld[3]]
            ldcrtlist.append(cldfunc(proplist,2*indsamplesize))
    return(numpy.mean(ldcrtlist))

def cmafgroup(cmaf):
    indexgrp = 0
    if cmaf < 0.4 and cmaf > 0.2:
        indexgrp = 1
    elif cmaf > 0.1 and cmaf < 0.2:
        indexgrp = 2
    elif cmaf < 0.1 and cmaf > 0.05:
        indexgrp = 3
    elif cmaf < 0.05:
        indexgrp = 4
    return(indexgrp)
#only design for the file which lead to samples at the second line.
#####################################
##initial the dataframe##############
#####################################
#point at the headline
line = genesample.readline()
line_list = line.split(',')
genestart = []
geneend = []
genelen = len(line_list)
refgene = ['1|1']*genelen
gene_cmaf = []
sampleind = range(0,genelen)
while line:
    genestart.append(float(line_list[2]))
    geneend.append(float(line_list[3]))
    problist = cal_prop(line_list,refgene)
    genecMAF = problist[0]+problist[2]
    gene_cmaf.append(cmafgroup(genecMAF))
    line = genesample.readline()
    line_list = line.split(',')
genesample.close()
#print genesample over#

line = openfile.readline() 
headcount = 0
while line:
    if line[1] == '#':
        line = openfile.readline() 
        headcount = headcount + 1
    else:
        print('headline finded,headcount is',headcount)
        break
head_list = line.split()
totallen = len(head_list)
##############n is the # of individuals###############
n = totallen - 9
print('the # of individual is ',n)

#skip the first line#
line = openfile.readline() 
line_list = line.split()

#use first n_size(samplesize) position as seed, then use these seeds to sample#

#print('seedlist is ',seedlist)
################################################
#####could change the sample method!(method 1)##
################################################
#for i in range(0,len(seedlist)):
#    random.seed(seedlist[i])
#    crtp = random.sample(ranrange,2)
#    crtp.sort()
#    totalrand.append(crtp[0])
#    totalrand.append(crtp[1])
#    #print('crtpsorted is',crtp)
#    #crtp[1] = crtp[1] - crtp[0]
#    rand_line.append(crtp)
###############################################################################
#####method 2##################(only suit for samplesize << linecount)!!!!!!!##
###############################################################################
#random.seed(seedlist[0])


#print('sample is ',rand_line)

#return to the start#
#################################
#choose the chr##################
#################################
###############################
####change the openfile########
###############################
ldlist = []
genepair_cmaf = []
ldvarlist = []
genesnp = [[],[],[],[],[],[],[],[],[],[],[],[],[],[],[],[],[],[],[],[],[],[],[],[],[]]*50
snpnum = [0]*50#this number equals the number of genes
#######################start the iteration################
t = 0

for i in range(0,len(genestart)-1):
    for j in range(i+1,len(genestart)):
        #adjust min, both, max here
        gene1cmaf = gene_cmaf[i]
        gene2cmaf = gene_cmaf[j]
        genepair_cmaf.append(''.join([str(gene1cmaf),'+',str(gene2cmaf)]))
cmafstr = ' '.join(genepair_cmaf)
with open(outfile,'a',newline='') as fdo:
    fdo.write(cmafstr+'\n')

for t in range(0,epoch):
#if t == 1:
    totalind = range(9,n+9)
    sampleind = random.sample(totalind,indsamplesize)#
    #sampleind = totalind#
    #indsamplesize = n#
    for i in range(0,len(genestart)-1):
        rareind = 0
        openfile.seek(0,0)
        line = openfile.readline()
        line = openfile.readline()
        line_list = line.split()
        snpcot = 0
        if (t == 0) and (i == 0):
            while line:
                snp1num = 0
                if (float(line_list[1]) < geneend[i]) and (float(line_list[1]) > genestart[i]):
                    rareind = allrare(line_list,thre)
                    if rareind == 1:
                        snp1num = snp1num+1
                    if rareind == 1 and snpcot < 25:
                        genesnp[i*25+snpcot] = line_list
                        snpcot = snpcot + 1
                        #if snpcot == 5:
                        #    break
                line = openfile.readline()
                line_list = line.split()
            snpnum[0] = snp1num
        for j in range(i+1,len(genestart)):
            rareind = 0
            openfile.seek(0,0)
            line = openfile.readline()
            line = openfile.readline()
            line_list = line.split()
            snpcot = 0
            snp2num = 0
            if (i == 0) and (t == 0):
                while line:
                    if (float(line_list[1]) < geneend[j]) and (float(line_list[1]) > genestart[j]):
                        rareind = allrare(line_list,thre)
                        if rareind == 1:
                            snp2num = snp2num + 1
                        if rareind == 1 and snpcot < 25:
                            genesnp[j*25+snpcot] = line_list
                            snpcot = snpcot + 1
                         #   if snpcot == 5:
                         #       break
                    line = openfile.readline()
                    line_list = line.split()
                snpnum[j] = snp2num
            #if len(genesnp[i*25+24]) == 0:
             #   print('now first is',i)
            #if len(genesnp[j*25+24]) == 0:
             #   print('now second is',j)
            if t == 0:#adjust min, both, max here
                gene1cmaf = gene_cmaf[i]
                gene2cmaf = gene_cmaf[j]
                genepair_cmaf.append(''.join([str(gene1cmaf),'+',str(gene2cmaf)]))
            #ldlist.append(str(snpnum[i]*snpnum[j]*groupld(genesnp[i*25:i*25+25],genesnp[j*25:j*25+25])))
            ldlist.append(str(groupld(genesnp[i*25:i*25+25],genesnp[j*25:j*25+25])))
    if t == 0:
        cmafstr = ' '.join(genepair_cmaf)
        with open(outfile,'a',newline='') as fdo:
            fdo.write(cmafstr+'\n')
    crtstr = ' '.join(ldlist)
    ldlist = []
    with open(outfile,'a',newline='') as fdo:
        fdo.write(crtstr+'\n')

    print('one iteration over')

################end the iteration##################


openfile.close()
print('process over')








