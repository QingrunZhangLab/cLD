import pandas as pd
import numpy
import random

#This code is to use Bootstrap method to estimate the cLD. The details could be found in the Supplementary Material.

openfile = open(r'/PATH/genesample.txt','r') #gene sample from randomgenes
openfile2 = open(r'/PATH/geneinfo.txt') #gene information list, you can find the example in the demo
outfile = '/PATH/bootstrapResult.txt' #the output is the Bootstap result each row represents a iteration

samplesize = 250 #for each iteraction in the Bootstrap method, the sample size is 250.
epoch = 500 #we run the Bootstrap for epoch times




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
    for i in sampleind:
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
    return(count/(2*len(sampleind)))

def cal_p01(line1,line2):
    line_list1 = line1.split(',')
    line_list2 = line2.split(',')
    count = 0
    for i in sampleind:
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
    return(count/(2*len(sampleind)))

def cal_p10(line1,line2):
    line_list1 = line1.split(',')
    line_list2 = line2.split(',')
    count = 0
    for i in sampleind:
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
    return(count/(2*len(sampleind)))


def cal_p00(line1,line2):
    line_list1 = line1.split(',')
    line_list2 = line2.split(',')
    count = 0
    for i in sampleind:
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
    return(count/(2*len(sampleind)))


def count_one(line_list):
    count = 0
    for element in line_list[4:n]:
        if element == '0|1'or element == '0|1\n' or element == '1|0'or element == '1|0\n':
            count = count + 1
        if element == '1|1'or element == '1|1\n':
            count = count + 2
    return(count/(2*(n-4)))

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
    return(n*varres)
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

def findname(line_list):
    crtstart = line_list[2]
    for i in range(0,len(geneid)):
        if crtstart == genestart[i]:
            crtname = geneid[i]
            break
    return(crtname)

with open(outfile,'w',newline='') as fdo:
    fdo.write('')

genestart = []
geneid = []
line2 = openfile2.readline()
while line2:
    line_list2 = line2.split()
    genestart.append(line_list2[2])
    geneid.append(line_list2[4])
    line2 = openfile2.readline()

openfile2.close()


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

namelist = []
##############start the iteration############
for t in range(0,epoch):
    cldlist = []
    np = len(pone)
    totalind = range(4,n)
    #samplesize = n-4#change
    sampleind = random.sample(totalind,samplesize)#change
    #sampleind = totalind
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
    i = 0
    while line1:
        line_list1 = line1.split(',')
        name1 = findname(line_list1)
        currentpos = line_list1[1]
        count = count_one(line_list1)
        line2 = openfile.readline()
        j = i + 1
        #if j == np:
            #print('the whole process is over,i is:',i)
            #break
        while line2:
            name2 = findname(line2.split(','))
            if t == 0:
                namelist.append(name1+'-'+name2)
            #nomalized cld#
            pab = cal_pab(line1,line2)
            #it's p11#
            p01 = cal_p01(line1,line2)
            p10 = cal_p10(line1,line2)
            p00 = cal_p00(line1,line2)
            nplist = [2*samplesize*pab,2*samplesize*p01,2*samplesize*p10,2*samplesize*p00]
            crtcld = cldfunc(nplist,2*samplesize)
            cldlist.append(str(crtcld))
            line2 = openfile.readline()
            j = j + 1
            if j == np:
                #print('this line is over,i is:',i)
                break
        print('cld line writed')
        find_pos(openfile,currentpos)
        line1 = openfile.readline()
        i = i + 1
    if t == 0:
        namestr = ' '.join(namelist)
        with open(outfile,'a',newline='') as fdo:
            fdo.write(namestr+'\n')
    crtstr = ' '.join(cldlist)
    with open(outfile,'a',newline='') as fdo:
        fdo.write(crtstr+'\n')



#########################end the iteration###############
print('process over')
openfile.close()


