package linkage_d;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Dinghao_1000 {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub

		File current = new File("");
		File input = new File(current.getAbsolutePath() + "/input");
		File output = new File(current.getAbsolutePath() + "/output");
		if (!output.exists()) {
			output.mkdir();
		}

		String[] file_List = input.list();
		DecimalFormat df = new DecimalFormat("0000");

		ExecutorService ex = Executors.newFixedThreadPool(7);

		for (String chr : file_List) {
			File input_Chr = new File(input.getAbsolutePath() + "/" + chr + "/input");
			File output_Chr = new File(output.getAbsolutePath() + "/" + chr);
			File output_Chr_input = new File(output.getAbsolutePath() + "/" + chr + "/input");
			if (!output_Chr.exists()) {
				output_Chr.mkdir();
				output_Chr_input.mkdir();
			}

			ex.execute(new Thread_collect_1000(input_Chr,df,output_Chr_input));

		}

		ex.shutdown();
		while (!ex.isTerminated()) {
		}

	}

}

class Thread_collect_1000 implements Runnable {
	File input_Chr;
	DecimalFormat df;
	File output_Chr_input;

	Thread_collect_1000(File input_Chr, DecimalFormat df, File output_Chr_input) {
		this.input_Chr = input_Chr;
		this.df = df;
		this.output_Chr_input = output_Chr_input;
	}

	public void run() {

		String[] gene_List = input_Chr.list();
		for (String gene : gene_List) {
			//String[] name_File = gene.split("_");
			//String chr_Number = name_File[0];
			//String gene_Number = df.format(Integer.parseInt(name_File[1].substring(0, name_File[1].indexOf("."))));
			String new_Name = gene.substring(0,gene.indexOf("."));
			System.out.println("Splitting file\t: " + new_Name);
			File gene_File = new File(input_Chr.getAbsolutePath() + "/" + gene);

			FileReader fr;
			try {
				fr = new FileReader(gene_File);
				BufferedReader br = new BufferedReader(fr);

				String line = br.readLine();
				int line_Count = 0;
				int file_Count = 1;

				while (line != null) {

					String file_Count_name = new_Name + "_" + df.format(file_Count) + ".txt";
					File file_Split = new File(output_Chr_input.getAbsolutePath() + "/" + file_Count_name);
					if (!file_Split.exists()) {
						file_Split.createNewFile();
					}

					FileWriter fw = new FileWriter(file_Split, true);
					BufferedWriter bw = new BufferedWriter(fw);
					bw.write(line + "\n");

					if (line_Count < 9) {
						line_Count++;
					} else {
						line_Count = 0;
						file_Count++;
					}
					bw.close();
					fw.close();
					line = br.readLine();
				}
				br.close();
				fr.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}
}
