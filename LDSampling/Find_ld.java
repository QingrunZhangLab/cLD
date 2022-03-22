package linkage_d;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class Find_ld extends Linkage_main {
	
	private File map_File;
	private File ped_File;
	private File output_Folder;
	private int ori_Count;
	private int counter;
	private int [][] ATGC;
	private File log;
	
	//HASHTABLE contains SNP NUMBER, SNP NAME & SNP BP distance
	private Hashtable <Integer, Hashtable <String, Integer>> filt_snp_Index = 
			new Hashtable <Integer, Hashtable <String, Integer>>();
	private Double D_prime;
	private Double rr;
	
	private File range_File;
	//HASHTABLE contains SNP NUMBER, Chromosome_number
	private Hashtable <Integer, Integer> snp_chr_Index = new Hashtable <Integer, Integer>();
	
	public Hashtable<Integer, Integer> getSnp_chr_Index() {
		return snp_chr_Index;
	}

	private int range_Bps;
	

	public void setRange_Bps(int range_Bps) {
		this.range_Bps = range_Bps;
	}

	public void setSnp_chr_Index(Hashtable<Integer, Integer> snp_chr_Index) {
		this.snp_chr_Index = snp_chr_Index;
	}

	public File getRange_File() {
		return range_File;
	}

	public void setRange_File(File range_File) {
		this.range_File = range_File;
	}

	Find_ld(File map_File, File ped_File, Hashtable <Integer, Hashtable <String, Integer> > snp_Index, 
			int [][] ATGC, int ori_Count, File output_Folder){
		this.map_File = map_File;
		this.ped_File = ped_File;
		this.output_Folder = output_Folder;
		this.ATGC = ATGC;
		this.filt_snp_Index = snp_Index;
		this.ori_Count = ori_Count;
		this.counter = this.filt_snp_Index.size();
		
	}
	
	public void linkage_Process(int id_count, String option) throws IOException {
		Enumeration snps = filt_snp_Index.keys();
		ArrayList <Integer> snp_Positions = new ArrayList <Integer>();
		System.out.println("Number of Individuals\t: " + id_count);
		
		while (snps.hasMoreElements()) {
			snp_Positions.add((Integer) snps.nextElement());
		}
		
		Collections.sort(snp_Positions);
		switch(option) {
		
			case "all":
				write_All(snp_Positions, id_count);
				break;
			
			case "range-hic":
				range_File_Reader();
				break;
			
			case "range":
				range_Write(snp_Positions, id_count);
				//range_File_Reader();
				break;
		
		}
		
		
	}
	
	public ArrayList <Integer> get_snp_Positions() {
		Enumeration snps = filt_snp_Index.keys();
		ArrayList <Integer> snp_Positions = new ArrayList <Integer>();
		
		while (snps.hasMoreElements()) {
			snp_Positions.add((Integer) snps.nextElement());
		}
		Collections.sort(snp_Positions);
		return snp_Positions;
		
	}
	
	
	public int get_Distance(int snp_Count) {
		
		Hashtable <String, Integer> snp_name_dist = filt_snp_Index.get(snp_Count);

		
		Enumeration data = snp_name_dist.keys();
		ArrayList <String> data_list = new ArrayList <String>();
		
		while (data.hasMoreElements()) {
			data_list.add((String) data.nextElement());
		}
		ArrayList <String> snp_name = data_list;

		int snp_Dist = snp_name_dist.get(snp_name.get(0));
		
		return snp_Dist;
	}
	
	public void log_Println(String write, File file) throws IOException {
		FileWriter writer = new FileWriter(file,true);
		BufferedWriter buf = new BufferedWriter(writer);
		PrintWriter line_printer = new PrintWriter(buf);
		line_printer.printf("%s" + "%n", write);
		buf.close();
		line_printer.close();
	}
	
	
	public void thread_Write(ArrayList <Integer> snp_Positions, int id_count, int chr) throws IOException {
		
			
		
		
			int current = chr;
			String file_name = Linkage_main.proj_Name + "_" + Integer.toString(current) + ".ld";
			File log = new File(output_Folder.getAbsolutePath() + "/logs/" + file_name + ".log");
			this.log = log;
			file_Creator(log);
			log_Println("Range (bps)\t\t: " + range_Bps + "\n",this.log);
			System.out.println("Chromosome: " + current);
			log_Println("Chromosome: " + current, this.log);
			log_Println("\n\t***************************************************\n",this.log);
			int snp_Count = 0;
			
			
			File range_Output = new File(output_Folder.getAbsolutePath() + "/" + file_name);
			file_Creator(range_Output);
			
			FileWriter writer_range = new FileWriter(range_Output,true);
			BufferedWriter br_range = new BufferedWriter(writer_range);
			PrintWriter line_printer = new PrintWriter(br_range);
			line_printer.printf("%s" + "%n","SNP_1|SNP_2 \tD_Prime \tR-Squared");
			
			while(snp_Count < snp_Positions.size() && snp_chr_Index.containsKey(snp_Count)) {
				int current_snp_ch = snp_chr_Index.get(snp_Positions.get(snp_Count));

				int snp_Dist = get_Distance(snp_Count);
				int check_Dist = snp_Dist + this.range_Bps;
				if(current_snp_ch == current) {
					int snp_2_Count = 0;

				
					while(snp_2_Count < snp_Positions.size() && snp_chr_Index.containsKey(snp_2_Count)) {
						int current_snp_2_ch = snp_chr_Index.get(snp_Positions.get(snp_2_Count));

						int snp_2_Dist = get_Distance(snp_2_Count);
						if (current_snp_2_ch == current && snp_2_Dist < check_Dist) {
							
							//System.out.println("SNPS: " + snp_Count + snp_2_Count);
							snp_Pair(snp_Count, snp_2_Count, snp_Positions, id_count);
							ArrayList <String> snp_prime_data = snp_Name_Extract(snp_Positions, snp_Count);
							ArrayList <String> snp_2_prime_data = snp_Name_Extract(snp_Positions, snp_2_Count);
							String snp_Duo = snp_prime_data.get(0) + "_" + snp_2_prime_data.get(0);
							line_printer.print(snp_Duo + "\t" + round_Off(this.D_prime) + "\t" + round_Off(this.rr) + "\n");
							line_printer.flush();
							log_Println("\n\t***************************************************\n",this.log);
						}
						snp_2_Count ++;
					}
	
				}
				snp_Count ++;
			}
			line_printer.close();
			br_range.close();
			writer_range.close();
			
		
		
		
	}
	
	
	public void range_Write(ArrayList <Integer> snp_Positions, int id_count) throws IOException {
		ArrayList <Integer> chromosomes = chromosome_Filter(snp_chr_Index);
		System.out.println("Range (bps)\t\t: " + range_Bps + "\n");
		

		
		for(int count = 0; count<chromosomes.size(); count++) {
			int current = chromosomes.get(count);
			String file_name = Linkage_main.proj_Name + "_" + Integer.toString(current) + ".ld";
			File log = new File(output_Folder.getAbsolutePath() + "/logs/" + file_name + ".log");
			file_Creator(log);
			this.log = log;
			System.out.println("Chromosome: " + current);
			System.out.println("\n\t***************************************************\n");
			log_Println("Chromosome: " + current, this.log);
			log_Println("\n\t***************************************************\n",this.log);
			int snp_Count = 0;
	
			File range_Output = new File(output_Folder.getAbsolutePath() + "/" + file_name);
			file_Creator(range_Output);
			
			FileWriter writer_range = new FileWriter(range_Output,true);
			BufferedWriter br_range = new BufferedWriter(writer_range);
			PrintWriter line_printer = new PrintWriter(br_range);
			line_printer.printf("%s" + "%n","SNP_1|SNP_2 \tD_Prime \tR-Squared");
			
			while(snp_Count < snp_Positions.size() && snp_chr_Index.containsKey(snp_Count)) {
				int current_snp_ch = snp_chr_Index.get(snp_Positions.get(snp_Count));

				int snp_Dist = get_Distance(snp_Count);
				int check_Dist = snp_Dist + this.range_Bps;
				if(current_snp_ch == current) {
					int snp_2_Count = 0;

				
					while(snp_2_Count < snp_Positions.size() && snp_chr_Index.containsKey(snp_2_Count)) {
						int current_snp_2_ch = snp_chr_Index.get(snp_Positions.get(snp_2_Count));

						int snp_2_Dist = get_Distance(snp_2_Count);
						if (current_snp_2_ch == current && snp_2_Dist < check_Dist) {
							
							//System.out.println("SNPS: " + snp_Count + snp_2_Count);
							snp_Pair(snp_Count, snp_2_Count, snp_Positions, id_count);
							ArrayList <String> snp_prime_data = snp_Name_Extract(snp_Positions, snp_Count);
							ArrayList <String> snp_2_prime_data = snp_Name_Extract(snp_Positions, snp_2_Count);
							String snp_Duo = snp_prime_data.get(0) + "_" + snp_2_prime_data.get(0);
							line_printer.print(snp_Duo + "\t" + round_Off(this.D_prime) + "\t" + round_Off(this.rr) + "\n");
							line_printer.flush();
							System.out.println("\n\t***************************************************\n");
							log_Println("\n\t***************************************************\n",this.log);
						}
						snp_2_Count ++;
					}
	
				}
				snp_Count ++;
			}
			line_printer.close();
			br_range.close();
			writer_range.close();
			
		}
		
		
	}
	
	
	public ArrayList <Integer> chromosome_Filter(Hashtable <Integer,Integer> HASH) {
		Enumeration data = HASH.keys();
		ArrayList <Integer> data_list = new ArrayList <Integer>();
		
		while (data.hasMoreElements()) {
			int chr_num = HASH.get(data.nextElement());
			if(!data_list.contains(chr_num)) {
				data_list.add(chr_num);
			}
		}
		
		Collections.sort(data_list);
		
		return data_list;
	}
	
	
	
	public void range_File_Reader() throws FileNotFoundException {
		//UNDER CONSTRUCTION
		Scanner line_Read = new Scanner(range_File);
		String [] line = line_Read.nextLine().split("\t");
		Printer(line);
		String file_Name = line[0] + "_" + Linkage_main.proj_Name;
		System.out.println("File Name: "+file_Name);
		String [] range_one = line[1].split(";");
		String [] range_two = line[2].split(";");
		
		int chr_one = Integer.parseInt(range_one[0]);
		int min_one = Integer.parseInt(range_one[1]);
		int max_one = Integer.parseInt(range_one[2]);
		
		ArrayList <String> snp_Locs = Double_hash_2_Arraylist(filt_snp_Index);

		System.out.println(snp_chr_Index);
		System.out.println(filt_snp_Index);
		
		/*while(line_Read.hasNext()) {
			String [] line = line_Read.nextLine().split("\t");
			Printer(line);
		}*/
		
	}
	
	public ArrayList <String> Double_hash_2_Arraylist(Hashtable <Integer, Hashtable <String, Integer>> HASH) {
		Enumeration data = HASH.keys();
		ArrayList <String> data_list = new ArrayList <String>();
		
		while (data.hasMoreElements()) {
			data_list.add((String) data.nextElement());
		}
		
		return data_list;
	}
	
	public void write_All(ArrayList <Integer> snp_Positions, int id_count) throws IOException {
		
		String Dp_File_Name = Linkage_main.proj_Name + "_D_Prime_all.ld";
		String RR_File_Name = Linkage_main.proj_Name + "_R_Square_all.ld";
		
		File dprime_Output = new File(output_Folder.getAbsolutePath() + "/" + Dp_File_Name);
		File r_Output = new File(output_Folder.getAbsolutePath() + "/" + RR_File_Name);
		
		file_Creator(dprime_Output);
		file_Creator(r_Output);
		
		FileWriter writer_d = new FileWriter(dprime_Output,true);
		BufferedWriter br_d = new BufferedWriter(writer_d);
		PrintWriter line_printer_d = new PrintWriter(br_d);
		
		FileWriter writer_r = new FileWriter(r_Output,true);
		BufferedWriter br_r = new BufferedWriter(writer_r);
		PrintWriter line_printer_r = new PrintWriter(br_r);
		
		line_printer_d.printf("%s","\t");
		line_printer_r.printf("%s","\t");
		
		for(int snp1 = 0; snp1 < snp_Positions.size(); snp1++) {
			
			ArrayList <String> snp_prime_data = snp_Name_Extract(snp_Positions, snp1);
			line_printer_d.print(snp_prime_data.get(0) + "\t");
			line_printer_r.print(snp_prime_data.get(0) + "\t");
			
		}
		
		line_printer_d.print("\n");
		line_printer_r.print("\n");

		for(int snp1 = 0; snp1 < snp_Positions.size(); snp1++) {
			ArrayList <String> snp_prime_data = snp_Name_Extract(snp_Positions, snp1);
			line_printer_d.print(snp_prime_data.get(0) + "\t");
			line_printer_r.print(snp_prime_data.get(0) + "\t");
			
			for(int snp2 = 0; snp2 < snp_Positions.size(); snp2++) {
				snp_Pair(snp1, snp2, snp_Positions, id_count);
				line_printer_d.print(round_Off(this.D_prime) + "\t");
				line_printer_r.print(round_Off(this.rr) + "\t");
				log_Println("",this.log);			
			}
			
			line_printer_d.print("\n");
			line_printer_r.print("\n");
		}
		
		line_printer_d.close();
		br_d.close();
		
		line_printer_r.close();
		br_r.close();
		
	}
	
	
	public void file_Creator(File file) {
		if (file.exists()) {
			file.delete();
		}
		
		try {
			if(!file.createNewFile()) {
				System.out.println("Failed Creating " + file.getAbsolutePath());
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public ArrayList <String> snp_Name_Extract(ArrayList <Integer> snp_Positions, int snp1) {
		Hashtable <String, Integer> snp_Name_table = filt_snp_Index.get(snp_Positions.get(snp1));
		Enumeration data = snp_Name_table.keys();
		ArrayList <String> data_list = new ArrayList <String>();
		
		while (data.hasMoreElements()) {
			data_list.add((String) data.nextElement());
		}
		
		return data_list;
	}
	
	public void snp_Pair(int snp1, int snp2, ArrayList <Integer> snp_Positions, int id_count) throws IOException {
		hap_Predictor(snp_Positions.get(snp1),snp_Positions.get(snp2), id_count, snp_Positions);
	}

	public void hap_Predictor(int snp1, int snp2, int id_count, ArrayList <Integer> snp_Positions) throws IOException {
		
		Hashtable <String,Double> snp1_Allele = snp_Finder(snp1);
		Hashtable <String,Double> snp2_Allele = snp_Finder(snp2);
		
		ArrayList <String> snp_1_name = snp_Name_Extract(snp_Positions, snp1);
		log_Println("Alleles present in " + snp_1_name.get(0) + ": " + snp1_Allele, this.log);
	    ArrayList <String> snp_2_name = snp_Name_Extract(snp_Positions, snp2);
	    log_Println("Alleles present in " + snp_2_name.get(0) + ": " + snp2_Allele, this.log);
	    
	    ArrayList <String> snp_1 = hash_2_Arraylist(snp1_Allele);
	    ArrayList <String> snp_2 = hash_2_Arraylist(snp2_Allele);

	    Hashtable <String,Double> poss_Haps = new Hashtable <String,Double>();
	    
	   for(int sone_c = 0; sone_c<2; sone_c ++) {
	    	for(int stwo_c = 0; stwo_c<2; stwo_c ++) {
	    		poss_Haps.put(snp_1.get(sone_c)+snp_2.get(stwo_c), 0.000);
	    	}
	    }
	   
	   log_Println("Haplotypes present\t: " + poss_Haps, this.log);
	   ldcal_Overhead(snp_1_name.get(0),snp_2_name.get(0), snp1, snp2, snp1_Allele, snp2_Allele, poss_Haps, id_count);
	   
	}
	
	public void ldcal_Overhead(String snp1_name, String snp2_name, int snp1, int snp2, Hashtable <String,Double> snp1_Allele, 
			Hashtable <String,Double> snp2_Allele, Hashtable <String,Double> poss_Haps, int id_count) throws IOException {
		
		try {
			BufferedReader line_Read = new BufferedReader(new FileReader(ped_File));
			String line_Ped = line_Read.readLine().trim();
			//Scanner line_Ped = new Scanner(ped_File);
			//System.out.println("Number of Ind: "+id_count);
			while(line_Ped!=null) {
			
				String [] line = line_Ped.split(" ");
				List<String> line_filtered = line_filter(line);
				
				Double increment = snp1_Allele.get(line_filtered.get(snp_Locator(snp1))) + 1;
				snp1_Allele.put(line_filtered.get(snp_Locator(snp1)), increment);

				increment = snp1_Allele.get(line_filtered.get(snp_Locator(snp1)+1)) + 1;
				snp1_Allele.put(line_filtered.get(snp_Locator(snp1)+1), increment);
				
				increment = snp2_Allele.get(line_filtered.get(snp_Locator(snp2))) + 1;
				snp2_Allele.put(line_filtered.get(snp_Locator(snp2)), increment);
				
				increment = snp2_Allele.get(line_filtered.get(snp_Locator(snp2)+1)) + 1;
				snp2_Allele.put(line_filtered.get(snp_Locator(snp2)+1), increment);
				
				String hap_1_1_2_1 = line_filtered.get(snp_Locator(snp1)) + line_filtered.get(snp_Locator(snp2));
				String hap_1_2_2_2 = line_filtered.get(snp_Locator(snp1)+1) + line_filtered.get(snp_Locator(snp2)+1);
				
				increment = poss_Haps.get(hap_1_1_2_1) + 1;
				poss_Haps.put(hap_1_1_2_1, increment);
				
				increment = poss_Haps.get(hap_1_2_2_2) + 1;
				poss_Haps.put(hap_1_2_2_2, increment);
				
				line_Ped = line_Read.readLine();
				
			}
			
			snp1_Allele = freq(snp1_Allele, id_count);
			snp2_Allele = freq(snp2_Allele, id_count);
			poss_Haps = freq(poss_Haps, id_count);
			
			log_Println("\nAlleles frequencies " +snp1_name+": "  + snp1_Allele, this.log);
			log_Println("Alleles frequencies "+ snp2_name + ": " + snp2_Allele, this.log);
			log_Println("Haplotype frequencies \t: " + poss_Haps + "\n", this.log);
			
			this.D_prime = d_Prime(snp1_Allele, snp2_Allele, poss_Haps);
			
			line_Read.close();
			
			
		} catch (FileNotFoundException e) {
			System.out.println("Error in PED file");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private Double r_Square(Double D, Hashtable <String,Double> snp1_Allele, Hashtable <String,Double> snp2_Allele) {
		ArrayList <String> Allele_1 = hash_2_Arraylist(snp1_Allele);
		ArrayList <String> Allele_2 = hash_2_Arraylist(snp2_Allele);
		
		Double denom = snp1_Allele.get(Allele_1.get(0)) * snp1_Allele.get(Allele_1.get(1)) * 
				snp2_Allele.get(Allele_2.get(0)) * snp2_Allele.get(Allele_2.get(1));
		
		Double rr = (D*D) / (denom);
		
		try {
			log_Println("R-Squared: " + round_Off(rr),this.log);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rr;
		
	}
	
	private Double d_Prime(Hashtable <String,Double> snp1_Allele,Hashtable <String,Double> snp2_Allele,
			Hashtable <String,Double> poss_Haps) {
		
		ArrayList <String> haps = hash_2_Arraylist(poss_Haps);
		String hap = haps.get(0);
		//System.out.println(haps.get(0));
		Character hap_al_1 = hap.charAt(0);
		Character hap_al_2 = hap.charAt(1);
		
		Double hap_freq = poss_Haps.get(hap);
		Double freq_hap_al_1 = snp1_Allele.get(hap_al_1.toString());
		Double freq_hap_al_2 = snp2_Allele.get(hap_al_2.toString());
		
		Double D = hap_freq - (freq_hap_al_1 * freq_hap_al_2);
		try {
			log_Println("D\t: " + D, this.log);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Double D_prime = 0.0;
		
		if(D > 0) {

			Double combo_1 = snp1_Allele.get(hap_al_1.toString()) * alt_allele(snp2_Allele,hap_al_2);
			Double combo_2 = snp2_Allele.get(hap_al_2.toString()) * alt_allele(snp1_Allele,hap_al_1);
			D_prime = D/find_Min(combo_1,combo_2);
		}
		else {
			Double combo_1 = freq_hap_al_1 * freq_hap_al_2;
			Double combo_2 = alt_allele(snp1_Allele,hap_al_1) * alt_allele(snp2_Allele,hap_al_2);
			D_prime = D/find_Max(combo_1,combo_2);
		}
		
		try {
			log_Println("D Prime\t: " + round_Off(D_prime), this.log);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		this.rr = r_Square(D,snp1_Allele, snp2_Allele);
		
		return D_prime;
		
	}

	
	private Double alt_allele(Hashtable <String,Double> snp_Allele, Character hap_al) {
		
		ArrayList <String> snp = hash_2_Arraylist(snp_Allele);
		
		int count = 0;
		
		while(snp.get(count).compareToIgnoreCase(hap_al.toString())==0) {
			count++;
		}
		
		String alt_al = snp.get(count);
		
		Double val =  snp_Allele.get(alt_al);
		
		return val;
	}
	
	
	private Double find_Max(Double combo_1, Double combo_2) {
		Double max=0.00;
		
		if(-combo_1>-combo_2) {
			max = -combo_1;			
		}
		else {
			max = -combo_2;
		}
		
		return max;
	}
	
	private Double find_Min(Double combo_1, Double combo_2) {
		Double min=0.00;
		
		if(combo_1>combo_2) {
			min = combo_2;			
		}
		else {
			min = combo_1;
		}
		
		return min;
	}
	
	
	private Hashtable <String,Double> freq(Hashtable <String,Double> snp_data, int id_count){
		ArrayList <String> snp = hash_2_Arraylist(snp_data);
		
		for(int count = 0; count<snp.size(); count++) {
			Double increment = snp_data.get(snp.get(count)) / (id_count * 2);
			increment = (double) Math.round(increment * 10000);
			snp_data.put(snp.get(count), (increment / 10000));
		}
		
		return snp_data;
	}
	
	public Double round_Off(Double val) {
		
		Double round_val = (double) Math.round(val * 10000);
		return round_val / 10000;
		
	}
	
	public int snp_Locator(int snp_Num) {
		int locator = snp_Num + 1;
		locator = locator + (locator - 1);
		locator = locator + 5;
		return locator;
	}
	

	public ArrayList <String> hash_2_Arraylist(Hashtable <String,Double> HASH) {
		Enumeration data = HASH.keys();
		ArrayList <String> data_list = new ArrayList <String>();
		
		while (data.hasMoreElements()) {
			data_list.add((String) data.nextElement());
		}
		
		return data_list;
	}
	
	public Hashtable <String,Double> snp_Finder(int snp) {
		
		Hashtable <String,Double> snp_Allele = new Hashtable <String,Double>();
		int count = 0;
		int check = 0;
		
		for (int loop_count = 0; loop_count<2; loop_count++) {
		
			while(check!=1) {
			check = ATGC[count][snp];
			count++;
		}
		
	    count = count - 1;
	    
	    switch(count) {
	    case 0:
	    	snp_Allele.put("A", 0.000);
	    	break;
	    case 1:
	    	snp_Allele.put("T", 0.000);
	    	break;
	    case 2:
	    	snp_Allele.put("G", 0.000);
	    	break;
	    case 3:
	    	snp_Allele.put("C", 0.000);
	    	break;
	    }
	    
	    check=0;
	    count++;
	    
		}
	    
	    return snp_Allele;
		
	}
	
	private static void Printer(String [] list) {
		for(int c = 0; c<list.length; c++) {
			System.out.println(list[c]);
		}
	}
	
	private List<String> line_filter(String []line){
		List<String> line_filtered = new ArrayList<String>(Arrays.asList(line));
		
		line_filtered.removeAll(Arrays.asList("",null));
		
		return line_filtered;
	}
		
	

}
