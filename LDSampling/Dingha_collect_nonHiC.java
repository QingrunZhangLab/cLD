package linkage_d;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Hashtable;

public class Dingha_collect_nonHiC {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub

		DecimalFormat df = new DecimalFormat("00");
		File current = new File("");
		File result_Folder = new File(current.getAbsolutePath() + "/Dinghao_hic");
		File summary_Folder = new File(current.getAbsolutePath() + "/Summary");
		if (!summary_Folder.exists()) {
			summary_Folder.mkdir();
		}

		String[] countries = { "AFR", "AMR", "EAS", "EUR", "SAS" };

		for (int c = 11; c <= 22; c++) {
			String chr = df.format(c);
			System.out.println("Combining: " + chr);
			File chr_Folder = new File(current.getAbsolutePath() + "/" + chr + "/output/Dinghao_nonHiC");
			for (String country : countries) {
				System.out.println("Country: " + country);

				File country_Folder = new File(chr_Folder.getAbsolutePath() + "/" + country);
				// System.out.println(country_Folder.getAbsolutePath());
				String[] list_all = country_Folder.list();

				File result_File = new File(summary_Folder.getAbsolutePath() + "/" + country + ".summaryDinghao");
				if (!result_File.exists()) {
					result_File.createNewFile();
					FileWriter fw = new FileWriter(result_File);
					BufferedWriter bw = new BufferedWriter(fw);
					bw.write(
							"Chromosome\tCombination\tHiC_Range\tDistance\tHiC_D_prime\tHiC_R_Squared\tNon_HiC_AVG_D_prime\tNon_HiC_AVG_R_Squared\n");
					bw.close();
					fw.close();
				}

				System.out.println("Loading nonHiC");
				Hashtable<String, String> index = new Hashtable<String, String>();
				for (String file : list_all) {
					if (file.contains(".")) {
						if (file.substring(file.lastIndexOf("."), file.length()).equals(".Dinghao_nonHiC")) {
							FileReader fr = new FileReader(country_Folder.getAbsolutePath() + "/" + file);
							BufferedReader br = new BufferedReader(fr);
							// skip first line
							br.readLine();
							String line = br.readLine();

							while (line != null) {
								String[] split = line.split("\t");
								index.put(split[0], split[3] + "\t" + split[4]);
								line = br.readLine();
							}
							br.close();
							fr.close();
						}
					}
				}
				// System.out.println(index);
				File hic_Full = new File(result_Folder.getAbsolutePath() + "/" + chr + "_" + country + ".summaryHiC");
				FileReader fr = new FileReader(hic_Full);
				BufferedReader br = new BufferedReader(fr);

				FileWriter fw = new FileWriter(result_File, true);
				BufferedWriter bw = new BufferedWriter(fw);

				// skip first line
				br.readLine();
				String line = br.readLine();

				while (line != null) {
					String[] split = line.split("\t");
					String key = split[1];
					// System.out.println(key);
					if (index.containsKey(key)) {
						String non_Hic_data = index.get(key);
						bw.write(line +"\t"+ non_Hic_data + "\n");
					} else {
						bw.write(line + "\tNA\tNA\n");
					}
					bw.flush();
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
