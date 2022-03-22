import os
import sys
import csv

gene_id_1=sys.argv[1]
gene_id_2=sys.argv[2]

def capturelist(filename):
    region_info_list=[]
    region_list=[]
    with open(filename) as region_txt:
        readregiontxt = csv.reader(region_txt, delimiter='\t')
        header=next(readregiontxt)
        region_line=next(readregiontxt)
        for curr_char in region_line[:5]:
            region_info_list.append(curr_char.strip())
        for curr_char in region_line[5:]:
            region_list.append(curr_char.strip())
    region_txt.close()
    return region_info_list,region_list

def calculate_cLD(region_list_1,region_list_2):
    pa=region_list_1.count("1")/len(region_list_1)
    pb=region_list_2.count("1")/len(region_list_2)
    num_common = 0
    for i in range(len(region_list_1)):
        if region_list_1[i] == "1" and region_list_1[i] == region_list_2[i]:
            num_common += 1
    pab=num_common/len(region_list_1)
    cLD=pab-pa*pb
    deno=(pa*(1-pa)*pb*(1-pb))**0.5
    if deno != 0:
        r=cLD/deno
        r2=r**2
    elif deno ==0:
        r="NA"
        r2="NA"
    return str(cLD),str(r),str(r2)

## From case
data_dir_1="/path_to_output/case/Gene_ID/"
data_dir_2="/path_to_output/case/Gene_ID/"
region_file_1=data_dir_1+gene_id_1+"/Region_Info_"+gene_id_1+".txt"
region_file_2=data_dir_2+gene_id_2+"/Region_Info_"+gene_id_2+".txt"
cld_case="None"
if os.path.isfile(region_file_1) and os.path.isfile(region_file_2):
    region_info_list_1,region_list_1=capturelist(region_file_1)
    region_info_list_2,region_list_2=capturelist(region_file_2)
    cld_case,r_case,r2_case=calculate_cLD(region_list_1,region_list_2)
    
## From control
data_dir_3="/path_to_output/control/Gene_ID/"
data_dir_4="/path_to_output/control/Gene_ID/"
region_file_3=data_dir_3+gene_id_1+"/Region_Info_"+gene_id_1+".txt"
region_file_4=data_dir_4+gene_id_2+"/Region_Info_"+gene_id_2+".txt"
cld_control="None"
if os.path.isfile(region_file_3) and os.path.isfile(region_file_4):
    region_info_list_3,region_list_3=capturelist(region_file_3)
    region_info_list_4,region_list_4=capturelist(region_file_4)
    cld_control,r_control,r2_control=calculate_cLD(region_list_3,region_list_4)
output_dir="/path_to_output/Gene2Gene/"+gene_id_1+"/"
if not os.path.exists(output_dir):
    os.makedirs(output_dir)
outfile=open(output_dir+ gene_id_1+"_"+gene_id_2+".txt",'w')
outfile.write("Metric\tCase\tControl")
outfile.write("\ncld\t"+cld_case+"\t"+cld_control)
outfile.write("\nr\t"+r_case+"\t"+r_control)
outfile.write("\nr2\t"+r2_case+"\t"+r2_control)
