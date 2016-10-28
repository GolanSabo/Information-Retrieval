package il.ac.shenkar.main;

import javax.swing.SwingUtilities;

import il.ac.shenkar.FileMonitor.FileMonitor;
import il.ac.shenkar.controller.Controller;
import il.ac.shenkar.view.MainView;

public class Main {

	public static void main(String[] args) 
	{
		final MainView view = new MainView();
		
		System.out.println( "  As a friend from friends his final withdrawal prolonging,\n  Good-bye and Good-bye with emotional lips repeating,\n  (So hard for his hand to release those hands--no more will they meet,\n  No more for communion of sorrow and joy, of old and young,\n  A far-stretching journey awaits him, to return no more,)\n  Shunning, postponing severance--seeking to ward off the last word\n      ever so little,\n  E'en at the exit-door turning--charges superfluous calling back--\n      e'en as he descends the steps,\n  Something to eke out a minute additional--shadows of nightfall deepening,\n  Farewells, messages lessening--dimmer the forthgoer's visage and form,\n  Soon to be lost for aye in the darkness--loth, O so loth to depart!\n  Garrulous to the very last.\n\n");

		
		
		SwingUtilities.invokeLater(new Runnable() 
		{
		    public void run() 
		    {
		    	
		    	view.createMainView();
		    }
		});

	}

}
