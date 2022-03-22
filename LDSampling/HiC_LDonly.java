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

public class HiC_LDonly {

	File input_Folder;
	File output_Folder;
	File HiC;
	String combo_File;

	HiC_LDonly(File input_Folder, File output_Folder, String combo_File) {
		this.input_Folder = input_Folder;
		this.output_Folder = output_Folder;
		this.combo_File = combo_File;
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
		String country = vcf.substring(vcf.lastIndexOf("_") + 1, vcf.lastIndexOf("."));
		// Function responsible for processing the vcf file in the calculation of LD per
		// HiC block as well as non HiC blocks

		// Load vcf file into RAM to enable multithreading
		File vcf_Current = new File(input_Folder.getAbsolutePath() + "/" + vcf);
		List<String> lines = Files.readAllLines(Paths.get(vcf_Current.getAbsolutePath()));

		// Discard first line of VCF for its a header (NOTE these are CUSTOM
		// pre-processed VCF files)
		String[] line_One = lines.get(1).split("\t");
		String[] line_last = lines.get(lines.size() - 1).split("\t");

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

		File output_Ding = new File(this.output_Folder.getAbsolutePath() + "/Leah");
		File output_country = new File(output_Ding.getAbsolutePath() + "/" + country);

		if (!output_Ding.exists()) {
			output_Ding.mkdir();
		}

		if (!output_country.exists()) {
			output_country.mkdir();
		}

		String pathname = output_country.getAbsolutePath() + "/" + country + "_"
				+ this.HiC.getName().substring(0, this.HiC.getName().lastIndexOf(".")) + ".hicld";
		File summary = new File(pathname);
		if (!summary.exists()) {
			summary.createNewFile();
			FileWriter fw = new FileWriter(summary, true);
			BufferedWriter bw = new BufferedWriter(fw);
			String header = "Combination\tHiC_Range\tDistance\tHiC_D_prime\tHiC_R_Squared\n";
			bw.write(header);
			bw.close();
			fw.close();
		}

		while (combination != null) {
			// The HiC file consists of three columns: The combination number, Block 1
			// (start block) Parameters and Block 2 (end block) Parameters
			String[] split = combination.split("\t");
			String combo_Num = split[0];
			System.out.println("\nProcessing Combination \t: " + combo_Num);

			// Get the start and end block parameters
			String hiC_start_Block = split[1];
			String hiC_end_Block = split[2];

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

			System.out.println("\nCollecting SNP Data");

			// Threads to collect snps from HiC region
			ex.execute(
					new Thread_collect(lines, line_Collect_hiC_start_Block, hic_start_Block, lines_Caught, temp_Lines));
			ex.execute(new Thread_collect(lines, line_Collect_hiC_end_Block, hic_end_Block, lines_Caught, temp_Lines));

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
			ExecutorService ex2 = Executors.newFixedThreadPool(11);
			Store_lines lines_storage_hiC = new Store_lines();
			// Store_lines[] lines_storage_non_hiC = new Store_lines[non_hic_Blocks.length];
			int start_Size = line_Collect_hiC_start_Block.lines.size();
			int end_Size = line_Collect_hiC_end_Block.lines.size();
			System.out.println("Start Block Size: " + start_Size);
			System.out.println("End Block Size: " + end_Size);
			System.out.println("Calculating LD");

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

				for (int c = 0; c < 200; c++) {
					new_Start_lines.add(line_Collect_hiC_start_Block.getLines().get(c));
					new_End_lines.add(line_Collect_hiC_end_Block.getLines().get(c));
				}

				line_Collect_hiC_start_Block.set(new_Start_lines);
				line_Collect_hiC_end_Block.set(new_End_lines);
				
				new_Start_lines.clear();
				new_End_lines.clear();

				System.out.println("New Start Block Size: " + line_Collect_hiC_start_Block.lines.size());
				System.out.println("New End Block Size: " + line_Collect_hiC_end_Block.lines.size());
				
			}


			ex2.execute(new LD_Calc(line_Collect_hiC_start_Block.getLines(), line_Collect_hiC_end_Block.getLines(),
					lines_storage_hiC));

			// WAIT till the threads finish
			ex2.shutdown();
			while (!ex2.isTerminated()) {
			}

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
					+ HiC_Average_D_Prime + "\t" + HiC_Average_R_Squared + "\n";

			bw.write(line);
			bw.flush();

			bw.close();
			fw.close();

			combination = hiC_br.readLine();
		}

