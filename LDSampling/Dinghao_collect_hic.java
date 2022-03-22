package linkage_d;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;

public class Dinghao_collect_hic {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		DecimalFormat df = new DecimalFormat("00");
		File current = new File("");
		File result_Folder = new File(current.getAbsolutePath() + "/Dinghao_hic");
		if (!result_Folder.exists()) {
			result_Folder.mkdir();
		}

		String[] countries = { "AFR", "AMR", "EAS", "EUR", "SAS" };

		for (int c = 1; c <= 22; c++) {
			String chr = df.format(c);
			System.out.println("Combining: " + chr);
			File chr_Folder = new File(current.getAbsolutePath() + "/" + chr + "/output/Dinghao");
			for (String country : countries) {
				System.out.println("Country: " + country);
				File country_Folder = new File(chr_Folder.getAbsolutePath() + "/" + country);
				System.out.println(country_Folder.getAbsolutePath());
				String[] list_all = country_Folder.list();

				File result_File = new File(
						result_Folder.getAbsolutePath() + "/" + chr + "_" + country + ".summaryHiC");
				if (!result_File.exists()) {
					result_File.createNewFile();
					FileWriter fw = new FileWriter(result_File);
					BufferedWriter bw = new BufferedWriter(fw);
					bw.write("Chromosome\tCombination\tHiC_Range\tDistance\tHiC_D_prime\tHiC_R_Squared\n");
					bw.close();
					fw.close();
				}

				for (String file : list_all) {
					String ext = file.substring(file.lastIndexOf("."), file.length());
					// System.out.println(ext);
					if (ext.equals(".hicld_dinghao")) {
						File open = new File(country_Folder.getAbsolutePath() + "/" + file);

						FileWriter fw = new FileWriter(result_File, true);
						BufferedWriter bw = new BufferedWriter(fw);

						FileReader fr = new FileReader(open);
						BufferedReader br = new BufferedReader(fr);
						// skip first line
						br.readLine();
						String line = br.readLine();

						while (line != null) {
							bw.write(chr + "\t" + line + "\n");
							line = br.readLine();
						}
						br.close();
						fr.close();
						bw.close();
						fw.close();
					}
				}

			}

		}

	}

}
