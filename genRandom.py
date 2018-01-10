from numpy import random
from numpy import round

f_1 = 1/3.0
list_1 = [[random.uniform(0,f_1-0.01),random.uniform(0,1)] for i in range(0,100)]
file = open("py_pattern/list_1.dat","w+")
for item in list_1:
    file.write('{}\t{}\n'.format(item[0],item[1]))
file.close()
f_2 = 2/3.0
list_2 = [[random.uniform(f_1,f_2-0.01),random.uniform(0,1)] for i in range(0,100)]
file = open("py_pattern/list_2.dat","w+")
for item in list_2:
    file.write('{}\t{}\n'.format(item[0],item[1]))
file.close()
list_3 = [[random.uniform(f_2,1),random.uniform(0,1)] for i in range(0,100)]
file = open("py_pattern/list_3.dat","w+")
for item in list_3:
    file.write('{}\t{}\n'.format(item[0],item[1]))
file.close()

outList = list_1 + list_2 + list_3

file = open("Train/PA-D.dat","w+")
for item in outList:
    file.write('{}\t{}\n'.format(item[0],item[1]))
file.close()
