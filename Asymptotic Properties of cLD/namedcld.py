import os

#This code is to add genes' Esembl ID to the cLD file.

openfile = open(r'/PATH/toydemocld.txt','r')  #cLD file from cLD.py
genenamefile = open(r'/PATH/geneinfo.txt','r') #gene information list, you can find the example in the demo
outfile = '/PATH/toynamedcld.txt' #output is the cLD file with Gene Esmbel ID


with open(outfile,'w',newline='') as fdo:
        fdo.write('')
start = []
name = []
line = genenamefile.readline()
line_list = line.split()
while line:
    start.append(line_list[2])
    name.append(line_list[4])
    line = genenamefile.readline()
    line_list = line.split()
print('add process start')
line2 = openfile.readline()
line_list2 = line2.split(',')
while line2:
    startcheck = line_list2[2]
    namecheck = name[start.index(startcheck)]
    if len(line_list2) == 4:
        line_list2[3]=line_list2[3][0:(len(line_list2[3])-1)]
    line_list2.insert(4,namecheck)
    str_cur=','.join(line_list2)
    with open(outfile,'a',newline='') as fdo:
        fdo.write(str_cur)
    line2 = openfile.readline()
    line_list2 = line2.split(',')

print('process over')

openfile.close()
genenamefile.close()
