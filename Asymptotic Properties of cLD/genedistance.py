import time
import os
import pandas as pd

#This code is to calculate the gene distance based on the cLD file.

openfile = open(r'/PATH/namedcld.txt','r') #named cLD file
openfile2 = open(r'/PATH/genemid.txt','r')  #gene mid point file
outfile = '/PATH/genedistance.txt' #gene distance file
outfile2 = '/PATH/distseq.txt' #distance sequence file, a string


with open(outfile,'w',newline='') as fdo:
    fdo.write('')
with open(outfile2,'w',newline='') as fdo:
    fdo.write('')
line = openfile2.readline()
print(line)
print('process start')
mid = []
while line:
    mid.append(float(line))
    line = openfile2.readline()

np = len(mid)
openfile.seek(0,0)
totaldist = []
line = openfile.readline()
line_list = line.split(',')
num = 0
while line:
    newline = [line_list[0],line_list[1],line_list[2],line_list[3],line_list[4]]
    for i in range(num+1,np):
        newline.append(str(float(mid[i])-float(mid[num])))
        totaldist.append(str(float(mid[i])-float(mid[num])))
    str_cur = ','.join(newline)
    with open(outfile,'a',newline='') as fdo:
        fdo.write(str_cur+'\n')
    num = num + 1
    line = openfile.readline()
    line_list = line.split(',')

str_cur = ','.join(totaldist)
with open(outfile2,'a',newline='') as fdo:
    fdo.write(str_cur+'\n')
print('process over')

openfile.close()
openfile2.close()
