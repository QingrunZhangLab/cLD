import time
import os



####This code is to separate the interaction/no-interaction file to 13*2 groups

cldfile = open(r'/PATH/namedcld.txt','r')  # the named cld file
groupfile = open(r'/PATH/interactionDistGroup.txt','r')  #the upper triangule interaction-distance information file.
i0file = '/PATH/cldi0.txt' # output is the cld without interaction, this file contains 13 lines, each one represent a distance group.
i1file = '/PATH/cldi1.txt' # output is the cld with interaction, this file contains 13 lines, each one represent a distance group.


with open(i0file,'w',newline='') as fdo:
        fdo.write('')
with open(i1file,'w',newline='') as fdo:
        fdo.write('')


#total 26 group#
i1d1 = []
i1d2 = []
i1d3 = []
i1d4 = []
i1d5 = []
i1d6 = []
i1d7 = []
i1d8 = []
i1d9 = []
i1d10 = []
i1d11 = []
i1d12 = []
i1d13 = []

i0d1 = []
i0d2 = []
i0d3 = []
i0d4 = []
i0d5 = []
i0d6 = []
i0d7 = []
i0d8 = []
i0d9 = []
i0d10 = []
i0d11 = []
i0d12 = []
i0d13 = []

line = cldfile.readline()
line_list = line.split(',')
gpline = groupfile.readline()
grp_list = gpline.split()
cldlen = len(line_list)
gplen = len(grp_list)
if gplen == cldlen:
    print('gplen is',gplen)
    print('cldlen is',cldlen)
    print('length check pass, continue to main process')
print('gplen is',gplen)
print('cldlen is',cldlen)
print(grp_list[5],type(grp_list[5]))


crtpos = 0
while line:
    for i in range(5,cldlen - crtpos):
        if grp_list[i] == '1':
            i0d1.append(str(float(line_list[i])))
        elif grp_list[i] == '2':
            i0d2.append(str(float(line_list[i])))
        elif grp_list[i] == '3':
            i0d3.append(str(float(line_list[i])))
        elif grp_list[i] == '4':
            i0d4.append(str(float(line_list[i])))
        elif grp_list[i] == '5':
            i0d5.append(str(float(line_list[i])))
        elif grp_list[i] == '6':
            i0d6.append(str(float(line_list[i])))
        elif grp_list[i] == '7':
            i0d7.append(str(float(line_list[i])))
        elif grp_list[i] == '8':
            i0d8.append(str(float(line_list[i])))
        elif grp_list[i] == '9':
            i0d9.append(str(float(line_list[i])))
        elif grp_list[i] == '10':
            i0d10.append(str(float(line_list[i])))
        elif grp_list[i] == '11':
            i0d11.append(str(float(line_list[i])))
        elif grp_list[i] == '12':
            i0d12.append(str(float(line_list[i])))
        elif grp_list[i] == '13':
            i0d13.append(str(float(line_list[i])))
        elif grp_list[i] == '-1':
            i1d1.append(str(float(line_list[i])))
        elif grp_list[i] == '-2':
            i1d2.append(str(float(line_list[i])))
        elif grp_list[i] == '-3':
            i1d3.append(str(float(line_list[i])))
        elif grp_list[i] == '-4':
            i1d4.append(str(float(line_list[i])))
        elif grp_list[i] == '-5':
            i1d5.append(str(float(line_list[i])))
        elif grp_list[i] == '-6':
            i1d6.append(str(float(line_list[i])))
        elif grp_list[i] == '-7':
            i1d7.append(str(float(line_list[i])))
        elif grp_list[i] == '-8':
            i1d8.append(str(float(line_list[i])))
        elif grp_list[i] == '-9':
            i1d9.append(str(float(line_list[i])))
        elif grp_list[i] == '-10':
            i1d10.append(str(float(line_list[i])))
        elif grp_list[i] == '-11':
            i1d11.append(str(float(line_list[i])))
        elif grp_list[i] == '-12':
            i1d12.append(str(float(line_list[i])))
        elif grp_list[i] == '-13':
            i1d13.append(str(float(line_list[i])))
    line = cldfile.readline()
    line_list = line.split(',')
    gpline = groupfile.readline()
    grp_list = gpline.split()
    crtpos = crtpos + 1

print('first append over')

