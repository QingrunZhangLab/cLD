import time
import os
import pandas as pd
import numpy

#This code could calculate the cLD variance according to the distance groups and cMAF groups.#
#One can use this file and LDvar file to plot the ratio of variance#

openfile = open(r'/PATH/filteredgene.csv','r') #This is the filtered gene file from filter. 







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


#cmafcut1 = 0.00125
#cmafcut2 = 0.0025
#cmafcut3 = 0.005
#cmafcut4 = 0.01
#cmafcut5 = 0.02
cmafcut1 = 0.05
cmafcut2 = 0.1
cmafcut3 = 0.2
cmafcut4 = 0.4
cmafcut5 = 0.5

cmaf1p11 = [0,0,0,0,0,0,0,0,0,0,0,0,0]
cmaf1p10 = [0,0,0,0,0,0,0,0,0,0,0,0,0]
cmaf1p01 = [0,0,0,0,0,0,0,0,0,0,0,0,0]
cmaf1p00 = [0,0,0,0,0,0,0,0,0,0,0,0,0]

ncmaf1 = [0,0,0,0,0,0,0,0,0,0,0,0,0]


cmaf2p11 = [0,0,0,0,0,0,0,0,0,0,0,0,0]
cmaf2p10 = [0,0,0,0,0,0,0,0,0,0,0,0,0]
cmaf2p01 = [0,0,0,0,0,0,0,0,0,0,0,0,0]
cmaf2p00 = [0,0,0,0,0,0,0,0,0,0,0,0,0]

ncmaf2 = [0,0,0,0,0,0,0,0,0,0,0,0,0]

cmaf3p11 = [0,0,0,0,0,0,0,0,0,0,0,0,0]
cmaf3p10 = [0,0,0,0,0,0,0,0,0,0,0,0,0]
cmaf3p01 = [0,0,0,0,0,0,0,0,0,0,0,0,0]
cmaf3p00 = [0,0,0,0,0,0,0,0,0,0,0,0,0]

ncmaf3 = [0,0,0,0,0,0,0,0,0,0,0,0,0]

cmaf4p11 = [0,0,0,0,0,0,0,0,0,0,0,0,0]
cmaf4p10 = [0,0,0,0,0,0,0,0,0,0,0,0,0]
cmaf4p01 = [0,0,0,0,0,0,0,0,0,0,0,0,0]
cmaf4p00 = [0,0,0,0,0,0,0,0,0,0,0,0,0]

ncmaf4 = [0,0,0,0,0,0,0,0,0,0,0,0,0]


cmaf5p11 = [0,0,0,0,0,0,0,0,0,0,0,0,0]
cmaf5p10 = [0,0,0,0,0,0,0,0,0,0,0,0,0]
cmaf5p01 = [0,0,0,0,0,0,0,0,0,0,0,0,0]
cmaf5p00 = [0,0,0,0,0,0,0,0,0,0,0,0,0]

ncmaf5 = [0,0,0,0,0,0,0,0,0,0,0,0,0]


def find_pos(openfile,genepos):
    openfile.seek(0,0)
    line = openfile.readline()
    line_list = line.split(',')
    while line:
        if line_list[1] ==genepos:
            break
        line = openfile.readline()
        line_list = line.split(',')
    return(1)

def cal_pab(line1,line2):
    line_list1 = line1.split(',')
    line_list2 = line2.split(',')
    count = 0
    for i in range(4,n):
        if line_list1[i] == '1|0' or line_list1[i] == '1|0\n':
            if line_list2[i] == '1|0'or line_list2[i] == '1|0\n':
                count = count + 1
            if line_list2[i] == '1|1'or line_list2[i] == '1|1\n':
                count = count + 1
        if line_list1[i] == '0|1' or line_list1[i] == '0|1\n':
            if line_list2[i] == '0|1'or line_list2[i] == '0|1\n':
                count = count + 1
            if line_list2[i] == '1|1'or line_list2[i] == '1|1\n':
                count = count + 1
        if line_list1[i] == '1|1' or line_list1[i] == '1|1\n':
            if line_list2[i] == '1|0'or line_list2[i] == '1|0\n' or line_list2[i] == '0|1'or line_list2[i] == '0|1\n':
                count = count + 1
            if line_list2[i] == '1|1'or line_list2[i] == '1|1\n':
                count = count + 2
    return(count/(2*(n-4)))

