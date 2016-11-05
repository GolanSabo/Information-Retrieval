package il.ac.shenkar.FileMonitor;

import il.ac.shenkar.FileParser.FileParser;
import il.ac.shenkar.Utils.FileUtils;
import il.ac.shenkar.Utils.MapUtils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Writer;
/**
 * FileMonitor is responsible of scanning source folder every X sec in order to parse new files into inverted file
 * 
 */
public class FileMonitor extends Thread{


	private final String sourceFolderPath = "./SourceFolder";
	private final String InvertedFolderPath = "./InvertedFile";
	private final File batchFile = new File("./conf/batchDelay.txt");
	private long batchTime;
	private static boolean active = false;

	/**
	 * constructor
	 * @param batchTime - specify the delay (in millisec) between batch jobs  
	 */
	public FileMonitor() {
		super();
		if(batchFile.exists()){
			BufferedReader br = null;
			try {
				String sCurrentLine;

				br = new BufferedReader(new FileReader(batchFile));

				sCurrentLine = br.readLine();
					batchTime = Long.parseLong(sCurrentLine);
				br.close();
			} catch (IOException e) {
				
				e.printStackTrace();
			}
		}
			
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
	public long getBatchTime() {
		return batchTime;
	}

	public void setBatchTime(long batchTime) {
		if(batchTime < 0)
			return;
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new FileWriter(batchFile));

			bw.write(Long.toString(batchTime));
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}


		this.batchTime = batchTime;
	}

}
