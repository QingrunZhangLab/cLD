import os
import numpy

#This file is to run the MH test and Fisher's exact test.

thres = 0.5 #quantile, threshold of success
i0file = '/PATH/cldi0.txt' 
i1file = '/PATH/cldi1.txt'




print('process start')
####################################################
#read these files,get the count of success and fail#
####################################################
with open(i0file,'r') as fdo:
	line = fdo.readline()
	i0d1 = line.split(',')
	line = fdo.readline()
	i0d2 = line.split(',')
	line = fdo.readline()
	i0d3 = line.split(',')
	line = fdo.readline()
	i0d4 = line.split(',')
	line = fdo.readline()
	i0d5 = line.split(',')
	line = fdo.readline()
	i0d6 = line.split(',')
	line = fdo.readline()
	i0d7 = line.split(',')
	line = fdo.readline()
	i0d8 = line.split(',')
	line = fdo.readline()
	i0d9 = line.split(',')
	line = fdo.readline()
	i0d10 = line.split(',')
	line = fdo.readline()
	i0d11 = line.split(',')
	line = fdo.readline()
	i0d12 = line.split(',')
	line = fdo.readline()
	i0d13 = line.split(',')

with open(i1file,'r') as fdo:
	line = fdo.readline()
	i1d1 = line.split(',')
	line = fdo.readline()
	i1d2 = line.split(',')
	line = fdo.readline()
	i1d3 = line.split(',')
	line = fdo.readline()
	i1d4 = line.split(',')
	line = fdo.readline()
	i1d5 = line.split(',')
	line = fdo.readline()
	i1d6 = line.split(',')
	line = fdo.readline()
	i1d7 = line.split(',')
	line = fdo.readline()
	i1d8 = line.split(',')
	line = fdo.readline()
	i1d9 = line.split(',')
	line = fdo.readline()
	i1d10 = line.split(',')
	line = fdo.readline()
	i1d11 = line.split(',')
	line = fdo.readline()
	i1d12 = line.split(',')
	line = fdo.readline()
	i1d13 = line.split(',')


#extract the cut point#
cldlistf = []
cldlist = i0d1+i0d2+i0d3+i0d4+i0d5+i0d6+i0d7+i0d8+i0d9+i0d10+i0d11+i0d12+i0d13+i1d1+i1d2+i1d3+i1d4+i1d5+i1d6+i1d7+i1d8+i1d9+i1d10+i1d11+i1d12+i1d13
count9 = 0
count0 = 0
for i in range(0,len(cldlist)):
	if not cldlist[i] == '9.0' and not cldlist[i] == '0.0':
		cldlistf.append(float(cldlist[i])**2)
	elif cldlist[i] == '9.0':
		count9 = count9 + 1
	elif cldlist[i] == '0.0':
		count0 = count0 + 1
cutp =numpy.percentile(cldlistf,100*thres)
print('the',thres,'quantile is',cutp)



###############################
#calculat the success and fail#
###############################
i0d1s = 0
i0d1f = 0
for i in range(0,len(i0d1)):
	if not i0d1[i] == '9.0'and not i0d1[i] == '0.0':
		if float(i0d1[i])**2 > cutp:
			i0d1s = i0d1s + 1
		else:
			i0d1f = i0d1f + 1
print('i0d1s and f are:',i0d1s,i0d1f)
i0d2s = 0
i0d2f = 0
for i in range(0,len(i0d2)):
	if not i0d2[i] == '9.0'and not i0d2[i] == '0.0':
		if float(i0d2[i])**2 > cutp:
			i0d2s = i0d2s + 1
		else:
			i0d2f = i0d2f + 1
print('i0d2s and f are:',i0d2s,i0d2f)
i0d3s = 0
i0d3f = 0
for i in range(0,len(i0d3)):
	if not i0d3[i] == '9.0'and not i0d3[i] == '0.0':
		if float(i0d3[i])**2 > cutp:
			i0d3s = i0d3s + 1
		else:
			i0d3f = i0d3f + 1
print('i0d3s and f are:',i0d3s,i0d3f)
i0d4s = 0
i0d4f = 0
for i in range(0,len(i0d4)):
	if not i0d4[i] == '9.0'and not i0d4[i] == '0.0':
		if float(i0d4[i])**2 > cutp:
			i0d4s = i0d4s + 1
		else:
			i0d4f = i0d4f + 1
