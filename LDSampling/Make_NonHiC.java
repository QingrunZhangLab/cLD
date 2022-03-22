package linkage_d;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;

public class Make_NonHiC {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub

		File current = new File("");
		File collection = new File(current.getAbsolutePath() + "/collection");
		if (!collection.exists()) {
			collection.mkdir();
		}

		DecimalFormat df = new DecimalFormat("00");

		int combo_Track = 0;

		for (int c = 1; c <= 22; c++) {

			String current_Chrom = df.format(c);
			System.out.println("Processing\t: " + current_Chrom);

			File chrom = new File(current.getAbsolutePath() + "/" + current_Chrom + "/output/Combo_Details");

			if (chrom.exists()) {
				String[] list = chrom.list();
				for (String file : list) {
					if (file.substring(file.lastIndexOf("."), file.length()).equals(".summary")) {
						// System.out.println("Exracting Non_HiC from File\t: " + file);
						String country_extract = file.substring(file.indexOf("genotypes.vcf_"),
								file.indexOf(".vcf_HiC.summary"));
						String country = country_extract.substring(country_extract.lastIndexOf("_") + 1,
								country_extract.length());
						System.out.println("Processing Country\t: " + country);

						File country_Process = new File(chrom.getAbsolutePath() + "/" + file);
						FileReader fr = new FileReader(country_Process);

						File collect_Folder = new File(collection.getAbsoluteFile() + "/" + current_Chrom + "/nonHiCs");
						if (!collect_Folder.exists()) {
							collect_Folder.getParentFile().mkdirs();
							collect_Folder.mkdir();
						}
						File collection_Country = new File(
								collect_Folder.getAbsolutePath() + "/" + current_Chrom + "_" + country + ".txt");
						if (collection_Country.exists()) {
							collection_Country.delete();
							collection_Country.createNewFile();
						}

						FileWriter fw = new FileWriter(collection_Country, true);
						BufferedWriter bw = new BufferedWriter(fw);

						BufferedReader br = new BufferedReader(fr);
						br.readLine();

						String line = br.readLine();
						while (line != null) {
							// System.out.println(line);
							String[] line_Data = line.split("\t");
							for (int combo_Loc = 3; combo_Loc <= 12; combo_Loc++) {
								String combo = line_Data[combo_Loc];
								String start = combo.substring(0, combo.indexOf("|"));
								String end = combo.substring(combo.lastIndexOf("|") + 1, combo.length());
								String write = "C" + combo_Track + "\t" + c + ";" + start + "\t" + c + ";" + end+"\n";
								bw.write(write);
								bw.flush();
								combo_Track++;
							}
							// break;
							line = br.readLine();
						}
						br.close();
						fr.close();
						bw.close();
						fw.close();
					}

				}

			} else {
				System.out.println("Chromosome " + current_Chrom + " does not exist");
			}

		}

	}

}
