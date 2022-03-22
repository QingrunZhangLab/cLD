package linkage_d;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Christian_filter {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub

		// input_File MAF output_File

		String file_name = args[0];
		Double MAF = 0.00;
		MAF = Double.parseDouble(args[1]);
		File current = new File("");
		File vcf = new File(current.getAbsolutePath() + "/" + file_name);
		File output = new File(current.getAbsolutePath() + "/" + args[2]);

		if (output.exists()) {
			output.delete();
		}

		if (!output.exists()) {
			output.createNewFile();
		}
		System.out.println("Christian Filter");
		System.out.println("Processing:\t" + file_name);
		System.out.println("MAF cut off:\t" + MAF);

		FileReader fr = new FileReader(vcf);
		BufferedReader br = new BufferedReader(fr);

		FileWriter fw = new FileWriter(output, true);
		BufferedWriter bw = new BufferedWriter(fw);

		String line = br.readLine();

		while (line.startsWith("##")) {
			line = br.readLine();
		}

		System.out.println("Skipped Headers");

		String[] spliter = line.split("\t");

		for (int start = 0; start < spliter.length; start++) {
			bw.write(spliter[start] + "\t");
			if (start == 8) {
				bw.write("AF\t");
			}
		}

		bw.write("\n");
		// bw.write(line + "\n");

		System.out.println("Filtering MAF greater than " + MAF + "\n");

		line = br.readLine();
		while (line != null) {
			// System.out.println(line);
			spliter = line.split("\t");
			String ID = spliter[2];
			String[] REF = spliter[3].split(",");
			String[] ALT = spliter[4].split(",");

			if (REF.length == 1 && ALT.length == 1) {
				if (REF[0].length() == 1 && ALT[0].length() == 1) {
					String filter_Data = spliter[7];

					// System.out.println(filter_Data);
					String filter_Split[] = filter_Data.split(";");
					Double AF = 0.00;
					for (String check_AF : filter_Split) {
						if (check_AF.startsWith("AF=")) {
							String trim = check_AF.substring(check_AF.indexOf("=") + 1, check_AF.length());
							AF = Double.parseDouble(trim);
							// System.out.println(trim);
							break;
						}
					}

					if (AF > MAF) {
						System.out.println("Caught SNP:\t" + ID);

						String edit = spliter[0];

						for (int start = 1; start < 9; start++) {
							edit = edit + "\t" + spliter[start];
						}
						edit = edit + "\t" + AF + "\t";
						for (int c = 9; c < spliter.length; c++) {
							String checker = spliter[c];
							if (checker.equals("0|0")) {
								edit = edit + "0\t";
							} else if (checker.equals("0|1") || checker.equals("1|0")) {
								edit = edit + "1\t";
							} else if (checker.equals("1|1")) {
								edit = edit + "2\t";
							}
						}
						bw.write(edit + "\n");
					}
				}
			}
			line = br.readLine();
		}
		br.close();
		fr.close();
		bw.close();
		fw.close();
		System.out.println("\nSuccessful Filter");
	}

}
