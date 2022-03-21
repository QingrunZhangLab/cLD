import pandas as pd
import numpy
import random

#This file is to estimate the LD's variance by randomly sample samplesize SNPs from the SNP file

openfile = open(r'/PATH/SNPfile.txt','r') #SNP file
outfile = '/PATH/LDvarsample.txt'
chr = '1' #specify the chromosome
thre = 0.005     #threshold of rare variant#
samplesize = 1500 #sample size you want in each dist group, total length is n*(n-1)/2#


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

distp11 = [0,0,0,0,0,0,0,0,0,0,0,0,0]
distp10 = [0,0,0,0,0,0,0,0,0,0,0,0,0]
distp01 = [0,0,0,0,0,0,0,0,0,0,0,0,0]
distp00 = [0,0,0,0,0,0,0,0,0,0,0,0,0]

ndist = [0,0,0,0,0,0,0,0,0,0,0,0,0]

distvar = [0,0,0,0,0,0,0,0,0,0,0,0,0]
distld = [0,0,0,0,0,0,0,0,0,0,0,0,0]
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
            #elif element == '1|1'or element == '1|1\n':
            else:
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
        print('n+2 is',n+2)
        if not len(line_list2) == n+2:
            print('wrong line2 pos is',line_list2[1])
        if not len(line_list1) == n+2:
            print('wrong line1 pos is',line_list1[1])
        lenindex = 1
    if lenindex == 0:
        for i in range(2,n+2):
            count11 = count11 + p11count(line_list1[i],line_list2[i])
            count01 = count01 + p01count(line_list1[i],line_list2[i])
            count10 = count10 + p10count(line_list1[i],line_list2[i])
            count00 = count00 + p00count(line_list1[i],line_list2[i])
    p11 = count11/(2*n)
    p01 = count01/(2*n)
    p10 = count10/(2*n)
    p00 = count00/(2*n)
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
    #return(float(n*varres))
    return(float(varres))


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


def distance(line_list1,line_list2):
    dist1 = float(line_list1[1])
    dist2 = float(line_list2[1])
    crtdist = abs(dist1-dist2)
    distgroup = 0
    if crtdist < cut1:
            distgroup = 1
    elif crtdist < cut2:
            distgroup = 2
    elif crtdist < cut3:
            distgroup = 3
    elif crtdist < cut4:
            distgroup = 4
    elif crtdist < cut5:
            distgroup = 5
    elif crtdist < cut6:
            distgroup = 6
    elif crtdist < cut7:
            distgroup = 7
    elif crtdist < cut8:
            distgroup = 8
    elif crtdist < cut9:
            distgroup = 9
    elif crtdist < cut10:
            distgroup = 10
    elif crtdist < cut11:
            distgroup = 11
    elif crtdist < cut12:
            distgroup = 12
    elif crtdist > cut12:
            distgroup = 13
    return(distgroup)

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
head_list = line.split()
totallen = len(head_list)
##############n is the # of individuals###############
n = totallen - 9
print('the # of individual is ',n)

#skip the first line#
line = openfile.readline() 
line_list = line.split()

#use first n_size(samplesize) position as seed, then use these seeds to sample#
seedlist = []
linecount = 0
while line:
    linecount = linecount + 1
    if not linecount > samplesize:
        seedlist.append(int(line_list[1]))
    line = openfile.readline() 
    line_list = line.split()


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
random.seed(seedlist[0])
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
line = choose_chr(openfile,chr)
line_list = line.split()
print('len line_list',len(line_list))
print('choose chr over',line_list[0])
readindex = 1
while line:
    line = openfile.readline()
    line_list = line.split()
    if readindex in totalrand:
        if len(line) == 0:
            openfile.seek(0,0)
            while line:
                if line[0] == '#':
                    line = openfile.readline()
                else:
                    break
        while line:
            if allrare(line_list,thre) == 1:
                with open(outfile,'a',newline='') as fdo:
                    fdo.write(line)
                break
            else:
                line = openfile.readline()
                line_list = line.split()
    readindex = readindex + 1
