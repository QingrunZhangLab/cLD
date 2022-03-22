package linkage_d;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class AVGDENS_outliers {

	File output_Folder;

	AVGDENS_outliers(File output_Folder) {
		this.output_Folder = output_Folder;
	}

	public void initiate() throws IOException {

		File outlier_Folder = new File(this.output_Folder.getAbsoluteFile() + "/outliers");

		if (!outlier_Folder.exists()) {
			outlier_Folder.mkdir();
		}

		File output = new File(this.output_Folder.getAbsoluteFile() + "/MAF");
		String[] files = output.list();

		for (String file : files) {
			String country = file.substring(0, file.indexOf("."));
			System.out.println("Processing\t: " + country);
			FileReader fr = new FileReader(new File(output.getAbsoluteFile() + "/" + file));
			BufferedReader br = new BufferedReader(fr);
			br.readLine();

			String line = br.readLine();
			while (line != null) {
				String[] line_Data = line.split("\t");

				String combo = line_Data[0];
				System.out.println("Combination\t: "+combo);

				Double MAF = Double.parseDouble(line_Data[1]);
				int snp_No = Integer.parseInt(line_Data[2]);
				Double avg_MAF = MAF / snp_No;

				int HiC_size = Integer.parseInt(line_Data[3]);
				Double density = ((double) snp_No / (double) HiC_size);

				File outlier = new File(outlier_Folder.getAbsoluteFile() + "/" + country + ".all");
				if (!outlier.exists()) {
					outlier.createNewFile();
					FileWriter fw = new FileWriter(outlier);
					BufferedWriter bw = new BufferedWriter(fw);
					String header = "Combination\tAvg_MAF\tSNP_density\n";
					bw.write(header);
					bw.close();
					fw.close();
				}
				
				FileWriter fw = new FileWriter(outlier,true);
				BufferedWriter bw = new BufferedWriter(fw);
				String liner = combo+"\t"+avg_MAF+"\t"+density+"\n";
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
