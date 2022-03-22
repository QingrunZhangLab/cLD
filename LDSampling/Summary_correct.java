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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Summary_correct {

	public void initiate() throws IOException {

		File current = new File("");
		File current_Folder = new File(current.getAbsolutePath());
		DecimalFormat df = new DecimalFormat("00");

		for (int c = 1; c <= 22; c++) {
			System.out.println("*****************************************************************");
			System.out.println("Processing\t: " + df.format(c));
			String current_Chromosome = current_Folder.getAbsolutePath() + "/" + df.format(c);
			File input = new File(current_Chromosome + "/input");
			if (input.exists()) {
				String[] file_List = input.list();
				List<String> vcf_List = new ArrayList<String>();
				String HiC_pathname = null;

				for (String file : file_List) {
					if (file.contains("vcf")) {
						vcf_List.add(file);
					}
					if (file.contains("txt")) {
						HiC_pathname = input.getAbsolutePath() + "/" + file;
					}
				}

				File HiC = new File(HiC_pathname);
				for (String file : vcf_List) {
					System.out.println("*****************************************************************");
					String country = file.substring(file.lastIndexOf("_") + 1, file.lastIndexOf("."));
					System.out.println("Processing\t: " + country);
					List<String> lines = Files.readAllLines(Paths.get(input.getAbsolutePath() + "/" + file));

					FileReader fr = new FileReader(HiC);
					BufferedReader hiC_br = new BufferedReader(fr);
					Line_catcher lines_Caught = new Line_catcher();
					Line_catcher temp_Lines = new Line_catcher();

					String combination = hiC_br.readLine();
					int tot_HiC_SNP = 0;
					int HiC_bp_Coverage = 0;
					while (combination != null) {
						String[] split = combination.split("\t");
						String combo_Num = split[0];
						System.out.println("\nProcessing Combination \t: " + combo_Num);

						String hiC_start_Block = split[1];
						String hiC_end_Block = split[2];

						ExecutorService ex = Executors.newFixedThreadPool(23);
						// Get data of HiC start and end blocks
						String hic_start_Block = hiC_start_Block.substring(hiC_start_Block.indexOf(";") + 1,
								hiC_start_Block.length());
						String hic_end_Block = hiC_end_Block.substring(hiC_end_Block.indexOf(";") + 1,
								hiC_end_Block.length());

						String[] start_Size = hic_start_Block.split(";");
						String[] end_Size = hic_end_Block.split(";");

						int start = Integer.parseInt(start_Size[1]) - Integer.parseInt(start_Size[0]);
						int end = Integer.parseInt(end_Size[1]) - Integer.parseInt(end_Size[0]);

						int tot = start + end;

						Range line_Collect_hiC_start_Block = new Range();
						Range line_Collect_hiC_end_Block = new Range();

						System.out.println("Collecting SNP Data");

						ex.execute(new Thread_collect(lines, line_Collect_hiC_start_Block, hic_start_Block,
								lines_Caught, temp_Lines));
						ex.execute(new Thread_collect(lines, line_Collect_hiC_end_Block, hic_end_Block, lines_Caught,
								temp_Lines));

						ex.shutdown();
						while (!ex.isTerminated()) {
						}

						for (int pos : temp_Lines.Positions) {
							lines_Caught.Positions.add(pos);
							lines_Caught.set(pos, temp_Lines.positions_lineNum.get(pos));
						}

						Collections.sort(lines_Caught.Positions);
						temp_Lines.Positions.clear();
						temp_Lines.positions_lineNum.clear();

						tot_HiC_SNP = tot_HiC_SNP + (line_Collect_hiC_start_Block.lines.size())
								+ (line_Collect_hiC_end_Block.lines.size());

						HiC_bp_Coverage = HiC_bp_Coverage + tot;

						System.out.println("Total HiC SNP Count\t: " + tot_HiC_SNP);
						System.out.println("Total HiC bp Count\t: " + HiC_bp_Coverage);

						combination = hiC_br.readLine();
					}
					
					hiC_br.close();
					fr.close();
					
					String[] line_VCF_one = lines.get(1).split("\t");
					String[] line_VCF_last = lines.get(lines.size() - 1).split("\t");
					int total_Non_Len = Integer.parseInt(line_VCF_last[1]) - Integer.parseInt(line_VCF_one[1]);
					// SNP number total
					int snp_All_Total = lines.size() - 1;
					
					File density_Country = new File(
							current_Folder.getAbsolutePath() + "/summary/" + country + "_Density_Data.den");
					if (!density_Country.exists()) {
						density_Country.createNewFile();
						FileWriter fw = new FileWriter(density_Country);
						BufferedWriter bw = new BufferedWriter(fw);
						String header = "Chromosome\t" + "HiC_SNP_Number\t" + "Total_SNP_Number\t"
								+ "HiC_bp_Distance\t" + "Total_bp_Distance\t" + "Ratio_Snps\t" + "Ratio_Distance\t" + "Check\n";
						bw.write(header);
						bw.flush();
						bw.close();
						fw.close();
					}
					
					Double snp_Ratio = ((double) tot_HiC_SNP / (double) snp_All_Total);
					Double distance_Ratio = ((double) HiC_bp_Coverage / (double) total_Non_Len);
					String status = null;
					if (snp_Ratio > distance_Ratio) {
						status = "Pass";
					} else {
						status = "Fail";
					}
					
					FileWriter fw = new FileWriter(density_Country, true);
					BufferedWriter bw = new BufferedWriter(fw);
					String line_Write = df.format(c) + "\t" + tot_HiC_SNP + "\t" + snp_All_Total + "\t" + HiC_bp_Coverage
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

	public void Printer(String[] x) {
		for (String data : x) {
			System.out.println(data);
		}
	}

}
