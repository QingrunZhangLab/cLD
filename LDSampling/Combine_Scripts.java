package linkage_d;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Combine_Scripts {

	public static void main(String[] args) throws IOException {

		File current = new File("");
		File folders = new File(current.getAbsolutePath() + "/input/combine");
		String chromosomes[] = folders.list();
		for (String chr_num : chromosomes) {
			System.out.println(chr_num);
			File folder = new File(current.getAbsolutePath() + "/input/combine/" + chr_num);
			String w_Dir = "/work/long_lab/deshan/1000_Genome/LD_Process/" + chr_num;

			File output = new File(current.getAbsolutePath() + "/output");
			File output_Combine = new File(output.getAbsolutePath() + "/scripts_arc");
			if (!output_Combine.exists()) {
				output_Combine.mkdir();
			}
			String files_all[] = folder.list();
			int start = 1;
			int end = files_all.length;
			int collect = 1;
			ArrayList<String> AL = new ArrayList<String>();

			for (int count = start; count <= end; count++) {
				// System.out.println(count);
				File check = new File(folder.getAbsolutePath() + "/" + count + ".sh");
				if (check.exists()) {
					FileReader fr = new FileReader(folder.getAbsolutePath() + "/" + count + ".sh");
					BufferedReader br = new BufferedReader(fr);

					String liner = br.readLine();

					while (liner != null) {

						if (liner.contains("$java -Xmx100G -jar ld_only5.jar nonhicld")) {
							// liner = liner.replace("ldonly", "hicld");
							// System.out.println(line.substring(line.indexOf("ldonly")+7,line.length()-4));
							AL.add(liner);
						}

						if (AL.size() == 1) {
							Printer(AL, output_Combine, collect, w_Dir, chr_num);
							AL.clear();
							collect++;
						}
						liner = br.readLine();
					}
				}
			}

			if (AL.size() > 0) {
				Printer(AL, output_Combine, collect, w_Dir, chr_num);
			}
		}
	}

	public static void Printer(ArrayList<String> AL, File output_Combine, int collect, String w_Dir, String chromosome)
			throws IOException {

		String start_Name = AL.get(0).substring(AL.get(0).indexOf("hicld") + 6, AL.get(0).length() - 4);
		String end_Name = AL.get(AL.size() - 1).substring(AL.get(0).indexOf("hicld") + 6, AL.get(0).length() - 4);
		String file_Name = start_Name + "." + end_Name;
		File script = new File(output_Combine + "/" + chromosome + "_" + collect + ".sh");

		if (!script.exists()) {
			try {
				script.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		FileWriter fw = new FileWriter(script, true);
		BufferedWriter line = new BufferedWriter(fw);

		line.write("#!/bin/bash");
		line.write("\n");
		line.write("# Slurm Script Input Variables");
		line.write("\n");
		//line.write("#SBATCH --account=def-quanlong");
		//line.write("\n");
		line.write("#SBATCH --job-name=" + script.getName().substring(0, script.getName().indexOf(".")));
		line.write("\n");
		line.write("#SBATCH --chdir=" + w_Dir + "/");
		line.write("\n");
		line.write("#SBATCH --error=" + script.getName().substring(0, script.getName().indexOf(".")) + ".error");
		line.write("\n");
		line.write("#SBATCH --output=" + script.getName().substring(0, script.getName().indexOf(".")) + ".out");
		line.write("\n");

		line.write("#SBATCH --mem=110G");
		line.write("\n");
		line.write("#SBATCH --nodes=1");
		line.write("\n");
		line.write("#SBATCH --ntasks=1");
		line.write("\n");
		line.write("#SBATCH --cpus-per-task=10");
		line.write("\n");
		line.write("#SBATCH --time=7-00:00:00");
		line.write("\n");
		line.write("#SBATCH --partition=cpu2019,theia,cpu2013");
		line.write("\n");

		line.write("\n# Programs");
		line.write("\n");
		line.write("java=/work/long_lab/deshan/softwares/jre1.8.0_231/bin/java");
		//line.write("java=/scratch/deshan/software/jre1.8.0_281/bin/java");
		//line.write("java=/lustre04/scratch/deshan/software/jre1.8.0_281/bin/java");
		line.write("\n");

		line.flush();

		line.write("\n# Print");
		line.write("\n");
		line.write("echo \"==============\"");
		line.write("\n");
		line.write("echo \"Running " + file_Name + "\"");
		line.write("\n");
		line.write("echo \"==============\"");
		line.write("\n");
		line.write("echo \"\"");
		line.write("\n");

		line.flush();

		for (String lines : AL) {
			line.write(lines);
			line.write("\n");
		}

		line.write("\n");
		line.write("end=$SECONDS");
		line.write("\n");
		line.write("echo \"duration: $((end-start)) seconds.\"");
		line.flush();

		line.close();
		fw.close();

	}

}
