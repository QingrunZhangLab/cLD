#!/bin/bash
# Slurm Script Input Variables
#SBATCH --account=def-quanlong
#SBATCH --job-name=02_batch
#SBATCH --chdir=/project/6004755/deshan/1000_Genome/scripts/batch/
#SBATCH --error=02_100.error
#SBATCH --output=02_100.out
#SBATCH --mem=5G
#SBATCH --nodes=1
#SBATCH --ntasks=1
#SBATCH --cpus-per-task=1
#SBATCH --time=7-00:00:00

# Programs
java=/work/long_lab/deshan/softwares/jre1.8.0_231/bin/java

# Print
echo "=============="
echo "Running 02"
echo "=============="
echo ""

cd /project/6004755/deshan/1000_Genome/scripts/scripts_2
pwd
jobs_total=839
echo $jobs_total
count_jobs=1

while [ "$count_jobs" -le "$jobs_total" ]

do
    #echo $i
    current_run=$(sq -u deshan | wc -l)
    #echo "current run: "$current_run
    #current_running=$(squeue -u duwagedahampriyabala -t RUNNING | wc -l)
    #echo "current_running : "$current_running
    #if [[ current_run -le 1501 ]] && [[ current_running -le 500 ]]
    if [[ current_run -le 500 ]]
    then
      sbatch $count_jobs.sh
      echo $count_jobs
      ((count_jobs++))
    else
      sleep 30
    fi
done

end=$SECONDS
echo "duration: $((end-start)) seconds."
