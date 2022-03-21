import pandas as pd

#This code is: to remove the gene line with only '0|0'.

openfile = open(r'/PATH/intergratedGene.csv','r') # the integrated gene file
outfile = '/PATH/filteredgene.csv' #the filtered gene file



#initial the selected file,point at the headline
line = openfile.readline()
head_list = line.split(',')
n = len(head_list)
head_list[n-1] = head_list[n-1][0:7]
df = pd.DataFrame(columns = head_list)
with open(outfile,'w',newline='') as fdo:
    df.to_csv(fdo, sep=',', mode='a', index=False, header=True)
print('initial over')
line = openfile.readline()
line_list = line.split(',')
nrow = 0
while line:
    select = 0
    for element in line_list[4:n]:
        if element == '1|0'or element == '1|0\n' or element == '0|1'or element == '0|1\n' or element == '1|1'or element == '1|1\n':
            select = 1
            break
    if select == 1:
        line_list[n-1] = line_list[n-1][0:3]
        df.loc[nrow] = line_list
        nrow = nrow + 1
    line = openfile.readline()
    line_list = line.split(',')
print('select over, writing in..')
with open(outfile,'a',newline='') as fdo:
    df.to_csv(fdo, sep=',', mode='a', index=False, header=False)
print('write in over')
openfile.close()



