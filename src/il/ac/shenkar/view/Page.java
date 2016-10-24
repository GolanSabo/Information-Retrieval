package il.ac.shenkar.view;

import java.util.ArrayList;

public class Page 
{
	private  ArrayList<String> data= new ArrayList<>();
	public void addToData(String word)
	{
		data.add(word);
	}
	public  ArrayList<String> getData() {
		return data;
	}
	public  void setData(ArrayList<String> _data) {
		data = _data;
	};
	
	
	
}
