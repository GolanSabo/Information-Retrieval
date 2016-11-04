package il.ac.shenkar.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;

import il.ac.shenkar.Details.Node;
import il.ac.shenkar.Functionality.Searcher;
import il.ac.shenkar.controller.Controller;
import il.ac.shenkar.errors.NoResultsException;

public class MainView extends JFrame implements ActionListener
{
	final static int extraWindowWidth = 100;
	private JTextField input;
	private JButton search;
	private JPanel body;
	private int numberOfResults=0;
	private int pages=0;
	private int page = 1;
	private Vector<Link> links;
	private JButton back;
	private JButton next;
	private JPanel tabbedPaneContainer;
	private JTabbedPane tabbedPane;
	private SettingsContainer settings; 
	private JPanel card1;
	private JPanel card2;
	private JPanel resultContainer;
	private JPanel bottomPanel;
	private JTextField changePage;
	private JLabel numberOfPages;
	private JPanel queryPanel;
	private JLabel searchTime;
	
	public MainView()
	{
		Controller.getInstance().init();
		Controller.getInstance().setView(this);
		searchTime = new JLabel("");
		input = new JTextField(20);
		input.addActionListener(this);
		search = new JButton();
		search.setIcon(new ImageIcon("Search.png"));
		search.addActionListener(this);
		body = new JPanel();
		back = new JButton();
		back.setIcon(new ImageIcon("Back.png"));
		back.addActionListener(this);
		next = new JButton();
		next.setIcon(new ImageIcon("Next.png"));
		next.addActionListener(this);
		links = new Vector<Link>();
		changePage = new JTextField(2);
		changePage.addActionListener(this);
		numberOfPages = new JLabel("");
		tabbedPaneContainer = new JPanel();
		settings = new SettingsContainer();
		bottomPanel = new JPanel();
		tabbedPane = new JTabbedPane();
		queryPanel = new JPanel();
		bottomPanel.setVisible(false);
		card1 = new JPanel() {

			public Dimension getPreferredSize() {
				Dimension size = super.getPreferredSize();
				size.width += extraWindowWidth;
				return size;
			}
		};
		card2 = new JPanel(){
			public Dimension getPreferredSize() {
				Dimension size = super.getPreferredSize();
				size.width += extraWindowWidth;
				return size;
			}
		};
		resultContainer = new JPanel(){
			public Dimension getPreferredSize() {
				Dimension size = super.getPreferredSize();
				size.width += extraWindowWidth;
				return size;
			}
		};


	}
	
	
	public void createMainView()
	{
		
		setTitle("My Search Engine");
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent ev)
			{
				setVisible(false);
				dispose();
				System.exit(0);
				
			}
		});
		
		
		addComponentToPane(tabbedPaneContainer);
		add(tabbedPane,BorderLayout.CENTER);
		setVisible(true);
		pack();
		
	}
	
public void addComponentToPane(Container pane) {
        
    	
        card1.setLayout(new BorderLayout());
        queryPanel.add(input);
        queryPanel.add(search);
        //searchTime.setVisible(false);
        queryPanel.add(searchTime);
        
       
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
        body.setAlignmentX(LEFT_ALIGNMENT);
        bottomPanel.add(back);
		if(page<=1)
			bottomPanel.remove(back);
		
		repaint();
		changePage.setText(""+page);
		bottomPanel.add(changePage);
		bottomPanel.add(numberOfPages);
		bottomPanel.add(next);
		if(page==pages)
			bottomPanel.remove(next);
		
		
        card1.add(queryPanel,BorderLayout.NORTH);
        card1.add(body,BorderLayout.WEST);
     
        card1.add(bottomPanel, BorderLayout.SOUTH);
        card2.add(settings);
 
        tabbedPane.addTab("Search", card1);
        tabbedPane.addTab("Settings", card2);
 
        pane.add(tabbedPane, BorderLayout.NORTH);
    }

	

	@Override
	public void actionPerformed(ActionEvent event) 
	{
		
		if( event.getSource()==search || event.getSource()==input)
		{
			body.removeAll();
			this.repaint();
			try {
				bottomPanel.setVisible(true);
				page=1;
				links = Controller.getInstance().getSearchResults(input.getText());
				numberOfResults = links.size();
				if(numberOfResults%4==0)
					pages = numberOfResults/4; 
				else
					pages = pages = numberOfResults/4+1;
				numberOfPages.setText("/ "+Integer.toString(pages));
				addLinksToPanel();
			} catch (NoResultsException e) {
				bottomPanel.setVisible(false);
				JOptionPane.showMessageDialog(this,e.getMessage()
						, "No results",JOptionPane.ERROR_MESSAGE);
			}
			
			
		}
		if(event.getSource()==back)
		{
			page--;
			addLinksToPanel();
		}
		if(event.getSource()==next)
		{
			page++;
			addLinksToPanel();
		}
		
	}

	public void setSearchTime(long milliseconds, int linkCount)
	{
		double time = milliseconds/1000.0; 
		searchTime.setText("Found " + linkCount + " Results " + " in " 
		+ String.valueOf(time) + " seconds");
		searchTime.setVisible(true);
	}


	/*
	 * Adds up to 4 links to the main view according to the current page
	 */
	private void addLinksToPanel() 
	{
		body.removeAll();
		int offset = page*4-4;
		int linksToAdd = 4;
		if(page==pages)
			linksToAdd = numberOfResults%4;
		if(linksToAdd==0)
			linksToAdd=4;
		for(int i=0;i<linksToAdd;++i)
		{
			body.add(links.elementAt(offset));
			offset++;
		}
		createMainView();
		
	}
	
	
}
