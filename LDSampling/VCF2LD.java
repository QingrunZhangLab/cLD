package linkage_d;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.LineNumberReader;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.DataFormatter;

public class VCF2LD {

	File input_Folder;
	File output_Folder;
	File pop_Details;
	File super_Pop_Master;
	Double MAF;
	int BP_range;
	int pop_Indexing;
	ArrayList<String> vcf_Files;
	Hashtable<String, String> pop_Index;
	Hashtable<String, String> super_pop_Index;

	VCF2LD(String[] files, File input_Folder, File output_Folder) {
		this.input_Folder = input_Folder;
		this.output_Folder = output_Folder;
		try {
			index_Properties(files);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (pop_Indexing == 1) {
			try {
				this.pop_Index = pop_Index();
				System.out.println("Completed\t: Indexing Population");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		this.vcf_Files = index_VCF(files);

	}

	private ArrayList<String> index_VCF(String[] list) {
		ArrayList<String> vcf_Files = new ArrayList<String>();

		for (int c = 0; c < list.length; c++) {
			if (list[c].substring(list[c].lastIndexOf("."), list[c].length()).equals(".vcf")) {
				vcf_Files.add(list[c]);
			}
		}

		System.out.println("VCF Files\t: " + vcf_Files.size());

		return vcf_Files;
	}

	public void master_VCF() throws IOException {
		
		int cores = 1;
		if (cores > 1) {
			cores = cores - 1;			
		}
		//ExecutorService ex = Executors.newFixedThreadPool(cores);
		//System.out.println("\nNumber of Cores\t: " + cores + "\n");
		
		for (int c = 0; c < vcf_Files.size(); c++) {
			System.out.println("\nProcessing\t: " + (c+1) +" of " + (vcf_Files.size()) + "\n");
			String name_VCF = vcf_Files.get(c);
			VCF_Split vcf = new VCF_Split(this.input_Folder, this.output_Folder, this.pop_Index, this.super_pop_Index, 
					this.MAF, this.BP_range);
			//try {
				//vcf.process_VCF(name_VCF);
			//} catch (IOException e) {
				// TODO Auto-generated catch block
			//	e.printStackTrace();
			//}
		}
		
		//ex.shutdown();
		//while(!ex.isTerminated()) {}
		
		System.out.println("\n******************\tPROGRAM COMPLETED\t******************");

	}
	
	private static File make_Dir(String current_Path, String folder) {
		// if the folder does not exist it will be made newly
		File Folder = new File(current_Path + "/" + folder);

		if (!Folder.exists()) {
			Folder.mkdir();
		}

		return Folder;

	}

	/*private void process_VCF(String name_VCF) throws IOException {
		String pathname = this.input_Folder.getAbsolutePath() + "/" + name_VCF;
		File yield = make_Dir(this.output_Folder.getAbsolutePath(), name_VCF);
		File vcf = new File(pathname);
		
		File log_File = new File(yield.getAbsolutePath() + "/" + name_VCF + ".log");
		if (!log_File.exists()) {
			log_File.createNewFile();
		}

		
		//Hashtable<Integer, String> vcf_pop_Index = new Hashtable<Integer, String>();
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
			//vcf_pop_Index.put(c, country);
		}
		//System.out.println(super_pop_Index.size());
		//ArrayList<String> vcf_Countries = get_Unique_IS(vcf_pop_Index);
		ArrayList<String> vcf_super_Pop = get_Unique_IS(vcf_super_pop_Index);

		
		// System.out.println(vcf_Super_Pop.size());
		
		line_Count++;

		ArrayList<String> snps_All;
		String snps_one;

		int check_snp1 = 0;
		int check_snp2 = 0;
		int snp1_Count = line_Count;
		
		while (check_snp1 != 1) {
			
			ArrayList <String> super_POP = new ArrayList <String>(vcf_super_Pop);
			//System.out.println("SIZE: "+countries.size());

			try (Stream<String> lines = Files.lines(Paths.get(pathname))) {
				snps_one = lines.skip(snp1_Count).findFirst().get();

				if (snp_Allele_Filter(snps_one) == 1) {
					
					String[] line_snp_1 = snps_one.split("\t");
					Integer pos_Range = Integer.valueOf(line_snp_1[1])+ this.BP_range;			
					//System.out.println(line_snp_1[2]);
					
					ArrayList <String> remove = new ArrayList<>();
					
					for(int c = 0; c<super_POP.size(); c++) {	
						String country = super_POP.get(c);
						Super_pop check_snp = new Super_pop(country,vcf_super_pop_Index,line_snp_1,this.MAF);
						if (check_snp.snp_Check()==1) {
							remove.add(country);
							//countries.remove(new String(country));
						}
						else {
							//System.out.println("***********Good snp_1*****************");
						}
					}
					
					for(int c = 0; c<remove.size(); c++) {
						String rem = remove.get(c);
						super_POP.remove(new String(rem));
					}
					
					int snp2_Count = snp1_Count;
					
					if(super_POP.size()>=1) {

					while (check_snp2 != 1) {
						ArrayList <String> super_POP2 = new ArrayList <String>(super_POP);
						String snps_two;

						try (Stream<String> lines_2 = Files.lines(Paths.get(pathname))) {
							snps_two = lines_2.skip(snp2_Count).findFirst().get();

							if (snp2_Allele_Filter(snps_two, pos_Range) == 1) {
								String[] line_snp_2 = snps_two.split("\t");
								ArrayList <String> remove_2 = new ArrayList<>();
								for(int c = 0; c<super_POP2.size(); c++) {
									String country = super_POP2.get(c);
									Super_pop check_snp = new Super_pop(country,vcf_super_pop_Index,line_snp_2,this.MAF);
									if (check_snp.snp_Check()==1) {
										remove_2.add(country);
										//countries2.remove(new String(country));
									}
									else {
										//System.out.println("*************Good snp_2**************");
									}
								}
								for(int c = 0; c<remove_2.size(); c++) {
									String rem = remove_2.get(c);
									super_POP2.remove(new String(rem));
								}
								if(super_POP2.size()>=1) {
								//System.out.println("SNP1: " + line_snp_1[2] + "\t" + "SNP2: " + line_snp_2[2]);
								for(int c = 0; c<super_POP2.size(); c++) {
								Super_pop GBR = new Super_pop(yield,super_POP2.get(c),vcf_super_pop_Index,line_snp_1,line_snp_2,this.MAF);
								GBR.incite(log_File);
								}
								}

							}
							//System.out.println("LEAVE");
						}
						catch (NoSuchElementException ex) {
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

	} */

	private ArrayList<String> get_Unique_SS(Hashtable<String, String> vcf_pop_Index) {
		ArrayList<String> list = new ArrayList<>(vcf_pop_Index.values());
		LinkedHashSet<String> hashSet = new LinkedHashSet<>(list);
		list = new ArrayList<>(hashSet);

		return list;
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
		String ref = line_Split[3];
		String alt = line_Split[4];
		if (alt.length() == 1 && ref.length() == 1) {
			valid = 1;
		}

		return valid;
	}
	
	private int snp2_Allele_Filter(String line, int Range) {
		int snp_Valid = 0;
		int snp_Range = 0;
		String[] line_Split = line.split("\t");
		String ref = line_Split[3];
		String alt = line_Split[4];
		int snp_Pos = Integer.parseInt(line_Split[1]);
		
		if (alt.length() == 1 && ref.length() == 1) {
			snp_Valid = 1;
		}
		
		if(snp_Pos < Range) {
			snp_Range = 1;
		}
		
		int AND = snp_Valid * snp_Range;

		return AND;
	}

	private Hashtable<String, String> pop_Index() throws IOException {
		System.out.println("Initiating\t: Indexing Population");
		FileInputStream fis = new FileInputStream(pop_Details);
		XSSFWorkbook myWorkBook = new XSSFWorkbook(fis);
		XSSFSheet mySheet = myWorkBook.getSheetAt(6);
		// System.out.println(mySheet.getSheetName());
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
				// System.out.print(cell.toString() + ";");
			}
			cell_Count = 0;
		}

		BufferedReader line_read = new BufferedReader(new FileReader(this.super_Pop_Master));
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

	private void Printer(String[] list) {
		// used for troubleshooting arrays, not utilized in the main program
		for (int c = 0; c < list.length; c++) {
			System.out.println(list[c]);
		}
	}

	private void index_Properties(String[] list) throws FileNotFoundException {
		BufferedReader line_read = null;
		for (int c = 0; c < list.length; c++) {
			if (list[c].substring(list[c].lastIndexOf("."), list[c].length()).equals(".properties")) {
				line_read = new BufferedReader(
						new FileReader(new File(input_Folder.getAbsolutePath() + "/" + list[c])));
				break;
			}
		}

		try {
			String line = line_read.readLine();
			while (line != null) {
				if (!line.startsWith("#")) {
					String[] criteria = line.split(":");
					switch (criteria[0]) {
					case "Sample_population_file":
						if (!criteria[1].toLowerCase().equals("none")) {
							this.pop_Details = new File(input_Folder.getAbsolutePath() + "/" + criteria[1]);
							this.pop_Indexing = 1;
						} else {
							this.pop_Indexing = 0;
						}
						break;
					case "Minor_Allele_Frequency":
						this.MAF = Double.parseDouble(criteria[1]);
						break;
					case "Base_Pair_range":
						this.BP_range = Integer.parseInt(criteria[1]);
						break;
					case "Super_population":
						this.super_Pop_Master = new File(input_Folder.getAbsolutePath() + "/" + criteria[1]);
						break;
					}
				}
				line = line_read.readLine();
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}

/*class Threadscape_2 implements Runnable{
	
	File input_Folder;
	File output_Folder;
	Hashtable<String, String> pop_Index;
	Hashtable<String, String> super_pop_Index;
	Double MAF;
	int BP_range;
	String name_VCF;
	
	Threadscape_2(File input_Folder, File output_Folder, Hashtable<String, String> pop_Index, Hashtable<String, String> super_pop_Index, 
			Double MAF, int BP_range, String name_VCF){
		this.input_Folder = input_Folder;
		this.output_Folder = output_Folder;
		this.pop_Index = pop_Index;
		this.super_pop_Index = super_pop_Index;
		this.MAF = MAF;
		this.BP_range = BP_range;
		this.name_VCF = name_VCF;
	}
	
	public void run() {
		VCF_Split vcf = new VCF_Split(this.input_Folder, this.output_Folder, this.pop_Index, this.super_pop_Index, 
				this.MAF, this.BP_range);
		try {
			vcf.process_VCF(this.name_VCF);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/*
		Processing_VCF vcf = new Processing_VCF(this.input_Folder, this.output_Folder, this.pop_Index, this.super_pop_Index, 
				this.MAF, this.BP_range);
		try {
			vcf.process_VCF(this.name_VCF);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
/*	}
	
}*/