def cal_p01(line1,line2):
    line_list1 = line1.split(',')
    line_list2 = line2.split(',')
    count = 0
    for i in range(4,n):
        if line_list1[i] == '1|0' or line_list1[i] == '1|0\n':
            if line_list2[i] == '0|1'or line_list2[i] == '0|1\n':
                count = count + 1
            if line_list2[i] == '1|1'or line_list2[i] == '1|1\n':
                count = count + 1
        if line_list1[i] == '0|1' or line_list1[i] == '0|1\n':
            if line_list2[i] == '1|0'or line_list2[i] == '1|0\n':
                count = count + 1
            if line_list2[i] == '1|1'or line_list2[i] == '1|1\n':
                count = count + 1
        if line_list1[i] == '0|0' or line_list1[i] == '0|0\n':
            if line_list2[i] == '1|0'or line_list2[i] == '1|0\n' or line_list2[i] == '0|1'or line_list2[i] == '0|1\n':
                count = count + 1
            if line_list2[i] == '1|1'or line_list2[i] == '1|1\n':
                count = count + 2
    return(count/(2*(n-4)))

def cal_p10(line1,line2):
    line_list1 = line1.split(',')
    line_list2 = line2.split(',')
    count = 0
    for i in range(4,n):
        if line_list1[i] == '1|0' or line_list1[i] == '1|0\n':
            if line_list2[i] == '0|1'or line_list2[i] == '0|1\n':
                count = count + 1
            if line_list2[i] == '0|0'or line_list2[i] == '0|0\n':
                count = count + 1
        if line_list1[i] == '0|1' or line_list1[i] == '0|1\n':
            if line_list2[i] == '1|0'or line_list2[i] == '1|0\n':
                count = count + 1
            if line_list2[i] == '0|0'or line_list2[i] == '0|0\n':
                count = count + 1
        if line_list1[i] == '1|1' or line_list1[i] == '1|1\n':
            if line_list2[i] == '1|0'or line_list2[i] == '1|0\n' or line_list2[i] == '0|1'or line_list2[i] == '0|1\n':
                count = count + 1
            if line_list2[i] == '0|0'or line_list2[i] == '0|0\n':
                count = count + 2
    return(count/(2*(n-4)))


def cal_p00(line1,line2):
    line_list1 = line1.split(',')
    line_list2 = line2.split(',')
    count = 0
    for i in range(4,n):
        if line_list1[i] == '1|0' or line_list1[i] == '1|0\n':
            if line_list2[i] == '1|0'or line_list2[i] == '1|0\n':
                count = count + 1
            if line_list2[i] == '0|0'or line_list2[i] == '0|0\n':
                count = count + 1
        if line_list1[i] == '0|1' or line_list1[i] == '0|1\n':
            if line_list2[i] == '0|1'or line_list2[i] == '0|1\n':
                count = count + 1
            if line_list2[i] == '0|0'or line_list2[i] == '0|0\n':
                count = count + 1
        if line_list1[i] == '0|0' or line_list1[i] == '0|0\n':
            if line_list2[i] == '1|0'or line_list2[i] == '1|0\n' or line_list2[i] == '0|1'or line_list2[i] == '0|1\n':
                count = count + 1
            if line_list2[i] == '0|0'or line_list2[i] == '0|0\n':
                count = count + 2
    return(count/(2*(n-4)))

def distance(line1,line2):
    line_list1 = line1.split(',')
    line_list2 = line2.split(',')
    dist1 = (float(line_list1[2])+float(line_list1[3]))/2
    dist2 = (float(line_list2[2])+float(line_list2[3]))/2
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

def cmaffun(cmaf1,cmaf2):
    cmafindex = 0
    if min(cmaf1,cmaf2) < cmafcut1:
        cmafindex = 1
    elif min(cmaf1,cmaf2) < cmafcut2:
        cmafindex = 2
    elif min(cmaf1,cmaf2) < cmafcut3:
        cmafindex = 3
    elif min(cmaf1,cmaf2) < cmafcut4:
        cmafindex = 4
    elif min(cmaf1,cmaf2) > cmafcut4:
        cmafindex = 5
    return(cmafindex)



def cldvar(plist,n):
    p1 = plist[0]
    #p2 = plist[1]
    #p3 = plist[2]
    p4 = plist[3]
    p2 = (plist[1] + plist[2])/2
    p3 = p2
    index = 1
    if ((p1+p2)**2)*((p1+p3)**2)*((-n+p1+p2)**2)*((-n+p1+p3)**2) == 0 or ((p1+p2)**2)*(p1+p3)*((-n+p1+p2)**2)*(-n+p1+p3)==0 or ((p1+p3)**2)*(p1+p2)*((-n+p1+p3)**2)*(-n+p1+p2) == 0:
        index = 0
    else:
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
    if index == 0:
        ans = 0
    else:
        varres = v*matr*v.T
        ans = float(n*varres)
    return(ans)


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



