package il.ac.shenkar.Utils;

import il.ac.shenkar.Details.Node;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
/**
 * 
 * File Utilities
 *
 */
public class FileUtils {
	private static final File indexPath = new File("./conf/CurrentIndex.txt");
	/**
	 * copy file to destination
	 * @param file - the file to copy
	 * @param path - the path to the destination folder
	 * @return
	 */
	public static boolean CopyFile(File file, String path){
		File dest = new File(path + file.getName());
		InputStream input = null;
		OutputStream output = null;
		try {
			input = new FileInputStream(file);
			output = new FileOutputStream(dest);
			byte[] buf = new byte[1024];
			int bytesRead;
			while ((bytesRead = input.read(buf)) > 0) {
				output.write(buf, 0, bytesRead);
			}
			if(input != null)
				input.close();
			if(output != null)
				output.close();
		}catch(IOException e){
			return false;
		}
		return true;
	}

	/**
	 * save data of Serializable interface to a file
	 * @param data - Serializable type data
	 * @param fileName - destination file name
	 * @param path - destination folder name
	 * @throws IOException
	 */
	public static void SaveToFile(Serializable data, String fileName, String path) throws IOException{

		FileOutputStream fos = new FileOutputStream(new File(path + "/" + RemoveExtension(fileName) +".ser"));
		ObjectOutputStream oos = new ObjectOutputStream(fos);
		oos.writeObject(data);
		oos.close();
		fos.close();
	}


	/**
	 * load inverted file and store it as hashmap
	 * @param fileName - the file to load
	 * @return Map contains Strings as keys and Nodes as values
	 */
	public static Map LoadFromFile(String fileName){
		HashMap<String,Node> invertedFile = null;
		try {
			FileInputStream inputFileStream = new FileInputStream(fileName);
			ObjectInputStream objectInputStream = new ObjectInputStream(inputFileStream);
			invertedFile = (HashMap<String,Node>)objectInputStream.readObject();
			objectInputStream.close();
			inputFileStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} 

		return invertedFile;
	}

	/**
	 * removes extension from file name
	 * @param str - file name
	 * @return file name without extension
	 */
	private static String RemoveExtension (String str){
		int pos = str.lastIndexOf(".");
		if (pos == -1) return str;
		return str.substring(0, pos);
	}

	/**
	 * reads the index of the file out of an file contains the index number
	 */
	public static int readIndex(){
		int index = 0;
		if(indexPath.exists()){
			try {
				Scanner scanner = new Scanner(indexPath);
				while (scanner.hasNextLine())
				{
					String num = scanner.nextLine();
					index = Integer.parseInt(num);
				}
				scanner.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		return index;

	}
	
	/**
	 * write next index to the index file
	 */
	public static void writeIndex(int index){
		try {
			PrintWriter printWriter = new PrintWriter(indexPath);
			printWriter.print(index + 1);
			printWriter.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}
	
}
