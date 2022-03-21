import time

#This file is to transfer the interaction intervals into Esemble based gene-gene interactions

openfile = open(r'/PATH/Hi-C3DinteractionData.txt','r') #Input is the Hi-C interaction file
cldfile = open(r'/PATH/namedcld.txt','r') #Named cld file
outfile = '/PATH/Hi-Cgeneint.txt' #output is the gene-gene interactions in terms of Esmbel ID 

chr = '1'  #Specify the chromosome



def checkint(int3d1,int3d2,int3d3,int3d4,cld1,cld2,cld3,cld4):
    checkindex = 0
    #############################################################################################
    #check11 represents cld1 in the interval 1, check12 represents cld1 in the interval 2. so on#
    #############################################################################################
    check11 = 0
    check12 = 0
    check21 = 0
    check22 = 0
    if (int3d1 < cld1 and int3d2 > cld1) or (int3d1 < cld2 and int3d2 > cld2) or (int3d1 > cld1 and int3d2 < cld2):
        check11 = 1
    if (int3d3 < cld1 and int3d4 > cld1) or (int3d3 < cld2 and int3d4 > cld2) or (int3d3 > cld1 and int3d4 < cld2):
        check12 = 1
    if (int3d1 < cld3 and int3d2 > cld3) or (int3d1 < cld4 and int3d2 > cld4) or (int3d1 > cld3 and int3d2 < cld4):
        check21 = 1
    if (int3d3 < cld3 and int3d4 > cld3) or (int3d3 < cld4 and int3d4 > cld4) or (int3d3 > cld3 and int3d4 < cld4):
        check22 = 1
    ##########################
    if (check11 == 1 and check22 == 1) or (check12 == 1 and check21 == 1):
        checkindex = 1
    return(checkindex)


with open(outfile,'w',newline='') as fdo:
        fdo.write('')

int1start = []
int2start = []
int1end = []
int2end = []

######################
#find the needed chr##
######################
line = openfile.readline()
line_list = line.split()
while line:
    crtchr = line_list[1].split(';')[0]
    if crtchr == chr:
        print('cuurrrent chr finded')
        break
    line = openfile.readline()
    line_list = line.split()

print('crt chr is:',line_list[1].split(';')[0])

#line = openfile.readline()
line_list = line.split()
while line:
    crtchr = line_list[1].split(';')[0]
    if not crtchr == chr:
        print('cuurrrent chr end')
        break
    int1end.append(float(line_list[1].split(';')[2]))
    int1start.append(float(line_list[1].split(';')[1]))
    int2end.append(float(line_list[2].split(';')[2]))
    int2start.append(float(line_list[2].split(';')[1]))
    line = openfile.readline()
    line_list = line.split()

print('3d interaction is read over, len is',len(int1start))


cldname = []
cldstart = []
cldend = []
line = cldfile.readline()
line_list = line.split(',')
while line:
    cldname.append(line_list[4])
    cldstart.append(float(line_list[2]))
    cldend.append(float(line_list[3]))
    line = cldfile.readline()
    line_list = line.split(',')
print('cld name start and end read, start detact')

for i in range(0,len(cldname)):
    for j in range(i+1,len(cldname)):
        crtstart1 = cldstart[i]
        crtend1 = cldend[i]
        crtstart2 = cldstart[j]
        crtend2 = cldend[j]
        check_ind = 0
        for k in range(0,len(int1start)):
            check_ind = checkint(int1start[k],int1end[k],int2start[k],int2end[k],crtstart1,crtend1,crtstart2,crtend2)
            if check_ind == 1:
                namelist = [cldname[i],cldname[j]]
                str_cur = '  '.join(namelist)
                with open(outfile,'a',newline='') as fdo:
                    fdo.write(str_cur+'\n')
                break

print('the whole process over,thank you')


openfile.close()
cldfile.close()

