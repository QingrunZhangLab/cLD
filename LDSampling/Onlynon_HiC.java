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
import java.util.Hashtable;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Onlynon_HiC {

	File input_Folder;
	File output_Folder;
	File HiC;

	Onlynon_HiC(File input_Folder, File output_Folder, String HiC_file) {
		System.out.println("Non Interaction Random Sampler");
		this.input_Folder = input_Folder;
		this.output_Folder = output_Folder;
		String pathname = input_Folder.getAbsolutePath() + "/" + HiC_file;
		this.HiC = new File(pathname);

	}

	public void initiate() {
		// Get all vcf files and HiC file in input folder
		List<String> vcf_All = find_Files();
		for (String vcf : vcf_All) {
			System.out.println("Processing\t: " + vcf.substring(0, vcf.lastIndexOf(".")));
			try {
				// Submit vcf file to be processed
				vcf_Process(vcf);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("Completed\t: " + vcf.substring(0, vcf.lastIndexOf(".")));
		}

	}

	public void vcf_Process(String vcf) throws IOException {
		// Load vcf file into RAM to enable multithreading
		String country = vcf.substring(vcf.lastIndexOf("_") + 1, vcf.lastIndexOf("."));
		File vcf_Current = new File(input_Folder.getAbsolutePath() + "/" + vcf);
		List<String> lines = Files.readAllLines(Paths.get(vcf_Current.getAbsolutePath()));

		// Discard first line of VCF for its a header (NOTE these are CUSTOM
		// pre-processed VCF files)
		String[] line_One = lines.get(1).split("\t");
		String[] line_last = lines.get(lines.size() - 1).split("\t");

		// Get the position limits of the vcf file to prevent over stepping in the
		// selection of random non HiC blocks
		int min_Point = Integer.parseInt(line_One[1]);
		int max_Point = Integer.parseInt(line_last[1]);

		// Read the HiC data file
		FileReader fr = new FileReader(this.HiC);
		BufferedReader hiC_br = new BufferedReader(fr);

		// Initiate Line_Catcher class targeted to index the vcf file in real-time
		Line_catcher lines_Caught = new Line_catcher();
		// Initiate TEMPORARY Line_Catcher class to make the code Thread SAFE, used to
		// index the vcf file
		Line_catcher temp_Lines = new Line_catcher();

		// Read the HiC file and get the Combination
		String combination = hiC_br.readLine();

		// Create the MAIN output file

		File output_Ding = new File(this.output_Folder.getAbsolutePath() + "/Dinghao_nonHiC");
		File output_country = new File(output_Ding.getAbsolutePath() + "/" + country);

		if (!output_Ding.exists()) {
			output_Ding.mkdir();
		}

		if (!output_country.exists()) {
			output_country.mkdir();
		}

		String pathname = output_country.getAbsolutePath() + "/" + country + "_"
				+ this.HiC.getName().substring(0, this.HiC.getName().lastIndexOf(".")) + ".Dinghao_nonHiC";
		System.out.println(pathname);
		File summary = new File(pathname);
		if (!summary.exists()) {
			FileWriter fw = new FileWriter(summary, true);
			BufferedWriter bw = new BufferedWriter(fw);
			String header = "Combination\tHiC_Range\tDistance\tNon_HiC_AVG_D_prime\tNon_HiC_AVG_R_Squared\n";
			bw.write(header);
			bw.close();
			fw.close();
		}

		/*
		 * Store the detailed data on the calculations including the: HiC SNP number,
		 * ,Non HiC range and Individual regions D prime and R squared
		 */
		File output_Combos_pre = new File(output_country.getAbsolutePath() + "/Combo_1000_Dinghao");
		output_Combos_pre.mkdir();
		File HiC_Non_HiC_Full = new File(output_Combos_pre.getAbsolutePath() + "/" + country + "_"
				+ this.HiC.getName().substring(0, this.HiC.getName().lastIndexOf(".")) + ".Summary_nonHiC");

		if (!HiC_Non_HiC_Full.exists()) {
			FileWriter fw = new FileWriter(HiC_Non_HiC_Full, true);
			BufferedWriter bw = new BufferedWriter(fw);
			String header = "Combination\tHiC_Range\t";
			DecimalFormat df = new DecimalFormat("00");
			for (int c = 0; c < 1000; c++) {
				header = header + "Non_HiC_Range_" + df.format(c + 1) + "\t";
			}
			for (int c = 0; c < 1000; c++) {
				header = header + "Non_HiC_SNP_Configs_" + df.format(c + 1) + "\t";
			}
			for (int c = 0; c < 1000; c++) {
				header = header + "Non_HiC_D_Prime_" + df.format(c + 1) + "\t";
			}
			for (int c = 0; c < 1000; c++) {
				header = header + "Non_HiC_R_Squared_" + df.format(c + 1) + "\t";
			}
			header = header + "\n";
			bw.write(header);
			bw.close();
			fw.close();
		}

		// List for collecting the non_HiC_regions to prevent the same blocks from being
		// used
		List<String> non_HIC_Collection = new ArrayList<String>();
		// Start processing the HiC combination
		int activate_Coll = 1;

		while (combination != null) {
			// The HiC file consists of three columns: The combination number, Block 1
			// (start block) Parameters and Block 2 (end block) Parameters
			String[] split = combination.split("\t");
			String combo_Num = split[0];
			System.out.println("\nProcessing Combination \t: " + combo_Num);

			// Get the start and end block parameters
			String hiC_start_Block = split[1];
			String hiC_end_Block = split[2];

			// Generate the RANDOM non HiC blocks combinations that meet the parameters of
			// the HiC combination being processed
			String[] non_hic_Blocks = non_HiC_Range(hiC_start_Block, hiC_end_Block, min_Point, max_Point,
					non_HIC_Collection);
			String[] non_HiC_start_Blocks = new String[non_hic_Blocks.length];
			String[] non_HiC_end_Blocks = new String[non_hic_Blocks.length];

			for (int c = 0; c < non_hic_Blocks.length; c++) {
				// Tracking the used non HiC block configurations to
				// prevent redundancy
				// non_HIC_Collection.add(non_hic_Blocks[c]);
				// Extraction of the data on non HiC blocks
				// System.out.println(c+"\t"+non_hic_Blocks[c]);
				String[] split_non_HiC = non_hic_Blocks[c].split("\t");
				non_HiC_start_Blocks[c] = split_non_HiC[0];
				non_HiC_end_Blocks[c] = split_non_HiC[1];
			}

			Range[] line_Collect_Non_hiC_start_Block = new Range[non_hic_Blocks.length];
			Range[] line_Collect_Non_hiC_end_Block = new Range[non_hic_Blocks.length];
			ExecutorService ex = Executors.newFixedThreadPool(10);
			System.out.println("\nCollecting SNP Data");
			int limit = 100;
			// Threads to collect snps from non HiC regions
			System.out.println("First " + limit + " analysis");
			for (int c = 0; c < limit; c++) {
				// Initiate the class arrays
				line_Collect_Non_hiC_start_Block[c] = new Range();
				line_Collect_Non_hiC_end_Block[c] = new Range();
				ex.execute(new ONOFF_collect(lines, line_Collect_Non_hiC_start_Block[c], non_HiC_start_Blocks[c],
						lines_Caught, temp_Lines, activate_Coll));
				ex.execute(new ONOFF_collect(lines, line_Collect_Non_hiC_end_Block[c], non_HiC_end_Blocks[c],
						lines_Caught, temp_Lines, activate_Coll));
			}

			// WAIT till the threads finish
			ex.shutdown();
			while (!ex.isTerminated()) {
			}

			if (activate_Coll == 1) {
				System.out.println("Indexing file");
				// Complete the indexing of vcf files by permanent storage
				// THE INDEXING AND EXTRACTION STEPS IN THE PROCESS ARE DONE SEPERATELY TO
				// PREVENT THREAD CRASH

				for (int pos : temp_Lines.Positions) {
					lines_Caught.Positions.add(pos);
					lines_Caught.set(pos, temp_Lines.positions_lineNum.get(pos));
				}
				System.out.println("Points caught: " + temp_Lines.Positions.size());
				// Once added the List is sorted and temp storage is cleaned up to prevent
				// redundancy
				Collections.sort(lines_Caught.Positions);
				temp_Lines.Positions.clear();
				temp_Lines.positions_lineNum.clear();
			}

			ExecutorService ex_remain = Executors.newFixedThreadPool(10);
			System.out.println("Remainder analysis");
			for (int c = limit; c < non_hic_Blocks.length; c++) {
				// Initiate the class arrays
				line_Collect_Non_hiC_start_Block[c] = new Range();
				line_Collect_Non_hiC_end_Block[c] = new Range();
				ex_remain.execute(new ONOFF_collect(lines, line_Collect_Non_hiC_start_Block[c], non_HiC_start_Blocks[c],
						lines_Caught, temp_Lines, activate_Coll));
				ex_remain.execute(new ONOFF_collect(lines, line_Collect_Non_hiC_end_Block[c], non_HiC_end_Blocks[c],
						lines_Caught, temp_Lines, activate_Coll));
			}

			// WAIT till the threads finish
			ex_remain.shutdown();
			while (!ex_remain.isTerminated()) {
			}

			if (activate_Coll == 1) {
				System.out.println("Indexing file");
				// Complete the indexing of vcf files by permanent storage
				// THE INDEXING AND EXTRACTION STEPS IN THE PROCESS ARE DONE SEPERATELY TO
				// PREVENT THREAD CRASH

				for (int pos : temp_Lines.Positions) {
					lines_Caught.Positions.add(pos);
					lines_Caught.set(pos, temp_Lines.positions_lineNum.get(pos));
				}
				System.out.println("Points caught: " + temp_Lines.Positions.size());
				// Once added the List is sorted and temp storage is cleaned up to prevent
				// redundancy
				Collections.sort(lines_Caught.Positions);
				temp_Lines.Positions.clear();
				temp_Lines.positions_lineNum.clear();

				System.out.println("Deactivating indexer");
				activate_Coll = 0;
			}

			// Check if any of the Non HiC blocks are empty
			int non_zero_Count = 0;
			for (int count = 0; count < non_hic_Blocks.length; count++) {
				if (line_Collect_Non_hiC_start_Block[count].lines.size() == 0
						|| line_Collect_Non_hiC_end_Block[count].lines.size() == 0) {
					non_zero_Count = 1;
				}
			}

			// If they are, a research is done to ensure that every block has at least one
			// snp
			while (non_zero_Count != 0) {
				for (int c = 0; c < non_hic_Blocks.length; c++) {
					if (line_Collect_Non_hiC_start_Block[c].lines.size() == 0
							|| line_Collect_Non_hiC_end_Block[c].lines.size() == 0) {
						ExecutorService ex_temp = Executors.newFixedThreadPool(2);
						/*
						 * A parameter adjustment is done on the non HiC block generation by reducing
						 * the size of the exclusion zone A new function with pretty much the same
						 * function for non HiC block is used in this instance but the exclusion zone is
						 * adjusted
						 */
						non_hic_Blocks[c] = non_HiC_One(hiC_start_Block, hiC_end_Block, min_Point, max_Point,
								non_HIC_Collection);
						non_HIC_Collection.add(non_hic_Blocks[c]);
						String[] split_non_HiC = non_hic_Blocks[c].split("\t");
						non_HiC_start_Blocks[c] = split_non_HiC[0];
						non_HiC_end_Blocks[c] = split_non_HiC[1];
						ex_temp.execute(new ONOFF_collect(lines, line_Collect_Non_hiC_start_Block[c],
								non_HiC_start_Blocks[c], lines_Caught, temp_Lines, activate_Coll));
						ex_temp.execute(new ONOFF_collect(lines, line_Collect_Non_hiC_end_Block[c],
								non_HiC_end_Blocks[c], lines_Caught, temp_Lines, activate_Coll));

						// WAIT till the threads finish
						ex_temp.shutdown();
						while (!ex_temp.isTerminated()) {
						}

						/*
						 * if (activate_Coll == 1) { System.out.println("Indexing file"); // Complete
						 * the indexing of vcf files by permanent storage // THE INDEXING AND EXTRACTION
						 * STEPS IN THE PROCESS ARE DONE SEPERATELY TO // PREVENT THREAD CRASH
						 * 
						 * for (int pos : temp_Lines.Positions) { lines_Caught.Positions.add(pos);
						 * lines_Caught.set(pos, temp_Lines.positions_lineNum.get(pos)); }
						 * System.out.println("Points caught: " + temp_Lines.Positions.size()); // Once
						 * added the List is sorted and temp storage is cleaned up to prevent //
						 * redundancy Collections.sort(lines_Caught.Positions);
						 * temp_Lines.Positions.clear(); temp_Lines.positions_lineNum.clear(); }
						 */

					}
				}
				// Re-check to ensure all blocks are not empty
				non_zero_Count = 0;
				for (int count = 0; count < non_hic_Blocks.length; count++) {
					if (line_Collect_Non_hiC_start_Block[count].lines.size() == 0
							|| line_Collect_Non_hiC_end_Block[count].lines.size() == 0) {
						non_zero_Count = 1;
					}
				}
			}

			/*
			 * if (activate_Coll == 1) { System.out.println("Deactivating indexer");
			 * activate_Coll = 0; }
			 */

			// HiC and non HiC LD is calculated in separate threads

			Store_lines[] lines_storage_non_hiC = new Store_lines[non_hic_Blocks.length];

			System.out.println("Checking Block Sizes");

			for (int c = 0; c < non_hic_Blocks.length; c++) {

				int start_Size = line_Collect_Non_hiC_start_Block[c].lines.size();
				int end_Size = line_Collect_Non_hiC_end_Block[c].lines.size();
				// System.out.println(c + ": Start Block Size: " + start_Size);
				// System.out.println(c + ": End Block Size: " + end_Size);

				// check of combination number is feasible
				if (start_Size > 200 && end_Size > 200) {
					System.out.println("Resizing blocks");
					int block_Size = start_Size - 1;

					Random random = new Random();

					ArrayList<Integer> start_values = new ArrayList<Integer>();
					ArrayList<Integer> end_values = new ArrayList<Integer>();

					while (start_values.size() < 200) {

						int value = random.nextInt(block_Size - 0) + 0;
						if (!start_values.contains(value)) {
							start_values.add(value);
						}

					}
					block_Size = end_Size - 1;
					while (end_values.size() < 200) {

						int value = random.nextInt(block_Size - 0) + 0;
						if (!end_values.contains(value)) {
							end_values.add(value);
						}

					}

					List<String> new_Start_lines = new ArrayList<String>();
					List<String> new_End_lines = new ArrayList<String>();

					for (int c1 = 0; c1 < 200; c1++) {
						new_Start_lines.add(line_Collect_Non_hiC_start_Block[c].getLines().get(c1));
						new_End_lines.add(line_Collect_Non_hiC_end_Block[c].getLines().get(c1));
					}

					line_Collect_Non_hiC_start_Block[c].set(new_Start_lines);
					line_Collect_Non_hiC_end_Block[c].set(new_End_lines);

					new_Start_lines.clear();
					new_End_lines.clear();

					System.out
							.println(c + ": New Start Block Size: " + line_Collect_Non_hiC_start_Block[c].lines.size());
					System.out.println(c + ": New End Block Size: " + line_Collect_Non_hiC_end_Block[c].lines.size());

				}
			}

			ExecutorService ex2 = Executors.newFixedThreadPool(10);
			System.out.println("Calculating LD");
			for (int c = 0; c < non_hic_Blocks.length; c++) {
				lines_storage_non_hiC[c] = new Store_lines();
				ex2.execute(new LD_Calc(line_Collect_Non_hiC_start_Block[c].getLines(),
						line_Collect_Non_hiC_end_Block[c].getLines(), lines_storage_non_hiC[c]));
			}

			// WAIT till the threads finish
			ex2.shutdown();
			while (!ex2.isTerminated()) {
			}

			Double[] Non_HiC_D_Prime_Tot = new Double[non_hic_Blocks.length];
			Double[] Non_HiC_R_Squared_Tot = new Double[non_hic_Blocks.length];
			Double[] Non_HiC_D_Prime_Average = new Double[non_hic_Blocks.length];
			Double[] Non_HiC_R_Squared_Average = new Double[non_hic_Blocks.length];
			int[] Non_HiC_SNP_Configs = new int[non_hic_Blocks.length];

			// Average EACH Non HiC block data
			for (int block = 0; block < non_hic_Blocks.length; block++) {
				Non_HiC_SNP_Configs[block] = lines_storage_non_hiC[block].lines_Store.size();
				Non_HiC_D_Prime_Tot[block] = 0.00;
				Non_HiC_R_Squared_Tot[block] = 0.00;
				Non_HiC_D_Prime_Average[block] = 0.00;
				Non_HiC_R_Squared_Average[block] = 0.00;
				for (int c = 0; c < lines_storage_non_hiC[block].lines_Store.size(); c++) {
					String line = lines_storage_non_hiC[block].lines_Store.get(c);
					String line_Data[] = line.split("\t");
					Double D_Prime = Double.parseDouble(line_Data[6]);
					Double R_Squared = Double.parseDouble(line_Data[7]);

					Non_HiC_D_Prime_Tot[block] = Non_HiC_D_Prime_Tot[block] + D_Prime;
					Non_HiC_R_Squared_Tot[block] = Non_HiC_R_Squared_Tot[block] + R_Squared;
				}
				Non_HiC_D_Prime_Average[block] = Non_HiC_D_Prime_Tot[block]
						/ lines_storage_non_hiC[block].lines_Store.size();
				Non_HiC_R_Squared_Average[block] = Non_HiC_R_Squared_Tot[block]
						/ lines_storage_non_hiC[block].lines_Store.size();
			}

			Double Non_HiC_D_Prime_Super_Tot = 0.00;
			Double Non_HiC_R_Squared_Super_Tot = 0.00;
			Double Non_HiC_Super_Average_D_Prime = 0.00;
			Double Non_HiC_Super_Average_R_Squared = 0.00;

			for (int c = 0; c < non_hic_Blocks.length; c++) {
				Non_HiC_D_Prime_Super_Tot = Non_HiC_D_Prime_Super_Tot + Non_HiC_D_Prime_Average[c];
				Non_HiC_R_Squared_Super_Tot = Non_HiC_R_Squared_Super_Tot + Non_HiC_R_Squared_Average[c];
			}

			Non_HiC_Super_Average_D_Prime = Non_HiC_D_Prime_Super_Tot / non_hic_Blocks.length;
			Non_HiC_Super_Average_R_Squared = Non_HiC_R_Squared_Super_Tot / non_hic_Blocks.length;

			// START writing to main SUMMARY file and detailed LOG File
			System.out.println("Writing Data");

			FileWriter fw = new FileWriter(summary, true);
			BufferedWriter bw = new BufferedWriter(fw);
			String hic_start_Block = hiC_start_Block.substring(hiC_start_Block.indexOf(";") + 1,
					hiC_start_Block.length());
			String hic_end_Block = hiC_end_Block.substring(hiC_end_Block.indexOf(";") + 1, hiC_end_Block.length());
			String[] start_Block = hic_start_Block.split(";");
			String[] end_Block = hic_end_Block.split(";");

			int start_block_Mid = (Integer.parseInt(start_Block[1]) + Integer.parseInt(start_Block[0])) / 2;
			int end_block_Mid = (Integer.parseInt(end_Block[1]) + Integer.parseInt(end_Block[0])) / 2;
			int Distance = end_block_Mid - start_block_Mid;

			// String header =
			// "Combination\tHiC_Range\tDistance\tNon_HiC_AVG_D_prime\tNon_HiC_AVG_R_Squared\n";
			String line = combo_Num + "\t" + hic_start_Block + "|" + hic_end_Block + "\t" + Distance + "\t"
					+ Non_HiC_Super_Average_D_Prime + "\t" + Non_HiC_Super_Average_R_Squared + "\n";

			bw.write(line);
			bw.flush();

			bw.close();
			fw.close();

			FileWriter fw2 = new FileWriter(HiC_Non_HiC_Full, true);
			BufferedWriter bw2 = new BufferedWriter(fw2);
			String header = combo_Num + "\t" + hic_start_Block + "|" + hic_end_Block + "\t";

			for (int c = 0; c < 1000; c++) {
				header = header + non_HiC_start_Blocks[c] + "|" + non_HiC_end_Blocks[c] + "\t";
			}
			for (int c = 0; c < 1000; c++) {
				header = header + Non_HiC_SNP_Configs[c] + "\t";
			}
			for (int c = 0; c < 1000; c++) {
				header = header + Non_HiC_D_Prime_Average[c] + "\t";
			}
			for (int c = 0; c < 1000; c++) {
				header = header + Non_HiC_R_Squared_Average[c] + "\t";
			}
			header = header + "\n";
			bw2.write(header);
			bw2.close();
			fw2.close();
			combination = hiC_br.readLine();

		}

		System.out.println("");

	}

	public String non_HiC_One(String start_Block, String end_Block, int min_Point, int max_Point,
			List<String> non_HIC_Collection) {

		String non_hic_Block = new String();

		String[] start_Split = start_Block.split(";");
		String[] end_Split = end_Block.split(";");
		int HiC_Range = Integer.parseInt(end_Split[1]) - Integer.parseInt(start_Split[2]);
		int start_block_Size = Integer.parseInt(start_Split[2]) - Integer.parseInt(start_Split[1]);
		int end_block_Size = Integer.parseInt(end_Split[2]) - Integer.parseInt(end_Split[1]);

		// System.out.println("HiC Range\t: " + start_Block + "\t" + end_Block);
		int exclusion_zone_Start = 0;
		int exclusion_zone_End = 0;

		Random generator_Num = new Random();

		int non_HiC_start_block_start = 0;
		int non_HiC_start_block_end = 0;
		int non_HiC_end_block_start = 0;
		int non_HiC_end_block_end = 0;

		// If the distance between the HiC blocks is too large the exclusion zone is
		// adjusted
		if (HiC_Range <= 500000) {
			exclusion_zone_Start = Integer.parseInt(start_Split[1]) - 1000000;
			exclusion_zone_End = Integer.parseInt(end_Split[2]) + 1000000;
		} else {
			exclusion_zone_Start = Integer.parseInt(start_Split[1]) - 1000000;
			exclusion_zone_End = Integer.parseInt(start_Split[2]) + 1000000;
		}

		if (exclusion_zone_Start < min_Point) {
			exclusion_zone_Start = min_Point;
		}
		if (exclusion_zone_End > max_Point) {
			exclusion_zone_End = max_Point;
		}

		// If the distance between the HiC blocks is too large the exclusion zone is
		// adjusted
		if (HiC_Range <= 500000) {
			do {
				non_HiC_start_block_start = generator_Num.nextInt((max_Point - min_Point) + 1) + min_Point;
				non_HiC_start_block_end = non_HiC_start_block_start + start_block_Size;
				non_HiC_end_block_start = non_HiC_start_block_end + HiC_Range;
				non_HiC_end_block_end = non_HiC_end_block_start + end_block_Size;

			} while ((non_HiC_start_block_end > exclusion_zone_Start && non_HiC_start_block_end < exclusion_zone_End)
					|| (non_HiC_end_block_start > exclusion_zone_Start
							&& non_HiC_end_block_start < exclusion_zone_End));
		} else {
			do {
				non_HiC_start_block_start = generator_Num.nextInt((max_Point - min_Point) + 1) + min_Point;
				non_HiC_start_block_end = non_HiC_start_block_start + start_block_Size;
				non_HiC_end_block_start = non_HiC_start_block_end + HiC_Range;
				non_HiC_end_block_end = non_HiC_end_block_start + end_block_Size;
			} while ((non_HiC_end_block_start > exclusion_zone_Start && non_HiC_end_block_start < exclusion_zone_End));
		}

		String non_HIC_start_block = non_HiC_start_block_start + ";" + non_HiC_start_block_end;
		String non_HIC_end_block = non_HiC_end_block_start + ";" + non_HiC_end_block_end;
		String non_HiC_Range = non_HIC_start_block + "\t" + non_HIC_end_block;

		System.out.println("New Non HiC Range: " + non_HiC_Range);
		if (!non_HIC_Collection.contains(non_HiC_Range)) {
			non_hic_Block = non_HiC_Range;
			non_HIC_Collection.add(non_HiC_Range);
		}

		return non_hic_Block;

	}

	public String[] non_HiC_Range(String start_Block, String end_Block, int min_Point, int max_Point,
			List<String> non_HIC_Collection) {
		// The function is designed to generate 10 Random Non-HiC combinations based on
		// the HiC combination being processed.
		// Aesthetics
		String pattern = "00";
		DecimalFormat df = new DecimalFormat(pattern);

		// Store 10 random non HiC blocks will be generated
		String[] non_hic_Blocks = new String[1000];

		// Split start and end blocks to get the dimensions of each block
		String[] start_Split = start_Block.split(";");
		String[] end_Split = end_Block.split(";");

		// Calculate the distance between the two blocks and each blocks size
		int HiC_Range = Integer.parseInt(end_Split[1]) - Integer.parseInt(start_Split[2]);
		int start_block_Size = Integer.parseInt(start_Split[2]) - Integer.parseInt(start_Split[1]);
		int end_block_Size = Integer.parseInt(end_Split[2]) - Integer.parseInt(end_Split[1]);

		System.out.println("HiC Range\t: " + start_Block + "\t" + end_Block);
		// Setting up a 2Mbp exclusion zone to prevent selection of zones from the HiC
		// region under study
		// If exclusion zones exceed the dimensions of the vcf file data they are reeled
		// back
		int exclusion_zone_Start = Integer.parseInt(start_Split[1]) - 1000000;
		if (exclusion_zone_Start < min_Point) {
			exclusion_zone_Start = min_Point;
		}
		int exclusion_zone_End = Integer.parseInt(end_Split[2]) + 1000000;
		if (exclusion_zone_End > max_Point) {
			exclusion_zone_End = max_Point;
		}

		System.out.println("Exlusion Zone\t: " + exclusion_zone_Start + ";" + exclusion_zone_End + "\n");
		Random generator_Num = new Random();

		// Generation of non HiC blocks
		for (int c = 0; c < 1000; c++) {
			int non_HiC_start_block_start = 0;
			int non_HiC_start_block_end = 0;
			int non_HiC_end_block_start = 0;
			int non_HiC_end_block_end = 0;

			do {
				// Generate the start block value randomly within the constraints of the vcf
				// file data
				non_HiC_start_block_start = generator_Num.nextInt((max_Point - min_Point) + 1) + min_Point;
				non_HiC_start_block_end = non_HiC_start_block_start + start_block_Size;
				non_HiC_end_block_start = non_HiC_start_block_end + HiC_Range;
				non_HiC_end_block_end = non_HiC_end_block_start + end_block_Size;

				// Ensure that the generated value is not in the exclusion zone
			} while ((non_HiC_start_block_end > exclusion_zone_Start && non_HiC_start_block_end < exclusion_zone_End)
					|| (non_HiC_end_block_start > exclusion_zone_Start
							&& non_HiC_end_block_start < exclusion_zone_End));

			// Setup the Non HiC block parameters to match the HiC block being processed
			String non_HIC_start_block = non_HiC_start_block_start + ";" + non_HiC_start_block_end;
			String non_HIC_end_block = non_HiC_end_block_start + ";" + non_HiC_end_block_end;
			String non_HiC_Range = non_HIC_start_block + "\t" + non_HIC_end_block;
			System.out.println("Non HiC Range " + df.format((c + 1)) + ": " + non_HiC_Range);

			// Ensure that the block has not been previously used
			// if (!non_HIC_Collection.contains(non_HiC_Range)) {
			non_hic_Blocks[c] = non_HiC_Range;
			// non_HIC_Collection.add(non_HiC_Range);
			// }

		}

		// Returns the 10 randomly generated Non HiC blocks
		return non_hic_Blocks;

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

class ONOFF_collect implements Runnable {
	// Multithreaded class for collecting SNPS in the target range
	/*
	 * Line catcher sub function is too running in this class which is responsible
	 * for indexing the vcf file as the programme is running. This enables the
	 * programme to jump to the required line on the file as needed. Leads to a
	 * dramatic speed increase.
	 *************************************************************************************************
	 * PLEASE NOTE THE CODE WORKS ONLY IF THE VCF FILE IS SORTED IN ASCENDING ORDER
	 * BASED ON POSITION
	 *************************************************************************************************
	 */

	Range line_Collect;
	List<String> vcf;
	String range;
	Line_catcher temp_Lines;
	Line_catcher lines_Caught;
	int ON;

	ONOFF_collect(List<String> vcf, Range line_Collect, String range, Line_catcher lines_Caught,
			Line_catcher temp_Lines, int ON) {
		this.vcf = vcf;
		this.range = range;
		this.line_Collect = line_Collect;
		this.temp_Lines = temp_Lines;
		this.lines_Caught = lines_Caught;
		this.ON = ON;
	}

	public void run() {

		String[] split_Range = range.split(";");
		// Extract data on block
		int start = Integer.parseInt(split_Range[0]);
		int end = Integer.parseInt(split_Range[1]);
		int catcher = 0;

		// Variable to set the start location
		int start_Search = 1;
		int catch_Pos = 0;

		// Get indexed information
		List<Integer> Positions = new ArrayList<Integer>(lines_Caught.getPositions());
		// Sort caught lines based on bp position to ensure a smooth search for closest
		// match to current search region
		Collections.sort(Positions);
		// Get indexed information from Hashtable to get the line number
		Hashtable<Integer, Integer> positions_lineNum = new Hashtable<Integer, Integer>(
				lines_Caught.getPositions_lineNum());
		// Indexed database is used only if it contains data
		if (!Positions.isEmpty()) {
			for (int pos : Positions) {
				// Get the closest match
				if (pos < start) {
					catch_Pos = pos;
				}
			}
			if (catch_Pos != 0) {
				start_Search = positions_lineNum.get(catch_Pos);
			}
		}

		// Search for the snp now starts from the closet matching snp location
		for (int c = start_Search; c < vcf.size(); c++) {
			String line = vcf.get(c);
			String[] split_Line = line.split("\t");
			int pos_Current = Integer.parseInt(split_Line[1]);
			// All snps in the target range are stored

			if (pos_Current >= start && pos_Current <= end) {
				line_Collect.found(line);
				if (ON == 1) {
					if (catcher == 0) {
						// If first snp in this range the line data is indexed. This is done to prevent
						// RAM overload and unnecessary indexing
						temp_Lines.set(pos_Current, c);
						catcher = 1;
					}
				}
			}

			if (pos_Current > end) {
				// If the current search position exceeds the block limit the search is
				// terminated and the end location is indexed
				if (ON == 1) {
					temp_Lines.set(pos_Current, c);
				}
				break;
			}

		}

	}

}
