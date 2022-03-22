package linkage_d;

import java.util.ArrayList;
import java.util.List;

public class Range {
// The class contains the lines from the vcf file on the snps in the target region
	public List<String> lines = new ArrayList<String>();
	public List<Double> MAFs = new ArrayList<Double>();

	public void set(List<String> new_Set) {
		lines.clear();
		for (int c = 0; c < new_Set.size(); c++) {
			lines.add(new_Set.get(c));
		}
		new_Set.clear();
	}

	public List<String> getLines() {
		return lines;
	}

	public void found(String line) {
		synchronized (this) {
			lines.add(line);
		}
	}

	public void found_MAF(Double MAF) {
		synchronized (this) {
			MAFs.add(MAF);
		}
	}

}
