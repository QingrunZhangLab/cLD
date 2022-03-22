package linkage_d;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Thread_func {
	
	int [][] ATGC_check;
	public int[][] getATGC_check() {
		return ATGC_check;
	}

	public int[][] getAllele_Freq() {
		return allele_Freq;
	}


	int [][] allele_Freq;
	
	Thread_func(int [][] ATGC_check, int [][] allele_Freq){
		this.ATGC_check = ATGC_check;
		this.allele_Freq = allele_Freq;
	}

	public synchronized void  snp_allele_Counter(List<String> line_filtered,int snp, int snp_Count) {
			

			//System.out.println("snp_Count: " + snp_Count);
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
	
	}
	
	private synchronized String[] line_filter(String []line){
		String[] data = Arrays.stream(line).filter(value -> value != null && value.length() > 0).toArray(size -> new String[size]);

		return data;
	}
	
	private static void Printer(String [] list) {
		//used for troubleshooting arrays, not utilized in the main program 
		for(int c = 0; c<list.length; c++) {
			System.out.println(list[c]);
		}
	}

}
