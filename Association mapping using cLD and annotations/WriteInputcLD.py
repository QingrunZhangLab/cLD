import csv
import sys
import os

gene_id=sys.argv[1]
maf_cutoff=float(0.005)

def count_maf(row):
    num_alt=0
    num_ref=0
    for snp_info in row:
        if snp_info == "0/1":
            num_alt+=1
            num_ref+=1
        if snp_info == "1/1":
            num_alt+=2
        else:
            num_ref+=2
    maf=num_alt/(2*len(row))
    return maf


with open("/path_to_gencode/gencode.v19.genes.v7.no_sex.csv") as gencode_csv:
    readgencodeCSV = csv.reader(gencode_csv, delimiter=',')
    for row in readgencodeCSV:
        target_gene_id="gene_id "+"\""+gene_id+"\""
        if row[4].strip() == target_gene_id:
            target_chr=row[0]
            loc_start=int(row[2])
            loc_end=int(row[3])
            gene_name=row[5].split("\"")[1]
            break

rare_variant_list=[]
with open("/path_to_genotype/data_prefix.clean.chr"+target_chr+".vcf") as all_vcf:
    readallvcf = csv.reader(all_vcf, delimiter='\t')
    for row in readallvcf:
        if "#" not in row[0].strip():
            curr_pos=int(row[1].strip())
            if curr_pos >=loc_start and curr_pos <=loc_end:
                maf=count_maf(row[9:])
                snp_chrpos=row[0].strip()+"_"+row[1].strip()
                if maf < maf_cutoff and maf!=0.0:
                    rare_variant_list.append(snp_chrpos)
            elif curr_pos > loc_end:
                break
all_vcf.close()

if len(rare_variant_list) !=0:
    output_dir="/path_to_output/case/Gene_ID/"+gene_id+"/"
    if not os.path.exists(output_dir):
        os.makedirs(output_dir)
    with open("/path_to_genotype/case/data_prefix.case.clean.chr"+target_chr+".vcf") as case_vcf:
        out_rare_snp_file = open(output_dir+"Rare_Variante_Info_"+gene_id+".txt", "w")
        out_region_file = open(output_dir+"Region_Info_"+gene_id+".txt", "w")
        out_region_file.write("Gene_ID\tGene_Name\tChr\tStart\tEnd")
        rare_snp_sample_dict={}
        sample_id_list=[]
        readcasevcf = csv.reader(case_vcf, delimiter='\t')
        for row in readcasevcf:
            if row[0].strip() == "#CHROM":
                out_rare_snp_file.write(row[0].strip())
                for curr_id in row[9:]:
                    sample_id_list.append(curr_id.strip())
                    out_region_file.write("\t"+curr_id.strip())
                for curr_char in row[1:]:
                    out_rare_snp_file.write("\t"+curr_char.strip())
            if "#" not in row[0].strip():
                curr_pos=int(row[1].strip())
                if curr_pos >=loc_start and curr_pos <=loc_end:
                    curr_snp_chrpos=row[0].strip()+"_"+row[1].strip()
                    if curr_snp_chrpos in rare_variant_list:
                        out_rare_snp_file.write("\n"+row[0].strip())
                        for curr_char in row[1:]:
                            out_rare_snp_file.write("\t"+curr_char.strip())
                        num_id=0
                        for curr_snp in row[9:]:
                             curr_id=sample_id_list[num_id]
                             if curr_id not in rare_snp_sample_dict.keys():
                                 rare_snp_sample_dict[curr_id]=[]
                             rare_snp_sample_dict[curr_id].append(curr_snp.strip())
                             num_id+=1
        out_region_file.write("\n"+gene_id+"\t"+gene_name+"\t"+str(target_chr)+"\t"+str(loc_start)+"\t"+str(loc_end))
        num_count=0
        for i in range(len(sample_id_list)):
            sample_id=sample_id_list[i]
            region_alt=0
            if "0/1" in rare_snp_sample_dict[sample_id] or "1/1" in rare_snp_sample_dict[sample_id]:
                region_alt=1        
                num_count+=1
            out_region_file.write("\t"+str(region_alt))
        print(num_count/len(sample_id_list))
        out_region_file.write("\ncMAF\t"+str(num_count/len(sample_id_list)))  
    out_region_file.close()
    out_rare_snp_file.close()
    case_vcf.close()
			
    output_dir_ctrl="/path_to_output/control/Gene_ID/"+gene_id+"/"
    if not os.path.exists(output_dir_ctrl):
        os.makedirs(output_dir_ctrl)
    with open("/path_to_genotype/control/data_prefix.control.clean.chr"+target_chr+".vcf") as control_vcf:
        out_rare_snp_file_ctrl = open(output_dir_ctrl+"Rare_Variante_Info_"+gene_id+".txt", "w")
        out_region_file_ctrl = open(output_dir_ctrl+"Region_Info_"+gene_id+".txt", "w")
        out_region_file_ctrl.write("Gene_ID\tGene_Name\tChr\tStart\tEnd")
        rare_snp_sample_dict_ctrl={}
        sample_id_list_ctrl=[]
        readcontrolvcf = csv.reader(control_vcf, delimiter='\t')
        for row in readcontrolvcf:
            if row[0].strip() == "#CHROM":
                out_rare_snp_file_ctrl.write(row[0].strip())
                for curr_id in row[9:]:
                    sample_id_list_ctrl.append(curr_id.strip())
                    out_region_file_ctrl.write("\t"+curr_id.strip())
                for curr_char in row[1:]:
                    out_rare_snp_file_ctrl.write("\t"+curr_char.strip())
            if "#" not in row[0].strip():
                curr_pos=int(row[1].strip())
                if curr_pos >=loc_start and curr_pos <=loc_end:
                    curr_snp_chrpos=row[0].strip()+"_"+row[1].strip()
                    if curr_snp_chrpos in rare_variant_list:
                        out_rare_snp_file_ctrl.write("\n"+row[0].strip())
                        for curr_char in row[1:]:
                            out_rare_snp_file_ctrl.write("\t"+curr_char.strip())
                        num_id=0
                        for curr_snp in row[9:]:
                             curr_id=sample_id_list_ctrl[num_id]
                             if curr_id not in rare_snp_sample_dict_ctrl.keys():
                                 rare_snp_sample_dict_ctrl[curr_id]=[]
                             rare_snp_sample_dict_ctrl[curr_id].append(curr_snp.strip())
                             num_id+=1
        out_region_file_ctrl.write("\n"+gene_id+"\t"+gene_name+"\t"+str(target_chr)+"\t"+str(loc_start)+"\t"+str(loc_end))
        num_count=0
        for i in range(len(sample_id_list_ctrl)):
            sample_id=sample_id_list_ctrl[i]
            region_alt=0
            if "0/1" in rare_snp_sample_dict_ctrl[sample_id] or "1/1" in rare_snp_sample_dict_ctrl[sample_id] or "1/0" in rare_snp_sample_dict_ctrl[sample_id]:
                region_alt=1
                num_count+=1
            out_region_file_ctrl.write("\t"+str(region_alt))
        out_region_file_ctrl.write("\ncMAF\t"+str(num_count/len(sample_id_list_ctrl)))
    out_region_file_ctrl.close()
    out_rare_snp_file_ctrl.close()
    control_vcf.close()
