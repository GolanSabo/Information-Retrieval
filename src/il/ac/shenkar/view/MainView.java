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
	
	private Vector<Link> links;
	private JButton back;
	private JButton next;
	private JPanel tabbedPaneContainer;
	private JTabbedPane tabbedPane;
	private SettingsContainer settings; 
	private JPanel card1;
	private JPanel card2;
	private JPanel resultContainer;
	
	private JPanel queryPanel;
	
	public MainView()
	{
		Controller.getInstance().init();
		input = new JTextField(20);
		search = new JButton();
		search.setIcon(new ImageIcon("Search.png"));
		search.addActionListener(this);
		body = new JPanel();
		back = new JButton();
		back.setIcon(new ImageIcon("Back.png"));
		next = new JButton();
		next.setIcon(new ImageIcon("Next.png"));
		links = new Vector<Link>();
		
		tabbedPaneContainer = new JPanel();
		settings = new SettingsContainer();
		
		tabbedPane = new JTabbedPane();
		queryPanel = new JPanel();
        
		
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
		addComponentToPane(tabbedPaneContainer);
		add(tabbedPane,BorderLayout.CENTER);
		setVisible(true);
		pack();
		
	}
	
public void addComponentToPane(Container pane) {
        
    	
        card1.setLayout(new BorderLayout());
        queryPanel.add(input);
        queryPanel.add(search);
        
       
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
        body.setAlignmentX(LEFT_ALIGNMENT);
       
        card1.add(queryPanel,BorderLayout.NORTH);
        card1.add(body,BorderLayout.WEST);
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
			try {
				links = Controller.getInstance().getResults(input.getText());
			} catch (NoResultsException e) {
				JOptionPane.showMessageDialog(this,"Search returned no results\n"
						, "No results",JOptionPane.ERROR_MESSAGE);
			}
			body.removeAll();
			for(Link link:links)
			{
				body.add(link);
			}
			
			repaint();
		}
		
	}
	
	
}
