package linkage_d;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class script_gen {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		ExecutorService ex = Executors.newFixedThreadPool(7);
		File current = new File("");
		File input = new File(current.getAbsolutePath() + "/input");
		File output = new File(current.getAbsolutePath() + "/output/scripts_cedar_2");
		DecimalFormat df = new DecimalFormat("00");
		if (!output.exists()) {
			output.mkdir();
		}

		String[] list = input.list();

		for (int c = 1; c <= list.length; c++) {

			String name = list[c - 1];
			File chr_Folder = new File(output.getAbsolutePath());
			if (!chr_Folder.exists()) {
				chr_Folder.mkdir();
			}

			File contents = new File(input.getAbsolutePath() + "/" + name + "/input");
			if (contents.exists()) {
				System.out.println(contents.getAbsolutePath());
			}
			// cut
			ex.execute(new script_Generator(contents, name, chr_Folder));
			System.out.println();
		}
		ex.shutdown();
		while (!ex.isTerminated()) {
		}
		System.out.println("\nCompleted Script Generation");
	}

}

class script_Generator implements Runnable {
	File contents;
	String name;
	File chr_Folder;

	script_Generator(File contents, String name, File chr_Folder) {
		this.contents = contents;
		this.name = name;
		this.chr_Folder = chr_Folder;

	}

	public void run() {
		String[] combos = contents.list();
		// DecimalFormat df2 = new DecimalFormat("0000");
		int count_Combo = 1;
		for (String combo_Name : combos) {

			System.out.println("Generating script\t: " + name + "_" + count_Combo);

			String w_Dir = "/scratch/deshan/1000_Genome";

			File script_Folder = chr_Folder;
			String combo_Name_trim = combo_Name.substring(0, combo_Name.lastIndexOf("."));
			String pathname = script_Folder.getAbsolutePath() + "/" + name + "_" + count_Combo + ".sh";
			File script = new File(pathname);
			if (!script.exists()) {
				try {
					script.createNewFile();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			FileWriter fw;
			try {
				fw = new FileWriter(script, true);
				BufferedWriter line = new BufferedWriter(fw);

				line.write("#!/bin/bash");
				line.write("\n");
				line.write("# Slurm Script Input Variables");
				line.write("\n");
				// line.write("#SBATCH --account=def-quanlong");
				// line.write("\n");
				line.write("#SBATCH --job-name=" + combo_Name_trim);
				line.write("\n");
				line.write("#SBATCH --chdir=" + w_Dir + "/" + name + "/");
				line.write("\n");
				line.write("#SBATCH --error=" + combo_Name_trim + ".error");
				line.write("\n");
				line.write("#SBATCH --output=" + combo_Name_trim + ".out");
				line.write("\n");

				line.write("#SBATCH --mem=110G");
				line.write("\n");
				line.write("#SBATCH --nodes=1");
				line.write("\n");
				line.write("#SBATCH --ntasks=1");
				line.write("\n");
				line.write("#SBATCH --cpus-per-task=15");
				line.write("\n");
				line.write("#SBATCH --time=5-00:00:00");
				line.write("\n");
				line.write("#SBATCH --partition=cpu2019,theia,cpu2013");
				line.write("\n");

				line.write("\n# Programs");
				line.write("\n");
				line.write("java=/home/duwagedahampriyabala/softwares/jre1.8.0_231/bin/java");
				// line.write("java=/scratch/deshan/software/jre1.8.0_281/bin/java");
				line.write("\n");

				line.flush();

				line.write("\n# Print");
				line.write("\n");
				line.write("echo \"==============\"");
				line.write("\n");
				line.write("echo \"Running " + combo_Name_trim + "\"");
				line.write("\n");
				line.write("echo \"==============\"");
				line.write("\n");
				line.write("echo \"\"");
				line.write("\n");

				line.flush();

				line.write("$java -Xmx100G -jar ld_only5.jar nonhicld " + combo_Name);

				line.write("\n");
				line.write("end=$SECONDS");
				line.write("\n");
				line.write("echo \"duration: $((end-start)) seconds.\"");
				line.flush();

				line.close();
				fw.close();
				count_Combo++;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
