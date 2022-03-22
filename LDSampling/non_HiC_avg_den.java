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
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class non_HiC_avg_den {

	File input_Folder;
	File output_Folder;
	File nonHiC_Folder;

	non_HiC_avg_den(File input_Folder, File output_Folder, File nonHiC_Folder) {
		this.output_Folder = output_Folder;
		this.nonHiC_Folder = nonHiC_Folder;
		this.input_Folder = input_Folder;
	}

	public Hashtable<String, String> index_vcf() {
		Hashtable<String, String> vcf = new Hashtable<String, String>();

		String[] vcf_List = this.input_Folder.list();

		for (String file : vcf_List) {
			if (file.contains(".vcf")) {
				// System.out.println(file);
				String extract = file.substring(file.indexOf("genotypes.vcf_"), file.length());
				String country = extract.substring(extract.indexOf("_") + 1, extract.lastIndexOf(".vcf"));
				// System.out.println(country);
				vcf.put(country, file);
			}
		}
		// System.out.println(vcf);

		return vcf;

	}

	public Hashtable<String, String> index_nonHiC() {
		Hashtable<String, String> nonHiC = new Hashtable<String, String>();

		String[] nonHiC_List = this.nonHiC_Folder.list();

		for (String file : nonHiC_List) {
			String country = file.substring(file.indexOf("_") + 1, file.lastIndexOf("."));
			// System.out.println(country);
			nonHiC.put(country, file);
		}
		// System.out.println(nonHiC);
		return nonHiC;
	}

	public void initiate() throws IOException {
		System.out.println("Calculating non HIC MAF Average and SNP density");
		System.out.println("\nStatus\t: Indexing VCF and Non HiC");
		Hashtable<String, String> vcf = index_vcf();
		Hashtable<String, String> nonHiC = index_nonHiC();
		System.out.println("Status\t: Completed Indexing VCF and Non HiC\n");

		Set<String> vcf_Countries = vcf.keySet();
		// Set<String> nonHiC_Countries = nonHiC.keySet();

		for (String current_Country : vcf_Countries) {
			System.out.println("Processing\t: " + current_Country);
			String vcf_File = vcf.get(current_Country);
			String nonHiC_File = nonHiC.get(current_Country);
			System.out.println("VCF File\t: " + vcf_File);
			System.out.println("non HiC File\t: " + nonHiC_File);
			vcf_Process(vcf_File, nonHiC_File, current_Country);
			System.out.println();
		}

	}

	public void vcf_Process(String vcf, String non_HiC, String country) throws IOException {

		System.out.println("\nStatus\t: Intiated calculation");

		File non_HiC_dir = new File(this.output_Folder.getAbsolutePath() + "/non_HiC");
		if (!non_HiC_dir.exists()) {
			non_HiC_dir.mkdir();
		}

		File vcf_Current = new File(input_Folder.getAbsolutePath() + "/" + vcf);
		File nonHiC = new File(this.nonHiC_Folder.getAbsolutePath() + "/" + non_HiC);

		System.out.println("Loading\t: VCF");
		List<String> lines = Files.readAllLines(Paths.get(vcf_Current.getAbsolutePath()));
		System.out.println("VCF\t: Loaded");

		// Read the HiC data file
		FileReader fr = new FileReader(nonHiC);
		BufferedReader NhiC_br = new BufferedReader(fr);

		// Initiate Line_Catcher class targeted to index the vcf file in real-time
		Line_catcher lines_Caught = new Line_catcher();
		// Initiate TEMPORARY Line_Catcher class to make the code Thread SAFE, used to
		// index the vcf file
		Line_catcher temp_Lines = new Line_catcher();

		// Read the HiC file and get the Combination
		String combination = NhiC_br.readLine();

		while (combination != null) {

			String[] split = combination.split("\t");
			String combo_Num = split[0];
			System.out.println("\nProcessing Combination \t: " + combo_Num);

			// Get the start and end block parameters
			String nhiC_start_Block = split[1];
			String nhiC_end_Block = split[2];

			// Initiate multi-threading phase
			ExecutorService ex = Executors.newFixedThreadPool(23);
			// Get data of HiC start and end blocks
			String nhic_start_Block = nhiC_start_Block.substring(nhiC_start_Block.indexOf(";") + 1,
					nhiC_start_Block.length());
			String nhic_end_Block = nhiC_end_Block.substring(nhiC_end_Block.indexOf(";") + 1, nhiC_end_Block.length());

			/*
			 * Collect all MAF present in the target regions Data collected in List through
			 * classes to ensure thread safety
			 */
			Range MAF_nhiC_start_Block = new Range();
			Range MAF_nhiC_end_Block = new Range();

			System.out.println("\nCollecting SNP Data");

			// Threads to collect MAF from HiC region
			ex.execute(new Thread_collect_MAF(lines, MAF_nhiC_start_Block, nhic_start_Block, lines_Caught, temp_Lines));
			ex.execute(new Thread_collect_MAF(lines, MAF_nhiC_end_Block, nhic_end_Block, lines_Caught, temp_Lines));

			// WAIT till the threads finish
			ex.shutdown();
			while (!ex.isTerminated()) {
			}

			String[] start_split_Range = nhic_start_Block.split(";");
			// Extract data on block
			int start_start = Integer.parseInt(start_split_Range[0]);
			int start_end = Integer.parseInt(start_split_Range[1]);

			int hiC_Start_Size = start_end - start_start;

			String[] split_Range = nhic_end_Block.split(";");
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

			for (Double MAF : MAF_nhiC_start_Block.MAFs) {
				tot_MAF = tot_MAF + MAF;
			}

			for (Double MAF : MAF_nhiC_end_Block.MAFs) {
				tot_MAF = tot_MAF + MAF;
			}

			int tot_Snps = MAF_nhiC_start_Block.MAFs.size() + MAF_nhiC_end_Block.MAFs.size();

			File MAF_output = new File(non_HiC_dir.getAbsolutePath() + "/" + country + ".maf");
			if (!MAF_output.exists()) {
				MAF_output.createNewFile();
				FileWriter fw = new FileWriter(MAF_output);
				BufferedWriter bw = new BufferedWriter(fw);
				bw.write("Combination\tMAF_Tot\tNo_SNPs\tNon_HiC_Size\n");
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

			combination = NhiC_br.readLine();
		}

		NhiC_br.close();
		fr.close();

		File MAF_output = new File(non_HiC_dir.getAbsolutePath() + "/" + country + ".maf");
		FileReader fr_2 = new FileReader(MAF_output);
		BufferedReader br = new BufferedReader(fr_2);

		System.out.println("\n Calculating total Non_HiC cMAF");
		br.readLine();
		String value = br.readLine();

		Double tot_nHiC_MAF = 0.00;
		int tot_nHiC_Snps = 0;
		int tot_nHiC_Size = 0;

		while (value != null) {
			String[] data = value.split("\t");
			Double MAF = Double.parseDouble(data[1]);
			tot_nHiC_MAF = tot_nHiC_MAF + MAF;
			tot_nHiC_Snps = tot_nHiC_Snps + Integer.parseInt(data[2]);
			tot_nHiC_Size = tot_nHiC_Size + Integer.parseInt(data[3]);
			value = br.readLine();
		}

		br.close();
		fr_2.close();

		System.out.println("Completed total HiC cMAF");

		System.out.println("\nWriting output");

		File Final = new File(this.output_Folder.getAbsolutePath() + "/" + country + ".result_nonHIC");
		if (!Final.exists()) {
			Final.createNewFile();
			FileWriter fw = new FileWriter(Final);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write("Non_HiC_SNP_No\tNon_HiC_Tot_MAF\tAverage_Non_HiC\tNon_HiC_bp_Size\tNon_HiC_Density\n");
			bw.flush();
			bw.close();
			fw.close();
		}

		Double nHiC_Avg = tot_nHiC_MAF / tot_nHiC_Snps;
		Double nHiC_Dens = (double) (tot_nHiC_Snps / (double) tot_nHiC_Size);

		FileWriter fw = new FileWriter(Final, true);
		BufferedWriter bw = new BufferedWriter(fw);
		String data = tot_nHiC_Snps + "\t" + tot_nHiC_MAF + "\t" + +nHiC_Avg + "\t" + tot_nHiC_Size + "\t" + nHiC_Dens
				+ "\n";
		bw.write(data);
		bw.flush();
		bw.close();
		fw.close();
		
		System.out.println("\n***COMPLETED***");

	}

}
