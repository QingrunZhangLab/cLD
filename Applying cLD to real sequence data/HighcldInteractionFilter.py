#This code is to select high cLD(greater than the thre)
#After we get the high cLD gene pairs, use the cMAF value we can filter the gene pairs with the condition about cMAF


thre = 0.4 #thres of cld^0.5
cmaf_thre = 0.4 #thres of cMAF


openfile1 = 'NA' #could be another interaction file
openfile2 = open(r'/PATH/geneint.txt','r') #interaction file
openfile3 = open(r'/PATH/namedcld.txt','r') #named cld
openfile4 = open(r'/PATH/genemaf.txt','r') #MAF of genes
outfile = '/PATH/highcld_noint.txt' #High cld gene pairs with no-interactions, could have other result if you change the codes.



def check_in(name1,name2):
    global totaliat1
    global totaliat2
    global totaliat3
    global totaliat4
    global niat
    global niat2
    check_ind = 0
    num = 0
    for elem in range(0,niat):
        num = num + 1
        if (name1 == totaliat1[elem]) and (name2 == totaliat2[elem]):
            check_ind = 1
            break
        if (name1 == totaliat2[elem]) and (name2 == totaliat1[elem]):
            check_ind = 1
            break
    return(check_ind,num)


def check_cmaf(geneid1,geneid2):
    check_index = 0
    cmaf1 = 0
    cmaf2 = 0
    if (geneid1 in genename) and (geneid2 in genename):
        cmaf1 = float(genecmaf[genename.index(geneid1)])
        cmaf2 = float(genecmaf[genename.index(geneid2)])
        if (cmaf1 > cmaf_thre) and (cmaf2 > cmaf_thre):
            check_index = 1
    return(check_index)


with open(outfile,'w',newline='') as fdo:
        fdo.write('')

if not openfile1 == 'NA':
    line1 = openfile1.readline()
    line1_list = line1.split()

totaliat1 = []
totaliat2 = []

line2 = openfile2.readline()
line2_list = line2.split()

totaliat3 = []
totaliat4 = []

print('Process start')
if not openfile1 == 'NA':
    while line1:
        totaliat1.append(line1_list[0])
        totaliat2.append(line1_list[1])
        line1 = openfile1.readline()
        line1_list = line1.split()

while line2:
    totaliat3.append(line2_list[0])
    totaliat4.append(line2_list[1])
    line2 = openfile2.readline()
    line2_list = line2.split()

print('interaction name done')

genename = []
genecmaf = []
line4 = openfile4.readline()
line4_list = line4.split(',')
while line4:
    genename.append(line4_list[4])
    genecmaf.append(line4_list[5])
    line4 = openfile4.readline()
    line4_list = line4.split(',')

print('genecmaf read over')

niat = len(totaliat1)
niat2 = len(totaliat3)
openfile3.seek(0,0)
nameset = []
line = openfile3.readline()
line_list = line.split(',')
while line:
    nameset.append(line_list[4])
    line = openfile3.readline()
    line_list = line.split(',')

print('len of nameset',len(nameset))
openfile3.seek(0,0)
line = openfile3.readline()
line_list = line.split(',')
crtpos = 0
cldtotal = 0 
sigcld = 0
sig3dcldnoint_highcmaf = 0
while line:
    crtname = line_list[4]
    crtlen = len(line_list)
    if crtlen == 5:
        print('the matrix over')
    for i in range(5,crtlen):
        cldtotal = cldtotal + 1
        if float(line_list[i])**2 > thre and (not float(line_list[i]) == 9):
            sigcld = sigcld + 1
            crtname2 = nameset[i+crtpos-4]
            checkindex,num = check_in(crtname,crtname2)
            if (checkindex == 0) and (check_cmaf(crtname,crtname2) == 1):
                sig3dcldnoint_highcmaf = sig3dcldnoint_highcmaf + 1
                namelist = [crtname,crtname2,str(float(line_list[i])**2)]
                str_cur = '  '.join(namelist)
                with open(outfile,'a',newline='') as fdo:
                    fdo.write(str_cur+'\n')
    crtpos = crtpos + 1
    line = openfile3.readline()
    line_list = line.split(',')


print('total cld is',cldtotal)
print('sig cld is',sigcld)
print('total 3dint is',len(totaliat3))
print('total recint is',len(totaliat1))
print('sig cld no int and high cmaf is',sig3dcldnoint_highcmaf)
print('percent of sig in int is',(sigcld-sig3dcldnoint_highcmaf)/len(totaliat3))
print('percent of sig in total is',sigcld/cldtotal)
print('improved',((sigcld-sig3dcldnoint_highcmaf)/len(totaliat3))/(sigcld/cldtotal))
print('the whole process over')

if not openfile1 == 'NA':
    openfile1.close()
openfile2.close()
openfile3.close()
openfile4.close()


