package linkage_d;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class OnV2LD {

	File input_Folder;
	File output_Folder;
	int BP_range;
	ArrayList<String> vcf_Files;

	OnV2LD(String[] files, File input_Folder, File output_Folder) {
		this.input_Folder = input_Folder;
		this.output_Folder = output_Folder;
		try {
			index_Properties(files);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.vcf_Files = index_VCF(files);

	}

	public void master_VCF() {
		for (int c = 0; c < this.vcf_Files.size(); c++) {
			System.out.println("Processing\t: " + this.vcf_Files.get(c));
			String pathname = input_Folder.getAbsolutePath() + "/" + this.vcf_Files.get(c);
			try {
				process_VCF(pathname);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("Completed\t: " + this.vcf_Files.get(c));
		}

	}

	private void process_VCF(String path_VCF) throws IOException {
		File vcf = new File(path_VCF);
		List<String> lines = Files.readAllLines(Paths.get(path_VCF));
		String file_name =this.output_Folder.getAbsolutePath() + "/" + vcf.getName().substring(0, vcf.getName().lastIndexOf(".")) + ".ld";
		File yield = new File (file_name);
		
		if(!yield.exists()) {
			yield.createNewFile();
			FileWriter fw = new FileWriter(yield, true);
			BufferedWriter line_Writer = new BufferedWriter(fw);
			String header = "SNP_1\tSNP_2\tAlleles_SNP1\tAlleles_SNP2\tHaplotypes\tD\tD_Prime\tR_Squared\n";
			line_Writer.write(header);
			line_Writer.close();
			fw.close();
		}
		
		//String [] line_snp_1 = lines.get(1).split("\t");
		//System.out.println(line_snp_1.length - 9);
		FileWriter fw = new FileWriter(yield, true);
		BufferedWriter line_Writer = new BufferedWriter(fw);
		for (int snp1_I = 1; snp1_I < lines.size(); snp1_I++) {
			ExecutorService ex = Executors.newFixedThreadPool(5);
			Long time  = System.currentTimeMillis();
			Hashtable <Integer, String> lines_Store = new Hashtable<Integer, String>();
			
			Store_lines lines_Storage = new Store_lines();
			
			String[] line_snp_1 = lines.get(snp1_I).split("\t");
			System.out.print(snp1_I +" Processing\t: "+ line_snp_1[2]+"\r");
			Integer pos_Range = Integer.valueOf(line_snp_1[1]) + this.BP_range;
			int line = 0;
			for (int snp2_I = snp1_I; snp2_I < lines.size(); snp2_I++) {
				String[] line_snp_2 = lines.get(snp2_I).split("\t");
				int snp2_Pos = Integer.parseInt(line_snp_2[1]); 
				if(snp2_Pos < pos_Range) {
					ex.execute(new Threadscape_3(lines_Storage, yield, line_snp_1, line_snp_2, line));
					//Super_pop GBR = new Super_pop(yield, line_snp_1, line_snp_2);
					//String write = GBR.incite();
					//lines_Store.put(line, write);
					line++;
				}
				else {
					break;
				}

			}
			
			ex.shutdown();
			while(!ex.isTerminated()) {}
			
			lines_Store = lines_Storage.getLines_Store();
			System.out.println(snp1_I +" Completed\t: "+ line_snp_1[2]);

			for(int c = 0; c<lines_Store.size();c++) {
				line_Writer.write(lines_Store.get(c));
				line_Writer.flush();
			}

			System.out.println("\nTime: " + (((System.currentTimeMillis())-time)));
		}
		fw.close();
		line_Writer.close();
		
		System.out.println("\n******************\tPROGRAM COMPLETED\t******************");

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
					case "Base_Pair_range":
						this.BP_range = Integer.parseInt(criteria[1]);
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
	
	class Threadscape_3 implements Runnable{
		
		Store_lines lines_Storage;
		File yield;
		String[] line_snp_1;
		String[] line_snp_2;
		int line;
		
		Threadscape_3(Store_lines lines_Storage, File yield, String[] snp1, String[] snp2, int line){
			this.lines_Storage = lines_Storage;
			this.yield = yield;
			this.line_snp_1 = snp1;
			this.line_snp_2 = snp2;
			this.line=line;
		}
		
		public void run() {
			
			Super_pop GBR = new Super_pop(yield, line_snp_1, line_snp_2);
			
			try {
				String write;
				write = GBR.incite();
				lines_Storage.put(line, write);	
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
			
		}
		
	}

}