		System.out.println("");
	}

	private void Printer(String[] list) {
		// Used for troubleshooting arrays, not utilized in the main program
		for (int c = 0; c < list.length; c++) {
			System.out.println(list[c]);
		}
	}

	/*
	 * public String non_HiC_One(String start_Block, String end_Block, int
	 * min_Point, int max_Point, List<String> non_HIC_Collection) {
	 * 
	 * String non_hic_Block = new String();
	 * 
	 * String[] start_Split = start_Block.split(";"); String[] end_Split =
	 * end_Block.split(";"); int HiC_Range = Integer.parseInt(end_Split[1]) -
	 * Integer.parseInt(start_Split[2]); int start_block_Size =
	 * Integer.parseInt(start_Split[2]) - Integer.parseInt(start_Split[1]); int
	 * end_block_Size = Integer.parseInt(end_Split[2]) -
	 * Integer.parseInt(end_Split[1]);
	 * 
	 * // System.out.println("HiC Range\t: " + start_Block + "\t" + end_Block); int
	 * exclusion_zone_Start = 0; int exclusion_zone_End = 0;
	 * 
	 * Random generator_Num = new Random();
	 * 
	 * int non_HiC_start_block_start = 0; int non_HiC_start_block_end = 0; int
	 * non_HiC_end_block_start = 0; int non_HiC_end_block_end = 0;
	 * 
	 * // If the distance between the HiC blocks is too large the exclusion zone is
	 * // adjusted if (HiC_Range <= 500000) { exclusion_zone_Start =
	 * Integer.parseInt(start_Split[1]) - 1000000; exclusion_zone_End =
	 * Integer.parseInt(end_Split[2]) + 1000000; } else { exclusion_zone_Start =
	 * Integer.parseInt(start_Split[1]) - 1000000; exclusion_zone_End =
	 * Integer.parseInt(start_Split[2]) + 1000000; }
	 * 
	 * if (exclusion_zone_Start < min_Point) { exclusion_zone_Start = min_Point; }
	 * if (exclusion_zone_End > max_Point) { exclusion_zone_End = max_Point; }
	 * 
	 * // If the distance between the HiC blocks is too large the exclusion zone is
	 * // adjusted if (HiC_Range <= 500000) { do { non_HiC_start_block_start =
	 * generator_Num.nextInt((max_Point - min_Point) + 1) + min_Point;
	 * non_HiC_start_block_end = non_HiC_start_block_start + start_block_Size;
	 * non_HiC_end_block_start = non_HiC_start_block_end + HiC_Range;
	 * non_HiC_end_block_end = non_HiC_end_block_start + end_block_Size;
	 * 
	 * } while ((non_HiC_start_block_end > exclusion_zone_Start &&
	 * non_HiC_start_block_end < exclusion_zone_End) || (non_HiC_end_block_start >
	 * exclusion_zone_Start && non_HiC_end_block_start < exclusion_zone_End)); }
	 * else { do { non_HiC_start_block_start = generator_Num.nextInt((max_Point -
	 * min_Point) + 1) + min_Point; non_HiC_start_block_end =
	 * non_HiC_start_block_start + start_block_Size; non_HiC_end_block_start =
	 * non_HiC_start_block_end + HiC_Range; non_HiC_end_block_end =
	 * non_HiC_end_block_start + end_block_Size; } while ((non_HiC_end_block_start >
	 * exclusion_zone_Start && non_HiC_end_block_start < exclusion_zone_End)); }
	 * 
	 * String non_HIC_start_block = non_HiC_start_block_start + ";" +
	 * non_HiC_start_block_end; String non_HIC_end_block = non_HiC_end_block_start +
	 * ";" + non_HiC_end_block_end; String non_HiC_Range = non_HIC_start_block +
	 * "\t" + non_HIC_end_block;
	 * 
	 * System.out.println("New Non HiC Range: " + non_HiC_Range); if
	 * (!non_HIC_Collection.contains(non_HiC_Range)) { non_hic_Block =
	 * non_HiC_Range; non_HIC_Collection.add(non_HiC_Range); }
	 * 
	 * return non_hic_Block;
	 * 
	 * }
	 * 
	 * public String[] non_HiC_Range(String start_Block, String end_Block, int
	 * min_Point, int max_Point, List<String> non_HIC_Collection) { // The function
	 * is designed to generate 10 Random Non-HiC combinations based on // the HiC
	 * combination being processed. // Aesthetics String pattern = "00";
	 * DecimalFormat df = new DecimalFormat(pattern);
	 * 
	 * // Store 10 random non HiC blocks will be generated String[] non_hic_Blocks =
	 * new String[1000];
	 * 
	 * // Split start and end blocks to get the dimensions of each block String[]
	 * start_Split = start_Block.split(";"); String[] end_Split =
	 * end_Block.split(";");
	 * 
	 * // Calculate the distance between the two blocks and each blocks size int
	 * HiC_Range = Integer.parseInt(end_Split[1]) -
	 * Integer.parseInt(start_Split[2]); int start_block_Size =
	 * Integer.parseInt(start_Split[2]) - Integer.parseInt(start_Split[1]); int
	 * end_block_Size = Integer.parseInt(end_Split[2]) -
	 * Integer.parseInt(end_Split[1]);
	 * 
	 * System.out.println("HiC Range\t: " + start_Block + "\t" + end_Block); //
	 * Setting up a 2Mbp exclusion zone to prevent selection of zones from the HiC
	 * // region under study // If exclusion zones exceed the dimensions of the vcf
	 * file data they are reeled // back int exclusion_zone_Start =
	 * Integer.parseInt(start_Split[1]) - 1000000; if (exclusion_zone_Start <
	 * min_Point) { exclusion_zone_Start = min_Point; } int exclusion_zone_End =
	 * Integer.parseInt(end_Split[2]) + 1000000; if (exclusion_zone_End > max_Point)
	 * { exclusion_zone_End = max_Point; }
	 * 
	 * System.out.println("Exlusion Zone\t: " + exclusion_zone_Start + ";" +
	 * exclusion_zone_End + "\n"); Random generator_Num = new Random();
	 * 
	 * // Generation of non HiC blocks for (int c = 0; c < 1000; c++) { int
	 * non_HiC_start_block_start = 0; int non_HiC_start_block_end = 0; int
	 * non_HiC_end_block_start = 0; int non_HiC_end_block_end = 0;
	 * 
	 * do { // Generate the start block value randomly within the constraints of the
	 * vcf // file data non_HiC_start_block_start = generator_Num.nextInt((max_Point
	 * - min_Point) + 1) + min_Point; non_HiC_start_block_end =
	 * non_HiC_start_block_start + start_block_Size; non_HiC_end_block_start =
	 * non_HiC_start_block_end + HiC_Range; non_HiC_end_block_end =
	 * non_HiC_end_block_start + end_block_Size;
	 * 
	 * // Ensure that the generated value is not in the exclusion zone } while
	 * ((non_HiC_start_block_end > exclusion_zone_Start && non_HiC_start_block_end <
	 * exclusion_zone_End) || (non_HiC_end_block_start > exclusion_zone_Start &&
	 * non_HiC_end_block_start < exclusion_zone_End));
	 * 
	 * // Setup the Non HiC block parameters to match the HiC block being processed
	 * String non_HIC_start_block = non_HiC_start_block_start + ";" +
	 * non_HiC_start_block_end; String non_HIC_end_block = non_HiC_end_block_start +
	 * ";" + non_HiC_end_block_end; String non_HiC_Range = non_HIC_start_block +
	 * "\t" + non_HIC_end_block; System.out.println("Non HiC Range " + df.format((c
	 * + 1)) + ": " + non_HiC_Range);
	 * 
	 * // Ensure that the block has not been previously used //if
	 * (!non_HIC_Collection.contains(non_HiC_Range)) { non_hic_Blocks[c] =
	 * non_HiC_Range; //non_HIC_Collection.add(non_HiC_Range); //}
	 * 
	 * }
	 * 
	 * // Returns the 10 randomly generated Non HiC blocks return non_hic_Blocks;
	 * 
	 * }
	 */

	public List find_Files() {
		String[] files = input_Folder.list();
		List<String> vcf_All = new ArrayList<String>();

		for (String file : files) {
			String extension = file.substring(file.lastIndexOf("."), file.length());
			/*
			 * if (extension.equals(".txt")) { // Get the hiC file String pathname =
			 * input_Folder.getAbsolutePath() + "/" + file; this.HiC = new File(pathname); }
			 * else
			 */ if (extension.equals(".vcf")) {
				// Get all vcf files
				vcf_All.add(file);
			}
		}

		String pathname = input_Folder.getAbsolutePath() + "/" + this.combo_File;
		this.HiC = new File(pathname);

		Collections.sort(vcf_All);
		return vcf_All;
	}

}