print('i0d4s and f are:',i0d4s,i0d4f)
i0d5s = 0
i0d5f = 0
for i in range(0,len(i0d5)):
	if not i0d5[i] == '9.0'and not i0d5[i] == '0.0':
		if float(i0d5[i])**2 > cutp:
			i0d5s = i0d5s + 1
		else:
			i0d5f = i0d5f + 1
print('i0d5s and f are:',i0d5s,i0d5f)
i0d6s = 0
i0d6f = 0
for i in range(0,len(i0d6)):
	if not i0d6[i] == '9.0'and not i0d6[i] == '0.0':
		if float(i0d6[i])**2 > cutp:
			i0d6s = i0d6s + 1
		else:
			i0d6f = i0d6f + 1
print('i0d6s and f are:',i0d6s,i0d6f)
i0d7s = 0
i0d7f = 0
for i in range(0,len(i0d7)):
	if not i0d7[i] == '9.0'and not i0d7[i] == '0.0':
		if float(i0d7[i])**2 > cutp:
			i0d7s = i0d7s + 1
		else:
			i0d7f = i0d7f + 1
print('i0d7s and f are:',i0d7s,i0d7f)
i0d8s = 0
i0d8f = 0
for i in range(0,len(i0d8)):
	if not i0d8[i] == '9.0'and not i0d8[i] == '0.0':
		if float(i0d8[i])**2 > cutp:
			i0d8s = i0d8s + 1
		else:
			i0d8f = i0d8f + 1
print('i0d8s and f are:',i0d8s,i0d8f)
i0d9s = 0
i0d9f = 0
for i in range(0,len(i0d9)):
	if not i0d9[i] == '9.0'and not i0d9[i] == '0.0':
		if float(i0d9[i])**2 > cutp:
			i0d9s = i0d9s + 1
		else:
			i0d9f = i0d9f + 1
print('i09s and f are:',i0d9s,i0d9f)
i0d10s = 0
i0d10f = 0
for i in range(0,len(i0d10)):
	if not i0d10[i] == '9.0'and not i0d10[i] == '0.0':
		if float(i0d10[i])**2 > cutp:
			i0d10s = i0d10s + 1
		else:
			i0d10f = i0d10f + 1
print('i0d10s and f are:',i0d10s,i0d10f)
i0d11s = 0
i0d11f = 0
for i in range(0,len(i0d11)):
	if not i0d11[i] == '9.0'and not i0d11[i] == '0.0':
		if float(i0d11[i])**2 > cutp:
			i0d11s = i0d11s + 1
		else:
			i0d11f = i0d11f + 1
print('i0d11s and f are:',i0d11s,i0d11f)
i0d12s = 0
i0d12f = 0
for i in range(0,len(i0d12)):
	if not i0d12[i] == '9.0'and not i0d12[i] == '0.0':
		if float(i0d12[i])**2 > cutp:
			i0d12s = i0d12s + 1
		else:
			i0d12f = i0d12f + 1
print('i0d12s and f are:',i0d12s,i0d12f)
i0d13s = 0
i0d13f = 0
for i in range(0,len(i0d13)):
	if not i0d13[i] == '9.0'and not i0d13[i] == '0.0':
		if float(i0d13[i])**2 > cutp:
			i0d13s = i0d13s + 1
		else:
			i0d13f = i0d13f + 1
print('i0d13s and f are:',i0d13s,i0d13f)


i1d1s = 0
i1d1f = 0
for i in range(0,len(i1d1)):
	if not i1d1[i] == '9.0'and not i1d1[i] == '0.0':
		if float(i1d1[i])**2 > cutp:
			i1d1s = i1d1s + 1
		else:
			i1d1f = i1d1f + 1
print('i1d1s and f are:',i1d1s,i1d1f)
i1d2s = 0
i1d2f = 0
for i in range(0,len(i1d2)):
	if not i1d2[i] == '9.0'and not i1d2[i] == '0.0':
		if float(i1d2[i])**2 > cutp:
			i1d2s = i1d2s + 1
		else:
			i1d2f = i1d2f + 1
