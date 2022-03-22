package linkage_d;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;

public class Dinghao_convert {

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

		String ld_List = null;

		for (String check : file_List) {
			if (check.contains(".txt")) {
				ld_List = check;
			}
		}

		File ld_Full = new File(input.getAbsolutePath() + "/" + ld_List);

		FileReader fr = new FileReader(ld_Full);
		BufferedReader br = new BufferedReader(fr);

		String line = br.readLine();

		while (line != null) {
			// System.out.println(line);
			String[] line_Data = line.split(" ");
			String chr_Number = df.format(
					Integer.parseInt(line_Data[0].substring(line_Data[0].indexOf("chr") + 3, line_Data[0].length())));

			File folder_Chr = new File(output.getAbsolutePath() + "/" + chr_Number);
			File folder_Chr_input = new File(folder_Chr.getAbsolutePath() + "/input");
			if (!folder_Chr.exists()) {
				folder_Chr.mkdir();
				folder_Chr_input.mkdir();
				File gene_List = new File(folder_Chr_input.getAbsolutePath() + "/"
						+ ld_List.substring(0, ld_List.indexOf(".")) + "_" + chr_Number + ".txt");
				gene_List.createNewFile();
				System.out.println(chr_Number);
			}
			File gene_List = new File(folder_Chr_input.getAbsolutePath() + "/"
					+ ld_List.substring(0, ld_List.indexOf(".")) + "_" + chr_Number + ".txt");
			String write = line_Data[8] + "\t" + chr_Number + ";" + line_Data[4] + ";" + line_Data[6] + "\t"
					+ chr_Number + ";" + line_Data[4] + ";" + line_Data[6] + "\n";

			FileWriter fw = new FileWriter(gene_List, true);
			BufferedWriter bw = new BufferedWriter(fw);

			bw.write(write);

			bw.close();
			fw.close();

			line = br.readLine();
		}
		br.close();
		fr.close();
	}

}
