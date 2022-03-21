import pandas as pd

#This code could intergrate SNPs according to the gene information file.

genefile = open(r'/PATH/geneinfo.txt','r')  #gene information file
openfile = open(r'/PATH/SNPfile.txt','r')  #original vcf data, contains the SNP information
outfile = '/PATH/intergratedGene.csv' # the output is the intergrated gene file
chr = '1' # specify the chromosome!
thre = 0.005 #Threshold of rare SNPs



genefile.seek(0,0)
lowerarry = []
upperarry = []
geneline = genefile.readline()
while geneline:
    gene_list = geneline.split()
    lowerarry.append(int(gene_list[2]))
    upperarry.append(int(gene_list[3]))
    geneline = genefile.readline()

print('lenof lower',len(lowerarry))

with open(outfile,'w',newline='') as fdo:
    fdo.write('')


def get_newline(line):
    line_list = line.split()
    rare = allrare(line_list,thre)
    if rare == 0:
        add_line = ['0|0']*(n-4)
        newline = [line_list[0],line_list[1]]
        newline.extend(add_line)
    else:
        newline = [line_list[0],line_list[1]]
        for element in line_list[9:np]:
            if element == '0|0' or element == '0|0\n':
                newline.append('0|0')
            elif element == '1|0' or element == '1|0\n':
                newline.append('1|0')
            elif element == '0|1' or element == '0|1\n':
                newline.append('0|1')
            elif element == '1|1' or element == '1|1\n':
                newline.append('1|1')
    return(newline)

def allrare(line_list,thres):
    n2 = 0
    n0 = 0
    rare = 0
    for elem in line_list:
        if elem == '1|1'or elem == '1|1\n':
           n2 = n2 + 1
        elif elem == '0|0' or elem == '1|1\n':
               n0 = n0 + 1
    n1 = n - 4 - n0 - n2
    allelfq = (n1+2*n2)/(2*(n-4))
    if allelfq < thres:
        rare = 1
    return(rare)

def choose_chr(openfile,chr):
    #skip the headline
    line = openfile.readline() 
    line_list = line.split()
    while line:
        if not line_list[0]==str(chr):    
            line = openfile.readline()
            line_list = line.split(',')
        else:
            print('choose over(in-func)',line_list[0])
            break
    return(line)

def write_dataframe(gene,ith):
    global df
    print('len of gene',len(gene))
    #ith-1 starts from 0
    df.loc[ith-1] = gene
    return(1)

#########################
#initial the gene's line#
#########################
def initial_gene(chr,location,lower,upper):
    gene = [chr,location,lower,upper]
    #n-4 of individual
    for i in range(0,n-4):
        gene.append('0|0')
    return(gene)


########################
##update geneline#######
########################
def update_gene(ith,nline):
    global genelist
    for i in range(2,len(nline)):
        if not len(nline[i]) == 3:
            print('nline is wrong, its:',nline)
            print('i is wrong,its',i)
        if nline[i][0] == '1':
            genelist[ith][i+2] = '1'+ genelist[ith][i+2][1:3]
        if nline[i][2] == '1':
            genelist[ith][i+2] =genelist[ith][i+2][0:2] + '1'
    return(1)






#only design for the file which lead to samples at the second line.
#####################################
##initial the dataframe##############
#####################################
#point at the headline
line = openfile.readline() 
head_list = line.split()
while line:
    if line[1] == '#':
        line = openfile.readline() 
    else:
        print('headline finded')
        break
head_list = line.split()
#n prior
np = len(head_list)
#count the length of line
head_list.pop(8)
head_list.pop(7)
head_list.pop(6)
head_list.pop(5)
head_list.pop(4)
head_list.pop(3)
head_list.pop(2)
head_list.pop(1)
head_list.insert(1,'end')
head_list.insert(1,'start')
head_list.insert(1,'gene')
n = len(head_list)
print('n after is ',len(head_list))
df = pd.DataFrame(columns = head_list)
with open(outfile,'w',newline='') as fdo:
    df.to_csv(fdo, sep=",", mode='a', index=False, header=True)
print('headline print over1')
#################################
#choose the chr##################
#################################
line = choose_chr(openfile,chr)
line_list = line.split()
print('len line_list',len(line_list))
print('choose chr over',line_list[0])
genelist = []
for i in range(0,len(lowerarry)):
    gene = initial_gene(chr,i,lowerarry[i],upperarry[i])
    genelist.append(gene)


while line:
    line_list = line.split()
    pos = int(line_list[1])
    newline = get_newline(line)
    for i in range(0,len(lowerarry)):
        if (pos > lowerarry[i]) and (pos < upperarry[i]):
            update_gene(i,newline)
    line = openfile.readline()


for i in range(0,len(genelist)):
    df.loc[i+1] = genelist[i]

with open(outfile,'a',newline='') as fdo:
    df.to_csv(fdo, sep=",", mode='a', index=False, header=False)

print('process over')

genefile.close()
openfile.close()
#generate a gene list and a start end point list(could be one list,use awk and grep). First decide newline's position in which genep[i], 
#then update gene[i] according to the newline info. Newline's position is reflected by the i in gene[i].








