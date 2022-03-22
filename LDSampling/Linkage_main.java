package linkage_d;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;

public class Linkage_main {

	// THREE classes are present
	// Main class calls the functions based on the terminal inputs
	// Filter class filters the data based on the program requirements such as MAF
	// and bi-alleles
	// Find LD class calculates LD based on D_prime and R-squared based on the user
	// formats

	public static String proj_Name;

	public static void main(String[] args) throws IOException, InterruptedException {
		File current_Path = new File("");
		File current_Folder = new File(current_Path.getAbsolutePath());
		File input_Folder = new File(current_Folder.getAbsolutePath() + "/input");
		File output_Folder = make_Dir(current_Folder.getAbsolutePath(), "output");

		String[] files = input_Folder.list();
		Hashtable<String, String> map_ped_Names = new Hashtable<String, String>();

		if (args[0].toLowerCase().equals("vcf2ld")) {
			VCF2LD process = new VCF2LD(files, input_Folder, output_Folder);
			process.master_VCF();
		}

		else if (args[0].toLowerCase().equals("hic_split")) {
			Hi_C hic = new Hi_C(input_Folder, output_Folder);
			hic.initiate();
		}
		
		else if (args[0].toLowerCase().equals("split")) {
			VCF_Split hic = new VCF_Split(input_Folder, output_Folder);
			hic.initiate();
		}
		
		else if (args[0].toLowerCase().equals("csplit")) {
			VCF_SplitChristian hic = new VCF_SplitChristian(input_Folder, output_Folder);
			hic.initiate();
		}
		
		else if (args[0].toLowerCase().equals("papb")) {
			Dinghao_PaPb hic = new Dinghao_PaPb(input_Folder, output_Folder);
			hic.initiate();
		}

		else if (args[0].toLowerCase().equals("cmaf")) {
			String bypass_input = input_Folder.getAbsolutePath() + "/check/02/input";
			String bypass_output = input_Folder.getAbsolutePath() + "/check/02/output";
			File by_input_Folder = new File(bypass_input);
			File by_output_Folder = make_Dir(input_Folder.getAbsolutePath() + "/check/02", "output");
			cMAF cmaf = new cMAF(by_input_Folder, by_output_Folder);
			cmaf.initiate();

			// cMAF cmaf = new cMAF(input_Folder, output_Folder);
			// cmaf.initiate();

		} else if (args[0].toLowerCase().equals("hicld")) {
			HiC_LD hicld = new HiC_LD(input_Folder, output_Folder, args[1]);
			hicld.initiate();
			
		} else if (args[0].toLowerCase().equals("nonhicld")) {
			Onlynon_HiC hicld = new Onlynon_HiC(input_Folder, output_Folder, args[1]);
			hicld.initiate();

		} else if (args[0].toLowerCase().equals("ldonly")) {
			HiC_LDonly hicld = new HiC_LDonly(input_Folder, output_Folder, args[1]);
			hicld.initiate();

		} else if (args[0].toLowerCase().equals("summary")) {
			Summary_correct sum = new Summary_correct();
			sum.initiate();
		}

		else if (args[0].toLowerCase().equals("hiccmaf")) {
			Summary_correct sum = new Summary_correct();
			sum.initiate();
		}

		else if (args[0].toLowerCase().equals("hiccclassic")) {
			HiC_LD_Classic hicld = new HiC_LD_Classic(input_Folder, output_Folder);
			hicld.initiate();
		}

		else if (args[0].toLowerCase().equals("fullcmaf")) {
			HiCcmaf hicld = new HiCcmaf(input_Folder, output_Folder);
			hicld.initiate();
		}

		else if (args[0].toLowerCase().equals("outlier")) {
			AVGDENS_outliers outlier = new AVGDENS_outliers(output_Folder);
			outlier.initiate();
		}

		else if (args[0].toLowerCase().equals("nonhic_calc")) {
			File nonHiC_collect = new File(current_Folder.getAbsolutePath() + "/nonHiCs");
			non_HiC_avg_den non_HiC = new non_HiC_avg_den(input_Folder, output_Folder, nonHiC_collect);
			non_HiC.initiate();
		}

		else if (args[0].toLowerCase().equals("qing")) {
			Collect_qing coll = new Collect_qing();
			coll.initiate();
		}

		else {
			File log_Folder = make_Dir(output_Folder.getAbsolutePath(), "logs");
			// get the function passed by user and convert to lower case to prevent user
			// error
			String option = args[0].toLowerCase();

			// gets the names of the MAP and PED files in the input directory
			map_ped_Names = Extraction(files, map_ped_Names);

			// checks if both MAP & PED files were indexed and are present before execution
			if (map_ped_Names.get("checker").equals("1")) {

				Filter_map_ped filter = new Filter_map_ped(map_ped_Names.get("map"), map_ped_Names.get("ped"),
						input_Folder);

				// gets the number of SNPs in the MAP file
				filter.snp_Counter();

				// ensures that the number of SNPs in the MAP file coincide with that present in
				// the PED file
				if (filter.ped_check() == true) {
					// get the number of SNPs present before filtering
					System.out.println("SNPs present\t: " + filter.getCounter());
					int ori_Count = filter.getCounter();
					System.out.println("Initiating\t: Filtering SNPs");
					// gets which of ATGC bases are present in each SNP
					int[][] ATGC = filter.snp_allele_Filter();
					System.out.println();
					// ensures alleles are present after filtering

					if (filter.getSnp_Index().size() > 0) {

						switch (option) {
						case "all":
							// calculates ld for all possible SNP combinations present. Output in the form
							// of a square grid
							System.out.println("Generate all possible LD\n");
							Find_ld all_Process = new Find_ld(filter.getMap_File(), filter.getPed_File(),
									filter.getSnp_Index(), ATGC, ori_Count, output_Folder);
							all_Process.linkage_Process(filter.getId_count(), option);
							break;

						case "range-hic":
							// UNDER CONSTRUCTION
							System.out.println("Generate LD from range file\n");
							System.out.println("Range file\t:" + args[1]);
							Find_ld range_Process = new Find_ld(filter.getMap_File(), filter.getPed_File(),
									filter.getSnp_Index(), ATGC, ori_Count, output_Folder);
							File range_File = new File(current_Folder.getAbsolutePath() + "/input/" + args[1]);
							range_Process.setRange_File(range_File);
							range_Process.setSnp_chr_Index(filter.getSnp_chr_Index());
							range_Process.linkage_Process(filter.getId_count(), option);
							break;

						case "range":
							// calculates LD per chromosome within a user pre-defined base pair distance per
							// SNP
							System.out.println("Generate ranged LD\n");
							Find_ld ranged_Process = new Find_ld(filter.getMap_File(), filter.getPed_File(),
									filter.getSnp_Index(), ATGC, ori_Count, output_Folder);
							// gets the user defined bps distance
							ranged_Process.setRange_Bps(Integer.parseInt(args[1]));
							// passes the filtered HASHTABLE containing SNP NUMBER, Chromosome_number to the
							// LD calculating class
							ranged_Process.setSnp_chr_Index(filter.getSnp_chr_Index());
							// initiates the function to calculate LD
							ranged_Process.linkage_Process(filter.getId_count(), option);
							break;

						case "thread":
							System.out.println("Threaded Generate ranged LD\n");
							Find_ld thread_Range = new Find_ld(filter.getMap_File(), filter.getPed_File(),
									filter.getSnp_Index(), ATGC, ori_Count, output_Folder);
							thread_Range.setRange_Bps(Integer.parseInt(args[1]));
							thread_Range.setSnp_chr_Index(filter.getSnp_chr_Index());
							Thread_chromosomes charm = new Thread_chromosomes(filter, thread_Range, 5, ATGC, ori_Count,
									output_Folder, Integer.parseInt(args[1]));
							charm.Agent();
							break;
						}
						System.out.println("------------------------------");
						System.out.println(" Program Sucessfully Finished");
						System.out.println("------------------------------");
					} else {
						System.out.println("No SNPs present after filtering");
					}

				} else {
					System.out.println("Number of SNPs in MAP file do not coincide with PED file");
				}
			} else {
				System.out.println("Missing MAP or PED file");
			}
		}
	}

	private static File make_Dir(String current_Path, String folder) {
		// if the folder does not exist it will be made newly
		File Folder = new File(current_Path + "/" + folder);

		if (!Folder.exists()) {
			Folder.mkdir();
		}

		return Folder;

	}

	private static Hashtable<String, String> Extraction(String[] files, Hashtable<String, String> map_ped_Names) {
		// gets the names of the MAP and PEd files in the input directory as well as
		// indexes their presence
		// the MAP, PED and CHECKER keys are stored in a HashTable to make searching for
		// the values easily.
		int map = 0;
		int ped = 0;

		for (String file : files) {
			if (file.endsWith(".map")) {
				proj_Name = file.substring(0, file.lastIndexOf("."));
				map_ped_Names.put("map", file);
				map = 1;
			} else if (file.endsWith(".ped")) {
				map_ped_Names.put("ped", file);
				ped = 1;
			}
		}

		// AND gate check for MAP and PED files
		String checker = String.valueOf(ped * map);
		map_ped_Names.put("checker", checker);

		return map_ped_Names;
	}

	private static void Printer(String[] list) {
		// used for troubleshooting arrays, not utilized in the main program
		for (int c = 0; c < list.length; c++) {
			System.out.println(list[c]);
		}
	}

}
