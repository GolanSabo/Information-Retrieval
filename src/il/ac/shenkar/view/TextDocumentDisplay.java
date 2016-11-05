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
import java.awt.print.Pageable;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.text.*;

import org.apache.pdfbox.printing.PDFPageable;

import il.ac.shenkar.Details.FileDetails;
import sun.print.PageableDoc;

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
	private final ArrayList<Color> colors = 
			new ArrayList<Color>(Arrays.asList(Color.blue,Color.red, Color.green,Color.magenta, Color.orange, Color.pink));
	private JTextPane tPane;
	private JPanel emptyPanelRight;
	private JPanel emptyPanelBottom;
	private JPanel emptyPanelTop;
	private String text;
	private ArrayList<Integer> locations;
	private JScrollPane scrollpane;
	private String path;
	private String author;
	private String subject;
	private String description;
	private String date;
	private String title;
	private JPanel optionsPanel;
	private JButton showDetails;
	private JButton print;
	private ArrayList<String> keywords;
	public TextDocumentDisplay(FileDetails fd, ArrayList<Integer> _locations, ArrayList<String> words)
	{
		keywords = words;
		title = fd.getDocumentName();
		path = fd.getPath();
		locations = _locations;
		author = fd.getAuthor();
		subject = fd.getSubject();
		description = fd.getDescription();
		date = fd.getDate().toString();
		emptyPanelRight = new JPanel();
		emptyPanelBottom = new JPanel();
		emptyPanelTop = new JPanel();
		tPane = new JTextPane();
		text = getText();
		scrollpane = new JScrollPane(tPane);
		optionsPanel = new JPanel();
		setTextPaneContent();
		showDetails = new JButton("Details");
		showDetails.setToolTipText("Shows the details of this document");
		showDetails.addActionListener(this);
		print = new JButton("Print");
		print.setToolTipText("Prints the document");
		print.addActionListener(this);
		this.addMouseListener(this);	
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
	
	private void setTextPaneContent()
	{
		
		int index=0;
		String [] words = text.split(" ");
		for(String word:words)
		{
			String tmp = word.toLowerCase().replaceAll("[-+.^:;,\'\"\\()?!“”‘’— =<>\0&%$#*!?@|]*","");
			
			if(keywords.contains((String)tmp))
			{
				
				index = keywords.indexOf(tmp);
				if(index>=colors.size())
					index = index%colors.size();
				appendToPane(tPane, word+ " ", colors.get(index));
			}
			else
			{
				appendToPane(tPane, word+ " ", Color.black);
			}
			

		}

		
	}

	public void createDisplay()
	{
		this.setPreferredSize(new Dimension(800,400));
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		tPane.setMaximumSize(new Dimension(350,380));
		scrollpane = new JScrollPane(tPane);
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
		
		if(event.getSource()==showDetails)
		{
			JOptionPane.showMessageDialog(this,getDetails()
					, "File Details",JOptionPane.INFORMATION_MESSAGE);
		}
		else if(event.getSource()==print)
		{
			
		
			    try {
					tPane.print();
				} catch (PrinterException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		
		
		
	}
	
	/**
	 * Gets the text from the txt file
	 */
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
			textReader.close();
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
			JOptionPane.showMessageDialog(this,getDetails()
					, "File Details",JOptionPane.INFORMATION_MESSAGE);
		}
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// Do nothing
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// Do nothing		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// Do nothing		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// Do nothing		
	}

	

	
}
