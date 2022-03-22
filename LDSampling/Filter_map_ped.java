package linkage_d;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Scanner;

public class Filter_map_ped extends Linkage_main {
	
	private File map_File;
	private File ped_File;
	private int counter;
	private int id_count;
	private int [][] allele_Freq;
	//int b_size = 1073741824;

	//HASHTABLE contains SNP NUMBER, SNP NAME & SNP BP distance
	private Hashtable <Integer, Hashtable <String, Integer> > snp_Index = 
			new Hashtable <Integer, Hashtable <String, Integer> >();
	
	//HASHTABLE contains SNP NUMBER, Chromosome_number
	private Hashtable <Integer, Integer> snp_chr_Index = new Hashtable <Integer, Integer>();
	
	public Hashtable<Integer, Integer> getSnp_chr_Index() {
		return snp_chr_Index;
	}

	public Hashtable<Integer, Hashtable<String, Integer>> getSnp_Index() {
		return snp_Index;
	}

	public File getMap_File() {
		return map_File;
	}

	public void setMap_File(File map_File) {
		this.map_File = map_File;
	}

	public File getPed_File() {
		return ped_File;
	}

	public void setPed_File(File ped_File) {
		this.ped_File = ped_File;
	}

	public int getCounter() {
		return counter;
	}
	
	public int getId_count() {
		return id_count;
	}
	
	Filter_map_ped(String map_Name, String ped_Name, File input_Folder){
		this.map_File = new File(input_Folder + "/" + map_Name);
		this.ped_File = new File(input_Folder + "/" + ped_Name);
	}
	
	public int[][] snp_allele_Filter() throws IOException {
		//long stime = System.currentTimeMillis();
		//System.out.println(snp_Index);
		int [][] ATGC = snp_allele_Counter();
		
		//column and row for loop **inverse of regular two-dimensional array for loop
		for(int snp = 0; snp < ATGC[0].length; snp++) {
			int check = 0;
			for(int base = 0; base < 4; base ++) {
				check = check + ATGC[base][snp];
			}
			if (check != 2) {
				snp_Index.remove(snp);
				snp_chr_Index.remove(snp);
			}
		}
		
		System.out.println("Completed\t: Filtering Bi Allele SNPs");
		filter_MAF();
		System.out.println("Completed\t: Filtering MAF");
		System.out.println("SNPs remaining\t: "+ snp_Index.size());
		System.out.println("Completed\t: Filtering SNPs");
		//System.out.println(snp_Index);
		//System.out.println("snp_allele_Filter: " + (System.currentTimeMillis() - stime)/100);
		return ATGC;
	}
	
	private void filter_MAF() {
		//long stime = System.currentTimeMillis();
		System.out.println("Initiated\t: Filtering MAF");
		ArrayList <Integer> SNPs = get_SNPs(snp_chr_Index);
		for(int count = 0; count<SNPs.size(); count++) {
			int current = SNPs.get(count);
			for(int check = 0; check < 4; check++) {
				if(allele_Freq[check][current]!=0) {
					double freq = (double)allele_Freq[check][current] / (double)allele_Freq[4][current];
					if(freq < 0.05) {
						snp_Index.remove(current);
						snp_chr_Index.remove(current);
					}
				}
			}
			
		}
		//System.out.println("filter_MAF: " + (System.currentTimeMillis() - stime)/100);
	}
	

	public ArrayList <Integer> get_SNPs(Hashtable <Integer, Integer> HASH) {
		Enumeration data = HASH.keys();
		ArrayList <Integer> data_list = new ArrayList <Integer>();
		
		while (data.hasMoreElements()) {
			data_list.add((Integer) data.nextElement());
		}
		
		return data_list;
	}
	