def count_one(line_list):
    count = 0
    for element in line_list[4:n]:
        if element == '0|1'or element == '0|1\n' or element == '1|0'or element == '1|0\n':
            count = count + 1
        if element == '1|1'or element == '1|1\n':
            count = count + 2
    return(count/(2*(n-4)))


#save pa pb pc ...
pone = []
#head
line = openfile.readline()
#second
line = openfile.readline()
line_list = line.split(',')
n = len(line_list)
while line:
    pone.append(count_one(line_list))
    line = openfile.readline()
    line_list = line.split(',')

np = len(pone)
#refresh the start
openfile.seek(0,0)
#first line
line = openfile.readline()
head_list = line.split(',')
#second line & initial
line1 = openfile.readline()
line_list1 = line1.split(',')
print('n of sample',len(line_list1))
print('initial end, start loop')
pablist = []
p01list = []
p10list = []
p00list = []
numsample = 2*(n-4)
cldvarlist1 = [0,0,0,0,0,0,0,0,0,0,0,0,0]
cldvarlist2 = [0,0,0,0,0,0,0,0,0,0,0,0,0]
cldvarlist3 = [0,0,0,0,0,0,0,0,0,0,0,0,0]
cldvarlist4 = [0,0,0,0,0,0,0,0,0,0,0,0,0]
cldvarlist5 = [0,0,0,0,0,0,0,0,0,0,0,0,0]
cldlist1 = [0,0,0,0,0,0,0,0,0,0,0,0,0]
cldlist2 = [0,0,0,0,0,0,0,0,0,0,0,0,0]
cldlist3 = [0,0,0,0,0,0,0,0,0,0,0,0,0]
cldlist4 = [0,0,0,0,0,0,0,0,0,0,0,0,0]
cldlist5 = [0,0,0,0,0,0,0,0,0,0,0,0,0]