openfile.close()
###############################
####change the openfile########
###############################
openfile = open(outfile,'r') 
line1 = openfile.readline()




p11list = []
p01list = []
p10list = []
p00list = []
i = 0
while line1:
    line_list1 = line1.split()
    currentpos = line_list1[1]
    line2 = openfile.readline()
    j = i + 1
    while line2:
        linereal1 = get_newline(line1)
        linereal2 = get_newline(line2)
        plist = cal_prop(linereal1,linereal2)
        dist12 = distance(line1.split(),line2.split())
        #nomalized cld#
        p11list.append(plist[0])
        p01list.append(plist[1])
        p10list.append(plist[2])
        p00list.append(plist[3])
        if (not plist[0]+plist[1] == 0) and (not plist[0]+plist[2] == 0) and (not plist[2]+plist[3] == 0) and (not plist[1]+plist[3] == 0):
            ndist[dist12-1] = ndist[dist12-1] + 1
            distp11[dist12-1] = distp11[dist12-1]+plist[0]
            distp01[dist12-1] = distp01[dist12-1]+plist[1]
            distp10[dist12-1] = distp10[dist12-1]+plist[2]
            distp00[dist12-1] = distp00[dist12-1]+plist[3]
            nplist = [2*n*plist[0],2*n*plist[1],2*n*plist[2],2*n*plist[3]]
            distvar[dist12-1] = distvar[dist12-1] + cldvar(nplist,2*n)
            distld[dist12-1] = distld[dist12-1] + cldfunc(nplist,2*n)
        line2 = openfile.readline()
        j = j + 1
        if j == samplesize:
            #print('this line is over,i is:',i)
            break
    find_pos_oneline(openfile,currentpos)
    line1 = openfile.readline()
    i = i + 1

print('p11 len is',len(p11list))#,', p11 list is',p11list)
print('p01 len is',len(p01list))#,', p01 list is',p01list)
print('p10 len is',len(p10list))#,', p10 list is',p10list)
print('p00 len is',len(p00list))#,', p11 list is',p00list)
fileredp11 = []
fileredp01 = []
fileredp10 = []
fileredp00 = []
filerindex = 0
for i in range(0,len(p11list)):
    if not p11list[i]+p01list[i]+p10list[i]+p00list[i] == 0:
        fileredp11.append(p11list[i])
        fileredp01.append(p01list[i])
        fileredp10.append(p10list[i])
        fileredp00.append(p00list[i])
        filerindex = filerindex + 1
pro11 = numpy.mean(fileredp11)
pro01 = numpy.mean(fileredp01)
pro10 = numpy.mean(fileredp10)
pro00 = numpy.mean(fileredp00)
print('filerindex(normal pairs) is',filerindex)
print('filered len is',len(fileredp11))
print('current chr is ',chr)
print('the sample size is ',samplesize)
print('the proportion is',pro11,pro01,pro10,pro00)

for i in range(0,13):
    if ndist[i] == 0:
        ndist[i] = 1

print('ndist is',ndist)



print('mean of distp11 is:',numpy.divide(numpy.array(distp11),numpy.array(ndist)))
print('mean of distp01 is:',numpy.divide(numpy.array(distp01),numpy.array(ndist)))
print('mean of distp10 is:',numpy.divide(numpy.array(distp10),numpy.array(ndist)))
print('mean of distp00 is:',numpy.divide(numpy.array(distp00),numpy.array(ndist)))
print('mean of distvar is:',numpy.divide(numpy.array(distvar),numpy.array(ndist)))
print('mean of distLD is:',numpy.divide(numpy.array(distld),numpy.array(ndist)))
meanld = numpy.divide(numpy.array(distvar),numpy.array(ndist))
def square(num):
    return(num*num)
meanld = list(map(square,meanld))
print('scaled LDvar is:',numpy.divide(numpy.array(distvar),numpy.array(meanld)))



openfile.close()
print('process over')








