package linkage_d;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;

public class Collect_ind {

	public static void main(String[] args) throws IOException {

		File current = new File("");
		File current_Folder = new File(current.getAbsolutePath());
		File output_Folder = new File(current_Folder.getAbsolutePath() + "/output_Detailed");
		if (!output_Folder.exists()) {
			output_Folder.mkdir();
		}

		DecimalFormat df = new DecimalFormat("00");

		for (int c = 1; c <= 22; c++) {
			System.out.println("Processing Chromosome\t: " + df.format(c));
			File current_Chrom = new File(current_Folder.getAbsolutePath() + "/" + df.format(c) + "/output");
			File current_Chrom_out = new File(
					current_Folder.getAbsolutePath() + "/" + df.format(c) + "/output/Combo_Details");
			if (current_Chrom.exists()) {
				// System.out.println(current_Chrom.getAbsolutePath());
				String[] files = current_Chrom.list();
				for (String file : files) {
					if (file.contains("hicld")) {
						String extension = file.substring(file.lastIndexOf("."), file.length());
						if (extension.equals(".hicld")) {
							// System.out.println(file);
							String country = file.substring(file.lastIndexOf("_") + 1, file.lastIndexOf("."));
							System.out.println(country);
							String[] combo_Files = current_Chrom_out.list();
							String combo_Country = null;
							for (String check : combo_Files) {
								if (check.contains(country)) {
									combo_Country = check;
									break;
								}
							}
							// System.out.println(combo_Country);
							FileReader fr_Main = new FileReader(current_Chrom.getAbsolutePath() + "/" + file);
							FileReader fr_Combo = new FileReader(
									current_Chrom_out.getAbsolutePath() + "/" + combo_Country);
							BufferedReader br_Main = new BufferedReader(fr_Main);
							BufferedReader br_Combo = new BufferedReader(fr_Combo);

							String pathname = output_Folder.getAbsolutePath() + "/" + country + ".complete";
							File country_All = new File(pathname);
							if (!country_All.exists()) {
								FileWriter fw = new FileWriter(country_All);
								BufferedWriter bw = new BufferedWriter(fw);
								String header = "Chromosome\t" + "Combination\t" + "Distance\t" + "Type\t" + "Number\t"
										+ "D_prime\t" + "R_squared\n";
								bw.write(header);
								bw.flush();
								bw.close();
								fw.close();
							}
							
							br_Main.readLine();
							br_Combo.readLine();

							String line_Main = br_Main.readLine();
							String line_Combo = br_Combo.readLine();

							FileWriter fw = new FileWriter(country_All, true);
							BufferedWriter bw = new BufferedWriter(fw);
							while (line_Main != null) {
								String line_Write = df.format(c);
								String[] main_Data = line_Main.split("\t");
								line_Write = line_Write + "\t" + main_Data[0] + "\t" + main_Data[2] + "\t" + "HiC"
										+ "\t" + "00" + "\t" + main_Data[3] + "\t" + main_Data[5] + "\n";
								bw.write(line_Write);
								bw.flush();

								String[] combo_Data = line_Combo.split("\t");
								for (int comb = 1; comb <= 10; comb++) {
									line_Write = null;
									line_Write = df.format(c);
									line_Write = line_Write + "\t" + combo_Data[0];
									line_Write = line_Write + "\t" + main_Data[2];
									line_Write = line_Write + "\t" + "Non_HiC";
									line_Write = line_Write + "\t" + df.format(comb);
									line_Write = line_Write + "\t" + combo_Data[comb + 22];
									line_Write = line_Write + "\t" + combo_Data[comb + 32] + "\n";
									bw.write(line_Write);
									bw.flush();
								}
								line_Combo = br_Combo.readLine();
								line_Main = br_Main.readLine();
							}
							bw.flush();
							bw.close();
							fw.close();
						}
					}
				}
			} else {
				System.out.println("Chromosome " + df.format(c) + " not found");
			}
		}

	}

}
