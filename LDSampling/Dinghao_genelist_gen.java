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
import java.util.Collections;
import java.util.Hashtable;

public class Dinghao_genelist_gen {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		File current = new File("");
		File output = new File(current.getAbsolutePath() + "/output/new");

		if (!output.exists()) {
			output.mkdir();
		}

		File input = new File(current.getAbsolutePath() + "/input");
		File genepairs = new File(current.getAbsolutePath() + "/input/Genepairs_HiC3dint");
		File geneindex = new File(current.getAbsolutePath() + "/input/genenameidlist");

		String[] gene_Pairs = genepairs.list();
		String[] gene_Library = geneindex.list();

		Hashtable<String, String> gene_Pair_index = new Hashtable<String, String>();
		Hashtable<String, String> gene_Library_index = new Hashtable<String, String>();
		ArrayList<String> chr_List = new ArrayList<String>();

		DecimalFormat df = new DecimalFormat("00");

		for (String file : gene_Pairs) {
			int chr = Integer.parseInt(file.substring(file.indexOf("chr") + 3, file.indexOf("int")));
			gene_Pair_index.put(df.format(chr), file);
			chr_List.add(df.format(chr));
		}

		// System.out.println(gene_Pair_index);
		Collections.sort(chr_List);

		for (String file : gene_Library) {
			int chr = Integer.parseInt(file.substring(file.indexOf("chr") + 3, file.indexOf("name")));
			gene_Library_index.put(df.format(chr), file);
		}

		// System.out.println(gene_Library_index);

		for (String chromosome : chr_List) {

			String file = gene_Library_index.get(chromosome);
			File Full_chr_index = new File(geneindex.getAbsolutePath() + "/" + file);
			FileReader fr = new FileReader(Full_chr_index);
			BufferedReader br = new BufferedReader(fr);

			Hashtable<String, String> gene_Library_write = new Hashtable<String, String>();

			String line = br.readLine();

			while (line != null) {
				String[] line_Data = line.split(" ");
				String chr_Number = df.format(Integer
						.parseInt(line_Data[0].substring(line_Data[0].indexOf("chr") + 3, line_Data[0].length())));
				// System.out.println(chr_Number);

				gene_Library_write.put(line_Data[8], chr_Number + ";" + line_Data[4] + ";" + line_Data[6]);

				line = br.readLine();
			}
			br.close();
			fr.close();

			file = gene_Pair_index.get(chromosome);
			File Full_chr_gene_pairs = new File(genepairs.getAbsolutePath() + "/" + file);
			FileReader fr_genepairs = new FileReader(Full_chr_gene_pairs);
			BufferedReader br_genepairs = new BufferedReader(fr_genepairs);

			File folder_Chr = new File(output.getAbsolutePath() + "/" + chromosome);
			File folder_Chr_input = new File(folder_Chr.getAbsolutePath() + "/input");
			if (!folder_Chr.exists()) {
				folder_Chr.mkdir();
				folder_Chr_input.mkdir();
				File gene_List = new File(folder_Chr_input.getAbsolutePath() + "/" + chromosome + ".txt");
				gene_List.createNewFile();
			}

			File gene_List = new File(folder_Chr_input.getAbsolutePath() + "/" + chromosome + ".txt");
			FileWriter fw = new FileWriter(gene_List, true);
			BufferedWriter bw = new BufferedWriter(fw);

			line = br_genepairs.readLine();

			while (line != null) {
				// System.out.println(line);
				String[] line_Data = line.split(" ");
				String combo_1 = line_Data[0];
				String combo_2 = line_Data[2];

				String write = combo_1 + "_" + combo_2 + "\t" + gene_Library_write.get(combo_1) + "\t"
						+ gene_Library_write.get(combo_2) + "\n";
				bw.write(write);

				line = br_genepairs.readLine();
			}
			bw.close();
			fw.close();
			br_genepairs.close();
			fr_genepairs.close();
		}

	}

}
