package linkage_d;

public class Snps {

	String ALT;
	int count_REF;
	int count_ALT;
	Double MAF;
	int tot_Alleles;

	String REF;

	public String getREF() {
		return REF;
	}

	public String getALT() {
		return ALT;
	}

	public Double getFreq_REF() {
		return (double) ((double) count_REF / (double) tot_Alleles);
	}

	public Double getFreq_ALT() {
		return (double) ((double) count_ALT / (double) tot_Alleles);
	}

	public int MAF_pass(Double MAF) {

		int check = 0;

		Double getMAF = 0.0;

		Double REF = getFreq_REF();
		Double ALT = getFreq_ALT();

		if (REF < ALT) {
			getMAF = REF;
		} else {
			getMAF = ALT;
		}

		if (getMAF > MAF) {
			check = 1;
		} else {
			System.out.println("MAF: " + getMAF);
		}

		return check;

	}

	public Double return_MAF() {

		Double REF = getFreq_REF();
		Double ALT = getFreq_ALT();

		if (REF < ALT) {
			return REF;
		} else {
			return ALT;
		}

	}

	Snps(String REF, String ALT, int tot_Alleles) {
		// Store the snps REFERENCE and ALTERNATE alleles as well set their ALLELE
		// counts and Total allele counts
		this.REF = REF;
		this.ALT = ALT;
		this.count_REF = 0;
		this.count_ALT = 0;
		// The total allele count is the multiplication by 2 of the total columns in the
		// vcf file - the header columns
		// The tot Alleles passed is minus the header columns
		this.tot_Alleles = tot_Alleles * 2;
	}

	public void increment(String field) {
		// Gets the GT location data from individual/ pool data
		String[] chromosomes = field.split("|");
		// Printer(chromosomes);
		for (int c = 0; c < chromosomes.length; c = c + 2) {
			/*******************************************************
			 * PLEASE REFER TO VCF USER MANUAL FOR MORE INFORMATION
			 *******************************************************/
			// 0 refers to the REFERENCE Allele
			if (chromosomes[c].equals("0")) {
				this.count_REF++;
				// 1 refers to the ALTERNATE Allele
			} else if (chromosomes[c].equals("1")) {
				this.count_ALT++;
			}
		}
	}

	private void Printer(String[] list) {
		// Used for troubleshooting arrays, not utilized in the main program
		for (int c = 0; c < list.length; c++) {
			System.out.println(list[c]);
		}
	}

}
