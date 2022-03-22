package linkage_d;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;

public class Haplotype {
	int tot_Haps;
	String hap_1_0_0;
	int Hap_0_0;
	String hap_2_0_1;
	int Hap_0_1;
	String hap_3_1_0;
	int Hap_1_0;
	String hap_4_1_1;
	int Hap_1_1;

	Haplotype(File log_File, String snp1_REF, String snp1_ALT, String snp2_REF, String snp2_ALT) throws IOException {
		tot_Haps = 0;
		Hap_0_0 = 0;
		Hap_0_1 = 0;
		Hap_1_0 = 0;
		Hap_1_1 = 0;

		hap_Construct(log_File, snp1_REF, snp1_ALT, snp2_REF, snp2_ALT);

	}

	Haplotype(String snp1_REF, String snp1_ALT, String snp2_REF, String snp2_ALT) throws IOException {
		// Thread safe constructor
		// DURING THE CALL OF THE CLASS THE HAPLOTYPE COUNTER ARE SET TO ZERO AND THE
		// POSSIBLE VARIATIONS ARE CALCULATED
		// Counts for each possible haplotype set to 0
		tot_Haps = 0;
		Hap_0_0 = 0;
		Hap_0_1 = 0;
		Hap_1_0 = 0;
		Hap_1_1 = 0;

		// Construct all possible haplotype variations
		hap_Construct(snp1_REF, snp1_ALT, snp2_REF, snp2_ALT);

	}

	public void increment(String snp1, String snp2) {
		// SNPS GT IS ONLY LOADED AN INDIVIDUAL AT A TIME
		String combination;
		String[] snp1_chromosomes = snp1.split("|");
		String[] snp2_chromosomes = snp2.split("|");

		// Printer(snp2_chromosomes);

		for (int c = 0; c < snp1_chromosomes.length; c = c + 2) {
			combination = snp1_chromosomes[c] + snp2_chromosomes[c];
			tot_Haps++;
			// Haplotype combination detected and accounted for
			switch (combination) {
			case "00":
				this.Hap_0_0++;
				break;
			case "01":
				this.Hap_0_1++;
				break;
			case "10":
				this.Hap_1_0++;
				break;
			case "11":
				this.Hap_1_1++;
				break;

			}

		}

	}

