package linkage_d;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.LinkedHashSet;
import java.util.NoSuchElementException;
import java.util.stream.Stream;

public class Processing_VCF {

	File input_Folder;
	File output_Folder;
	Hashtable<String, String> pop_Index;
	Hashtable<String, String> super_pop_Index;
	Double MAF;
	int BP_range;

	Processing_VCF(File input_Folder, File output_Folder, Hashtable<String, String> pop_Index,
			Hashtable<String, String> super_pop_Index, Double MAF, int BP_range) {
		this.input_Folder = input_Folder;
		this.output_Folder = output_Folder;
		this.pop_Index = pop_Index;
		this.super_pop_Index = super_pop_Index;
		this.MAF = MAF;
		this.BP_range = BP_range;
	}

	private static File make_Dir(String current_Path, String folder) {
		// if the folder does not exist it will be made newly
		File Folder = new File(current_Path + "/" + folder);

		if (!Folder.exists()) {
			Folder.mkdir();
		}

		return Folder;

	}

	private ArrayList<String> get_Unique_IS(Hashtable<Integer, String> vcf_pop_Index) {
		ArrayList<String> list = new ArrayList<>(vcf_pop_Index.values());
		LinkedHashSet<String> hashSet = new LinkedHashSet<>(list);
		list = new ArrayList<>(hashSet);

		return list;
	}

	private int snp_Allele_Filter(String line) {
		int valid = 0;
		String[] line_Split = line.split("\t");
		if (line_Split.length > 1) {
			String ref = line_Split[3];
			String alt = line_Split[4];
			if (alt.length() == 1 && ref.length() == 1) {
				valid = 1;
			}
		}
		return valid;
	}

	private int snp2_Allele_Filter(String line, int Range) {
		int snp_Valid = 0;
		int snp_Range = 0;
		String[] line_Split = line.split("\t");
		int AND = 0;
		if (line_Split.length > 1) {
			String ref = line_Split[3];
			String alt = line_Split[4];
			int snp_Pos = Integer.parseInt(line_Split[1]);

			if (alt.length() == 1 && ref.length() == 1) {
				snp_Valid = 1;
			}

			if (snp_Pos < Range) {
				snp_Range = 1;
			}

			AND = snp_Valid * snp_Range;
		}
		return AND;
	}

	public void process_VCF(String name_VCF) throws IOException {
		String pathname = this.input_Folder.getAbsolutePath() + "/" + name_VCF;
		File yield = make_Dir(this.output_Folder.getAbsolutePath(), name_VCF);
		File vcf = new File(pathname);

		File log_File = new File(yield.getAbsolutePath() + "/" + name_VCF + ".log");
		if (!log_File.exists()) {
			log_File.createNewFile();
		}

		// Hashtable<Integer, String> vcf_pop_Index = new Hashtable<Integer, String>();
		Hashtable<Integer, String> vcf_super_pop_Index = new Hashtable<Integer, String>();
		Hashtable<String, String> super_Populations = new Hashtable<String, String>();
		System.out.println("Processing\t: " + vcf.getName());
		BufferedReader read_Line = new BufferedReader(new FileReader(vcf));
		String line = read_Line.readLine();
		int line_Count = 0;

		while (line.startsWith("##")) {
			line = read_Line.readLine();
			line_Count++;
		}

		String[] headers = line.split("\t");

		for (int c = 9; c < headers.length; c++) {
			String country = this.pop_Index.get(headers[c]);
			String super_Pop = this.super_pop_Index.get(country);
			vcf_super_pop_Index.put(c, super_Pop);
			// vcf_pop_Index.put(c, country);
		}
		// System.out.println(super_pop_Index.size());
		// ArrayList<String> vcf_Countries = get_Unique_IS(vcf_pop_Index);
		ArrayList<String> vcf_super_Pop = get_Unique_IS(vcf_super_pop_Index);

		// System.out.println(vcf_Super_Pop.size());

		line_Count++;

		ArrayList<String> snps_All;
		String snps_one;

		int check_snp1 = 0;
		int check_snp2 = 0;
		int snp1_Count = line_Count;

		while (check_snp1 != 1) {

			ArrayList<String> super_POP = new ArrayList<String>(vcf_super_Pop);
			// System.out.println("SIZE: "+countries.size());

			try (Stream<String> lines = Files.lines(Paths.get(pathname))) {
				snps_one = lines.skip(snp1_Count).findFirst().get();

				if (snp_Allele_Filter(snps_one) == 1) {

					String[] line_snp_1 = snps_one.split("\t");
					Integer pos_Range = Integer.valueOf(line_snp_1[1]) + this.BP_range;
					// System.out.println(line_snp_1[2]);

					ArrayList<String> remove = new ArrayList<>();

					for (int c = 0; c < super_POP.size(); c++) {
						String country = super_POP.get(c);
						Super_pop check_snp = new Super_pop(country, vcf_super_pop_Index, line_snp_1, this.MAF);
						if (check_snp.snp_Check() == 1) {
							remove.add(country);
							// countries.remove(new String(country));
						} else {
							// System.out.println("***********Good snp_1*****************");
						}
					}

					for (int c = 0; c < remove.size(); c++) {
						String rem = remove.get(c);
						super_POP.remove(new String(rem));
					}

					int snp2_Count = snp1_Count;

					if (super_POP.size() >= 1) {

						while (check_snp2 != 1) {
							ArrayList<String> super_POP2 = new ArrayList<String>(super_POP);
							String snps_two;

							try (Stream<String> lines_2 = Files.lines(Paths.get(pathname))) {
								snps_two = lines_2.skip(snp2_Count).findFirst().get();

								if (snp2_Allele_Filter(snps_two, pos_Range) == 1) {
									String[] line_snp_2 = snps_two.split("\t");
									ArrayList<String> remove_2 = new ArrayList<>();
									for (int c = 0; c < super_POP2.size(); c++) {
										String country = super_POP2.get(c);
										Super_pop check_snp = new Super_pop(country, vcf_super_pop_Index, line_snp_2,
												this.MAF);
										if (check_snp.snp_Check() == 1) {
											remove_2.add(country);
											// countries2.remove(new String(country));
										} else {
											// System.out.println("*************Good snp_2**************");
										}
									}
									for (int c = 0; c < remove_2.size(); c++) {
										String rem = remove_2.get(c);
										super_POP2.remove(new String(rem));
									}
									if (super_POP2.size() >= 1) {
										// System.out.println("SNP1: " + line_snp_1[2] + "\t" + "SNP2: " +
										// line_snp_2[2]);
										for (int c = 0; c < super_POP2.size(); c++) {
											Super_pop GBR = new Super_pop(yield, super_POP2.get(c), vcf_super_pop_Index,
													line_snp_1, line_snp_2, this.MAF);
											GBR.incite(log_File);
										}
									}

								}
								// System.out.println("LEAVE");
							} catch (NoSuchElementException ex) {
								check_snp2 = 1;
							}
							snp2_Count++;
							continue;

						}
					}
				}

			}

			catch (NoSuchElementException ex) {
				check_snp1 = 1;
			}
			snp1_Count++;
			check_snp2 = 0;
			continue;

		}

		read_Line.close();
		System.out.println("Completed\t: " + vcf.getName());

	}

}
