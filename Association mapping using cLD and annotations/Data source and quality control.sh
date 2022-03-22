plink="/path_to_plink/plink"

##Delete individuals with missingness>0.1
$plink --bfile "$prefix" --mind 0.1 --make-bed --out "$prefix".1
##Delete snp with missingness>0.1
$plink --bfile "$prefix".1 --geno 0.1 --make-bed --out "$prefix".2
##Check the distribution of HWE p-values for all SNPs
##First, use a stringent HWE threshold for controls
$plink --bfile "$prefix".2 --hwe 1e-6 --make-bed --out "$pefix".step1
##Second, use a less stringent threshold for the case
$plink --bfile "$prefix".step1 --hwe 1e-10 --hwe-all --make-bed --out "$prefix".step2
##Remove duplicate SNPs
$plink --noweb --bfile "$prefix".step2 --list-duplicate-vars
cut -f4 plink.dupvar | cut -f1 -d" " > Duplicates.list
$plink --noweb --bfile "$prefix".step2 --exclude Duplicates.list --make-bed --out "$prefix".clean
