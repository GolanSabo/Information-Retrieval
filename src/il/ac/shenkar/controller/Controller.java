package il.ac.shenkar.controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;

import javax.swing.text.DefaultEditorKit.CopyAction;

import il.ac.shenkar.Details.FileDetails;
import il.ac.shenkar.Details.Node;
import il.ac.shenkar.FileMonitor.FileMonitor;
import il.ac.shenkar.Functionality.SearchResult;
import il.ac.shenkar.Functionality.Searcher;
import il.ac.shenkar.Utils.FileUtils;
import il.ac.shenkar.errors.DuplicateNameException;
import il.ac.shenkar.errors.FilePathException;
import il.ac.shenkar.errors.NoResultsException;
import il.ac.shenkar.view.Link;
import il.ac.shenkar.view.MainView;


public class Controller 
{
	public enum ResultType {DOCUMENT, IMAGE, SOUND};
	private  final String sourceFolderPath = "./SourceFolder";
	private  final String storageFolderPath = "./StorageFolder";
	private  final String fileDetailsPath = "./FileDetailsStorage/details.txt";
	private final ArrayList<String> documentExtension = 
			new ArrayList<String>(Arrays.asList(".txt",".doc",".pdf"));
	private final ArrayList<String> imageExtension = 
			new ArrayList<String>(Arrays.asList(".png",".jpg",".bmp", ".gif"));
	private final ArrayList<String> soundExtension = 
			new ArrayList<String>(Arrays.asList("wav","mp3"));
	private  ArrayList<FileDetails> filesInfo = new ArrayList<FileDetails>();
	private MainView view;
	private FileMonitor fileMonitor;
	private static Controller _instance = null;
	
	private Controller(){}
	
	
	public static Controller getInstance()
	{
		if(_instance==null)
			_instance = new Controller();
		return _instance;
	}
	
	public MainView getView() {
		return view;
	}


	public void setView(MainView view) {
		this.view = view;
	}


	public FileMonitor getFileMonitor() {
		return fileMonitor;
	}


	public void setFileMonitor(FileMonitor fileMonitor) {
		this.fileMonitor = fileMonitor;
	}


	public  Vector<String> getDocumentsNames() 
	{
		
		Vector<String> names = new Vector<>();
		
	    for(FileDetails fd: filesInfo)
	    {
	    	names.add(fd.getDocumentName());
	    }
	    return names;
	}
	
	private ResultType getResultType(String ext)
	{
		if(documentExtension.contains(ext))
			return ResultType.DOCUMENT;
		else if(imageExtension.contains(ext))
			return ResultType.IMAGE;
		else return ResultType.DOCUMENT;
	}
	
