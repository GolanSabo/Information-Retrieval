package il.ac.shenkar.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.text.*;

import il.ac.shenkar.Details.FileDetails;

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
import javax.print.attribute.standard.MediaSize;
import javax.print.attribute.standard.MediaSizeName;
import javax.swing.*;

public class TextDocumentDisplay extends JFrame implements ActionListener, MouseListener
	,ILinkDisplay, ITextDocument
{
	
	private int currentPage = 1;
	private JTextPane tPane;
	private JPanel documentPanel;
	private JTextPane detailsPane;
	private String text;
	private ArrayList<Integer> locations;
	//private ArrayList<Page> pages;
	//private JButton back;
	//private JButton next;
	//private JPanel bottomPanel;
	//private JTextField changePage;
	//private JLabel numberOfPages;
	private JScrollPane scrollpane;
	private String path;
	private String author;
	private String subject;
	private String description;
	private String date;
	private String title;
	private JPanel detailsPanel;
	private JButton showDetails;
	private JButton print;
	public TextDocumentDisplay(FileDetails fd, ArrayList<Integer> _locations)
	{
		title = fd.getDocumentName();
		path = fd.getPath();
		locations = _locations;
		author = fd.getAuthor();
		subject = fd.getSubject();
		description = fd.getDescription();
		
		date = fd.getDate().toString();
		documentPanel = new JPanel();
		tPane = new JTextPane();
		//back = new JButton();
		//next = new JButton();
		
		text = getText();
		
		
	/*	pages = new ArrayList<Page>();
		bottomPanel = new JPanel();
		back.addActionListener(this);
		back.setSize(50, 50);
		back.setIcon(new ImageIcon("Back.png"));
		next.setIcon(new ImageIcon("Next.png"));
		next.addActionListener(this);
		changePage = new JTextField(2);
		changePage.addActionListener(this);
		numberOfPages = new JLabel("");
		*/scrollpane = new JScrollPane(tPane);
		detailsPanel = new JPanel();
		detailsPane = new JTextPane();
		
		setTextPaneContent();
		
		setDetailsPanel();
		detailsPane.setEditable(false);
		showDetails = new JButton("Details");
		showDetails.addActionListener(this);
		print = new JButton("Print");
		print.addActionListener(this);
		//scrollpane = new JScrollPane(tPane);
		this.addMouseListener(this);
		//getPages();
		
	}
	
	private void setDetailsPanel() 
	{
		StringBuilder sb = new StringBuilder();
		sb.append("Title: " + title + "\n" 
				+ "Author: " + author + "\n"
				+ "Subject: " + subject + "\n"
				+ "Description: " + description + "\n"
				+ "Published: " + date + "\n");
		detailsPane.setText(sb.toString());
	}
	
	private void setTextPaneContent()
	{
		int wordCounter = 0;

		String [] words = text.split(" ");
		for(String word:words)
		{
			if(locations.contains(wordCounter))
			{
				appendToPane(tPane, word+ " ", Color.red);
			}
			else
			{
				appendToPane(tPane, word+ " ", Color.black);
			}
			wordCounter++;

		}
		//appendToPane(tPane,"\n", Color.black);

		
	}

	public void createDisplay()
	{
		
		this.setPreferredSize(new Dimension(500,380));
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		//bottomPanel.setLayout(new FlowLayout());
		int charCounter = 0;
				
		//tPane.setEditable(false);
		tPane.setMaximumSize(new Dimension(350,380));
		scrollpane = new JScrollPane(tPane);
	//	scrollpane.setMaximumSize(new Dimension(350,380));
		add(scrollpane,BorderLayout.CENTER);
		//add(documentPanel,BorderLayout.CENTER);
		
		/*bottomPanel.add(back);
		if(currentPage<=1)
			bottomPanel.remove(back);
		
		this.repaint();
		changePage.setText(""+currentPage);
		bottomPanel.add(changePage);
		bottomPanel.add(numberOfPages);
		if(currentPage==pages.size())
			next.setEnabled(false);
		else
			next.setEnabled(true);
		bottomPanel.add(next);
		add(bottomPanel,BorderLayout.SOUTH);
		*/
		detailsPanel.setLayout(new GridBagLayout()); 
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.insets = new Insets(10,10,10,10);
		detailsPanel.add(showDetails,c);
		c.gridx = 0;
		c.gridy = 2;
		detailsPanel.add(print,c);
		add(detailsPanel,BorderLayout.WEST);
		setVisible(true);
		pack();
	}
	/*
	private void getPages()
	{
		pages.removeAll(pages);
		Page temp = new Page();
		int counter = 0;
		for(String line : text)
		{
			String [] words = line.split(" ");
			for(String word:words)
			{
				if(counter%400==0 && counter!=0)
				{
					pages.add(temp);
					temp= new Page();
					temp.addToData(word);
				}
				else
				{
					temp.addToData(word);
				}
				counter++;
			}
		}
		pages.add(temp);
		numberOfPages.setText("/"+pages.size());

		
	}*/
	private void appendToPane(JTextPane tp, String msg, Color c)
    {
        StyleContext sc = StyleContext.getDefaultStyleContext();
        AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, c);

        aset = sc.addAttribute(aset, StyleConstants.FontFamily, "Lucida Console");
        aset = sc.addAttribute(aset, StyleConstants.Alignment, StyleConstants.ALIGN_JUSTIFIED);
        aset = sc.addAttribute(aset, StyleConstants.FontSize, 12);

        int len = tp.getDocument().getLength();
        tp.setCaretPosition(len);
        tp.setCharacterAttributes(aset, false);
        tp.replaceSelection(msg);
    }
	@Override
	public void actionPerformed(ActionEvent event) 
	{
		/*
		if(event.getSource()==back)
		{
			currentPage--;
			createDisplay();
		}
		else if(event.getSource()==next)
		{
			currentPage++;
			createDisplay();
		}
		else if(event.getSource()==changePage)
		{
			currentPage = Integer.parseInt(changePage.getText());
			createDisplay();
		}*/
		if(event.getSource()==showDetails)
		{
			JOptionPane.showMessageDialog(this,detailsPane.getText()
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
	
	
	
	public String getText() 
	{
		StringBuilder textData =null;
		try {
			FileReader fr;

			fr = new FileReader(path);

			BufferedReader textReader = new BufferedReader(fr);
			int numberOfLines = readLines();
			textData = new StringBuilder();
			for(int i=0;i<numberOfLines;i++)
			{
				textData.append(textReader.readLine());
				textData.append("\n");
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return textData.toString();
	}
	private int readLines() 
	{
		int numberOfLines = 0;
		try {
			FileReader fr = new FileReader(path);
			BufferedReader textReader = new BufferedReader(fr);
			String aLine;
			while((aLine=textReader.readLine())!=null)
			{
				numberOfLines++;
			}
			textReader.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return numberOfLines;
	}
	
	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}


	
	@Override
	public void mousePressed(MouseEvent e) {
		if(e.getButton()==MouseEvent.BUTTON3)
		{
			JOptionPane.showMessageDialog(this,detailsPane.getText()
					, "File Details",JOptionPane.INFORMATION_MESSAGE);
		}
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	

	
}
