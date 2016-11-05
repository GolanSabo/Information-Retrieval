package il.ac.shenkar.FileParser;

import il.ac.shenkar.Details.FileDetails;
import il.ac.shenkar.Details.Node;
import il.ac.shenkar.Utils.StopList;
import il.ac.shenkar.controller.Controller;
import il.ac.shenkar.pdf.utils.PDFHandler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import org.apache.commons.io.FileUtils;
/**
 * 
 * File Parser is responsible of parsing files found while batch job and
 * parse them into a temporary inverted file
 *
 */
public class FileParser {

	public static final int NameLocation = -2;
	public static final int DescriptionLocation = -3;
	public static final int AuthourLocation = -4;
	public static final int DateLocation = -5;
	
	private final static String storageFolderPath = "./StorageFolder/";
	private final static String InvertedFileFolderPath = "./InvertedFile";
	private static final String fileDetailsPath = "./FileDetailsStorage/details.txt";
	private static HashMap<String,Node> invertedFile = new HashMap<String, Node>();
	/**
	 * Parse file into a temporary inverted file and passes it to InvertedFile folder
	 * @param file
	 */
	public static void Parse(File file){
		int counter = 0;
		Scanner scanner;
		invertedFile = new HashMap<String, Node>();
		FileDetails fileDetails= null;
		ArrayList<FileDetails> details = Controller.getInstance().getFilesInfo();
		for(FileDetails fd: details)
		{
			Path a = Paths.get(fd.getPath());
			Path b = Paths.get(file.getPath());
			if (a.compareTo(b)==0)
				fileDetails = fd;
		}
		Node node;
		try {
			System.out.println("fileDetails = " + fileDetails);
			parse(fileDetails.getDocumentName(), fileDetails, -2);
			parse(fileDetails.getDescription(), fileDetails, -3);
			parse(fileDetails.getAuthor(), fileDetails, -4);
			
			DateFormat sdf = new SimpleDateFormat("dd/mm/yy");
			parse(sdf.format(fileDetails.getDate()), fileDetails, -5);
			parse(fileDetails.getSubject(), fileDetails, -6);
			
			switch (fileDetails.getExtension())
			{
			case ".txt":
				BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
				StringBuilder sb = new StringBuilder();
				String tmp;
				while((tmp = bufferedReader.readLine()) != null)
					sb.append(tmp);
				parse(sb.toString(),fileDetails,0);
				bufferedReader.close();
				break;
				
			case ".pdf":
				String text = PDFHandler.getText(fileDetails.getPath());
				parse(text,fileDetails,0);
				break;
				
			default:
				break;
				
			}
			

			//after the file have been fully parsed -> save temporary inverted file and copy it to storage folder 
			il.ac.shenkar.Utils.FileUtils.SaveToFile(invertedFile, fileDetails.getDocumentName(), InvertedFileFolderPath);
			FileUtils.copyFile(file, new File(storageFolderPath+"/"+fileDetails.getDocumentName()+fileDetails.getExtension()));
			String fileDest = storageFolderPath+"/"+fileDetails.getDocumentName()+fileDetails.getExtension();
			System.out.println(fileDest);
			FileUtils.forceDelete(file);
			/*if(FileUtils.CopyFile(file, storageFolderPath))
			{
				if(file.delete()){
					System.out.println("FILE DELETED!!");
				}else{
					System.out.println("FILE NOT DELETED!!");
				}
			}*/

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static void parse(String text, FileDetails fileDetails, int location){
		if(text == null)
			return;
		Scanner scanner = new Scanner(text);
		Node node;
		/*scan a file word by word and add it to a hashmap with word as key and file data as value */
		while(scanner.hasNext()){
			String word = scanner.next().replaceAll("[-+.^:;,\'\"\\()?!“”‘’— =<>\0&%$#*!?@|]*","");

			word = word.toLowerCase();
			if(StopList.stopList.contains(word)||word.length() <= 1){
				if(location >= 0)
					++location;
				continue;
			}
			
			if(invertedFile.containsKey(word)){
				node = invertedFile.get(word);
			}
			else{
				node = new Node(fileDetails,word);
			}
			//save word location in file
			node.AddLocation(location);
			if(location >= 0)
				++location;
			invertedFile.put(word, node);
		}
	}
	
	
	


}

/*
public static void Parse(File file){
	int counter = 0;
	Scanner scanner;
	invertedFile = new HashMap<String, Node>();
	FileDetails fileDetails= null;
	ArrayList<FileDetails> details = Controller.getInstance().getFilesInfo();
	for(FileDetails fd: details)
	{
		Path a = Paths.get(fd.getPath());
		Path b = Paths.get(file.getPath());
		if (a.compareTo(b)==0)
			fileDetails = fd;
	}
	Node node;
	try {
		parse(fileDetails.getDocumentName(), fileDetails, -2);
		parse(fileDetails.getDescription(), fileDetails, -3);
		parse(fileDetails.getAuthor(), fileDetails, -4);
		DateFormat sdf = new SimpleDateFormat("dd/mm/yy");
		parse(sdf.format(fileDetails.getDate()), fileDetails, -5);
		if(fileDetails.getExtension().equals(".txt"))
		{
			BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
			StringBuilder sb = new StringBuilder();
			String tmp;
			while((tmp = bufferedReader.readLine()) != null)
				sb.append(tmp);
			parse(sb.toString(),fileDetails,0);
		}
		else
		{
			String text = PDFHandler.getText(fileDetails.getPath());
			parse(text,fileDetails,0);
		}
		//after the file have been fully parsed -> save temporary inverted file and copy it to storage folder 
		FileUtils.SaveToFile(invertedFile, fileDetails.getDocumentName(), InvertedFileFolderPath);
		
		Path src = Paths.get(file.getAbsolutePath());
		Path dest = Paths.get(storageFolderPath+"/" + fileDetails.getDocumentName()+fileDetails.getExtension());
		Files.copy(src, dest, StandardCopyOption.REPLACE_EXISTING);
		file.delete();
		/*
		if(FileUtils.CopyFile(file, storageFolderPath))
		{
//			scanner.close();
			//bufferedReader.close();
			file.delete();
		}

	} catch (IOException e) {
		e.printStackTrace();
	}
}*/

