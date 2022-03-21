import time
import os
import pandas as pd

#This file is to calculate the gene mid point.

openfile = open(r'/PATH/namedcld.txt','r') #named cLD file.
outfile = '/PATH/genemid.txt' #gene mid point list.



with open(outfile,'w',newline='') as fdo:
    fdo.write('')
line = openfile.readline()
line_list = line.split(',')
print('process start')
while line:
    mid = (int(line_list[2])+int(line_list[3]))/2
    leng = (int(line_list[3])-int(line_list[2]))
    with open(outfile,'a',newline='') as fdo:
        fdo.write(str(mid)+'\n')
    line = openfile.readline()
    line_list = line.split(',')
print('process over')

openfile.close()
