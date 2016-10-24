package il.ac.shenkar.FileMonitor;

import java.io.File;
import java.util.TimerTask;


public class MonitorTask extends TimerTask{
	private String sourceFolderPath = null;
	private String storageFolderPath = null;
	private File file;
	public MonitorTask(String sourcePath, String storagePath){
		sourceFolderPath = sourcePath;
		storageFolderPath = storagePath;
	}

	@Override
	public void run() {
		File folder = new File(sourceFolderPath);
		File[] listOfFiles = folder.listFiles();
		for (int i = 0; i < listOfFiles.length; ++i) {
			
		}
		
	}

}
