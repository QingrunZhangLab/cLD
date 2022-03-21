import time
import os
import numpy

#This code is to separate the gene pairs in the bootstrap result into different cMAF groups.

groupfile = open(r'/PATH/bootstrapResult.txt') #result from bootstrap
openfile = open(r'/PATH/bootstrapTarget.txt') #use the first row in the target file to separate the gene pairs into different groups.




#main run:
line = groupfile.readline()
line_list = line.split()
print('len',len(line_list))
groupnum = [0]*10
groupind = [[],[],[],[],[],[],[],[],[],[]]
#print('line is',line_list)
i = 0
for i in range(0,len(line_list)):
    if line_list[i] == '1+1':
        groupind[0].append(i)
        #print('for groupind[0], i is:',i,'line_list[i] is',line_list[i])
        groupnum[0] = groupnum[0]+1
    elif (line_list[i] == '1+2')or (line_list[i] == '2+1'):
        groupind[1].append(i)
        groupnum[1] = groupnum[1]+1
    elif line_list[i] == '1+3'or line_list[i] == '3+1':
        groupind[2].append(i)
        groupnum[2] = groupnum[2]+1
    elif line_list[i] == '1+4' or line_list[i] == '4+1':
        groupind[3].append(i)
        groupnum[3] = groupnum[3]+1
    elif line_list[i] == '2+2':
        groupind[4].append(i)
        groupnum[4] = groupnum[4]+1
    elif line_list[i] == '2+3'or line_list[i] == '3+2':
        groupind[5].append(i)
        groupnum[5] = groupnum[5]+1
    elif line_list[i] == '2+4' or line_list[i] == '4+2':
        groupind[6].append(i)
        groupnum[6] = groupnum[6]+1
    elif line_list[i] == '3+3':
        groupind[7].append(i)
        groupnum[7] = groupnum[7]+1
    elif line_list[i] == '3+4' or line_list[i] == '4+3':
        groupind[8].append(i)
        groupnum[8] = groupnum[8]+1
    elif line_list[i] == '4+4':
        groupind[9].append(i)
        groupnum[9] = groupnum[9]+1
print('the group num is:',groupnum)
print('the ind 1 are:',groupind[0])
print('the ind 2 are:',groupind[1])
groupvalue1 = []
groupvalue2 = []
groupvalue3 = []
groupvalue4 = []
groupvalue5 = []
groupvalue6 = []
groupvalue7 = []
groupvalue8 = []
groupvalue9 = []
groupvalue10 = []


#line = groupfile.readline()
line = openfile.readline()
#skip the first line in both file
line = openfile.readline()
#line = groupfile.readline()
line_list = line.split()
while line:
    crtsum1 = 0
    crtsum2 = 0
    crtsum3 = 0
    crtsum4 = 0
    crtsum5 = 0
    crtsum6 = 0
    crtsum7 = 0
    crtsum8 = 0
    crtsum9 = 0
    crtsum10 = 0
    for i in range(0,len(line_list)):
        if i in groupind[0]:
            crtsum1 = crtsum1 + float(line_list[i])
        elif i in groupind[1]:
            crtsum2 = crtsum2 + float(line_list[i])
        elif i in groupind[2]:
            crtsum3 = crtsum3 + float(line_list[i])
        elif i in groupind[3]:
            crtsum4 = crtsum4 + float(line_list[i])
        elif i in groupind[4]:
            crtsum5 = crtsum5 + float(line_list[i])
        elif i in groupind[5]:
            crtsum6 = crtsum6 + float(line_list[i])
        elif i in groupind[6]:
            crtsum7 = crtsum7 + float(line_list[i])
        elif i in groupind[7]:
            crtsum8 = crtsum8 + float(line_list[i])
        elif i in groupind[8]:
            crtsum9 = crtsum9 + float(line_list[i])
        elif i in groupind[9]:
            crtsum10 = crtsum10 + float(line_list[i])
    groupvalue1.append(crtsum1/len(groupind[0]))
    groupvalue2.append(crtsum2/len(groupind[1]))
    groupvalue3.append(crtsum3/len(groupind[2]))
    groupvalue4.append(crtsum4/len(groupind[3]))
    groupvalue5.append(crtsum5/len(groupind[4]))
    groupvalue6.append(crtsum6/len(groupind[5]))
    groupvalue7.append(crtsum7/len(groupind[6]))
    groupvalue8.append(crtsum8/len(groupind[7]))
    groupvalue9.append(crtsum9/len(groupind[8]))
    groupvalue10.append(crtsum10/len(groupind[9]))
    #line = groupfile.readline()
    line = openfile.readline()
    line_list = line.split()

print('group values are: 1:',groupvalue1)
print('group values are: 2:',groupvalue2)
print('group values are: 3:',groupvalue3)
print('group values are: 4:',groupvalue4)
print('group values are: 5:',groupvalue5)
print('group values are: 6:',groupvalue6)
print('group values are: 7:',groupvalue7)
print('group values are: 8:',groupvalue8)
print('group values are: 9:',groupvalue9)
print('group values are: 10:',groupvalue10)

print('group count is:',groupnum)



print('process over')
openfile.close()
groupfile.close()

