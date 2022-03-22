package linkage_d;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.List;

public class Summary_add {

	Summary_add() {

	}

	public void initiate() throws IOException {

		File current = new File("");
		File current_Folder = new File(current.getAbsolutePath());
		DecimalFormat df = new DecimalFormat("00");

		for (int c = 1; c <= 22; c++) {
			System.out.println("Processing\t: " + df.format(c));
			String current_Chromosome = current_Folder.getAbsolutePath() + "/" + df.format(c);
			File input = new File(current_Chromosome + "/input");
			if (input.exists()) {
				String[] file_List = input.list();
				for (String file : file_List) {
					if (file.contains("vcf")) {
						String country = file.substring(file.lastIndexOf("_") + 1, file.lastIndexOf("."));
						System.out.println("Processing\t: " + country);
						List<String> lines = Files.readAllLines(Paths.get(input.getAbsolutePath() + "/" + file));
						// SNP number total
						int snp_Total = lines.size() - 1;
						File output = new File(current_Chromosome + "/output/Combo_Details");
						String[] all_Summary = output.list();
						String summary_Country = null;
						for (String check : all_Summary) {
							if (check.contains(country)) {
								summary_Country = check;
								break;
							}
						}
						String hiC_Country_path = output.getAbsolutePath() + "/" + summary_Country;
						File hiC_Country = new File(hiC_Country_path);
						FileReader fr = new FileReader(hiC_Country);
						BufferedReader br = new BufferedReader(fr);
						br.readLine();
						String line = br.readLine();
						// HiC SNP total
						int tot_SNP = 0;
						int start_Tot = 0;
						int end_Tot = 0;
						while (line != null) {
							String[] line_data = line.split("\t");
							tot_SNP = tot_SNP + Integer.parseInt(line_data[2]);
							String HiC_block = line_data[1];

							String HiC_Start_block = HiC_block.substring(0, HiC_block.indexOf("|"));
							String[] Start_split = HiC_Start_block.split(";");
							int start_Diff = Integer.parseInt(Start_split[1]) - Integer.parseInt(Start_split[0]);
							start_Tot = start_Tot + start_Diff;

							String HiC_End_block = HiC_block.substring(HiC_block.indexOf("|") + 1, HiC_block.length());
							// System.out.println(HiC_End_block);
							String[] end_Split = HiC_End_block.split(";");
							int end_Diff = Integer.parseInt(end_Split[1]) - Integer.parseInt(end_Split[0]);
							end_Tot = end_Tot + end_Diff;
							line = br.readLine();
						}

						br.close();
						fr.close();

						int total_HiC_Len = start_Tot + end_Tot;

						String[] line_VCF_one = lines.get(1).split("\t");
						String[] line_VCF_last = lines.get(lines.size() - 1).split("\t");
						int total_Non_Len = Integer.parseInt(line_VCF_last[1]) - Integer.parseInt(line_VCF_one[1]);

						File density_Country = new File(
								current_Folder.getAbsolutePath() + "/summary/" + country + "_Density_Data.den");
						if (!density_Country.exists()) {
							density_Country.createNewFile();
							FileWriter fw = new FileWriter(density_Country);
							BufferedWriter bw = new BufferedWriter(fw);
							String header = "Chromosome\t" + "HiC_SNP_Number\t" + "Total_SNP_Number\t"
									+ "HiC_bp_Distance\t" + "Total_bp_Distance\t" + "Ratio_Snps\t" + "Ratio_Distance\n";
							bw.write(header);
							bw.flush();
							bw.close();
							fw.close();
						}

						Double snp_Ratio = ((double) tot_SNP / (double) snp_Total);
						Double distance_Ratio = ((double) total_HiC_Len / (double) total_Non_Len);
						String status = null;
						if (snp_Ratio > distance_Ratio) {
							status = "Pass";
						} else {
							status = "Fail";
						}

						FileWriter fw = new FileWriter(density_Country, true);
						BufferedWriter bw = new BufferedWriter(fw);
						String line_Write = df.format(c) + "\t" + tot_SNP + "\t" + snp_Total + "\t" + total_HiC_Len
								+ "\t" + total_Non_Len + "\t" + snp_Ratio + "\t" + distance_Ratio + "\t" + status
								+ "\n";
						bw.write(line_Write);
						bw.flush();
						bw.close();
						fw.close();

					}
				}
			}
		}

	}

	public void Printer(String[] x) {
		for (String data : x) {
			System.out.println(data);
		}
	}

}
