package linkage_d;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;

public class Collect_outliers {

	public static void main(String[] args) throws IOException {

		File current = new File("");
		File current_Folder = new File(current.getAbsolutePath());
		File results = new File(current_Folder.getAbsoluteFile() + "/results");
		if (!results.exists()) {
			results.mkdir();
		}

		DecimalFormat df = new DecimalFormat("00");

		for (int c = 1; c <= 22; c++) {
			String chrom = df.format(c);
			System.out.println("Processing\t: " + chrom);
			File folder = new File(current_Folder.getAbsoluteFile() + "/" + chrom + "/output/outliers");
			if (folder.exists()) {
				String[] files = folder.list();
				for (String file : files) {
					String country = file.substring(0, file.indexOf("."));
					System.out.println("Country\t: " + country);

					File country_All = new File(results.getAbsolutePath() + "/" + country + ".summary");
					if (!country_All.exists()) {
						FileWriter fw = new FileWriter(country_All);
						BufferedWriter bw = new BufferedWriter(fw);
						String header = "Chromosome\tCombination\tAvg_MAF\tSNP_density\n";
						bw.write(header);
						bw.close();
						fw.close();
					}
					File country_File = new File(folder.getAbsoluteFile() + "/" + file);

					FileReader fr = new FileReader(country_File);
					BufferedReader br = new BufferedReader(fr);
					br.readLine();

					String line = br.readLine();
					while (line != null) {

						FileWriter fw = new FileWriter(country_All, true);
						BufferedWriter bw = new BufferedWriter(fw);
						String liner = chrom + "\t" + line + "\n";
						bw.write(liner);
						bw.close();
						fw.close();

						line = br.readLine();

					}
					br.close();
					fr.close();

				}
			}
		}

	}

}
