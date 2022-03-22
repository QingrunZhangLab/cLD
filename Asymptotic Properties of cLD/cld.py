import pandas as pd

#This code is to calculate the cLD from the filtered gene file.

openfile = open(r'/PATH/filteredgene.csv','r')  #the filtered gene data from filter.
outfile = '/PATH/cld.txt'  #output is the cLD file without Esemble ID


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
    print('pab is',count/(2*(n-4)))
    print('count is',count)
    print('n-4 is',(n-4))
    return(count/(2*(n-4)))

def count_one(line_list):
    count = 0
    print(line_list[4:n])
    for element in line_list[4:n]:
        if element == '0|1'or element == '0|1\n' or element == '1|0'or element == '1|0\n':
            count = count + 1
        if element == '1|1'or element == '1|1\n':
            count = count + 2
    return(count/(2*(n-4)))

with open(outfile,'w',newline='') as fdo:
    fdo.write('')
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

i = 0
while line1:
    line_list1 = line1.split(',')
    current = []
    currentpos = line_list1[1]
    count = count_one(line_list1)
    current.append(line_list1[0])
    current.append(line_list1[1])
    current.append(line_list1[2])
    current.append(line_list1[3])
    line2 = openfile.readline()
    j = i + 1
    if j == np:
        print('the whole process is over,i is:',i)
        #break
    while line2:
        #nomalized cld#
        pab = cal_pab(line1,line2)
        print('pab is',pab)
        deno = pone[i]*(1-pone[i])*pone[j]*(1-pone[j])
        deno = deno ** 0.5
        if deno == 0:
            cld = 9
        else:
            cld = (pone[i]*pone[j] - pab)/deno
        current.append(str(cld))
        line2 = openfile.readline()
        j = j + 1
        if j == np:
            print('this line is over,i is:',i)
            break
    str_cur=','.join(current)
    with open(outfile,'a',newline='') as fdo:
        fdo.write(str_cur+'\n')
    print('cld line writed')
    find_pos(openfile,currentpos)
    line1 = openfile.readline()
    i = i + 1

print('process over')
openfile.close()

