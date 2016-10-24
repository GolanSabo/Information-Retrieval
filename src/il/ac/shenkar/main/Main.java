package il.ac.shenkar.main;

import javax.swing.SwingUtilities;

import il.ac.shenkar.FileMonitor.FileMonitor;
import il.ac.shenkar.controller.Controller;
import il.ac.shenkar.view.MainView;

public class Main {

	public static void main(String[] args) 
	{
		
		Controller.loadFileDetailsFromDatabase();
		FileMonitor fm = new FileMonitor(20 * 1000);
		fm.start();
		
		final MainView view = new MainView();
		SwingUtilities.invokeLater(new Runnable() 
		{
		    public void run() 
		    {
		    	
		    	view.createMainView();
		    }
		});

	}

}
