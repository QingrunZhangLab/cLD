package linkage_d;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Stream;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class cMAF {

	File input_Folder;
	File output_Folder;
	Hashtable<String, String> pop_Index;
	Hashtable<String, String> super_pop_Index;

	cMAF(File input, File output) {
		this.input_Folder = input;
		this.output_Folder = output;
		try {
			this.pop_Index = pop_Index();
			System.out.println("Completed\t: Indexing Population\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void file_Check(File file) {
		if (!file.exists()) {
			System.out.println("DOES NOT EXIST: " + file.getAbsolutePath());
			System.exit(0);
		}
	}

	private Hashtable<String, String> pop_Index() throws IOException {
		System.out.println("Initiating\t: Indexing Population");
		String[] file_list = this.input_Folder.list();
		String super_pop_Log = null;
		String super_pop__master_Log = null;

		for (String file : file_list) {
			File check = new File(this.input_Folder.getAbsolutePath() + "/" + file);
			if (!check.isDirectory()) {
				if (file.substring(file.lastIndexOf("."), file.length()).equals(".sup")) {
					super_pop_Log = file;
				} else if (file.substring(file.lastIndexOf("."), file.length()).equals(".xlsx")) {
					super_pop__master_Log = file;
				}
			}
		}

		File super_Pop_Master = new File(this.input_Folder.getAbsolutePath() + "/" + super_pop_Log);
		File pop_Details = new File(this.input_Folder.getAbsolutePath() + "/" + super_pop__master_Log);
		file_Check(super_Pop_Master);
		file_Check(pop_Details);

		FileInputStream fis = new FileInputStream(pop_Details);
		XSSFWorkbook myWorkBook = new XSSFWorkbook(fis);
		XSSFSheet mySheet = myWorkBook.getSheetAt(6);
		Iterator<Row> rowIterator = mySheet.iterator();
		Hashtable<String, String> pop_Index = new Hashtable<String, String>();

		int cell_Count = 0;
		String[] row_Content = new String[2];
		Row row = rowIterator.next();
		while (rowIterator.hasNext()) {
			row = rowIterator.next();
			Iterator<Cell> cellIterator = row.cellIterator();

			while (cellIterator.hasNext()) {
				Cell cell = cellIterator.next();
				if (cell_Count != -1) {

					row_Content[cell_Count] = cell.toString();
					cell_Count++;
				}

				if (cell_Count == 2) {
					pop_Index.put(row_Content[0], row_Content[1]);
					cell_Count = -1;
				}
			}
			cell_Count = 0;
		}

		BufferedReader line_read = new BufferedReader(new FileReader(super_Pop_Master));
		Hashtable<String, String> super_pop_Index = new Hashtable<String, String>();
		String line = line_read.readLine();

		while (line != null) {
			String[] line_Data = line.split("\t");
			super_pop_Index.put(line_Data[0], line_Data[1]);
			line = line_read.readLine();
		}

		this.super_pop_Index = super_pop_Index;

		return pop_Index;
	}

	private ArrayList<String> get_Unique_IS(Hashtable<Integer, String> vcf_pop_Index) {
		ArrayList<String> list = new ArrayList<>(vcf_pop_Index.values());
		LinkedHashSet<String> hashSet = new LinkedHashSet<>(list);
		list = new ArrayList<>(hashSet);

		return list;
	}

	public void initiate() throws IOException {

		String[] files = this.input_Folder.list();
		String pathname = "";
		File hic = new File(pathname);
		File vcf = new File(pathname);

		for (String file : files) {
			File check = new File(this.input_Folder.getAbsolutePath() + "/" + file);
			if (!check.isDirectory()) {
				if (file.substring(file.lastIndexOf("."), file.length()).equals(".txt")) {
					hic = new File(this.input_Folder.getAbsolutePath() + "/" + file);
				} else if (file.substring(file.lastIndexOf("."), file.length()).equals(".vcf")) {
					vcf = new File(this.input_Folder.getAbsolutePath() + "/" + file);
				}
			}
		}

		int line_Count = 0;
		List<Integer> pos_List = new ArrayList<Integer>();
		Hashtable<Integer, Integer> line_pos_List = new Hashtable<Integer, Integer>();
		BufferedReader vcf_File = new BufferedReader(new FileReader(vcf));
		Hashtable<Integer, String> vcf_super_pop_Index = new Hashtable<Integer, String>();

		String vcf_Line = vcf_File.readLine();
		while (vcf_Line.startsWith("##")) {
			vcf_Line = vcf_File.readLine();
			line_Count++;
		}

		String[] headers = vcf_Line.split("\t");
		line_Count++;

		int fixed = line_Count;

		for (int c = 9; c < headers.length; c++) {
			String country = this.pop_Index.get(headers[c]);
			String super_Pop = this.super_pop_Index.get(country);
			vcf_super_pop_Index.put(c, super_Pop);
		}

		ArrayList<String> vcf_super_Pop = get_Unique_IS(vcf_super_pop_Index);
		Hashtable<String, Double> super_pop_cMAF_0_010 = new Hashtable<String, Double>();
		Hashtable<String, Double> super_pop_cMAF_0_005 = new Hashtable<String, Double>();
		Hashtable<String, Double> super_pop_cMAF_0_001 = new Hashtable<String, Double>();

		Hashtable<String, Hashtable<String, Double>> super_Combination_0_010 = new Hashtable<String, Hashtable<String, Double>>();
		Hashtable<String, Hashtable<String, Double>> super_Combination_0_005 = new Hashtable<String, Hashtable<String, Double>>();
		Hashtable<String, Hashtable<String, Double>> super_Combination_0_001 = new Hashtable<String, Hashtable<String, Double>>();

		for (int count = 0; count < vcf_super_Pop.size(); count++) {
			super_pop_cMAF_0_010.put(vcf_super_Pop.get(count), 0.00);
			super_pop_cMAF_0_005.put(vcf_super_Pop.get(count), 0.00);
			super_pop_cMAF_0_001.put(vcf_super_Pop.get(count), 0.00);
		}

		// System.out.println(super_pop_cMAF);

		BufferedReader hic_Log = new BufferedReader(new FileReader(hic));
		String hic_Line = hic_Log.readLine();

		while (hic_Line != null) {

			String[] col_Data = hic_Line.split("\t");

			for (int c = 1; c < col_Data.length; c++) {
				// long start = System.currentTimeMillis();
				if (!pos_List.isEmpty()) {
					Collections.sort(pos_List);
				}

				// System.out.println("Positions: " + line_pos_List);

				line_Count = fixed;
				int catch_Check_1 = 0;
				int catch_Check_2 = 0;
				String[] sub_col_Data = col_Data[c].split(";");

				int min_Range = Integer.parseInt(sub_col_Data[1]);

				int check = line_Count;
				if (!pos_List.isEmpty()) {
					for (int num : pos_List) {
						if (num <= min_Range) {
							check = line_pos_List.get(num);
						}
					}
				}

				line_Count = check;

				int max_Range = Integer.parseInt(sub_col_Data[2]);
				System.out.println("Processing :\t " + "Combination: " + col_Data[0] + "\tRange: " + min_Range + " - "
						+ max_Range);
				// System.out.println("Line: " + line_Count);
				if (!super_Combination_0_010.containsKey(col_Data[c])) {
					int line_Check = 0;

					// while (line_Check != 1) {

					try (BufferedReader br = new BufferedReader(new FileReader(vcf.getAbsolutePath()))) {
						for (int lines = 0; lines < line_Count; lines++) {
							br.readLine();
						}
						vcf_Line = br.readLine();
						while (vcf_Line != null && line_Check != 1) {
								// vcf_Line = lines.skip(line_Count).findFirst().get();
								System.out.print("Line Count: " + line_Count + "\r");
								String[] line_Data = vcf_Line.split("\t");
								// System.out.println(line_Data[2]);
								int pos = Integer.parseInt(line_Data[1]);

								if (pos > min_Range && pos < max_Range) {

									if (snp_Allele_Filter(line_Data) == 1) {
										for (int sup_Increment = 0; sup_Increment < vcf_super_Pop
												.size(); sup_Increment++) {
											String country = vcf_super_Pop.get(sup_Increment);
											Super_pop get_MAF = new Super_pop(country, vcf_super_pop_Index, line_Data);
											Double MAF = get_MAF.return_MAF();

											if (c == 1 && catch_Check_1 == 0) {
												pos_List.add(pos);
												// System.out.println("Position: " + pos + "\t Line Count: " +
												// line_Count);
												line_pos_List.put(pos, line_Count);
												fixed = line_Count;
												catch_Check_1 = 1;
											}

											if (c == 2 && catch_Check_2 == 0) {
												pos_List.add(pos);
												// System.out.println("Position: " + pos + "\t Line Count: " +
												// line_Count);
												line_pos_List.put(pos, line_Count);
												catch_Check_2 = 1;
											}

											// System.out.println("********* "+ MAF);
											if (MAF < 0.01) {
												Double current_cMAF = super_pop_cMAF_0_010.get(country);
												current_cMAF = current_cMAF + MAF;
												super_pop_cMAF_0_010.replace(country, current_cMAF);
											}

											if (MAF < 0.005) {
												Double current_cMAF = super_pop_cMAF_0_005.get(country);
												current_cMAF = current_cMAF + MAF;
												super_pop_cMAF_0_005.replace(country, current_cMAF);
											}

											if (MAF < 0.001) {
												Double current_cMAF = super_pop_cMAF_0_001.get(country);
												current_cMAF = current_cMAF + MAF;
												super_pop_cMAF_0_001.replace(country, current_cMAF);

											}

										}

									}
								} else if (pos > max_Range) {
									pos_List.add(pos);
									// System.out.println("Position at end: " + pos + "\t Line Count: " +
									// line_Count);
									line_pos_List.put(pos, line_Count);
									line_Check = 1;
								}
							line_Count++;
							vcf_Line = br.readLine();
						}
					}

					catch (NoSuchElementException ex) {
						line_Check = 1;
					}
					// System.out.print(line_Count + "\r");
					// }
					super_Combination_0_010.put(col_Data[c], new Hashtable<String, Double>(super_pop_cMAF_0_010));
					super_Combination_0_005.put(col_Data[c], new Hashtable<String, Double>(super_pop_cMAF_0_005));
					super_Combination_0_001.put(col_Data[c], new Hashtable<String, Double>(super_pop_cMAF_0_001));
				} else {
					// System.out.println("**********Caught");
					super_pop_cMAF_0_010 = new Hashtable<String, Double>(super_Combination_0_010.get(col_Data[c]));
					super_pop_cMAF_0_005 = new Hashtable<String, Double>(super_Combination_0_005.get(col_Data[c]));
					super_pop_cMAF_0_001 = new Hashtable<String, Double>(super_Combination_0_001.get(col_Data[c]));
				}
				// System.out.print(line_Count);
				System.out.println("\n" + super_pop_cMAF_0_010);
				System.out.println(super_pop_cMAF_0_005);
				System.out.println(super_pop_cMAF_0_001 + "\n");

				for (int count = 0; count < vcf_super_Pop.size(); count++) {
					String super_pop_Name = vcf_super_Pop.get(count);
					File cMAF_Super_pop = new File(this.output_Folder.getAbsolutePath() + "/"
							+ hic.getName().substring(0, hic.getName().lastIndexOf(".")) + "_" + super_pop_Name
							+ ".cMAF");
					if (!cMAF_Super_pop.exists()) {
						cMAF_Super_pop.createNewFile();
						FileWriter write = new FileWriter(cMAF_Super_pop, true);
						BufferedWriter line_Writer = new BufferedWriter(write);
						line_Writer.write("C_No\tChromosome\tRange\t0.01\t0.005\t0.001\n");
						line_Writer.flush();
						line_Writer.close();
						write.close();
					}

					FileWriter write = new FileWriter(cMAF_Super_pop, true);
					BufferedWriter line_Writer = new BufferedWriter(write);

					line_Writer.write(col_Data[0] + "\t" + sub_col_Data[0] + "\t" + sub_col_Data[1] + ";"
							+ sub_col_Data[2] + "\t" + super_pop_cMAF_0_010.get(vcf_super_Pop.get(count)) + "\t"
							+ super_pop_cMAF_0_005.get(vcf_super_Pop.get(count)) + "\t"
							+ super_pop_cMAF_0_001.get(vcf_super_Pop.get(count)) + "\n");

					line_Writer.flush();
					line_Writer.close();
					write.close();
					super_pop_cMAF_0_010.replace(vcf_super_Pop.get(count), 0.00);
					super_pop_cMAF_0_005.replace(vcf_super_Pop.get(count), 0.00);
					super_pop_cMAF_0_001.replace(vcf_super_Pop.get(count), 0.00);
				}
				// System.out.println("Super: "+super_Combination_0_010);
				// long end =System.currentTimeMillis();
				// System.out.println("Time: " + (end-start));
			}

			hic_Line = hic_Log.readLine();
		}

		System.out.println("\ncMAF Completed");

	}

	private int snp_Allele_Filter(String[] line_Split) {
		int valid = 0;
		String ref = line_Split[3];
		String alt = line_Split[4];
		if (alt.length() == 1 && ref.length() == 1) {
			valid = 1;
		}

		return valid;
	}

}
