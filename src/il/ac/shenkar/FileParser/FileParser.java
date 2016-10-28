package il.ac.shenkar.FileParser;

import il.ac.shenkar.Details.FileDetails;
import il.ac.shenkar.Details.Node;
import il.ac.shenkar.Utils.FileUtils;
import il.ac.shenkar.Utils.StopList;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Scanner;

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
		FileDetails fileDetails = getFileDetailsFromDataBase(file);
		Node node;
		try {
			parse(fileDetails.getName(), fileDetails, -2);
			parse(fileDetails.getDescription(), fileDetails, -3);
			parse(fileDetails.getAuthor(), fileDetails, -4);
			DateFormat sdf = new SimpleDateFormat("dd/mm/yy");
			parse(sdf.format(fileDetails.getDate()), fileDetails, -5);
			BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
			StringBuilder sb = new StringBuilder();
			String tmp;
			while((tmp = bufferedReader.readLine()) != null)
				sb.append(tmp);
			parse(sb.toString(),fileDetails,0);
//			scanner = new Scanner(file);
//			/*scan a file word by word and add it to a hashmap with word as key and file data as value */
//			while(scanner.hasNext()){
//				String word = scanner.next().replaceAll("[-+.^:;,\'\"\\()?!“”‘’— ]*","");
//				word = word.toLowerCase();
//				if(invertedFile.containsKey(word)){
//					node = invertedFile.get(word);
//				}
//				else{
//					node = new Node(fileDetails);
//				}
//				//save word location in file
//				node.AddLocation(counter);
//				invertedFile.put(word, node);
//				++counter;
//			}
//			PrinterUtils.PrintMap(invertedFile);
			//after the file have been fully parsed -> save temporary inverted file and copy it to storage folder 
			FileUtils.SaveToFile(invertedFile, fileDetails.getDocumentName(), InvertedFileFolderPath);
			if(FileUtils.CopyFile(file, storageFolderPath))
			{
//				scanner.close();
				bufferedReader.close();
				file.delete();
			}

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
			String word = scanner.next().replaceAll("[-+.^:;,\'\"\\()?!“”‘’— ]*","");
			word = word.toLowerCase();
			if(StopList.stopList.contains(word)){
				if(location >= 0)
					++location;
				continue;
			}
			if(invertedFile.containsKey(word)){
				node = invertedFile.get(word);
			}
			else{
				node = new Node(fileDetails);
			}
			//save word location in file
			node.AddLocation(location);
			if(location >= 0)
				++location;
			invertedFile.put(word, node);
		}
	}
	
	private static FileDetails getFileDetailsFromDataBase(File file) 
	{
		FileDetails temp = null;
		BufferedReader br = null;
		String line = null;
		String[] tokens;
		try {
			File detailsFile = null;
			detailsFile = new File(fileDetailsPath);
			
			FileReader fr = new FileReader(detailsFile);
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
				String extension = tokens[7];
				boolean active = Boolean.getBoolean(tokens[7]);
				
				temp = new FileDetails(index,path,name,author,subject,description,date,extension,active);
				Path a = Paths.get(temp.getPath());
				Path b = Paths.get(file.getPath());
				if (a.compareTo(b)==0)
					return temp;
				
				line = br.readLine();
			}
			
		} 
		catch (IOException e) 
		{
            System.out.println("IO Error occured trying to read from local database");
		}
	
		
		return null;
	}
	
	


}