i = 0
while line1:
    line_list1 = line1.split(',')
    currentpos = line_list1[1]
    count = count_one(line_list1)
    line2 = openfile.readline()
    j = i + 1
    if j == np:
        print('the whole process is over,i is:',i)
        #break
    while line2:
        count2 = count_one(line2.split(','))
        cmaf1 = count
        cmaf2 = count2
        cmaf12 = cmaffun(cmaf1,cmaf2)
        dist12 = distance(line1,line2)
        #nomalized cld#
        pab = cal_pab(line1,line2) #it's p11#
        p01 = cal_p01(line1,line2)
        p10 = cal_p10(line1,line2)
        p00 = cal_p00(line1,line2)
        pablist.append(pab)
        p01list.append(p01)
        p10list.append(p10)
        p00list.append(p00)
        if cmaf12 == 1:
            cmaf1p00[dist12-1] = cmaf1p00[dist12-1] + p00
            cmaf1p11[dist12-1] = cmaf1p11[dist12-1] + pab
            cmaf1p01[dist12-1] = cmaf1p01[dist12-1] + p01
            cmaf1p10[dist12-1] = cmaf1p10[dist12-1] + p10
            ncmaf1[dist12-1] = ncmaf1[dist12-1] + 1
            nplist = [numsample*pab,numsample*p01,numsample*p10,numsample*p00]
            cldlist1[dist12-1] = cldlist1[dist12-1] + cldfunc(nplist,numsample)
            cldvarlist1[dist12-1] = cldvarlist1[dist12-1] + cldvar(nplist,numsample)
        if cmaf12 == 2:
            cmaf2p00[dist12-1] = cmaf2p00[dist12-1] + p00
            cmaf2p11[dist12-1] = cmaf2p11[dist12-1] + pab
            cmaf2p01[dist12-1] = cmaf2p01[dist12-1] + p01
            cmaf2p10[dist12-1] = cmaf2p10[dist12-1] + p10
            ncmaf2[dist12-1] = ncmaf2[dist12-1] + 1
            nplist = [numsample*pab,numsample*p01,numsample*p10,numsample*p00]
            cldlist2[dist12-1] = cldlist2[dist12-1] + cldfunc(nplist,numsample)
            cldvarlist2[dist12-1] = cldvarlist2[dist12-1] + cldvar(nplist,numsample)

        if cmaf12 == 3:
            cmaf3p00[dist12-1] = cmaf3p00[dist12-1] + p00
            cmaf3p11[dist12-1] = cmaf3p11[dist12-1] + pab
            cmaf3p01[dist12-1] = cmaf3p01[dist12-1] + p01
            cmaf3p10[dist12-1] = cmaf3p10[dist12-1] + p10
            ncmaf3[dist12-1] = ncmaf3[dist12-1] + 1
            nplist = [numsample*pab,numsample*p01,numsample*p10,numsample*p00]
            cldlist3[dist12-1] = cldlist3[dist12-1] + cldfunc(nplist,numsample)
            cldvarlist3[dist12-1] = cldvarlist3[dist12-1] + cldvar(nplist,numsample)

        if cmaf12 == 4:
            cmaf4p00[dist12-1] = cmaf4p00[dist12-1] + p00
            cmaf4p11[dist12-1] = cmaf4p11[dist12-1] + pab
            cmaf4p01[dist12-1] = cmaf4p01[dist12-1] + p01
            cmaf4p10[dist12-1] = cmaf4p10[dist12-1] + p10
            ncmaf4[dist12-1] = ncmaf4[dist12-1] + 1
            nplist = [numsample*pab,numsample*p01,numsample*p10,numsample*p00]
            cldlist4[dist12-1] = cldlist4[dist12-1] + cldfunc(nplist,numsample)
            cldvarlist4[dist12-1] = cldvarlist4[dist12-1] + cldvar(nplist,numsample)

        if cmaf12 == 5:
            cmaf5p00[dist12-1] = cmaf5p00[dist12-1] + p00
            cmaf5p11[dist12-1] = cmaf5p11[dist12-1] + pab
            cmaf5p01[dist12-1] = cmaf5p01[dist12-1] + p01
            cmaf5p10[dist12-1] = cmaf5p10[dist12-1] + p10
            ncmaf5[dist12-1] = ncmaf5[dist12-1] + 1
            nplist = [numsample*pab,numsample*p01,numsample*p10,numsample*p00]
            cldlist5[dist12-1] = cldlist5[dist12-1] + cldfunc(nplist,numsample)
            cldvarlist5[dist12-1] = cldvarlist5[dist12-1] + cldvar(nplist,numsample)

        line2 = openfile.readline()
        j = j + 1
        if j == np:
            print('this line is over,i is:',i)
            break
    print('cld line writed')
    find_pos(openfile,currentpos)
    line1 = openfile.readline()
    i = i + 1


for i in range(0,13):
    if ncmaf1[i] == 0:
        ncmaf1[i] = 1
    if ncmaf2[i] == 0:
        ncmaf2[i] = 1
    if ncmaf3[i] == 0:
        ncmaf3[i] = 1
    if ncmaf4[i] == 0:
        ncmaf4[i] = 1
    if ncmaf5[i] == 0:
        ncmaf5[i] = 1



print('length of pab is:',len(pablist))
print('length of p01 is:',len(p01list))
print('length of p10 is:',len(p10list))
print('length of p00 is:',len(p00list))

print('length of cmaf1 is:',ncmaf1)
print('length of cmaf2 is:',ncmaf2)
print('length of cmaf3 is:',ncmaf3)
print('length of cmaf4 is:',ncmaf4)
print('length of cmaf5 is:',ncmaf5)

print('mean of pab is:',numpy.mean(pablist))
print('mean of p01 is:',numpy.mean(p01list))
print('mean of p10 is:',numpy.mean(p10list))
print('mean of p00 is:',numpy.mean(p00list))

print('mean of cmaf1p11 is:',numpy.divide(numpy.array(cmaf1p11),numpy.array(ncmaf1)))
print('mean of cmaf1p01 is:',numpy.divide(numpy.array(cmaf1p01),numpy.array(ncmaf1)))
print('mean of cmaf1p10 is:',numpy.divide(numpy.array(cmaf1p10),numpy.array(ncmaf1)))
print('mean of cmaf1p00 is:',numpy.divide(numpy.array(cmaf1p00),numpy.array(ncmaf1)))

