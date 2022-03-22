package linkage_d;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;

public class Super_pop {
	File yield;
	String this_super_Pop;
	String[] snp1;
	String[] snp2;
	Hashtable<Integer, String> vcf_pop_Index;
	Double MAF;
	String[] headers;

	Super_pop(File yield, String this_super_Pop, Hashtable<Integer, String> vcf_pop_Index, String[] snp1, String[] snp2,
			Double MAF) {

		this.this_super_Pop = this_super_Pop;
		this.yield = new File(yield.getAbsolutePath() + "/" + yield.getName() + "_" + this.this_super_Pop + ".ld");
		try {
			save_LD(this.yield);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.snp1 = snp1;
		this.snp2 = snp2;
		this.vcf_pop_Index = vcf_pop_Index;
		this.MAF = MAF;

	}

	Super_pop(String this_super_Pop, Hashtable<Integer, String> vcf_pop_Index, String[] snp, Double MAF) {

		this.this_super_Pop = this_super_Pop;
		this.snp1 = snp;
		this.vcf_pop_Index = vcf_pop_Index;
		this.MAF = MAF;

	}

	Super_pop(String this_super_Pop, Hashtable<Integer, String> vcf_pop_Index, String[] snp) {

		this.this_super_Pop = this_super_Pop;
		this.snp1 = snp;
		this.vcf_pop_Index = vcf_pop_Index;
	}

	Super_pop(File yield, String[] snp1, String[] snp2) {
		/*
		 * CONSTRUCTOR FOR MULITHREAD INSTANCES
		 */
		this.yield = yield;
		this.snp1 = snp1;
		this.snp2 = snp2;
	}

	Super_pop(String name_VCF, File yield, String this_super_Pop, Hashtable<Integer, String> vcf_pop_Index,
			String[] snp, String[] headers) {

		this.this_super_Pop = this_super_Pop;
		this.snp1 = snp;
		this.vcf_pop_Index = vcf_pop_Index;
		this.headers = headers;
		this.yield = new File(yield.getAbsolutePath() + "/" + name_VCF + "_" + this.this_super_Pop + ".vcf");

	}
	
	Super_pop(){
		
	}

	public void write_Country() throws IOException {
		File file = this.yield;
		ArrayList<Integer> Fields = get_Fields();

		if (!file.exists()) {
			file.createNewFile();
			FileWriter write = new FileWriter(file, true);
			BufferedWriter line_Writer = new BufferedWriter(write);
			for (int c = 0; c < 9; c++) {
				line_Writer.write(headers[c] + "\t");
				line_Writer.flush();
			}

			for (int field : Fields) {
				line_Writer.write(headers[field] + "\t");
				line_Writer.flush();
			}
			line_Writer.write("\n");
			line_Writer.close();
		}

		FileWriter write = new FileWriter(file, true);
		BufferedWriter line_Writer = new BufferedWriter(write);

		for (int c = 0; c < 9; c++) {
			line_Writer.write(this.snp1[c] + "\t");
			line_Writer.flush();
		}

		for (int field : Fields) {
			line_Writer.write(this.snp1[field] + "\t");
			line_Writer.flush();
		}
		line_Writer.write("\n");
		line_Writer.close();

	}
	
	public void write_Country_Chr() throws IOException {
		File file = this.yield;
		ArrayList<Integer> Fields = get_Fields();

		if (!file.exists()) {
			file.createNewFile();
			FileWriter write = new FileWriter(file, true);
			BufferedWriter line_Writer = new BufferedWriter(write);
			for (int c = 0; c < 10; c++) {
				line_Writer.write(headers[c] + "\t");
				line_Writer.flush();
			}

			for (int field : Fields) {
				line_Writer.write(headers[field] + "\t");
				line_Writer.flush();
			}
			line_Writer.write("\n");
			line_Writer.close();
		}

		FileWriter write = new FileWriter(file, true);
		BufferedWriter line_Writer = new BufferedWriter(write);

		for (int c = 0; c < 10; c++) {
			line_Writer.write(this.snp1[c] + "\t");
			line_Writer.flush();
		}

		for (int field : Fields) {
			line_Writer.write(this.snp1[field] + "\t");
			line_Writer.flush();
		}
		line_Writer.write("\n");
		line_Writer.close();

	}

	public void save_LD(File file) throws IOException {
		if (!file.exists()) {
			file.createNewFile();
			FileWriter write = new FileWriter(file, true);
			BufferedWriter line_Writer = new BufferedWriter(write);
			line_Writer.write("SNP_PAIR \tD_Prime \tR_Squared\n");
			line_Writer.close();
			write.close();
		}
	}

	public void lg_Println(File log_File, String data) throws IOException {

		FileWriter log_Writer = new FileWriter(log_File, true);
		BufferedWriter log_Print = new BufferedWriter(log_Writer);

		try {
			log_Print.write(data + "\n");
		} catch (IOException e) {
			System.out.println("ERROR IN LOG WRITER");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		log_Print.close();
		log_Writer.close();

	}

	public void incite(File log_File) throws IOException {
		lg_Println(log_File, "\nSuper Population: " + this.this_super_Pop);
		ArrayList<Integer> Fields = get_Fields();
		lg_Println(log_File, "SNP_1: " + this.snp1[2] + " SNP_2: " + this.snp2[2]);
		FileWriter fw = new FileWriter(this.yield, true);
		BufferedWriter line_Writer = new BufferedWriter(fw);
		line_Writer.write(this.snp1[2] + ":" + this.snp2[2] + "\t");
		line_Writer.close();
		fw.close();
		Snps snp1 = new Snps(this.snp1[3], this.snp1[4], Fields.size());
		Snps snp2 = new Snps(this.snp2[3], this.snp2[4], Fields.size());
		lg_Println(log_File, "Alleles SNP1\t: " + snp1.getREF() + " " + snp1.getALT());
		lg_Println(log_File, "Alleles SNP2\t: " + snp2.getREF() + " " + snp2.getALT());
		Haplotype hap_Master = new Haplotype(log_File, this.snp1[3], this.snp1[4], this.snp2[3], this.snp2[4]);
		for (int field : Fields) {
			int region_GT_1 = position_GT(this.snp1);
			String[] field_Full_1 = this.snp1[field].split(":");
			snp1.increment(field_Full_1[region_GT_1]);

			int region_GT_2 = position_GT(this.snp2);
			String[] field_Full_2 = this.snp2[field].split(":");
			snp2.increment(field_Full_2[region_GT_2]);
			hap_Master.increment(field_Full_1[region_GT_1], field_Full_2[region_GT_2]);
		}

		hap_Master.linkage_GO(log_File, this.yield, snp1, snp2);

	}

	public String incite() throws IOException {
		// Function used in the instance Log file of each execution is not required
		// Thread safe function

		// SNP IDs stored
		String write = this.snp1[2] + "\t" + this.snp2[2] + "\t";
		// SNP detailed passed to dedicated class
		Snps snp1 = new Snps(this.snp1[3], this.snp1[4], this.snp1.length - 9);
		Snps snp2 = new Snps(this.snp2[3], this.snp2[4], this.snp1.length - 9);
		// LD calculator
		Haplotype hap_Master = new Haplotype(this.snp1[3], this.snp1[4], this.snp2[3], this.snp2[4]);
		// The ALLELE actual counts and Haplotype counts are incrementally calculated
		// along the SNP

		for (int field = 9; field < this.snp1.length; field++) {
			// The GT position in the Pool/ Individual data is looked at for allele
			// information
			int region_GT_1 = position_GT(this.snp1);
			String[] field_Full_1 = this.snp1[field].split(":");
			snp1.increment(field_Full_1[region_GT_1]);

			int region_GT_2 = position_GT(this.snp2);
			String[] field_Full_2 = this.snp2[field].split(":");
			snp2.increment(field_Full_2[region_GT_2]);
			hap_Master.increment(field_Full_1[region_GT_1], field_Full_2[region_GT_2]);
		}

		//Calculate D, D Prime and R Squared
		write = write + hap_Master.linkage_GO(this.yield, snp1, snp2);

		return write;

	}
	
	public String incite_2() throws IOException {
		// Function used in the instance Log file of each execution is not required
		// Thread safe function

		// SNP IDs stored
		String write = this.snp1[2] + "\t" + this.snp2[2] + "\t";
		// SNP detailed passed to dedicated class
		Snps snp1 = new Snps(this.snp1[3], this.snp1[4], this.snp1.length - 9);
		Snps snp2 = new Snps(this.snp2[3], this.snp2[4], this.snp1.length - 9);
		// LD calculator
		Haplotype hap_Master = new Haplotype(this.snp1[3], this.snp1[4], this.snp2[3], this.snp2[4]);
		// The ALLELE actual counts and Haplotype counts are incrementally calculated
		// along the SNP

		for (int field = 9; field < this.snp1.length; field++) {
			// The GT position in the Pool/ Individual data is looked at for allele
			// information
			int region_GT_1 = position_GT(this.snp1);
			String[] field_Full_1 = this.snp1[field].split(":");
			snp1.increment(field_Full_1[region_GT_1]);

			int region_GT_2 = position_GT(this.snp2);
			String[] field_Full_2 = this.snp2[field].split(":");
			snp2.increment(field_Full_2[region_GT_2]);
			hap_Master.increment(field_Full_1[region_GT_1], field_Full_2[region_GT_2]);
		}

		//Calculate D, D Prime and R Squared
		write = write + hap_Master.linkage_GO_papb(snp1, snp2);

		return write;

	}

	public int position_GT(String[] snp) {
		// Since the GT FORMAT is used to get the allele configuration, its position is
		// determined in the vcf file
		String[] format = snp[8].split(":");
		int c = 0;
		String data = format[c];

		while (!format[c].equals("GT")) {
			c++;
		}

		return c;

	}

	public int snp_Check() {
		ArrayList<Integer> country_Fields = get_Fields();
		Snps snp = new Snps(this.snp1[3], this.snp1[4], country_Fields.size());

		for (int field : country_Fields) {
			snp.increment(this.snp1[field]);
		}
		// System.out.println("Freq: "+ snp.getFreq_REF()+"\t"+snp.getFreq_ALT());
		return snp.MAF_pass(this.MAF);

	}

	public Double return_MAF() {
		ArrayList<Integer> country_Fields = get_Fields();
		Snps snp = new Snps(this.snp1[3], this.snp1[4], country_Fields.size());

		for (int field : country_Fields) {
			snp.increment(this.snp1[field]);
		}
		// System.out.println("Freq: "+ snp.getFreq_REF()+"\t"+snp.getFreq_ALT());
		return snp.return_MAF();

	}
	
	public Double return_MAF(String [] line) {
		//ArrayList<Integer> country_Fields = get_Fields();
		Snps snp = new Snps(line[3], line[4], (line.length-9));

		for (int c=9;c<line.length;c++) {
			snp.increment(line[c]);
		}
		// System.out.println("Freq: "+ snp.getFreq_REF()+"\t"+snp.getFreq_ALT());
		return snp.return_MAF();

	}

	public ArrayList<Integer> get_Fields() {
		ArrayList<Integer> Fields = new ArrayList<Integer>();
		Set<Integer> fields = this.vcf_pop_Index.keySet();

		for (int field : fields) {
			String name = this.vcf_pop_Index.get(field);
			if (name.equals(this.this_super_Pop)) {
				Fields.add(field);
			}
		}

		return Fields;

	}

}
