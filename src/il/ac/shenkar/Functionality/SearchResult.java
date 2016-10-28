package il.ac.shenkar.Functionality;

import il.ac.shenkar.Details.FileDetails;

import java.util.ArrayList;

public class SearchResult implements Comparable {
	private FileDetails fileDetails;
	private ArrayList<Integer> locations;
	
	public SearchResult(FileDetails fd, ArrayList<Integer> location){
		this(fd);
		locations = location;
	}

	public SearchResult(FileDetails fileDetails) {

		this.fileDetails = fileDetails;
	}

	public ArrayList<Integer> getLocations() {
		return locations;
	}

	public void setLocations(ArrayList<Integer> locations) {
		this.locations = locations;
	}

	public void addLocations(int locations) {
		this.locations.add(locations);
	}
	
	public void addLocations(ArrayList<Integer> locations) {
		this.locations.addAll(locations);
	}
	
	public FileDetails getFileDetails() {
		return fileDetails;
	}

	@Override
	public int compareTo(Object o) {
		int x = ((SearchResult)o).locations.size();

		return x - locations.size();
	}

	@Override
	public String toString() {
		return "SearchResult [fileDetails=" + fileDetails + ", locations="
				+ locations + "]\n";
	}
	
	
}