print('i1d2s and f are:',i1d2s,i1d2f)
i1d3s = 0
i1d3f = 0
for i in range(0,len(i1d3)):
	if not i1d3[i] == '9.0'and not i1d3[i] == '0.0':
		if float(i1d3[i])**2 > cutp:
			i1d3s = i1d3s + 1
		else:
			i1d3f = i1d3f + 1
print('i1d3s and f are:',i1d3s,i1d3f)
i1d4s = 0
i1d4f = 0
for i in range(0,len(i1d4)):
	if not i1d4[i] == '9.0'and not i1d4[i] == '0.0':
		if float(i1d4[i])**2 > cutp:
			i1d4s = i1d4s + 1
		else:
			i1d4f = i1d4f + 1
print('i1d4s and f are:',i1d4s,i1d4f)
i1d5s = 0
i1d5f = 0
for i in range(0,len(i1d5)):
	if not i1d5[i] == '9.0'and not i1d5[i] == '0.0':
		if float(i1d5[i])**2 > cutp:
			i1d5s = i1d5s + 1
		else:
			i1d5f = i1d5f + 1
print('i1d5s and f are:',i1d5s,i1d5f)
i1d6s = 0
i1d6f = 0
for i in range(0,len(i1d6)):
	if not i1d6[i] == '9.0'and not i1d6[i] == '0.0':
		if float(i1d6[i])**2 > cutp:
			i1d6s = i1d6s + 1
		else:
			i1d6f = i1d6f + 1
print('i1d6s and f are:',i1d6s,i1d6f)
i1d7s = 0
i1d7f = 0
for i in range(0,len(i1d7)):
	if not i1d7[i] == '9.0'and not i1d7[i] == '0.0':
		if float(i1d7[i])**2 > cutp:
			i1d7s = i1d7s + 1
		else:
			i1d7f = i1d7f + 1
print('i1d7s and f are:',i1d7s,i1d7f)
i1d8s = 0
i1d8f = 0
for i in range(0,len(i1d8)):
	if not i1d8[i] == '9.0'and not i1d8[i] == '0.0':
		if float(i1d8[i])**2 > cutp:
			i1d8s = i1d8s + 1
		else:
			i1d8f = i1d8f + 1
print('i1d8s and f are:',i1d8s,i1d8f)
i1d9s = 0
i1d9f = 0
for i in range(0,len(i1d9)):
	if not i1d9[i] == '9.0'and not i1d9[i] == '0.0':
		if float(i1d9[i])**2 > cutp:
			i1d9s = i1d9s + 1
		else:
			i1d9f = i1d9f + 1
print('i19s and f are:',i1d9s,i1d9f)
i1d10s = 0
i1d10f = 0
for i in range(0,len(i1d10)):
	if not i1d10[i] == '9.0'and not i1d10[i] == '0.0':
		if float(i1d10[i])**2 > cutp:
			i1d10s = i1d10s + 1
		else:
			i1d10f = i1d10f + 1
print('i1d10s and f are:',i1d10s,i1d10f)
i1d11s = 0
i1d11f = 0
for i in range(0,len(i1d11)):
	if not i1d11[i] == '9.0'and not i1d11[i] == '0.0':
		if float(i1d11[i])**2 > cutp:
			i1d11s = i1d11s + 1
		else:
			i1d11f = i1d11f + 1
print('i1d11s and f are:',i1d11s,i1d11f)
i1d12s = 0
i1d12f = 0
for i in range(0,len(i1d12)):
	if not i1d12[i] == '9.0'and not i1d12[i] == '0.0':
		if float(i1d12[i])**2 > cutp:
			i1d12s = i1d12s + 1
		else:
			i1d12f = i1d12f + 1
print('i1d12s and f are:',i1d12s,i1d12f)
i1d13s = 0
i1d13f = 0
for i in range(0,len(i1d13)):
	if not i1d13[i] == '9.0'and not i1d13[i] == '0.0':
		if float(i1d13[i])**2 > cutp:
			i1d13s = i1d13s + 1
		else:
			i1d13f = i1d13f + 1
print('i1d13s and f are:',i1d13s,i1d13f)

