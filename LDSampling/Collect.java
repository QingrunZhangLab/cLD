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

public class Collect {

	public static void main(String args[]) throws IOException {

		DecimalFormat df = new DecimalFormat("00");
		File current = new File("");
		File summary_Folder = new File(current.getAbsolutePath() + "/summary");
		summary_Folder.mkdir();

		for (int c = 1; c <= 22; c++) {
			String pathname = df.format(c) + "/output";
			File folder_Current = new File(pathname);
			System.out.println("Processing\t: " + df.format(c));
			if (folder_Current.exists()) {
				for (String file : folder_Current.list()) {
					if (file.contains(".hicld2")) {
						String country = file.substring(file.lastIndexOf("_") + 1, file.lastIndexOf("."));
						System.out.println("Country\t: " + country);
						File hicld = new File(folder_Current.getAbsolutePath() + "/" + file);

						List<Integer> distance = new ArrayList<Integer>();
						Hashtable<Integer, Integer> count = new Hashtable<Integer, Integer>();
						Hashtable<Integer, Double> HiC_Dprime = new Hashtable<Integer, Double>();
						Hashtable<Integer, Double> Non_Dprime = new Hashtable<Integer, Double>();
						Hashtable<Integer, Double> HiC_Rsquare = new Hashtable<Integer, Double>();
						Hashtable<Integer, Double> Non_Rsquare = new Hashtable<Integer, Double>();

						BufferedReader br = new BufferedReader(new FileReader(hicld));
						br.readLine();
						String line = br.readLine();
						while (line != null) {
							String[] line_Data = line.split("\t");
							if (!line_Data[3].equals("NaN") && !line_Data[5].equals("NaN") && !line_Data[4].equals("NaN") && !line_Data[6].equals("NaN")) {
								int dist = Integer.parseInt(line_Data[2]);
								System.out.print("Combination\t: " + line_Data[0] + "\r");
								if (!distance.contains(dist)) {
									distance.add(dist);
									count.put(dist, 1);
									HiC_Dprime.put(dist, Double.parseDouble(line_Data[3]));
									Non_Dprime.put(dist, Double.parseDouble(line_Data[4]));
									HiC_Rsquare.put(dist, Double.parseDouble(line_Data[5]));
									Non_Rsquare.put(dist, Double.parseDouble(line_Data[6]));
								} else {
									Double avg_HiC_D_Prime = Double.parseDouble(line_Data[3]);
									Double avg_Non_Dprime = Double.parseDouble(line_Data[4]);
									Double avg_HiC_Rsquare = Double.parseDouble(line_Data[5]);
									Double avg_Non_Rsquare = Double.parseDouble(line_Data[6]);

									int counter = count.get(dist);
									counter = counter + 1;
									count.replace(dist, counter);

									Double curr = HiC_Dprime.get(dist);
									curr = curr + avg_HiC_D_Prime;
									HiC_Dprime.replace(dist, curr);

									curr = Non_Dprime.get(dist);
									curr = curr + avg_Non_Dprime;
									Non_Dprime.replace(dist, curr);

									curr = HiC_Rsquare.get(dist);
									curr = curr + avg_HiC_Rsquare;
									HiC_Rsquare.replace(dist, curr);

									curr = Non_Rsquare.get(dist);
									curr = curr + avg_Non_Rsquare;
									Non_Rsquare.replace(dist, curr);

								}
							}
							line = br.readLine();
						}
						Collections.sort(distance);
						File country_Summary = new File(summary_Folder.getAbsolutePath() + "/" + country + ".SumHiC2");
						if (!country_Summary.exists()) {
							country_Summary.createNewFile();
							FileWriter fw = new FileWriter(country_Summary, true);
							BufferedWriter bw = new BufferedWriter(fw);
							bw.write("Chromosome" + "\tRange" + "\tType" + "\tD_prime" + "\tR_Squared\n");
							bw.flush();
							bw.close();
							fw.close();
						}
						System.out.println("\nWriting Data");
						FileWriter fw = new FileWriter(country_Summary, true);
						BufferedWriter bw = new BufferedWriter(fw);
						for (int curr_Dist : distance) {
							bw.write(df.format(c) + "\t");
							bw.write(curr_Dist + "\t");
							bw.write("HiC\t");

							Double avg_D_Prime_HiC = HiC_Dprime.get(curr_Dist) / count.get(curr_Dist);
							Double avg_R_Squared_HiC = HiC_Rsquare.get(curr_Dist) / count.get(curr_Dist);
							bw.write(avg_D_Prime_HiC + "\t");
							bw.write(avg_R_Squared_HiC + "\n");

							bw.write(df.format(c) + "\t");
							bw.write(curr_Dist + "\t");
							bw.write("Non_HiC\t");
							Double avg_D_Prime_Non = Non_Dprime.get(curr_Dist) / count.get(curr_Dist);
							Double avg_R_Squared_Non = Non_Rsquare.get(curr_Dist) / count.get(curr_Dist);
							bw.write(avg_D_Prime_Non + "\t");
							bw.write(avg_R_Squared_Non + "\n");
							bw.flush();
						}
						bw.close();
						fw.close();

					}
				}
			}
		}

	}

}