	private void Printer(String[] list) {
		// Used for troubleshooting arrays, not utilized in the main program
		for (int c = 0; c < list.length; c++) {
			System.out.println(list[c]);
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

	public void linkage_GO(File log_File, File yield, Snps snp1, Snps snp2) throws IOException {
		DecimalFormat df = new DecimalFormat("###.####");
		lg_Println(log_File, "Alleles SNP_1: " + snp1.getALT() + ": " + snp1.getFreq_ALT() + " " + snp1.getREF() + ": "
				+ snp1.getFreq_REF());
		lg_Println(log_File, "Alleles SNP_2: " + snp2.getALT() + ": " + snp2.getFreq_ALT() + " " + snp2.getREF() + ": "
				+ snp2.getFreq_REF());
		Double hap1_freq = (double) this.Hap_0_0 / (double) tot_Haps;
		lg_Println(log_File, "Frequency " + hap_1_0_0 + "\t: " + hap1_freq);
		Double hap2_freq = (double) this.Hap_0_1 / (double) tot_Haps;
		lg_Println(log_File, "Frequency " + hap_2_0_1 + "\t: " + hap2_freq);
		Double hap3_freq = (double) this.Hap_1_0 / (double) tot_Haps;
		lg_Println(log_File, "Frequency " + hap_3_1_0 + "\t: " + hap3_freq);
		Double hap4_freq = (double) this.Hap_1_1 / (double) tot_Haps;
		lg_Println(log_File, "Frequency " + hap_4_1_1 + "\t: " + hap4_freq);

		Double REF_REF = snp1.getFreq_REF() * snp2.getFreq_REF();
		Double D = hap1_freq - REF_REF;
		lg_Println(log_File, "D: " + D);

		Double D_prime = d_Prime(D, snp1, snp2);
		lg_Println(log_File, "D Prime: " + Round(D_prime));
		Double r_Squared = r_Square(D, snp1, snp2);
		lg_Println(log_File, "R Squared: " + Round(r_Squared));

		try {
			FileWriter fw = new FileWriter(yield, true);
			BufferedWriter line_Writer = new BufferedWriter(fw);
			line_Writer.write(Round(D_prime) + "\t" + Round(r_Squared) + "\n");
			line_Writer.close();
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public String linkage_GO(File yield, Snps snp1, Snps snp2) throws IOException {
		String write = snp1.getALT() + ": " + Round(snp1.getFreq_ALT()) + " | " + snp1.getREF() + ": "
				+ Round(snp1.getFreq_REF()) + "\t";
		write = write + snp2.getALT() + ": " + Round(snp2.getFreq_ALT()) + " | " + snp2.getREF() + ": "
				+ Round(snp2.getFreq_REF()) + "\t";
		// Get Haplotype frequencies
		Double hap1_freq = (double) this.Hap_0_0 / (double) tot_Haps;
		Double hap2_freq = (double) this.Hap_0_1 / (double) tot_Haps;
		Double hap3_freq = (double) this.Hap_1_0 / (double) tot_Haps;
		Double hap4_freq = (double) this.Hap_1_1 / (double) tot_Haps;
		write = write + hap_1_0_0 + ": " + Round(hap1_freq) + " | " + hap_2_0_1 + ": " + Round(hap2_freq) + " | "
				+ hap_3_1_0 + ": " + Round(hap3_freq) + " | " + hap_4_1_1 + ": " + Round(hap4_freq) + "\t";

		Double REF_REF = snp1.getFreq_REF() * snp2.getFreq_REF();

		// Calculate D
		// D = x11 - p1q1

		Double D = hap1_freq - REF_REF;
		write = write + Round(D) + "\t";

		Double D_prime = d_Prime(D, snp1, snp2);
		Double r_Squared = r_Square(D, snp1, snp2);

		write = write + Round(D_prime) + "\t" + Round(r_Squared) + "\n";

		return write;

	}
	
	public String linkage_GO_papb(Snps snp1, Snps snp2) throws IOException {
		String write = snp1.getALT() + ": " + Round(snp1.getFreq_ALT()) + " | " + snp1.getREF() + ": "
				+ Round(snp1.getFreq_REF()) + "\t";
		write = write + snp2.getALT() + ": " + Round(snp2.getFreq_ALT()) + " | " + snp2.getREF() + ": "
				+ Round(snp2.getFreq_REF()) + "\t";
		// Get Haplotype frequencies
		Double hap1_freq = (double) this.Hap_0_0 / (double) tot_Haps;
		Double hap2_freq = (double) this.Hap_0_1 / (double) tot_Haps;
		Double hap3_freq = (double) this.Hap_1_0 / (double) tot_Haps;
		Double hap4_freq = (double) this.Hap_1_1 / (double) tot_Haps;
		write = write + hap_1_0_0 + ": " + Round(hap1_freq) + " | " + hap_2_0_1 + ": " + Round(hap2_freq) + " | "
				+ hap_3_1_0 + ": " + Round(hap3_freq) + " | " + hap_4_1_1 + ": " + Round(hap4_freq) + "\t";

		Double REF_REF = snp1.getFreq_REF() * snp2.getFreq_REF();

		// Calculate D
		// D = x11 - p1q1

		Double D = hap1_freq - REF_REF;
		//write = write + Round(D) + "\t";

		Double D_prime = d_Prime(D, snp1, snp2);
		Double r_Squared = r_Square(D, snp1, snp2);

		write = write + Round(D_prime) + "\t" + Round(r_Squared) + "\n";

		return write;

	}

	public Double Round(Double value) {
		Double round_Value = (double) Math.round(value * 10000);
		return round_Value / 10000;
	}

	public Double r_Square(Double D, Snps snp1, Snps snp2) {
		// r squared = D^2 / (p1*p2*q1*q2)
		Double r = (D * D)
				/ (Double) (snp1.getFreq_ALT() * snp1.getFreq_REF() * snp2.getFreq_ALT() * snp2.getFreq_REF());
		return r;
	}

	public Double d_Prime(Double D, Snps snp1, Snps snp2) {
		// Calculate D Prime
		Double D_Prime = 0.0000;
		if (D >= 0) {
			// When D is POSITIVE
			// Dmax is the smaller of the allele frequency combination Dmax = smaller of
			// p1q2 or p2q1 D prime = D/ Dmax

			Double snp1_0_snp2_1 = snp1.getFreq_REF() * snp2.getFreq_ALT();
			Double snp1_1_snp2_0 = snp1.getFreq_ALT() * snp2.getFreq_REF();

			Double D_max = 0.0000;

			if (snp1_0_snp2_1 < snp1_1_snp2_0) {
				D_max = snp1_0_snp2_1;
			} else {
				D_max = snp1_1_snp2_0;
			}

			D_Prime = D / D_max;

		} else {
			// When D is NEGATIVE
			// Dmin is the larger of the NEGATIVE of the allele frequency combination
			// Dmin = larger of -p1q1 or -p2q2 D prime = D/ Dmin

			Double snp1_0_snp2_0 = -(snp1.getFreq_REF() * snp2.getFreq_REF());
			Double snp1_1_snp2_1 = -(snp1.getFreq_ALT() * snp2.getFreq_ALT());

			Double D_min = 0.0000;

			if (snp1_0_snp2_0 > snp1_1_snp2_1) {
				D_min = snp1_0_snp2_0;
			} else {
				D_min = snp1_1_snp2_1;
			}

			D_Prime = D / D_min;

		}

		return D_Prime;

	}

	private void hap_Construct(String snp1_REF, String snp1_ALT, String snp2_REF, String snp2_ALT) throws IOException {
		// Thread safe haplotype construction function
		// All possible variations
		this.hap_1_0_0 = snp1_REF + snp2_REF;
		this.hap_2_0_1 = snp1_REF + snp2_ALT;
		this.hap_3_1_0 = snp1_ALT + snp2_REF;
		this.hap_4_1_1 = snp1_ALT + snp2_ALT;

		// lg_Println(log_File,
		// "Available Haplotypes: " + " " + hap_1_0_0 + " " + hap_2_0_1 + " " +
		// hap_3_1_0 + " " + hap_4_1_1);

	}

	private void hap_Construct(File log_File, String snp1_REF, String snp1_ALT, String snp2_REF, String snp2_ALT)
			throws IOException {
		this.hap_1_0_0 = snp1_REF + snp2_REF;
		this.hap_2_0_1 = snp1_REF + snp2_ALT;
		this.hap_3_1_0 = snp1_ALT + snp2_REF;
		this.hap_4_1_1 = snp1_ALT + snp2_ALT;

		lg_Println(log_File,
				"Available Haplotypes: " + " " + hap_1_0_0 + " " + hap_2_0_1 + " " + hap_3_1_0 + " " + hap_4_1_1);

	}

}
