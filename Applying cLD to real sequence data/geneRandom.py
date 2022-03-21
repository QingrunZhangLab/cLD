import pandas as pd
import numpy
import random


#This is to randomly select samplesize genes from the filtered gene file.

openfile = open(r'/PATH/filteredgene.csv','r') 
outfile = '/PATH/genesample.txt'
thre = 0.005     #threshold of rare variant
samplesize = 100 #sample size, total number of gene pairs is n*(n-1)/2#





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
    return(1)


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
    m1 = float(mlist[0])
    m2 = float(mlist[1])
    m3 = float(mlist[2])
    m4 = float(mlist[3])
    cldnumer = (m1/n - (1/(n**2))*(m1+m2)*(m1+m3))**2
    clddeno = ((m1+m3)/n)*(1-((m1+m3)/n))*((m1+m2)/n)*(1-((m1+m2)/n))
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
#only design for the file which lead to samples at the second line.
#####################################
##initial the dataframe##############
#####################################
#point at the headline

line = openfile.readline() 
headcount = 0
while line:
    if line[1] == '#':
        line = openfile.readline() 
        headcount = headcount + 1
    else:
        print('headline finded,headcount is',headcount)
        break
head_list = line.split(',')
totallen = len(head_list)
##############n is the # of individuals###############
n = totallen - 4
print('the # of individual is ',n)

#skip the first line#
line = openfile.readline() 
line_list = line.split(',')
#use first n_size(samplesize) position as seed, then use these seeds to sample#
seedlist = []
linecount = 0
while line:
    linecount = linecount + 1
    if not linecount > samplesize:
        seedlist.append(int(line_list[1]))
    line = openfile.readline() 
    line_list = line.split(',')


if linecount < samplesize:
    dif = samplesize - linecount
    adddif = range(1,dif+1)
    seedlist.extend(adddif)

#print('seedlist is ',seedlist)
print('the total line is ',linecount)
print('all seeds recorded, start sample (the sample size is',samplesize,')')
rand_line = []
totalrand = []
ranrange = range(1,linecount)
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
crtp = random.sample(ranrange,samplesize)
print('sampled lines before unique is',crtp)
totalrand = numpy.unique(crtp)
print('sampled lines are:',totalrand)




#print('sample is ',rand_line)
print('sample over, start main process')
#return to the start#
openfile.seek(0,0)
#################################
#choose the chr##################
#################################
line = openfile.readline()
line_list = line.split(',')
print('len line_list',len(line_list))
print('choose chr over',line_list[0])
readindex = 1
while line:
    line = openfile.readline()
    line_list = line.split(',')
    if readindex in totalrand:
        if len(line) == 0:
            openfile.seek(0,0)
            while line:
                if line[0] == '#':
                    line = openfile.readline()
                else:
                    break
        with open(outfile,'a',newline='') as fdo:
            fdo.write(line)
    readindex = readindex + 1

openfile.close()
print('process over')








