package linkage_d;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;

public class Collect_cmaf {

	public static void main(String[] args) throws IOException {
		File folder = new File("");
		File current_Folder = new File(folder.getAbsolutePath());
		DecimalFormat df = new DecimalFormat("00");
		
		File result_Folder = new File(current_Folder.getAbsolutePath()+"/results");
		if(!result_Folder.exists()) {
			result_Folder.mkdir();
		}

		for (int c = 1; c <= 22; c++) {
			String chr = df.format(c);
			System.out.println("Processing\t: " + chr);
			File chr_Folder = new File(current_Folder + "/" + chr + "/output");
			if (chr_Folder.exists()) {
				String[] list = chr_Folder.list();
				for (String file : list) {
					if(file.contains(".result")) {
						String country = file.substring(0, file.lastIndexOf("."));
						System.out.println("Country\t: "+country);
						File summary = new File (result_Folder.getAbsolutePath()+"/"+country+".summary");
						if(!summary.exists()) {
							FileWriter fw = new FileWriter(summary);
							BufferedWriter bw = new BufferedWriter(fw);
							bw.write("Chromosome\tAverage_HiC_MAF\tTotal_Average_MAF\tHiC_Denstiy\tTotal_Density\n");
							bw.close();
							fw.close();
						}
						FileReader fr = new FileReader(new File(chr_Folder.getAbsolutePath()+"/"+file));
						BufferedReader br = new BufferedReader(fr);
						br.readLine();
						String line = br.readLine();
						while(line!=null) {
							String [] line_data = line.split("\t");
							FileWriter fw = new FileWriter(summary,true);
							BufferedWriter bw = new BufferedWriter(fw);
							Double Hic_Density=Double.parseDouble(line_data[0])/Double.parseDouble(line_data[6]);
							Double Tot_Density=Double.parseDouble(line_data[2])/Double.parseDouble(line_data[7]);
							String data = chr + "\t"+line_data[4]+ "\t"+ line_data[5]+ "\t"+ Hic_Density+ "\t"+ Tot_Density+"\n";
							bw.write(data);
							bw.close();
							fw.close();
							line = br.readLine();
						}
						br.close();
						fr.close();

					}

				}

			}else {
				System.out.println("Chromosome does not exist");
			}
		}

	}

}
