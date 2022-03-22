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
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HiC_LD_Classic {

	File input_Folder;
	File output_Folder;
	File HiC;

	HiC_LD_Classic(File input_Folder, File output_Folder) {
		this.input_Folder = input_Folder;
		this.output_Folder = output_Folder;
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
		// Function responsible for processing the vcf file in the calculation of LD per
		// HiC block as well as non HiC blocks

		// Load vcf file into RAM to enable multithreading
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
		String pathname = output_Folder.getAbsolutePath() + "/" + "HI_C_" + vcf.substring(0, vcf.lastIndexOf("."))
				+ ".hicld2";
		File summary = new File(pathname);
		if (!summary.exists()) {
			FileWriter fw = new FileWriter(summary, true);
			BufferedWriter bw = new BufferedWriter(fw);
			String header = "Combination\tHiC_Range\tDistance\tHiC_D_prime\tNon_HiC_AVG_D_prime\tHiC_R_Squared\tNon_HiC_AVG_R_Squared\n";
			bw.write(header);
			bw.close();
			fw.close();
		}

		/*
		 * Store the detailed data on the calculations including the: HiC SNP number,
		 * ,Non HiC range and Individual regions D prime and R squared
		 */
		File output_Combos_pre = new File(output_Folder.getAbsolutePath() + "/Combo_Details_2");
		output_Combos_pre.mkdir();
		File HiC_Non_HiC_Full = new File(output_Combos_pre.getAbsolutePath() + "/" + vcf + "_HiC.summary2");

		if (!HiC_Non_HiC_Full.exists()) {
			FileWriter fw = new FileWriter(HiC_Non_HiC_Full, true);
			BufferedWriter bw = new BufferedWriter(fw);
			String header = "Combination\tHiC_Range\tHiC_SNP_Configs\t";
			DecimalFormat df = new DecimalFormat("00");
			for (int c = 0; c < 1; c++) {
				header = header + "Non_HiC_Range_" + df.format(c + 1) + "\t";
			}
			for (int c = 0; c < 1; c++) {
				header = header + "Non_HiC_SNP_Configs_" + df.format(c + 1) + "\t";
			}
			for (int c = 0; c < 1; c++) {
				header = header + "Non_HiC_D_Prime_" + df.format(c + 1) + "\t";
			}
			for (int c = 0; c < 1; c++) {
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
				// Extraction of the data on non HiC blocks
				String[] split_non_HiC = non_hic_Blocks[c].split("\t");
				non_HiC_start_Blocks[c] = split_non_HiC[0];
				non_HiC_end_Blocks[c] = split_non_HiC[1];
			}

			// Initiate multi-threading phase
			ExecutorService ex = Executors.newFixedThreadPool(23);
			// Get data of HiC start and end blocks
			String hic_start_Block = hiC_start_Block.substring(hiC_start_Block.indexOf(";") + 1,
					hiC_start_Block.length());
			String hic_end_Block = hiC_end_Block.substring(hiC_end_Block.indexOf(";") + 1, hiC_end_Block.length());

			/*
			 * Collect all snps present in the target regions Data collected in List through
			 * classes to ensure thread safety
			 */
			Range line_Collect_hiC_start_Block = new Range();
			Range line_Collect_hiC_end_Block = new Range();
			Range[] line_Collect_Non_hiC_start_Block = new Range[non_hic_Blocks.length];
			Range[] line_Collect_Non_hiC_end_Block = new Range[non_hic_Blocks.length];

			System.out.println("\nCollecting SNP Data");

			// Threads to collect snps from HiC region
			ex.execute(
					new Thread_collect(lines, line_Collect_hiC_start_Block, hic_start_Block, lines_Caught, temp_Lines));
			ex.execute(new Thread_collect(lines, line_Collect_hiC_end_Block, hic_end_Block, lines_Caught, temp_Lines));

			// Threads to collect snps from non HiC regions
			for (int c = 0; c < non_hic_Blocks.length; c++) {
				// Initiate the class arrays
				line_Collect_Non_hiC_start_Block[c] = new Range();
				line_Collect_Non_hiC_end_Block[c] = new Range();
				ex.execute(new Thread_collect(lines, line_Collect_Non_hiC_start_Block[c], non_HiC_start_Blocks[c],
						lines_Caught, temp_Lines));
				ex.execute(new Thread_collect(lines, line_Collect_Non_hiC_end_Block[c], non_HiC_end_Blocks[c],
						lines_Caught, temp_Lines));
			}

			// WAIT till the threads finish
			ex.shutdown();
			while (!ex.isTerminated()) {
			}

			// Complete the indexing of vcf files by permanent storage
			// THE INDEXING AND EXTRACTION STEPS IN THE PROCESS ARE DONE SEPERATELY TO
			// PREVENT THREAD CRASH
			for (int pos : temp_Lines.Positions) {
				lines_Caught.Positions.add(pos);
				lines_Caught.set(pos, temp_Lines.positions_lineNum.get(pos));
			}

			// Once added the List is sorted and temp storage is cleaned up to prevent
			// redundancy
			Collections.sort(lines_Caught.Positions);
			temp_Lines.Positions.clear();
			temp_Lines.positions_lineNum.clear();

			// HiC and non HiC LD is calculated in separate threads
			ExecutorService ex2 = Executors.newFixedThreadPool(5);
			Store_lines lines_storage_hiC = new Store_lines();
			Store_lines[] lines_storage_non_hiC = new Store_lines[non_hic_Blocks.length];

			System.out.println("Calculating LD");

			ex2.execute(new LD_Calc(line_Collect_hiC_start_Block.getLines(), line_Collect_hiC_end_Block.getLines(),
					lines_storage_hiC));

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

			Double HiC_D_Prime_Tot = 0.00;
			Double HiC_R_Squared_Tot = 0.00;
			Double HiC_Average_D_Prime = 0.00;
			Double HiC_Average_R_Squared = 0.00;
			int HiC_SNP_Configs = lines_storage_hiC.lines_Store.size();

			System.out.println("Summarizing Data");
			// Average HiC block data
			for (int c = 0; c < lines_storage_hiC.lines_Store.size(); c++) {
				String line = lines_storage_hiC.lines_Store.get(c);
				String line_Data[] = line.split("\t");
				// Printer(line_Data);
				// Extract per snp pair LD data
				Double D_Prime = Double.parseDouble(line_Data[6]);
				Double R_Squared = Double.parseDouble(line_Data[7]);

				HiC_D_Prime_Tot = HiC_D_Prime_Tot + D_Prime;
				HiC_R_Squared_Tot = HiC_R_Squared_Tot + R_Squared;
			}
			// Average the D prime and R squared
			HiC_Average_D_Prime = HiC_D_Prime_Tot / lines_storage_hiC.lines_Store.size();
			HiC_Average_R_Squared = HiC_R_Squared_Tot / lines_storage_hiC.lines_Store.size();

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

			// Average overall 10 non HiC blocks
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

			String[] start_Block = hic_start_Block.split(";");
			String[] end_Block = hic_end_Block.split(";");

			int start_block_Mid = (Integer.parseInt(start_Block[1]) + Integer.parseInt(start_Block[0])) / 2;
			int end_block_Mid = (Integer.parseInt(end_Block[1]) + Integer.parseInt(end_Block[0])) / 2;
			int Distance = end_block_Mid - start_block_Mid;

			// String header =
			// "Combination\tHiC_Range\tDistance\tHiC_D_Prime\tNon_HiC_AVG_D_Prime\tHiC_R_Squared\tNon_HiC_AVG_R_Squared";
			String line = combo_Num + "\t" + hic_start_Block + "|" + hic_end_Block + "\t" + Distance + "\t"
					+ HiC_Average_D_Prime + "\t" + Non_HiC_Super_Average_D_Prime + "\t" + HiC_Average_R_Squared + "\t"
					+ Non_HiC_Super_Average_R_Squared + "\n";

			bw.write(line);
			bw.flush();

			bw.close();
			fw.close();

			FileWriter fw2 = new FileWriter(HiC_Non_HiC_Full, true);
			BufferedWriter bw2 = new BufferedWriter(fw2);
			String header = combo_Num + "\t" + hic_start_Block + "|" + hic_end_Block + "\t" + HiC_SNP_Configs + "\t";

			for (int c = 0; c < 1; c++) {
				header = header + non_HiC_start_Blocks[c] + "|" + non_HiC_end_Blocks[c] + "\t";
			}
			for (int c = 0; c < 1; c++) {
				header = header + Non_HiC_SNP_Configs[c] + "\t";
			}
			for (int c = 0; c < 1; c++) {
				header = header + Non_HiC_D_Prime_Average[c] + "\t";
			}
			for (int c = 0; c < 1; c++) {
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

	public List find_Files() {
		String[] files = input_Folder.list();
		List<String> vcf_All = new ArrayList<String>();

		for (String file : files) {
			String extension = file.substring(file.lastIndexOf("."), file.length());
			if (extension.equals(".txt")) {
				// Get the hiC file
				String pathname = input_Folder.getAbsolutePath() + "/" + file;
				this.HiC = new File(pathname);
			} else if (extension.equals(".vcf")) {
				// Get all vcf files
				vcf_All.add(file);
			}
		}

		Collections.sort(vcf_All);
		return vcf_All;
	}

	public String[] non_HiC_Range(String start_Block, String end_Block, int min_Point, int max_Point,
			List<String> non_HIC_Collection) {
		// The function is designed to generate 10 Random Non-HiC combinations based on
		// the HiC combination being processed.
		// Aesthetics
		String pattern = "00";
		DecimalFormat df = new DecimalFormat(pattern);

		String[] non_hic_Blocks = new String[1];

		// Split start and end blocks to get the dimensions of each block
		String[] start_Split = start_Block.split(";");
		String[] end_Split = end_Block.split(";");

		// Calculate the distance between the two blocks and each blocks size
		int HiC_Range = Integer.parseInt(end_Split[1]) - Integer.parseInt(start_Split[2]);
		int start_block_Size = Integer.parseInt(start_Split[2]) - Integer.parseInt(start_Split[1]);
		int end_block_Size = Integer.parseInt(end_Split[2]) - Integer.parseInt(end_Split[1]);

		int start_Mid = (Integer.parseInt(start_Split[2]) + Integer.parseInt(start_Split[1])) / 2;
		int end_Mid = (Integer.parseInt(end_Split[2]) + Integer.parseInt(end_Split[1])) / 2;
		int range_Mid = (start_Mid+end_Mid)/2;
		
		int start_block_half = start_block_Size/2;
		int startblock_start = range_Mid-start_block_half;
		int startblock_end = range_Mid+start_block_half;
		
		
		int end_block_half = end_block_Size/2;
		int end_Midpoint = range_Mid + HiC_Range;
		int endblock_start = end_Midpoint-end_block_half;
		int endblock_end = end_Midpoint+end_block_half;
		
		String non_HIC_start_block =startblock_start + ";" + startblock_end;
		String non_HIC_end_block = endblock_start + ";" + endblock_end;
		String non_HiC_Range = non_HIC_start_block + "\t" + non_HIC_end_block;

		System.out.println("HiC Range\t: " + start_Block + "\t" + end_Block);
		System.out.println("Non HiC Range \t: " + non_HiC_Range);

		non_hic_Blocks[0] = non_HiC_Range;
		// Generation of non HiC blocks

		/*
		 * String non_HIC_start_block = non_HiC_start_block_start + ";" +
		 * non_HiC_start_block_end; String non_HIC_end_block = non_HiC_end_block_start +
		 * ";" + non_HiC_end_block_end; String non_HiC_Range = non_HIC_start_block +
		 * "\t" + non_HIC_end_block; System.out.println("Non HiC Range " + df.format((c
		 * + 1)) + ": " + non_HiC_Range);
		 */
		// Returns the 10 randomly generated Non HiC blocks
		return non_hic_Blocks;

	}

}
