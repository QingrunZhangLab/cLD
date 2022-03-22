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
import java.util.List;
import java.util.Set;

public class Collect_qing {

	public void initiate() throws IOException {

		int[] chromosomes = { 1, 2, 3, 4, 5, 6, 7, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 22 };

		File current = new File("");
		DecimalFormat df = new DecimalFormat("00");

		for (int chr : chromosomes) {
			String chrom = df.format(chr);
			File main_Folder = new File(current.getAbsolutePath() + "/" + chrom + "/output/Leah_3");
			File sub_Folder = new File(main_Folder.getAbsolutePath() + "/Combo_Details_1000_1");

			if (main_Folder.exists()) {
				System.out.println("Processing Chromosome\t: " + chrom);
				String[] all_Files = main_Folder.list();
				String[] all_Files_Detailed = sub_Folder.list();
				Hashtable<String, String> files_1000 = new Hashtable<String, String>();
				Hashtable<String, String> files_1000_Detailed = new Hashtable<String, String>();

				List<String> countries = new ArrayList<String>();

				for (String file : all_Files) {
					if (file.contains(".Leah")) {
						String country = file.substring(0, 3);
						files_1000.put(country, file);
						countries.add(country);
					}
				}
				System.out.println("\nIndexed main folder");

				for (String file : all_Files_Detailed) {
					if (file.contains(".summary")) {
						String country = file.substring(0, 3);
						files_1000_Detailed.put(country, file);
					}
				}
				System.out.println("Indexed sub folder");

				Collections.sort(countries);

				Hashtable<String, String> AFR = new Hashtable<String, String>();
				Hashtable<String, String> AMR = new Hashtable<String, String>();
				Hashtable<String, String> EAS = new Hashtable<String, String>();
				Hashtable<String, String> EUR = new Hashtable<String, String>();
				Hashtable<String, String> SAS = new Hashtable<String, String>();

				List<String> range_List = new ArrayList<String>();

				System.out.println("Collecting Data");
				for (String country : countries) {
					// System.out.println(country);
					File main_File = new File(main_Folder.getAbsolutePath() + "/" + files_1000.get(country));
					File sub_File = new File(sub_Folder.getAbsolutePath() + "/" + files_1000_Detailed.get(country));

					FileReader mFile = new FileReader(main_File);
					BufferedReader brMFile = new BufferedReader(mFile);

					String line = brMFile.readLine();

					// System.out.println(line);

					int HiC_Range = 1;
					int HiC_dprime = 3;
					int HiC_Rsquared = 5;
					int non_HiC_dprime = 4;
					int non_HiC_Rsquared = 6;

					line = brMFile.readLine();

					while (line != null) {
						String[] line_Data = line.split("\t");

						String HiC_Rg = line_Data[HiC_Range].replace("|", "_");
						String[] HiC = HiC_Rg.split("_");

						String Range = chr + ";" + HiC[0] + "_" + chr + ";" + HiC[1];
						String HiC_D = line_Data[HiC_dprime];
						String HiC_R = line_Data[HiC_Rsquared];

						// System.out.println(Range);

						FileReader sFile = new FileReader(sub_File);
						BufferedReader brSubFile = new BufferedReader(sFile);

						String sub_Line = brSubFile.readLine();

						int nDprime_start = -1;
						int nDprime_end = -1;

						int nRsquare_start = -1;
						int nRsquare_end = -1;

						String[] sub_Data_header = sub_Line.split("\t");

						for (int c = 0; c < sub_Data_header.length; c++) {
							if (sub_Data_header[c].equals("Non_HiC_D_Prime_01")) {
								nDprime_start = c;
							}

							if (sub_Data_header[c].equals("Non_HiC_D_Prime_1000")) {
								nDprime_end = c;
							}

							if (sub_Data_header[c].equals("Non_HiC_R_Squared_01")) {
								nRsquare_start = c;
							}

							if (sub_Data_header[c].equals("Non_HiC_R_Squared_1000")) {
								nRsquare_end = c;
							}

						}
						// System.out.println(sub_Data_header[nDprime_start]+"\t"+sub_Data_header[nDprime_end]);
						// System.out.println(sub_Data_header[nRsquare_start]+"\t"+sub_Data_header[nRsquare_end]);

						sub_Line = brSubFile.readLine();

						while (sub_Line != null) {
							String[] sub_Data = sub_Line.split("\t");

							if (sub_Data[1].equals(line_Data[HiC_Range])) {
								// System.out.println(Range);
								List<Double> Dprime_All = new ArrayList<Double>();
								List<Double> RSquared_All = new ArrayList<Double>();

								for (int d = nDprime_start; d <= nDprime_end; d++) {
									Dprime_All.add(Double.parseDouble(sub_Data[d]));
								}

								for (int r = nRsquare_start; r <= nRsquare_end; r++) {
									RSquared_All.add(Double.parseDouble(sub_Data[r]));
								}

								Dprime_All.add(Double.parseDouble(HiC_D));
								RSquared_All.add(Double.parseDouble(HiC_R));

								Collections.sort(Dprime_All);
								Collections.sort(RSquared_All);

								int count_D = 0;
								while (Double.parseDouble(HiC_D) != Dprime_All.get(count_D)) {
									count_D++;
								}

								int D_pos = count_D + 1;
								Double D_quantile = (double) ((double) count_D / 1001) * 100;
								// System.out.println("D\t: " + D_quantile + "%");

								int count_R = 0;
								while (Double.parseDouble(HiC_R) != RSquared_All.get(count_R)) {
									count_R++;
								}

								int R_pos = count_R + 1;
								Double R_quantile = (double) ((double) count_R / 1001) * 100;
								// System.out.println("R\t: " + R_quantile + "%");

								if (!range_List.contains(Range)) {
									range_List.add(Range);
								}

								String write_Line = HiC_D + "\t" + line_Data[non_HiC_dprime] + "\t" + D_quantile + "\t"
										+ HiC_R + "\t" + line_Data[non_HiC_Rsquared] + "\t" + R_quantile;

								// System.out.println(country);

								if (country.equals("AFR")) {
									AFR.put(Range, write_Line);
								} else if (country.equals("AMR")) {
									AMR.put(Range, write_Line);
								} else if (country.equals("EAS")) {
									EAS.put(Range, write_Line);
								} else if (country.equals("EUR")) {
									EUR.put(Range, write_Line);
								} else if (country.equals("SAS")) {
									SAS.put(Range, write_Line);
								}

							}

							sub_Line = brSubFile.readLine();
						}

						brSubFile.close();
						sFile.close();

						line = brMFile.readLine();
					}

					brMFile.close();
					mFile.close();

				}

				// System.out.println(range_List);
				// System.out.println(AFR);

				File result = new File(current.getAbsolutePath() + "/Summary_Leah_3.csv");
				System.out.println("Writing to Summary File");
				if (!result.exists()) {
					FileWriter fw = new FileWriter(result, true);
					BufferedWriter bw = new BufferedWriter(fw);
					String header = "HiC_Range\t";
					for (String country : countries) {
						header = header + country + "_HiC_D_prime" + "\t" + country + "_Non_HiC_AVG_D_prime" + "\t"
								+ country + "_D_prime_Percentile_Rank" + "\t" + country + "_HiC_R_Squared" + "\t"
								+ country + "_Non_HiC_AVG_R_Squared\t" + country + "_R_squared_Percentile_Rank\t";
					}
					bw.write(header + "\n");
					bw.flush();
					bw.close();
					fw.close();
				}

				for (String range : range_List) {
					FileWriter fw = new FileWriter(result, true);
					BufferedWriter bw = new BufferedWriter(fw);

					// System.out.println(range);
					bw.write(range + "\t");
					bw.write(AFR.get(range) + "\t");
					bw.write(AMR.get(range) + "\t");
					bw.write(EAS.get(range) + "\t");
					bw.write(EUR.get(range) + "\t");
					bw.write(SAS.get(range) + "\n");

					bw.flush();
					bw.close();
					fw.close();

				}

			} else {
				System.out.println("\nChromosome " + chrom + " Does not exist");
			}
		}

		System.out.println("\n RUN COMPLETE");

	}

}
