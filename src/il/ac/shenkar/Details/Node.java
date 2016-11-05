package il.ac.shenkar.Details;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 
 * Node contains necessary data about every word in every document
 * Implements Serializable
 */
public class Node implements Serializable{
	private static final long serialVersionUID = 1L;
	private FileDetails fileDetails;
	private int numOfTimes;
	private String word;
	private ArrayList<Integer> locations;
	
	/**
	 * constructor
	 * @param fileDetails - details about the 
	 */
	public Node(FileDetails fileDetails,String word){
		this.fileDetails = fileDetails;
		this.word = word;
		locations = new ArrayList<Integer>();
	}
	
	public Node(FileDetails fileDetails){
		this.fileDetails = fileDetails;
		locations = new ArrayList<Integer>();
	}
	
	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public FileDetails getFileDetails(){
		return fileDetails;
	}
	
	public int getNumOfTimes() {
		return numOfTimes;
	}
	
	public int getFileIndex() {
		return fileDetails.getIndex();
	}
	
	public void AddLocation(int location){
		locations.add(location);
		++numOfTimes;
	}
	
	public ArrayList<Integer> getLocations(){
		return locations;
	}

	@Override
	public String toString() {
		return  fileDetails + " Number of appearance = "
				+ numOfTimes + ", In Locations = " + locations + "\n";
	}

//	@Override
//	public String toString() {
//		return "Node [numOfTimes=" + numOfTimes + ", locations=" + locations + ", fileIndex=" + fileIndex + "]";
//	}
	
	
	
}
