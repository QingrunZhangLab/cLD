package linkage_d;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Thread_chromosomes {
	
	Find_ld ranged_Process;
	Filter_map_ped filter;
	ArrayList <Integer> chromosomes;
	int thread_Count;
	int [][] ATGC;
	int ori_Count;
	File output_Folder;
	int Range;
	
	Thread_chromosomes(Filter_map_ped filter, Find_ld ranged_Process,int thread_Count, int [][] ATGC,int ori_Count,File output_Folder, int Range){
		this.filter = filter;
		this.ranged_Process = ranged_Process;
		this.chromosomes = ranged_Process.chromosome_Filter(ranged_Process.getSnp_chr_Index());
		this.thread_Count = thread_Count;
		this.ATGC = ATGC;
		this.ori_Count = ori_Count;
		this.output_Folder = output_Folder;
		this.Range = Range;
	}
	
	public void Agent() throws InterruptedException {
		
		int handler = 0;
		int cores = Runtime.getRuntime().availableProcessors();
		if (cores > 1) {
			cores = cores - 1;			
		}
		ExecutorService ex = Executors.newFixedThreadPool(cores);
		System.out.println("Number of Cores\t: " + cores + "\n");
		
		while(handler<chromosomes.size()) {
			ex.execute(new Threadscape(chromosomes.get(handler), filter, ATGC, ori_Count, output_Folder,Range));
			//thread_1.start();
			handler++;
		}
		ex.shutdown();
		while(!ex.isTerminated()) {}
		
	}
	
}

class Threadscape implements Runnable{
	
	int chr;
	Find_ld ranged_Process;
	ArrayList <Integer> snp_Positions;
	Filter_map_ped filter;
	
	Threadscape(int chr, Filter_map_ped filter,int [][] ATGC,int ori_Count,File output_Folder,int Range){
		this.filter = filter;
		this.chr = chr;
		this.ranged_Process = new Find_ld(filter.getMap_File(),filter.getPed_File(),
				filter.getSnp_Index(), ATGC, ori_Count, output_Folder);
		this.ranged_Process.setRange_Bps(Range);
		//passes the filtered HASHTABLE containing SNP NUMBER, Chromosome_number to the LD calculating class
		this.ranged_Process.setSnp_chr_Index(filter.getSnp_chr_Index());
		this.snp_Positions = ranged_Process.get_snp_Positions();
	}
	
	public void run() {
		try {
			ranged_Process.thread_Write(snp_Positions, filter.getId_count(), chr);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	

	}
	
}