	private int [][] snp_allele_Counter() throws IOException {
		//long stime = System.currentTimeMillis();
		this.id_count = 0;
		int [][] ATGC_check = new int [4][counter];
		this.allele_Freq = new int[5][counter];
		int snp_Count = 0;
		try {
			BufferedReader line_Read = new BufferedReader(new FileReader(ped_File));
			//Scanner line_Read = new Scanner(ped_File);
			String line_data = line_Read.readLine().trim();
			while(line_data!=null) {
				id_count++;
				String [] line = line_data.split(" ");
				List<String> line_filtered = line_filter(line);
				for(int snp = 6; snp < line_filtered.size(); snp = snp + 2) {
					for(int ch = snp; ch < (snp+2); ch++) {
						String base = line_filtered.get(ch);
						//System.out.println("BASE: "+base);
						switch(base) {
							case "A":
							case "a":
								ATGC_check[0][snp_Count] = 1;
								allele_Freq[0][snp_Count] = allele_Freq[0][snp_Count] + 1;
								allele_Freq[4][snp_Count] = allele_Freq[4][snp_Count] + 1;
								break;
							case "T":
							case "t":
								ATGC_check[1][snp_Count] = 1;
								allele_Freq[1][snp_Count] = allele_Freq[1][snp_Count] + 1;
								allele_Freq[4][snp_Count] = allele_Freq[4][snp_Count] + 1;
								break;
							case "G":
							case "g":
								ATGC_check[2][snp_Count] = 1;
								allele_Freq[2][snp_Count] = allele_Freq[2][snp_Count] + 1;
								allele_Freq[4][snp_Count] = allele_Freq[4][snp_Count] + 1;
								break;
							case "C":
							case "c":
								ATGC_check[3][snp_Count] = 1;
								allele_Freq[3][snp_Count] = allele_Freq[3][snp_Count] + 1;
								allele_Freq[4][snp_Count] = allele_Freq[4][snp_Count] + 1;
								break;						
						}
					}
					snp_Count++;
				}
				snp_Count = 0;
				line_data = line_Read.readLine();
			}
		line_Read.close();
		} catch (FileNotFoundException e) {
			System.out.println("Error in Reading PED file");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//System.out.println("snp_allele_Counter: " + (System.currentTimeMillis() - stime)/100);
		return ATGC_check;
		
	}
	
	public void snp_Counter() throws NumberFormatException, IOException {
		//long stime = System.currentTimeMillis();
		this.counter = 0;

		try {
			//Scanner line_Read = new Scanner(map_File);
			BufferedReader line_Read = new BufferedReader(new FileReader(map_File));
			String line_test;
			line_test = line_Read.readLine();
			while(!line_test.trim().isEmpty()) {
				//System.out.println("Line");
				//String line_test = line_Read.nextLine();
				String [] line = line_test.split("\t");
				//Printer(line);
				List<String> line_filtered = line_filter(line);
				if (line_filtered.size()!=4) {
					line = line_test.split(" ");
					line_filtered = line_filter(line);
				}
				Hashtable <String, Integer> snp_data = new Hashtable <String, Integer>();
				//System.out.println(line_filtered);
				snp_data.put(line_filtered.get(1), Integer.parseInt(line_filtered.get(3)));
				snp_Index.put(counter, snp_data);
				snp_chr_Index.put(counter, Integer.parseInt(line_filtered.get(0)));
				counter ++;
				line_test = line_Read.readLine();
				if(line_test==null) {
					break;
				}
			}
			line_Read.close();
			
		} catch (FileNotFoundException e) {
			System.out.println("Error in reading MAP File");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//System.out.println("snp_Counter: " + (System.currentTimeMillis() - stime)/100);
		
	}
	
	public Boolean ped_check() throws IOException {
		Boolean check = false;
		//long stime = System.currentTimeMillis();
		
		try {
			//Scanner line_Read = new Scanner(ped_File);
			BufferedReader line_Read = new BufferedReader(new FileReader(ped_File));
			String line_one = line_Read.readLine();
			line_Read.close();
			String [] line = line_one.split(" ");
			List<String> line_filtered = line_filter(line);
		
			int line_Count = (line_filtered.size() - 6);
			int check_Count = line_Count / 2;
			
			if(counter == check_Count) {
				check = true;
			}
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("Error in reading PED File");
			e.printStackTrace();
		}
		//System.out.println("ped_check: " + (System.currentTimeMillis() - stime)/100);
		return check;
		
	}
	
	private List<String> line_filter(String []line){
		List<String> line_filtered = new ArrayList<String>(Arrays.asList(line));
		
		line_filtered.removeAll(Arrays.asList("",null));
		
		return line_filtered;
	}
	
	private static void Printer(String [] list) {
		for(int c = 0; c<list.length; c++) {
			System.out.println(list[c]);
		}
	}

}