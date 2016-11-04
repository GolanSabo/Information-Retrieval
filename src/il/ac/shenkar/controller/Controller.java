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

/**
 * The Controller class is responsible to coordinate between the 
 *view and the model
 */
public class Controller 
{
	/**
	 * An enum to represent the type of document
	 */
	public enum ResultType {DOCUMENT, IMAGE};
	
	private  final String sourceFolderPath = "./SourceFolder";
	
	private  final String storageFolderPath = "./StorageFolder";
	
	private  final String detailsPath = "./FileDetailsStorage";
	
	//A list that contains text document extensions
	private final ArrayList<String> documentExtension = 
			new ArrayList<String>(Arrays.asList(".txt",".doc",".pdf"));	
	
	//A list that contains image document extensions
	private final ArrayList<String> imageExtension = 
			new ArrayList<String>(Arrays.asList(".png",".jpg",".bmp", ".gif"));
	
	//A list that holds the file details of the documents in database
	private  ArrayList<FileDetails> filesInfo = new ArrayList<FileDetails>();
	
	//The main view reference
	private MainView view;
	
	//Monitors the files in the source folder 
	private FileMonitor fileMonitor;
	
	//The only instance of the controller
	private static Controller _instance = null;
	
	//Basic constructor
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
	 * Stores the file details as received from the GUI in the source folder
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
		
		//Checks if the file name exists in database - if so, throws exception 
		checkDocumentsIfNameExists(docName);
		
		String extension  = getExtension(filePath);
		Path dest = Paths.get(sourceFolderPath+"/"+ docName + extension);
		int index = FileUtils.readIndex();
		FileUtils.writeIndex(index);
		FileDetails newFile = new FileDetails(index,dest.toString(), docName, _author, 
				_subject, _description, _date, extension,true);
		
		//Add the file details to the list and then save as serializable 
		filesInfo.add(newFile);
		FileUtils.SaveToFile(filesInfo, "fileDetails.ser", detailsPath);
		
		//Move the file to the source folder
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
	public  Vector<Link> getSearchResults(String query) throws NoResultsException
	{
		
		Vector<Link> links=null;
		
		try 
		{
			links = new Vector<Link>();
			ArrayList<SearchResult> results = new ArrayList<SearchResult>();
			long begin = System.currentTimeMillis();
			results = Searcher.SearchWord(query);
			long end = System.currentTimeMillis();
			
			view.setSearchTime(end-begin, results.size());
			for(SearchResult result: results)
			{
				result.getFileDetails().setPath(storageFolderPath + "/" + 
			result.getFileDetails().getDocumentName() + result.getFileDetails().getExtension());
				
				//Get the type of document and create a link based on the type
				ResultType type = getResultType(result.getFileDetails().getExtension());
				links.addElement(new Link(result,type));	
			}	
			if (links.size()==0)
				throw new NoResultsException("Search returned no results!");
		} 
		catch (Exception e) 
		{
			throw new NoResultsException(e.getMessage());
		}
		
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
		fileMonitor = new FileMonitor();
		fileMonitor.start();
		loadFileDetailsFromDatabase();
		
	}
	
	/**
	 * A method to check if the file is active based on file index
	 * @param fileIndex
	 * @return true if active, false otherwise
	 */
	public boolean checkIfFileIsActive(int fileIndex)
	{
		for(FileDetails fd: filesInfo)
		{
			if(fd.getIndex()==fileIndex)
				return fd.isActive();
		}
		return false;
	}
	
	/**
	 * A method to check the visibility given a string of the file name
	 * @param fileName - the file name given by the view
	 * @return - "Visible" if visible, "Hidden" otherwise
	 */
	public String checkVisibility(String fileName) 
	{
		for(FileDetails fd: filesInfo)
		{
			if(fd.getDocumentName().equals(fileName))
			{
				if (fd.isActive())
					return "Visible";
				else 
					return "Hidden";
			}
		}
		return null;
	}
	
	/**
	 * A getter for the file details list
	 * @return The filesInfo list
	 */
	public ArrayList<FileDetails> getFilesInfo() {
		return filesInfo;
	}

	/**
	 * A setter for the file details list
	 * @param The filesInfo list
	 */
	public void setFilesInfo(ArrayList<FileDetails> filesInfo) {
		this.filesInfo = filesInfo;
	}

	/**
	 *  A getter for the view
	 * @return the view
	 */
	public MainView getView() {
		return view;
	}

	/**
	 * A setter for the view
	 * @param view - the main view
	 */
	public void setView(MainView view) {
		this.view = view;
	}

	/**
	 * A getter for the file monitor
	 * @return - the file monitor
	 */
	public FileMonitor getFileMonitor() {
		return fileMonitor;
	}

	/**
	 * A setter for the monitor
	 * @param fileMonitor
	 */
	public void setFileMonitor(FileMonitor fileMonitor) {
		this.fileMonitor = fileMonitor;
	}

	



	
	
}