	public  void storeInDatabase(String filePath, String docName, String _author
			,String _subject, String _description, String _date) throws IOException, DuplicateNameException, ParseException, FilePathException
	{
		File source = new File(filePath);
		if(!source.exists())
			throw new FilePathException("File Does not exist");
		
		Path src = Paths.get(filePath);
		
		checkDocumentsIfNameExists(docName);
		String extension  = getExtension(filePath);
		Path dest = Paths.get(sourceFolderPath+"/"+ docName + extension);
		int index = FileUtils.readIndex();
		FileUtils.writeIndex(index);
		FileDetails newFile = new FileDetails(index,dest.toString(), docName, _author, 
				_subject, _description, _date, extension,true);
		saveFileDetails(newFile);
		filesInfo.add(newFile);
		
		
			Files.copy(src, dest, StandardCopyOption.REPLACE_EXISTING);
		
	}
	
	
	private  void checkDocumentsIfNameExists(String docName) throws DuplicateNameException
	{
		for(FileDetails file: filesInfo)
		{
			if(file.getDocumentName().equals(docName))
			{
				throw new DuplicateNameException("The document name already exists in database!");
			}
		}
		
	}
	public  void loadFileDetailsFromDatabase() throws ParseException 
	{
		BufferedReader br = null;
		String line = null;
		String[] tokens;
		try {
			File file = null;
			file = new File(fileDetailsPath);
			
			FileReader fr = new FileReader(file);
			br = new BufferedReader(fr);
			
			//Read the next currency
			line = br.readLine();
			
			while(line!=null)
			{
				
				tokens = line.split(";");
				int index = Integer.parseInt(tokens[0]);
				String name = tokens[1];
				String path = tokens[2];
				String author = tokens[3];
				String subject= tokens[4];
				String description= tokens[5];
				String date= tokens[6];
				String extension= tokens[7];
				Boolean active = Boolean.parseBoolean(tokens[8]);
				
				filesInfo.add(new FileDetails(index,path,name,author,subject,description,date,extension,active));
				
				
				line = br.readLine();
			}
			
		} 
		catch (IOException e) 
		{
            System.out.println("IO Error occured trying to read from local database");
		}
	
		
	}
	private  void saveFileDetails(FileDetails newFile) 
	{
		StringBuilder details = new StringBuilder(newFile.toString());
		BufferedWriter bw = null;
		File file = null;
		try 
		{
			
			file = new File(fileDetailsPath);
			FileWriter fw;
			if (!file.exists()) 
			{
			     file.createNewFile();
			}	
			fw = new FileWriter(file,true);
			
			bw = new BufferedWriter(fw);
			bw.append(details);
			bw.newLine();
		} 
		catch (IOException e) 
		{
			
			
            System.out.println("IO Error occured trying to add to local database");
		}
		finally
		{
			try
			{
				if(bw!=null)
					bw.close();
			}
			catch(Exception ex)
			{
				System.out.println("Error in closing the BufferedWriter"+ex);
			}
		}
		
		
	}
	public  Vector<Link> getResults(String query) throws NoResultsException
	{
		
		Vector<Link> links=null;
		
		try 
		{
			links = new Vector<Link>();
			ArrayList<SearchResult> results = new ArrayList<SearchResult>();
			results = Searcher.SearchWord(query);

			for(SearchResult result: results)
			{
				result.getFileDetails().setPath(storageFolderPath + "/" + 
			result.getFileDetails().getDocumentName() + result.getFileDetails().getExtension());
				ResultType type = getResultType(result.getFileDetails().getExtension());
				links.addElement(new Link(result,type));	
			}
			
			
		} 
		catch (NoResultsException e) 
		{
			throw new NoResultsException("Search returned no results!");
		}
		catch (Exception e) 
		{
			throw new NoResultsException("Search returned no results!");
		}
		
		return links;
	
	}
	
	
	private  String getExtension(String _path) {
		int pos = _path.lastIndexOf(".");
		if (pos == -1) return _path;
		return _path.substring(pos,_path.length());
	}
	public  void changeVisibility(String docName, boolean isActive) 
	{
		File details = new File(fileDetailsPath);
		details.delete();
		FileDetails temp = null;
		for(FileDetails file: filesInfo)
		{
			if(file.getDocumentName().equals(docName))
			{
				file.setActive(isActive);
				break;
			}
			saveFileDetails(file);
		}
		
		
	}
	public void setBatch(String str)
	{
		long time = 0;
		if(str.equals("20 seconds"))
			time = 20*1000;
		else if(str.equals("5 minutes"))
			time = 5*60*1000;
		else if(str.equals("20 minutes"))
			time = 20*60*1000;
		else if(str.equals("1 hour"))
			time = 60*60*1000;
		else if(str.equals("24 hours"))
			time = 24*60*60*1000;
		fileMonitor.setBatchTime(time);
	}
	public void init()
	{
		fileMonitor = new FileMonitor(20 * 1000);
		fileMonitor.start();
		try {
		loadFileDetailsFromDatabase();
		} catch (ParseException e) {
			System.out.println("Date Format incorrect");
		}
	}
	
	
}
