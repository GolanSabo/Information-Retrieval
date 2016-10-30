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
	private  final String detailsPath = "./FileDetailsStorage";
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
	
	/**
	 * Returns the instance of the controller
	 * @return the instance of the controller
	 */
	public static Controller getInstance()
	{
		if(_instance==null)
			_instance = new Controller();
		return _instance;
	}

	/**
	 * A method that returns the document names in database
	 * @return A vector of document names in database
	 */
	public  Vector<String> getDocumentsNames() 
	{

		Vector<String> names = new Vector<>();

		for(FileDetails fd: filesInfo)
		{
			names.add(fd.getDocumentName());
		}
		return names;
	}
	
	/*
	 * A method that checks the extension and returns the type
	 * @param ext - the extension
	 * @return - the result type (DOCUMENT/IMAGE)
	 */
	private ResultType getResultType(String ext)
	{
		if(documentExtension.contains(ext))
			return ResultType.DOCUMENT;
		else if(imageExtension.contains(ext))
			return ResultType.IMAGE;
		else return ResultType.DOCUMENT;
	}
	
	/**
	 * Stores the file details as received from the GUI
	 * @param filePath
	 * @param docName
	 * @param _author
	 * @param _subject
	 * @param _description
	 * @param _date
	 * @throws IOException - Thrown if cannot copy file to Source folder
	 * @throws DuplicateNameException - Thrown if the Document name already exist in Database
	 * @throws ParseException - Thrown when date format is incorrect
	 * @throws FilePathException - Thrown if the file path doesn't exist
	 */
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
		filesInfo.add(newFile);
		FileUtils.SaveToFile(filesInfo, "fileDetails.ser", detailsPath);
		
			Files.copy(src, dest, StandardCopyOption.REPLACE_EXISTING);
		
	}
	
	/*
	 * Checks if the document name exists in database
	 */
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
	
	/**
	 * Loads the file details from database to filesInfo
	 */
	public  void loadFileDetailsFromDatabase()
	{

		File file = new File(detailsPath + "/fileDetails.ser");
		if(!file.exists())
			filesInfo = new ArrayList<FileDetails>();
		else
			filesInfo = FileUtils.loadFileDetailsFromFile(detailsPath + "/fileDetails.ser");
		

	}
	
	/**
	 * A method that gets the query from the view and returns the links associated
	 * with the query
	 * @param query - The query from the user
	 * @return - a vector of Links
	 * @throws NoResultsException - Thrown when there are no results
	 */
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
		
		catch (Exception e) 
		{
			throw new NoResultsException("Search returned no results!");
		}
		if (links.size()==0)
			throw new NoResultsException("Search returned no results!");
		return links;
	
	}
	
	/*
	 * A method to get the extension of the file
	 */
	private  String getExtension(String _path) {
		int pos = _path.lastIndexOf(".");
		if (pos == -1) return _path;
		return _path.substring(pos,_path.length());
	}
	
	/**
	 * A method to change the visibility of a file
	 * @param docName - The name of the file to be changed
	 * @param isActive - True if visible, False if file to be hidden
	 */
	public  void changeVisibility(String docName, boolean isActive) 
	{
		
		for(FileDetails file: filesInfo)
		{
			if(file.getDocumentName().equals(docName))
			{
				file.setActive(isActive);
				
			}
		}
		try {
			FileUtils.SaveToFile(filesInfo, "fileDetails.ser", detailsPath);
			//filesInfo = FileUtils.loadFileDetailsFromFile(detailsPath + "fileDetails.ser");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	/**
	 * A method to set the batch time
	 * @param str - The string representing the time passed from the view
	 */
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
	
	/**
	 * The initialization process  
	 */
	public void init()
	{
		fileMonitor = new FileMonitor(20 * 1000);
		fileMonitor.start();
		loadFileDetailsFromDatabase();
		
	}
	
	public boolean checkIfFileIsActive(int fileIndex)
	{
		for(FileDetails fd: filesInfo)
		{
			if(fd.getIndex()==fileIndex)
				return fd.isActive();
		}
		return false;
	}
	
	public ArrayList<FileDetails> getFilesInfo() {
		return filesInfo;
	}

	public void setFilesInfo(ArrayList<FileDetails> filesInfo) {
		this.filesInfo = filesInfo;
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



	
	
}
