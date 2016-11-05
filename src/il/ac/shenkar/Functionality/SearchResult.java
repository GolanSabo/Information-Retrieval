package il.ac.shenkar.Functionality;

import il.ac.shenkar.Details.FileDetails;

import java.util.ArrayList;

public class SearchResult implements Comparable {
	private FileDetails fileDetails;
	private ArrayList<Integer> locations;
	private ArrayList<String> words;
	private int rank = 0;
	
	
	public SearchResult(FileDetails fileDetails) {
		if(!fileDetails.getExtension().contains("txt") && !fileDetails.getExtension().contains("pdf"))
			rank += 5000;
		this.fileDetails = fileDetails;
		locations = new ArrayList<>();
		words = new ArrayList<>();
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public ArrayList<Integer> getLocations() {
		return locations;
	}

	public void addLocations(int locations) {
		if(locations < -1)
			rank +=1000;
		else if(locations > -1)
			++rank;
		
		this.locations.add(locations);
	}
	
	public FileDetails getFileDetails() {
		return fileDetails;
	}

	@Override
	public int compareTo(Object o) {
		int x = ((SearchResult)o).getRank();

		return x - rank;
	}
	
	public ArrayList<String> getWords() {
		return words;
	}
	
	public void setWord(String word){
		words.add(word);
	}

	@Override
	public String toString() {
		return "SearchResult [rank = " + rank + " fileDetails = " + fileDetails + ", locations = "
				+ locations + ", Word = " + words + "]\n";
	}
	
	
}
