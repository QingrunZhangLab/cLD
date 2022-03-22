package linkage_d;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Hashtable;
import java.util.stream.Stream;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.RowFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

public class Checkers {
	static JFrame f;
	static JTable j;

	public static void main(String[] args) throws IOException {
		
		int x = 0;
		Printer(x);
		System.out.println(x);


	}

	public static void Printer(int x) {
		x=x+10;
	}

}
