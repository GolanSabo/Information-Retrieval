package il.ac.shenkar.FileMonitor;

import il.ac.shenkar.FileParser.FileParser;
import il.ac.shenkar.Utils.FileUtils;
import il.ac.shenkar.Utils.MapUtils;

import java.io.File;
/**
 * FileMonitor is responsible of scanning source folder every X sec in order to parse new files into inverted file
 * 
 */
public class FileMonitor extends Thread{
	public long getBatchTime() {
		return batchTime;
	}

	public void setBatchTime(long batchTime) {
		this.batchTime = batchTime;
	}

	private final String sourceFolderPath = "./SourceFolder";
	private final String InvertedFolderPath = "./InvertedFile";
	private long batchTime;
	private static boolean active = false;
	
	/**
	 * constructor
	 * @param batchTime - specify the delay (in millisec) between batch jobs  
	 */
	public FileMonitor(long batchTime) {
		super();
		this.batchTime = batchTime;
	}
	
	/**
	 * 
	 * @return true - if a batch job is currently active
	 * 		   false - if a batch job is not currently active
	 */
	public static boolean isActive(){
		return active;
	}

	@Override
	public void run() {
		while(true){
			active = true;
			File folder = new File(sourceFolderPath);
			File[] listOfFiles = folder.listFiles();
			if(listOfFiles.length!=0)
			{
				for (int i = 0; i < listOfFiles.length; ++i) {
					//File found -> parsing it
					FileParser.Parse(listOfFiles[i]);
				}
				folder = new File(InvertedFolderPath);
				listOfFiles = folder.listFiles();
				/*
				 * this for loop Merges the inverted files of the new files into the main inverted file and delete 
				 * the temporary inverted files
				*/
				for (int i = 0; i < listOfFiles.length; ++i) {
					MapUtils.MergeMaps(FileUtils.LoadFromFile(listOfFiles[i].getAbsolutePath()));
					listOfFiles[i].delete();
				
			}
			}
			try {
				/*wait for next batch*/
				active = false;
				sleep(batchTime);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}

}
