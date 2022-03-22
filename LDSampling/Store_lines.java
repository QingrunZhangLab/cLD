package linkage_d;

import java.util.Hashtable;

public class Store_lines {
	
	Hashtable <Integer, String> lines_Store = new Hashtable<Integer, String>();
	
	public Hashtable<Integer, String> getLines_Store() {
		return lines_Store;
	}

	public void put(int line, String write) {
		lines_Store.put(line, write);
	}
	

}