if len(i0d1) == 0:
    i0d1.append('0')
if len(i0d2) == 0:
    i0d2.append('0')
if len(i0d3) == 0:
    i0d3.append('0')
if len(i0d4) == 0:
    i0d4.append('0')
if len(i0d5) == 0:
    i0d5.append('0')
if len(i0d6) == 0:
    i0d6.append('0')
if len(i0d7) == 0:
    i0d7.append('0')
if len(i0d8) == 0:
    i0d8.append('0')
if len(i0d9) == 0:
    i0d9.append('0')
if len(i0d10) == 0:
    i0d10.append('0')
if len(i0d11) == 0:
    i0d11.append('0')
if len(i0d12) == 0:
    i0d12.append('0')
if len(i0d13) == 0:
    i0d13.append('0')

if len(i1d1) == 0:
    i1d1.append('0')
if len(i1d2) == 0:
    i1d2.append('0')
if len(i1d3) == 0:
    i1d3.append('0')
if len(i1d4) == 0:
    i1d4.append('0')
if len(i1d5) == 0:
    i1d5.append('0')
if len(i1d6) == 0:
    i1d6.append('0')
if len(i1d7) == 0:
    i1d7.append('0')
if len(i1d8) == 0:
    i1d8.append('0')
if len(i1d9) == 0:
    i1d9.append('0')
if len(i1d10) == 0:
    i1d10.append('0')
if len(i1d11) == 0:
    i1d11.append('0')
if len(i1d12) == 0:
    i1d12.append('0')
if len(i1d13) == 0:
    i1d13.append('0')

print('add 0 over')
print('leni1d1 is',len(i1d1))
print('leni1d2 is',len(i1d2))
print('leni1d3 is',len(i1d3))
print('leni1d4 is',len(i1d4))
print('leni1d5 is',len(i1d5))
print('leni1d6 is',len(i1d6))
print('leni1d7 is',len(i1d7))
print('leni1d8 is',len(i1d8))
print('leni1d9 is',len(i1d9))
print('leni1d10 is',len(i1d10))
print('leni1d11 is',len(i1d11))
print('leni1d12 is',len(i1d12))
print('leni1d13 is',len(i1d13))
print('leni0d1 is',len(i0d1))
print('leni0d2 is',len(i0d2))
print('leni0d3 is',len(i0d3))
print('leni0d4 is',len(i0d4))
print('leni0d5 is',len(i0d5))
print('leni0d6 is',len(i0d6))
print('leni0d7 is',len(i0d7))
print('leni0d8 is',len(i0d8))
print('leni0d9 is',len(i0d9))
print('leni0d10 is',len(i0d10))
print('leni0d11 is',len(i0d11))
print('leni0d12 is',len(i0d12))
print('leni0d13 is',len(i0d13))



#for i in range(0,len(i0d1)):
#    if float(i0d1[i]) ** 2 > 1:
#        print('err i is ',i)
#        print('err value is',i0d1[i])
#for i in range(0,len(i0d2)):
#    if float(i0d2[i]) ** 2 > 1:
#        print('err i is ',i)
#        print('err value is',i0d2[i])
#for i in range(0,len(i0d3)):
#    if float(i0d3[i]) ** 2 > 1:
#        #print('err i is ',i)
#        #print('err value is',i0d3[i])
#for i in range(0,len(i0d4)):
#    if float(i0d4[i]) ** 2 > 1:
#        print('err i is ',i)
#        print('err value is',i0d4[i])
#for i in range(0,len(i0d5)):
#    if float(i0d5[i]) ** 2 > 1:
#        print('err i is ',i)
#        print('err value is',i0d5[i])
#for i in range(0,len(i0d6)):
#    if float(i0d6[i]) ** 2 > 1:
#        print('err i is ',i)
#        print('err value is',i0d6[i])
#for i in range(0,len(i0d7)):
#    if float(i0d7[i]) ** 2 > 1:
#        print('err i is ',i)
#        print('err value is',i0d7[i])
#for i in range(0,len(i0d8)):
#    if float(i0d8[i]) ** 2 > 1:
#        print('err i is ',i)
#        print('err value is',i0d8[i])
#for i in range(0,len(i0d9)):
#    if float(i0d9[i]) ** 2 > 1:
#        print('err i is ',i)
#        print('err value is',i0d9[i])
#for i in range(0,len(i0d10)):
#    if float(i0d10[i]) ** 2 > 1:
#        print('err i is ',i)
#        print('err value is',i0d10[i])
#for i in range(0,len(i0d11)):
#    if float(i0d11[i]) ** 2 > 1:
#        print('err i is ',i)
#        print('err value is',i0d11[i])
#for i in range(0,len(i0d12)):
#    if float(i0d12[i]) ** 2 > 1:
#        print('err i is ',i)
#        print('err value is',i0d12[i])
#for i in range(0,len(i0d13)):
#    if float(i0d13[i]) ** 2 > 1:
#        print('err i is ',i)
#        print('err value is',i0d13[i])

