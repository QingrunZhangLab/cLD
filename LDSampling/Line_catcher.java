package linkage_d;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;

public class Line_catcher {

	public List<Integer> Positions = new ArrayList<Integer>();
	public Hashtable<Integer, Integer> positions_lineNum = new Hashtable<Integer, Integer>();

	public List<Integer> getPositions() {
		// Return the list of available SNP positions
		synchronized (this) {
			return Positions;
		}
	}

	public Hashtable<Integer, Integer> getPositions_lineNum() {
		// Return the Hashtable of available SNP positions
		synchronized (this) {
			return positions_lineNum;
		}
	}

	public void set(int position, int line_Number) {
		// Set the values for indexing the vcf files
		// The positions and line number of the respective SNP is stored
		synchronized (this) {
			this.Positions.add(position);
			this.positions_lineNum.put(position, line_Number);
		}
	}

}
