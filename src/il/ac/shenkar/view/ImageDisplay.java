package il.ac.shenkar.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.*;

import il.ac.shenkar.Details.FileDetails;

/**
 *  A class that represents an image link 
 */
public class ImageDisplay extends JFrame implements ActionListener, ILinkDisplay
{
	private ImagePanel image;
	private JButton showDetails;
	private JButton print;
	private JPanel emptyPanelRight;
	private JPanel emptyPanelBottom;
	private JPanel emptyPanelTop;
	private String path;
	private String author;
	private String subject;
	private String description;
	private String date;
	private String title;
	private JPanel optionsPanel;
	private JScrollPane scrollpane;

	
	public ImageDisplay(FileDetails fd)
	{
		image = new ImagePanel(fd.getPath());
		title = fd.getDocumentName();
		path = fd.getPath();
		author = fd.getAuthor();
		subject = fd.getSubject();
		description = fd.getDescription();
		date = fd.getDate().toString();
		emptyPanelRight = new JPanel();
		emptyPanelBottom = new JPanel();
		emptyPanelTop = new JPanel();
		optionsPanel = new JPanel();
		showDetails = new JButton("Details");
		showDetails.setToolTipText("Shows the details of this document");
		showDetails.addActionListener(this);
		print = new JButton("Print");
		print.setToolTipText("Prints the document");
		print.addActionListener(this);
		//this.addMouseListener(this);	
	}

	@Override
	public void createDisplay() 
	{
		this.setPreferredSize(new Dimension(800,400));
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		scrollpane = new JScrollPane(image);
		add(scrollpane,BorderLayout.CENTER);
		emptyPanelRight.setPreferredSize(new Dimension(100,100));
		emptyPanelTop.setPreferredSize(new Dimension(100,20));
		emptyPanelBottom.setPreferredSize(new Dimension(100,20));
		add(emptyPanelRight, BorderLayout.EAST);
		add(emptyPanelTop, BorderLayout.NORTH);
		add(emptyPanelBottom, BorderLayout.SOUTH);
		optionsPanel.setLayout(new GridBagLayout()); 
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.insets = new Insets(10,10,10,10);
		optionsPanel.add(showDetails,c);
		c.gridx = 0;
		c.gridy = 2;
		optionsPanel.add(print,c);
		add(optionsPanel,BorderLayout.WEST);
		setVisible(true);
		pack();

	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	private String getDetails() 
	{
		StringBuilder sb = new StringBuilder();
		sb.append("Title: " + title + "\n" 
				+ "Author: " + author + "\n"
				+ "Subject: " + subject + "\n"
				+ "Description: " + description + "\n"
				+ "Published: " + date + "\n");
		return sb.toString();
	}

	

}
