package linkage_d;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;

public class Hi_C {

	File hic_Folder;
	File output_Folder;

	Hi_C(File input, File output) {
		this.output_Folder = output;
		this.hic_Folder = new File(input.getAbsolutePath() + "/hic");
		if (!this.hic_Folder.exists()) {
			System.out.println("ERROR: Hi-C Folder does not exist");
			System.exit(0);
		}
	}

	public void initiate() throws IOException {

		DecimalFormat df = new DecimalFormat("00");
		String[] files = this.hic_Folder.list();
		String name = "GTEx_f20k_MSSNG_overlap_ADD.txt";

		for (String file : files) {
			if (file.substring(file.lastIndexOf("."), file.length()).equals(".txt")) {
				System.out.println(file);
				File current = new File(this.hic_Folder.getAbsolutePath() + "/" + file);
				File output_Folder = new File(this.output_Folder.getAbsolutePath() + "/" + file);
				output_Folder.mkdir();

				BufferedReader line_Read = new BufferedReader(new FileReader(current));
				String line = line_Read.readLine();

				while (line != null) {
					String[] line_Data = line.split("_");
					String[] col_Data = line_Data[1].split(":");

					int chr = Integer.parseInt(col_Data[0]);
					File chr_Output = new File(output_Folder.getAbsolutePath() + "/" + df.format(chr));
					if (!chr_Output.exists()) {
						chr_Output.mkdir();
					}

					String pathname = chr_Output.getAbsolutePath() + "/" + file.substring(0, file.lastIndexOf("."))
							+ "_" + df.format(chr) + ".txt";
					File filter_Output = new File(pathname);
					if (!filter_Output.exists()) {
						filter_Output.createNewFile();
					}

					FileWriter write = new FileWriter(filter_Output, true);
					BufferedWriter line_Writer = new BufferedWriter(write);
					String replacer = line.replace("_", "\t");
					replacer = replacer.replace(":", ";");
					String new_Line = line + "\t" + replacer;
					line_Writer.write(new_Line + "\n");
					line_Writer.flush();
					line_Writer.close();
					line = line_Read.readLine();
				}

				line_Read.close();

			}
		}

		System.out.println("DONE");

	}

}
