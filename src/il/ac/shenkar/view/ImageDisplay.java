package il.ac.shenkar.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Copies;
import javax.print.attribute.standard.MediaSizeName;
import javax.swing.*;

import il.ac.shenkar.Details.FileDetails;

/**
 *  A class that represents an image link 
 */
public class ImageDisplay extends JFrame implements ActionListener, ILinkDisplay, MouseListener
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
		image.setToolTipText(title);
		image.addMouseListener(this);
		this.addMouseListener(this);	
	}

	@Override
	public void createDisplay() 
	{
		this.setPreferredSize(new Dimension(800,400));
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		image.setSize(image.getPanelSize());
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
	
	@Override
	public void actionPerformed(ActionEvent event) 
	{
		
		if(event.getSource()==showDetails)
		{
			JOptionPane.showMessageDialog(this,getDetails()
					, "File Details",JOptionPane.INFORMATION_MESSAGE);
		}
		else if(event.getSource()==print)
		{
			// Input the file
			FileInputStream textStream = null; 
			try { 
			        textStream = new FileInputStream(path); 
			} catch (FileNotFoundException ffne) { 
			} 
			if (textStream == null) { 
			        return; 
			} 
			// Set the document type
			DocFlavor myFormat = DocFlavor.INPUT_STREAM.AUTOSENSE;
			// Create a Doc
			Doc myDoc = new SimpleDoc(textStream, myFormat, null); 
			// Build a set of attributes
			PrintRequestAttributeSet aset = new HashPrintRequestAttributeSet(); 
			aset.add(new Copies(1)); 
			aset.add(MediaSizeName.ISO_A4);
			// discover the printers that can print the format according to the
			// instructions in the attribute set
			PrintService[] services =
			        PrintServiceLookup.lookupPrintServices(myFormat, aset);
			// Create a print job from one of the print services
			if (services.length > 0) { 
			        DocPrintJob job = services[0].createPrintJob(); 
			        try { 
			                job.print(myDoc, aset); 
			        } catch (PrintException pe) {} 
			} 
		}
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		if(e.getButton()==MouseEvent.BUTTON3)
		{
			JOptionPane.showMessageDialog(this,getDetails()
					, "File Details",JOptionPane.INFORMATION_MESSAGE);
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// Do nothing
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// Do nothing
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// Do nothing
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// Do nothing
		
	}
		
}
