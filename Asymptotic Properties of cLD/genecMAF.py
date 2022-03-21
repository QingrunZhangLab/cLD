import pandas as pd

#This code is to calculate the gene cmaf.

openfile = open(r'/PATH/filteredgene.csv','r') #filteredgene file
genenamefile = open(r'/PATH/geneinfo.txt','r') #gene information file
outfile = '/PATH/genemaf.txt' #Output





def count_one(line_list):
    count = 0
    for element in line_list[4:n]:
        if element == '0|1'or element == '0|1\n' or element == '1|0'or element == '1|0\n':
            count = count + 1
        if element == '1|1'or element == '1|1\n':
            count = count + 2
    return(count/(2*(n-4)))
with open(outfile,'w',newline='') as fdo:
    fdo.write('')
#set start point and name list
start = []
name = []
line = genenamefile.readline()
line_list = line.split()
while line:
    start.append(line_list[2])
    name.append(line_list[4])
    line = genenamefile.readline()
    line_list = line.split()


#refresh the start
openfile.seek(0,0)
#first line
line = openfile.readline()
head_list = line.split(',')
#second line & initial
line = openfile.readline()
line_list = line.split(',')
n = len(line_list)
print('n of sample',len(line_list))
print('initial end, start loop')

i = 0
while line:
    line_list = line.split(',')
    current = []
    currentpos = line_list[1]
    startcheck = line_list[2]
    namecheck = name[start.index(startcheck)]
    pone_one = count_one(line_list)
    count = count_one(line_list)
    current.append(line_list[0])
    current.append(line_list[1])
    current.append(line_list[2])
    current.append(line_list[3])
    current.append(namecheck)
    current.append(str(pone_one))
    str_cur=','.join(current)
    with open(outfile,'a',newline='') as fdo:
        fdo.write(str_cur+'\n')
    line = openfile.readline()
    i = i + 1

print('process over')
openfile.close()
genenamefile.close()

