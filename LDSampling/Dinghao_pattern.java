package linkage_d;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class Dinghao_pattern {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub

		File folder = new File("");
		File current = new File(folder.getAbsolutePath());
		File input = new File(current.getAbsolutePath() + "/input");
		File output = new File(current.getAbsolutePath() + "/output");

		DecimalFormat df = new DecimalFormat("00");

		if (!output.exists()) {
			output.mkdir();
		}

		String[] file_List = input.list();

		for (String check : file_List) {
			File checker = new File(input.getAbsolutePath() + "/" + check);
			if (checker.isDirectory()) {
				System.out.println("Chromosome\t: " + check);
				File chr_Input = new File(checker.getAbsolutePath() + "/input");
				String[] chr_List = chr_Input.list();

				String ld_List = null;

				for (String get_List : chr_List) {
					if (get_List.contains(".txt")) {
						ld_List = get_List;
					}

					System.out.println("Chromosome LD list\t: " + ld_List);

					File chr_geneFile = new File(chr_Input.getAbsolutePath() + "/" + ld_List);
					FileReader fr = new FileReader(chr_geneFile);
					BufferedReader br = new BufferedReader(fr);

					String line = br.readLine();

					List<String> ld_Combos = new ArrayList<String>();

					String folder_Chr_input_main = null;
					String chr_Number = "";
					while (line != null) {
						// System.out.println(line);
						String[] line_Data = line.split("\t");

						chr_Number = line_Data[1].substring(0, line_Data[1].indexOf(";"));
						// System.out.println(chr_Number);

						File folder_Chr = new File(output.getAbsolutePath() + "/" + chr_Number);
						File folder_Chr_input = new File(folder_Chr.getAbsolutePath() + "/input");
						folder_Chr_input_main = folder_Chr_input.getAbsolutePath();
						if (!folder_Chr.exists()) {
							folder_Chr.mkdir();
							folder_Chr_input.mkdir();
							// File gene_List = new File(folder_Chr_input.getAbsolutePath() + "/" +
							// ld_List);
							// gene_List.createNewFile();
							// System.out.println(chr_Number);
						}

						ld_Combos.add(line_Data[0] + "\t" + line_Data[1]);

						line = br.readLine();
					}

					br.close();
					fr.close();

					// System.out.println(ld_Combos.size());

					int count = 1;

					for (int c = 0; c < ld_Combos.size(); c++) {
						String count_Name = df.format(count);
						File gene_List = new File(folder_Chr_input_main + "/" + chr_Number + "_" + count_Name + ".txt");

						if (!gene_List.exists()) {
							gene_List.createNewFile();
						}

						String[] gene_Main = ld_Combos.get(c).split("\t");
						String gene_1_name = gene_Main[0];
						String gene_1_config = gene_Main[1];
						System.out.println("Combining\t: " + gene_1_name);
						for (int c1 = c; c1 < ld_Combos.size(); c1++) {
							if (c != c1) {
								String[] gene_Sub = ld_Combos.get(c1).split("\t");
								String gene_2_name = gene_Sub[0];
								String gene_2_config = gene_Sub[1];

								String combo_Name = gene_1_name + "_" + gene_2_name;
								String write = combo_Name + "\t" + gene_1_config + "\t" + gene_2_config + "\n";
								// System.out.println(write);

								FileWriter fw = new FileWriter(gene_List, true);
								BufferedWriter bw = new BufferedWriter(fw);

								bw.write(write);

								bw.close();
								fw.close();

							}

						}
						count++;
					}

				}

			}
			System.out.println();
		}
		System.out.println("Completed run");
	}

}