print('mean of cmaf2p11 is:',numpy.divide(numpy.array(cmaf2p11),numpy.array(ncmaf2)))
print('mean of cmaf2p01 is:',numpy.divide(numpy.array(cmaf2p01),numpy.array(ncmaf2)))
print('mean of cmaf2p10 is:',numpy.divide(numpy.array(cmaf2p10),numpy.array(ncmaf2)))
print('mean of cmaf2p00 is:',numpy.divide(numpy.array(cmaf2p00),numpy.array(ncmaf2)))

print('mean of cmaf3p11 is:',numpy.divide(numpy.array(cmaf3p11),numpy.array(ncmaf3)))
print('mean of cmaf3p01 is:',numpy.divide(numpy.array(cmaf3p01),numpy.array(ncmaf3)))
print('mean of cmaf3p10 is:',numpy.divide(numpy.array(cmaf3p10),numpy.array(ncmaf3)))
print('mean of cmaf3p00 is:',numpy.divide(numpy.array(cmaf3p00),numpy.array(ncmaf3)))

print('mean of cmaf4p11 is:',numpy.divide(numpy.array(cmaf4p11),numpy.array(ncmaf4)))
print('mean of cmaf4p01 is:',numpy.divide(numpy.array(cmaf4p01),numpy.array(ncmaf4)))
print('mean of cmaf4p10 is:',numpy.divide(numpy.array(cmaf4p10),numpy.array(ncmaf4)))
print('mean of cmaf4p00 is:',numpy.divide(numpy.array(cmaf4p00),numpy.array(ncmaf4)))

print('mean of cmaf5p11 is:',numpy.divide(numpy.array(cmaf5p11),numpy.array(ncmaf5)))
print('mean of cmaf5p01 is:',numpy.divide(numpy.array(cmaf5p01),numpy.array(ncmaf5)))
print('mean of cmaf5p10 is:',numpy.divide(numpy.array(cmaf5p10),numpy.array(ncmaf5)))
print('mean of cmaf5p00 is:',numpy.divide(numpy.array(cmaf5p00),numpy.array(ncmaf5)))



print('cmaf1 var group', numpy.divide(numpy.array(cldvarlist1),numpy.array(ncmaf1)))
print('cmaf2 var group', numpy.divide(numpy.array(cldvarlist2),numpy.array(ncmaf2)))
print('cmaf3 var group', numpy.divide(numpy.array(cldvarlist3),numpy.array(ncmaf3)))
print('cmaf4 var group', numpy.divide(numpy.array(cldvarlist4),numpy.array(ncmaf4)))
print('cmaf5 var group', numpy.divide(numpy.array(cldvarlist5),numpy.array(ncmaf5)))

print('cmaf1 cld group', numpy.divide(numpy.array(cldlist1),numpy.array(ncmaf1)))
print('cmaf2 cld group', numpy.divide(numpy.array(cldlist2),numpy.array(ncmaf2)))
print('cmaf3 cld group', numpy.divide(numpy.array(cldlist3),numpy.array(ncmaf3)))
print('cmaf4 cld group', numpy.divide(numpy.array(cldlist4),numpy.array(ncmaf4)))
print('cmaf5 cld group', numpy.divide(numpy.array(cldlist5),numpy.array(ncmaf5)))

meancld1 = numpy.divide(numpy.array(cldlist1),numpy.array(ncmaf1))
meancld2 = numpy.divide(numpy.array(cldlist2),numpy.array(ncmaf2))
meancld3 = numpy.divide(numpy.array(cldlist3),numpy.array(ncmaf3))
meancld4 = numpy.divide(numpy.array(cldlist4),numpy.array(ncmaf4))
meancld5 = numpy.divide(numpy.array(cldlist5),numpy.array(ncmaf5))
def square(num):
    return(num*num)

#use cld^2 to scale var!!!!!
meancld1 = list(map(square,meancld1))
meancld2 = list(map(square,meancld2))
meancld3 = list(map(square,meancld3))
meancld4 = list(map(square,meancld4))
meancld5 = list(map(square,meancld5))


print('cmaf1 scaledvar group', numpy.divide(numpy.array(cldvarlist1),numpy.array(meancld1)))
print('cmaf2 scaledvar group', numpy.divide(numpy.array(cldvarlist2),numpy.array(meancld2)))
print('cmaf3 scaledvar group', numpy.divide(numpy.array(cldvarlist3),numpy.array(meancld3)))
print('cmaf4 scaledvar group', numpy.divide(numpy.array(cldvarlist4),numpy.array(meancld4)))
print('cmaf5 scaledvar group', numpy.divide(numpy.array(cldvarlist5),numpy.array(meancld5)))


print('process over')
openfile.close()


