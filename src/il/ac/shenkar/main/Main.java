package il.ac.shenkar.main;

import javax.swing.SwingUtilities;

import il.ac.shenkar.FileMonitor.FileMonitor;
import il.ac.shenkar.Functionality.Soundex;
import il.ac.shenkar.controller.Controller;
import il.ac.shenkar.view.MainView;

public class Main {

	public static void main(String[] args) 
	{
		final MainView view = new MainView();
		System.out.println(Soundex.soundex("meat") + "\n" +Soundex.soundex("meet") + "\n"+Soundex.soundex("meed") + "\n");
		
		SwingUtilities.invokeLater(new Runnable() 
		{
		    public void run() 
		    {
		    	
		    	view.createMainView();
		    }
		});

	}

}