str_cur = ','.join(i0d1)
with open(i0file,'a',newline='') as fdo:
    fdo.write(str_cur+'\n')
str_cur = ','.join(i0d2)
with open(i0file,'a',newline='') as fdo:
    fdo.write(str_cur+'\n')
str_cur = ','.join(i0d3)
with open(i0file,'a',newline='') as fdo:
    fdo.write(str_cur+'\n')
str_cur = ','.join(i0d4)
with open(i0file,'a',newline='') as fdo:
    fdo.write(str_cur+'\n')
str_cur = ','.join(i0d5)
with open(i0file,'a',newline='') as fdo:
    fdo.write(str_cur+'\n')
str_cur = ','.join(i0d6)
with open(i0file,'a',newline='') as fdo:
    fdo.write(str_cur+'\n')
str_cur = ','.join(i0d7)
with open(i0file,'a',newline='') as fdo:
    fdo.write(str_cur+'\n')#
str_cur = ','.join(i0d8)
with open(i0file,'a',newline='') as fdo:
    fdo.write(str_cur+'\n')
str_cur = ','.join(i0d9)
with open(i0file,'a',newline='') as fdo:
    fdo.write(str_cur+'\n')
str_cur = ','.join(i0d10)
with open(i0file,'a',newline='') as fdo:
    fdo.write(str_cur+'\n')
str_cur = ','.join(i0d11)
with open(i0file,'a',newline='') as fdo:
    fdo.write(str_cur+'\n')
str_cur = ','.join(i0d12)
with open(i0file,'a',newline='') as fdo:
    fdo.write(str_cur+'\n')
str_cur = ','.join(i0d13)
with open(i0file,'a',newline='') as fdo:
    fdo.write(str_cur+'\n')

str_cur = ','.join(i1d1)
with open(i1file,'a',newline='') as fdo:
    fdo.write(str_cur+'\n')
str_cur = ','.join(i1d2)
with open(i1file,'a',newline='') as fdo:
    fdo.write(str_cur+'\n')
str_cur = ','.join(i1d3)
with open(i1file,'a',newline='') as fdo:
    fdo.write(str_cur+'\n')
str_cur = ','.join(i1d4)
with open(i1file,'a',newline='') as fdo:
    fdo.write(str_cur+'\n')
str_cur = ','.join(i1d5)
with open(i1file,'a',newline='') as fdo:
    fdo.write(str_cur+'\n')
str_cur = ','.join(i1d6)
with open(i1file,'a',newline='') as fdo:
    fdo.write(str_cur+'\n')
str_cur = ','.join(i1d7)
with open(i1file,'a',newline='') as fdo:
    fdo.write(str_cur+'\n')
str_cur = ','.join(i1d8)
with open(i1file,'a',newline='') as fdo:
    fdo.write(str_cur+'\n')
str_cur = ','.join(i1d9)
with open(i1file,'a',newline='') as fdo:
    fdo.write(str_cur+'\n')
str_cur = ','.join(i1d10)
with open(i1file,'a',newline='') as fdo:
    fdo.write(str_cur+'\n')
str_cur = ','.join(i1d11)
with open(i1file,'a',newline='') as fdo:
    fdo.write(str_cur+'\n')
str_cur = ','.join(i1d12)
with open(i1file,'a',newline='') as fdo:
    fdo.write(str_cur+'\n')
str_cur = ','.join(i1d13)
with open(i1file,'a',newline='') as fdo:
    fdo.write(str_cur+'\n')
print('write over')

cldfile.close()
groupfile.close()

print('all process over')
