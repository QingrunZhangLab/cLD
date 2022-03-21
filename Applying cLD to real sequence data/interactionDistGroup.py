import time
import os
import numpy

#This code is to separate the cld into interaction/no-interaction groups. For each group, we have 13 distance subgroups.


openfile1 = open(r'/PATH/genedistance.txt','r')  #gene distance file
openfile2 = open(r'/PATH/namedcld.txt','r')  #namedcld file, the cLD value with gene esembl ID
openfile3 = open(r'/PATH/Hi-Cgeneint.txt','r') #Gene interaction file, e.g. Hi-Cgeneint.txt
outfile = '/PATH/interactionDistGroup.txt' #output is the triangule matrix. negtive value means with interaction, positive means no-interaction


def check_in(name1,name2):
    global firint
    global secint
    check_ind = 0
    num = 0
    for elem in range(0,intlen):
        num = num + 1
        if (name1 in firint[elem]) and (name2 in secint[elem]):
            check_ind = 1
            break
        if (name2 in firint[elem]) and (name1 in secint[elem]):
            check_ind = 1
            break
    return(check_ind,num)

with open(outfile,'w',newline='') as fdo:
        fdo.write('')
################
####13distgrps####
################
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

line1 = openfile1.readline()
line_list1 = line1.split(',')
line2 = openfile2.readline()
line_list2 = line2.split(',')
line3 = openfile3.readline()
line_list3 = line3.split()

len1 = len(line_list1)
len2 = len(line_list2)
if len1  == len2:
    print('the length is equal, continue to start')


firint = []
secint = []

print('Process start')
#interaction prepare#
while line3:
    firint.append(line_list3[0])
    secint.append(line_list3[1])
    line3 = openfile3.readline()
    line_list3 = line3.split()

intlen = len(firint)
print('interaction name done')

#namesetprepare#
openfile2.seek(0,0)
nameset = []
line2 = openfile2.readline()
line_list2 = line2.split(',')
while line2:
    nameset.append(line_list2[4])
    line2 = openfile2.readline()
    line_list2 = line2.split(',')

print('len of nameset',len(nameset))

#1 for dist, 2 for cld#
#main procedure#
openfile1.seek(0,0)
openfile2.seek(0,0)
line1 = openfile1.readline()
line_list1 = line1.split(',')
line2 = openfile2.readline()
line_list2 = line2.split(',')
crtlen = len(line_list2)
crtpos = 0
distgroup = 0

while line2:
    crtname = line_list2[4]
    print('the',crtpos,'is done')
    newline = [line_list2[0],line_list2[1],line_list2[2],line_list2[3],line_list2[4]]
    for i in range(5,crtlen - crtpos):
        #dist without name, then [i-1]. dist withname, then just [i]#
        #must be abs value#
        crtdist = abs(float(line_list1[i]))
        crtname2 = nameset[i+crtpos-4]
        checkindex,num = check_in(crtname,crtname2)
        checkpar = 1
        ##################################################
        #this part divide the distance in to sevral group#
        ##################################################
        if checkindex == 1:
            checkpar = -1
            print('checkindex is 1')
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
        newline.append(str(checkpar*distgroup))
    str_cur = '  '.join(newline)
    with open(outfile,'a',newline='') as fdo:
        fdo.write(str_cur+'\n')
    crtpos = crtpos + 1
    line1 = openfile1.readline()
    line_list1 = line1.split(',')
    line2 = openfile2.readline()
    line_list2 = line2.split(',')


print('the whole process over')

openfile1.close()
openfile2.close()
openfile3.close()