package linkage_d;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HiCcmaf {

	File input_Folder;
	File output_Folder;
	File HiC;

	HiCcmaf(File input_Folder, File output_Folder) {
		this.input_Folder = input_Folder;
		this.output_Folder = output_Folder;
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

	public void initiate() {

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
		File MAF_dir = new File(this.output_Folder.getAbsolutePath() + "/MAF");
		if (!MAF_dir.exists()) {
			MAF_dir.mkdir();
		}
		String country = vcf.substring(vcf.lastIndexOf("_") + 1, vcf.lastIndexOf("."));
		System.out.println("Country\t: " + country);
		File vcf_Current = new File(input_Folder.getAbsolutePath() + "/" + vcf);
		System.out.println("Loading\t: VCF");
		List<String> lines = Files.readAllLines(Paths.get(vcf_Current.getAbsolutePath()));
		System.out.println("VCF\t: Loaded");

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

		while (combination != null) {

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
			 * Collect all MAF present in the target regions Data collected in List through
			 * classes to ensure thread safety
			 */
			Range MAF_hiC_start_Block = new Range();
			Range MAF_hiC_end_Block = new Range();

			System.out.println("\nCollecting SNP Data");

			// Threads to collect MAF from HiC region
			ex.execute(new Thread_collect_MAF(lines, MAF_hiC_start_Block, hic_start_Block, lines_Caught, temp_Lines));
			ex.execute(new Thread_collect_MAF(lines, MAF_hiC_end_Block, hic_end_Block, lines_Caught, temp_Lines));

			// WAIT till the threads finish
			ex.shutdown();
			while (!ex.isTerminated()) {
			}

			String[] start_split_Range = hic_start_Block.split(";");
			// Extract data on block
			int start_start = Integer.parseInt(start_split_Range[0]);
			int start_end = Integer.parseInt(start_split_Range[1]);

			int hiC_Start_Size = start_end - start_start;

			String[] split_Range = hic_end_Block.split(";");
			// Extract data on block
			int start = Integer.parseInt(split_Range[0]);
			int end = Integer.parseInt(split_Range[1]);

			int hiC_end_Size = end - start;

			int hiC_Size = hiC_Start_Size + hiC_end_Size;

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

			Double tot_MAF = 0.00;

			for (Double MAF : MAF_hiC_start_Block.MAFs) {
				tot_MAF = tot_MAF + MAF;
			}

			for (Double MAF : MAF_hiC_end_Block.MAFs) {
				tot_MAF = tot_MAF + MAF;
			}

			int tot_Snps = MAF_hiC_start_Block.MAFs.size() + MAF_hiC_end_Block.MAFs.size();

			File MAF_output = new File(MAF_dir.getAbsolutePath() + "/" + country + ".maf");
			if (!MAF_output.exists()) {
				MAF_output.createNewFile();
				FileWriter fw = new FileWriter(MAF_output);
				BufferedWriter bw = new BufferedWriter(fw);
				bw.write("Combination\tMAF_Tot\tNo_SNPs\tHiC_Size\n");
				bw.flush();
				bw.close();
				fw.close();
			}

			FileWriter fw = new FileWriter(MAF_output, true);
			BufferedWriter bw = new BufferedWriter(fw);
			String data = combo_Num + "\t" + tot_MAF + "\t" + tot_Snps + "\t" + hiC_Size + "\n";
			bw.write(data);
			bw.flush();
			bw.close();
			fw.close();

			combination = hiC_br.readLine();
		}

		hiC_br.close();
		fr.close();

		File MAF_output = new File(MAF_dir.getAbsolutePath() + "/" + country + ".maf");
		FileReader fr_2 = new FileReader(MAF_output);
		BufferedReader br = new BufferedReader(fr_2);

		System.out.println("\n Calculating total HiC cMAF");
		br.readLine();
		String value = br.readLine();

		Double tot_HiC_MAF = 0.00;
		int tot_HiC_Snps = 0;
		Double tot_MAF = 0.00;
		int tot_HiC_Size = 0;
		int tot_Size = 0;

		String[] line_vcf = null;
		line_vcf = lines.get(1).split("\t");
		int start = Integer.parseInt(line_vcf[1]);
		line_vcf = lines.get(lines.size() - 1).split("\t");
		int end = Integer.parseInt(line_vcf[1]);
		tot_Size = end - start;

		while (value != null) {
			String[] data = value.split("\t");
			Double MAF = Double.parseDouble(data[1]);
			tot_HiC_MAF = tot_HiC_MAF + MAF;
			tot_HiC_Snps = tot_HiC_Snps + Integer.parseInt(data[2]);
			tot_HiC_Size = tot_HiC_Size + Integer.parseInt(data[3]);
			value = br.readLine();
		}

		br.close();
		fr_2.close();

		System.out.println("Completed total HiC cMAF");

		Super_pop GBR = new Super_pop();

		System.out.println("\nCalculating total cMAF");
		for (int c = 1; c < lines.size(); c++) {
			String line = lines.get(c);
			System.out.print("Line number\t: " + c + "\r");
			String[] split_Line = line.split("\t");
			Double MAF = GBR.return_MAF(split_Line);
			tot_MAF = tot_MAF + MAF;
		}

		System.out.println("\nCompleted total cMAF");

		System.out.println("\nWriting output");

		File Final = new File(this.output_Folder.getAbsolutePath() + "/" + country + ".result");
		if (!Final.exists()) {
			Final.createNewFile();
			FileWriter fw = new FileWriter(Final);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write("HiC_SNP_No\tHiC_TotMAF\tWG_SNP_No\tWG_TotMAF\tAVG_HiC\tAVG_Full_Genome\tHiC_bp\tTot_bp\n");
			bw.flush();
			bw.close();
			fw.close();
		}

		Double HiC_Density = tot_HiC_MAF / tot_HiC_Snps;
		Double Tot_Density = tot_MAF / (lines.size() - 1);

		FileWriter fw = new FileWriter(Final, true);
		BufferedWriter bw = new BufferedWriter(fw);
		String data = tot_HiC_Snps + "\t" + tot_HiC_MAF + "\t" + (lines.size() - 1) + "\t" + tot_MAF + "\t"
				+ HiC_Density + "\t" + Tot_Density +"\t"+tot_HiC_Size+"\t"+tot_Size+"\n";
		bw.write(data);
		bw.flush();
		bw.close();
		fw.close();

	}

}

class Thread_collect_MAF implements Runnable {
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

	Thread_collect_MAF(List<String> vcf, Range line_Collect, String range, Line_catcher lines_Caught,
			Line_catcher temp_Lines) {
		this.vcf = vcf;
		this.range = range;
		this.line_Collect = line_Collect;
		this.temp_Lines = temp_Lines;
		this.lines_Caught = lines_Caught;
	}

	public void run() {

		Super_pop GBR = new Super_pop();

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
				Double MAF = GBR.return_MAF(split_Line);
				line_Collect.found_MAF(MAF);
				if (catcher == 0) {
					// If first snp in this range the line data is indexed. This is done to prevent
					// RAM overload and unnecessary indexing
					temp_Lines.set(pos_Current, c);
					catcher = 1;
				}
			}

			if (pos_Current > end) {
				// If the current search position exceeds the block limit the search is
				// terminated and the end location is indexed
				temp_Lines.set(pos_Current, c);
				break;
			}
		}

	}

}
