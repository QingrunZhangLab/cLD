#!/bin/bash
# Slurm Script Input Variables
#SBATCH --job-name=01_0001
#SBATCH --chdir=/home/duwagedahampriyabala/deshan/1000_Genome/LD_Process/01/
#SBATCH --error=01_0001.error
#SBATCH --output=01_0001.out
#SBATCH --mem=110G
#SBATCH --nodes=1
#SBATCH --ntasks=1
#SBATCH --cpus-per-task=10
#SBATCH --time=7-00:00:00
#SBATCH --partition=cpu2019,theia,cpu2013

# Programs
java=/home/duwagedahampriyabala/softwares/jre1.8.0_231/bin/java

# Print
echo "=============="
echo "Running 01_0001"
echo "=============="
echo ""
$java -Xmx100G -jar ld_only4.jar nonhicld 01_0001.txt
end=$SECONDS
echo "duration: $((end-start)) seconds."
