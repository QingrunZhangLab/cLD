package linkage_d;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Dinghao_PaPb {

	File input_Folder;
	File output_Folder;

	Dinghao_PaPb(File input_Folder, File output_Folder) {
		System.out.println("You have selected bespoke PaPb\n");
		this.input_Folder = input_Folder;
		File output_custom = new File(output_Folder.getAbsolutePath() + "/Dinghao_PaPb");
		if (!output_custom.exists()) {
			output_custom.mkdir();
		}
		this.output_Folder = output_custom;
	}

	public void initiate() throws IOException {
		// Get all vcf files and HiC file in input folder
		List<String> vcf_All = find_Files();
		File lines = new File(this.input_Folder.getAbsolutePath() + "/lines.txt");
		FileReader fr = new FileReader(lines);
		BufferedReader br = new BufferedReader(fr);

		Hashtable<String, Integer> linecount_Index = new Hashtable<String, Integer>();

		String line = br.readLine();
		while (line != null) {
			String split[] = line.split(" ");
			if (split.length > 5) {
				// System.out.println(split[4]+"\t"+split[5].substring(split[5].lastIndexOf("/")+1,split[5].length()));
				linecount_Index.put(split[5].substring(split[5].lastIndexOf("/") + 1, split[5].length()),
						Integer.parseInt(split[4]));
			}
			line = br.readLine();
		}

		br.close();
		fr.close();

		// System.out.println(linecount_Index);
		for (String vcf : vcf_All) {
			try {
				// Submit vcf file to be processed
				int line_Count = linecount_Index.get(vcf);
				System.out.println("Processing\t: " + vcf.substring(0, vcf.lastIndexOf(".")));
				System.out.println("VCF Lines\t: " + line_Count);
				vcf_Process(vcf, line_Count);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("Completed\t: " + vcf.substring(0, vcf.lastIndexOf(".")));
		}

	}

	public void vcf_Process(String vcf, int line_Count) throws IOException {
		System.out.println();
		ArrayList<String> combos = new ArrayList<String>();
		ArrayList<Integer> lines_Total = new ArrayList<Integer>();

		String display = vcf.substring(0, vcf.lastIndexOf("."));
		String country = display.substring(display.lastIndexOf("_") + 1, display.length());
		String chr_long = display.substring(display.indexOf("chr") + 3, display.length());
		DecimalFormat df = new DecimalFormat("00");
		String chr = df.format(Integer.parseInt((chr_long.substring(0, chr_long.indexOf(".")))));
		// System.out.println(chr);

		System.out.println("Result: " + country + "_" + chr);
		File output = new File(this.output_Folder.getAbsolutePath() + "/" + country + "_" + chr + ".DinghaoPaPb");
		if (!output.exists()) {
			output.createNewFile();
			FileWriter fw = new FileWriter(output);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(
					"Chromosome\tSNP_1_ID\tSNP_2_ID\tSNP_1_Allele_Frequencies\tSNP_2_Allele_Frequencies\tHaplotype_Frequencies\tD_Prime\tR_Squared\n");
			bw.close();
			fw.close();
		}

		System.out.println("\nDeVoe on site");
		int variations = 5000;
		while (combos.size() < variations) {
			int snp1 = (int) (Math.random() * (line_Count - 1)) + 1;
			int snp2 = (int) (Math.random() * (line_Count - 1)) + 1;

			if (snp1 < snp2) {
				String combo = snp1 + "_" + snp2;
				if (!combos.contains(combo)) {
					combos.add(combo);
				}
				if (!lines_Total.contains(snp1)) {
					lines_Total.add(snp1);
				}
				if (!lines_Total.contains(snp2)) {
					lines_Total.add(snp2);
				}
			}
		}
		Collections.sort(lines_Total);
		// System.out.println(lines_Total.size());
		// System.out.println(combos.size());

		/*
		 * for (int check : lines_Total) { System.out.println(check); }
		 */

		File vcf_Current = new File(input_Folder.getAbsolutePath() + "/" + vcf);
		FileReader fr = new FileReader(vcf_Current);
		BufferedReader br = new BufferedReader(fr);

		String line_vcf = br.readLine();
		Hashtable<Integer, String> collector = new Hashtable<Integer, String>();

		int read_Count = 0;
		System.out.println("Tivan is working");
		while (line_vcf != null) {

			if (lines_Total.contains(read_Count)) {
				collector.put(read_Count, line_vcf);
				if (collector.size() == lines_Total.size()) {
					// System.out.println("breaking");
					break;
				}
			}

			read_Count++;
			line_vcf = br.readLine();
		}

		System.out.println("Tivan found " + collector.size() + " of " + lines_Total.size());
		br.close();
		fr.close();

		ExecutorService ex = Executors.newFixedThreadPool(10);
		Store_lines[] lines_storage = new Store_lines[variations];
		System.out.println("\nBraniac on site");
		System.out.println("Activating the 12th level");
		// int c = 0;
		for (int c = 0; c < variations; c++) {
			lines_storage[c] = new Store_lines();
			//System.out.println("Processing line\t: " + combos.get(c));
			String extract[] = combos.get(c).split("_");
			String snp1 = collector.get(Integer.parseInt(extract[0]));
			String snp2 = collector.get(Integer.parseInt(extract[1]));
			// System.out.println(snp1);
			// System.out.println(snp2);

			ex.execute(new PaPb_Calc(snp1, snp2, lines_storage[c]));

		}

		ex.shutdown();
		while (!ex.isTerminated()) {
		}
		System.out.println("Concluded the 12th level");
		// System.out.println(lines_storage[c].lines_Store.get(c));

		FileWriter fw = new FileWriter(output, true);
		BufferedWriter bw = new BufferedWriter(fw);
		// bw.write("SNP_1_ID\tSNP_2_ID\tSNP_1_Allele_Frequencies\tSNP_2_Allele_Frequencies\tHaplotype_Frequencies\tD_Prime\tR_Squared\n");
		System.out.println("\nWriting to file " + output.getName());
		for (int c = 0; c < variations; c++) {
			bw.write(chr + "\t" + lines_storage[c].lines_Store.get(0));
			// System.out.println(lines_storage[c].lines_Store.get(0));
		}

		bw.close();
		fw.close();

		// System.out.println(lines_Total.get(0));
		// System.out.println(collector.get(lines_Total.get(0)));
		System.out.println();
	}

	public List find_Files() {
		String[] files = input_Folder.list();
		List<String> vcf_All = new ArrayList<String>();

		for (String file : files) {
			String extension = file.substring(file.lastIndexOf("."), file.length());
			// if (extension.equals(".txt")) {
			// Get the hiC file
			// String pathname = input_Folder.getAbsolutePath() + "/" + file;
			// this.HiC = new File(pathname);
			// }
			if (extension.equals(".vcf")) {
				// Get all vcf files
				vcf_All.add(file);
			}
		}

		Collections.sort(vcf_All);
		return vcf_All;
	}

}

class PaPb_Calc implements Runnable {
	/*
	 * Multithread class for MAMANGING LD calculations Actual LD calculation
	 * performed by Threadscape_5
	 */
	String block_1;
	String block_2;
	Store_lines lines_Storage;

	PaPb_Calc(String block_1, String block_2, Store_lines lines_Storage) {
		this.block_1 = block_1;
		this.block_2 = block_2;
		this.lines_Storage = lines_Storage;
	}

	public void run() {
		int line = 0;
		/*
		 * Each configuration is calculated in separate threads Essentially the thread
		 * class spawns more threads :D
		 */

		ExecutorService ex = Executors.newFixedThreadPool(10);
		String pathname = "";
		File yield = new File(pathname);

		String[] snp_1 = block_1.split("\t");
		String[] snp_2 = block_2.split("\t");

		ex.execute(new Threadscape_PaPb(lines_Storage, yield, snp_1, snp_2, line));
		// line++;

		ex.shutdown();
		while (!ex.isTerminated()) {
		}

	}
}

class Threadscape_PaPb implements Runnable {
	// CLASS calculates the LD
	Store_lines lines_Storage;
	File yield;
	String[] line_snp_1;
	String[] line_snp_2;
	int line;

	Threadscape_PaPb(Store_lines lines_Storage, File yield, String[] snp1, String[] snp2, int line) {
		this.lines_Storage = lines_Storage;
		this.yield = yield;
		this.line_snp_1 = snp1;
		this.line_snp_2 = snp2;
		this.line = line;
	}

	public void run() {

		Super_pop GBR = new Super_pop(yield, line_snp_1, line_snp_2);

		try {
			// SNP combination sumbitted to be calculated
			String write;
			write = GBR.incite_2();
			// Store the LD data
			lines_Storage.put(line, write);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
