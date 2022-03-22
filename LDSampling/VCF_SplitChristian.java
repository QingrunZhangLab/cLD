package linkage_d;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
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

public class VCF_SplitChristian {

	File input_Folder;
	File output_Folder;
	Hashtable<String, String> pop_Index;
	Hashtable<String, String> super_pop_Index;
	Double MAF;
	int BP_range;

	VCF_SplitChristian(File input_Folder, File output_Folder, Hashtable<String, String> pop_Index,
			Hashtable<String, String> super_pop_Index, Double MAF, int BP_range) {
		this.input_Folder = input_Folder;
		this.output_Folder = output_Folder;
		this.pop_Index = pop_Index;
		this.super_pop_Index = super_pop_Index;
		this.MAF = MAF;
		this.BP_range = BP_range;
	}
	
	VCF_SplitChristian(File input_Folder, File output_Folder) {
		this.input_Folder = input_Folder;
		this.output_Folder = output_Folder;
		try {
			this.pop_Index = pop_Index();
			System.out.println("Completed\t: Indexing Population\n");
		} catch (IOException e) {
			// TODO Auto-generated catch block
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
		myWorkBook.close();
		line_read.close();
		return pop_Index;
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

	private void Printer(String[] list) {
		// used for troubleshooting arrays, not utilized in the main program
		for (int c = 0; c < list.length; c++) {
			System.out.println(list[c]);
		}
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

	public void vcf_Process(String name_VCF) throws IOException {
		String pathname = this.input_Folder.getAbsolutePath() + "/" + name_VCF;
		File vcf = new File(pathname);

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
		// Printer(headers);

		for (int c = 10; c < headers.length; c++) {
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
		String snps_one = read_Line.readLine();

		int check_snp1 = 0;
		int check_snp2 = 0;
		int snp1_Count = line_Count;

		while (snps_one != null) {
			System.out.print("Line Number\t: " + line_Count + "\r");
			//if (line_Count > 2559627) {
				ArrayList<String> super_POP = new ArrayList<String>(vcf_super_Pop);
				// System.out.println("SIZE: "+countries.size());

				// try (Stream<String> lines = Files.lines(Paths.get(pathname))) {
				// snps_one = lines.skip(snp1_Count).findFirst().get();

				//if (snp_Allele_Filter(snps_one) == 1) {

					String[] line_snp_1 = snps_one.split("\t");
					// Integer pos_Range = Integer.valueOf(line_snp_1[1]) + this.BP_range;
					// System.out.println(line_snp_1[2]);
					
					//ArrayList<String> remove = new ArrayList<>();

					/*for (int c = 0; c < super_POP.size(); c++) {
						String country = super_POP.get(c);
						Super_pop check_snp = new Super_pop(country, vcf_super_pop_Index, line_snp_1, 0.005);
						if (check_snp.snp_Check() == 1) {
							remove.add(country);
							// countries.remove(new String(country));
						} else {
							 //System.out.println("***********Good snp_1*****************");
						}
					}

					for (int c = 0; c < remove.size(); c++) {
						String rem = remove.get(c);
						super_POP.remove(new String(rem));
					}
 				*/
					if (super_POP.size() >= 1) {
						for (int c = 0; c < super_POP.size(); c++) {
							Super_pop GBR = new Super_pop(name_VCF, this.output_Folder, super_POP.get(c),
									vcf_super_pop_Index, line_snp_1, headers);
							GBR.write_Country_Chr();
						}

					}
				//}
			//}
			snps_one = read_Line.readLine();
			line_Count++;
		}

		read_Line.close();
		System.out.println("Completed\t: " + vcf.getName());

	}

}