###########################
######Total group test#####
###########################
o11 = i1d1s+i1d2s+i1d3s+i1d4s+i1d5s+i1d6s+i1d7s+i1d8s+i1d9s+i1d10s+i1d11s+i1d12s+i1d13s
o12 = i1d1f+i1d2f+i1d3f+i1d4f+i1d5f+i1d6f+i1d7f+i1d8f+i1d9f+i1d10f+i1d11f+i1d12f+i1d13f
o21 = i0d1s+i0d2s+i0d3s+i0d4s+i0d5s+i0d6s+i0d7s+i0d8s+i0d9s+i0d10s+i0d11s+i0d12s+i0d13s
o22 = i0d1f+i0d2f+i0d3f+i0d4f+i0d5f+i0d6f+i0d7f+i0d8f+i0d9f+i0d10f+i0d11f+i0d12f+i0d13f
cn = o11 + o12 + o21 + o22
t1 = (cn**0.5)*(o11*o22 - o12*o21)/(((o11+o12)*(o11+o21)*(o22+o21)*(o22+o12))**0.5)
print('stat t1 is',t1)
print('int total suc is',o11)
print('int total failure is',o12)
print('noint total suc is',o21)
print('noint total failure is',o22)

t3 = (o11-(o11+o12)*(o11+o21)/(cn))/((o11+o12)*(o11+o21)*(cn-o11-o12)*(cn-o11-o21)/((cn**2)*(cn-1)))**0.5
print('t3 is',t3)
#####################
##Test in Group######
#####################
xi = [i1d1s,i1d2s,i1d3s,i1d4s,i1d5s,i1d6s,i1d7s,i1d8s,i1d9s,i1d10s,i1d11s,i1d12s,i1d13s]
ni = [i1d1s+i1d1f+i0d1s+i0d1f,i1d2s+i1d2f+i0d2s+i0d2f,i1d3s+i1d3f+i0d3s+i0d3f,i1d4s+i1d4f+i0d4s+i0d4f,i1d5s+i1d5f+i0d5s+i0d5f,
	  i1d6s+i1d6f+i0d6s+i0d6f,i1d7s+i1d7f+i0d7s+i0d7f,i1d8s+i1d8f+i0d8s+i0d8f,i1d9s+i1d9f+i0d9s+i0d9f,i1d10s+i1d10f+i0d10s+i0d10f,
	  i1d11s+i1d11f+i0d11s+i0d11f,i1d12s+i1d12f+i0d12s+i0d12f,i1d13s+i1d13f+i0d13s+i0d13f]
ri = [i1d1s+i1d1f,i1d2s+i1d2f,i1d3s+i1d3f,i1d4s+i1d4f,i1d5s+i1d5f,
	  i1d6s+i1d6f,i1d7s+i1d7f,i1d8s+i1d8f,i1d9s+i1d9f,i1d10s+i1d10f,
	  i1d11s+i1d11f,i1d12s+i1d12f,i1d13s+i1d13f]
ci = [i1d1s+i0d1s,i1d2s+i0d2s,i1d3s+i0d3s,i1d4s+i0d4s,i1d5s+i0d5s,
	  i1d6s+i0d6s,i1d7s+i0d7s,i1d8s+i0d8s,i1d9s+i0d9s,i1d10s+i0d10s,
	  i1d11s+i0d11s,i1d12s+i0d12s,i1d13s+i0d13s]

sumxi = sum(xi)
sum2 = 0
for i in range(0,13):
	sum2 = sum2 + ri[i]*ci[i]/ni[i]
sum3 = 0
for i in range(0,13):
	sum3 = sum3 + ri[i]*ci[i]*(ni[i]-ri[i])*(ni[i]-ri[i])/((ni[i]**2)*(ni[i]-1))

t4 = (sumxi-sum2)/(sum3**0.5)
print('numer is',(sumxi-sum2))
print('sum3 is',sum3)
print('stat T4 is',t4)

sum4 = 0
for i in range(0,13):
	sum4 = sum4 + ri[i]*ci[i]*(ni[i]-ri[i])*(ni[i]-ri[i])/(ni[i]**3)
print('sum4 is',sum4)
t5 = (sumxi-sum2)/(sum4**0.5)
print('stat T5 is',t5)





print('the whole process over')
