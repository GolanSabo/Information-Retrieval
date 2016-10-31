package il.ac.shenkar.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.text.*;

import il.ac.shenkar.Details.FileDetails;

import javax.swing.*;

public class DocumentDisplay extends JFrame implements ActionListener, ILinkDisplay
{
	
	private int currentPage = 1;
	private JTextPane tPane;
	private JPanel textArea;
	private JTextPane detailsPane;
	private String [] text;
	private ArrayList<Integer> locations;
	private ArrayList<Page> pages;
	private JButton back;
	private JButton next;
	private JPanel bottomPanel;
	private JTextField changePage;
	private JLabel numberOfPages;
	private JScrollPane scrollpane;
	private String path;
	private String author;
	private String subject;
	private String description;
	private String date;
	private String title;
	private JPanel detailsPanel;
	public DocumentDisplay(FileDetails fd, ArrayList<Integer> _locations)
	{
		title = fd.getDocumentName();
		path = fd.getPath();
		locations = _locations;
		author = fd.getAuthor();
		subject = fd.getSubject();
		description = fd.getDescription();
		
		date = fd.getDate().toString();
		textArea = new JPanel();
		tPane = new JTextPane();
		back = new JButton();
		next = new JButton();
		try {
			text = getDocumentContent();
		} catch (IOException e) {
			
			System.out.println("Error reading link!");
		}
		
		pages = new ArrayList<Page>();
		bottomPanel = new JPanel();
		back.addActionListener(this);
		back.setSize(50, 50);
		back.setIcon(new ImageIcon("Back.png"));
		next.setIcon(new ImageIcon("Next.png"));
		next.addActionListener(this);
		changePage = new JTextField(2);
		changePage.addActionListener(this);
		numberOfPages = new JLabel("");
		scrollpane = new JScrollPane(tPane);
		detailsPanel = new JPanel();
		detailsPane = new JTextPane();
		
		
		setDetailsPanel();
		detailsPane.setEditable(false);
		getPages();
		
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

	public void createDisplay()
	{
		
		this.setPreferredSize(new Dimension(1000,380));
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		bottomPanel.setLayout(new FlowLayout());
		int charCounter = 0;
		int wordCounter = 0;
		
		Page temp = pages.get(currentPage-1);
		tPane.setText("");
		for(String line : text)
		{
			String [] words = line.split(" ");
			for(String word:words)
			{
				if(locations.contains(wordCounter))
				{
					appendToPane(tPane, word+ " ", Color.yellow);
				}
				else
				{
					appendToPane(tPane, word+ " ", Color.black);
				}
				wordCounter++;
				
			}
			appendToPane(tPane,"\n", Color.black);
		}
				
		tPane.setEditable(false);
		textArea.add(scrollpane);
		add(textArea,BorderLayout.CENTER);
		
		bottomPanel.add(back);
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
		detailsPanel.add(detailsPane);
		add(detailsPanel,BorderLayout.WEST);
		setVisible(true);
		pack();
	}
	
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
		}
		
	}
	
	public String[] getDocumentContent() throws IOException
	{
		FileReader fr = new FileReader(path);
		BufferedReader textReader = new BufferedReader(fr);
		int numberOfLines = readLines();
		String[] textData = new String[numberOfLines];
		for(int i=0;i<numberOfLines;i++)
		{
			textData[i] = textReader.readLine();
		}
		return textData;
	}
	public int readLines() throws IOException
	{
		FileReader fr = new FileReader(path);
		BufferedReader textReader = new BufferedReader(fr);
		String aLine;
		int numberOfLines = 0;
		while((aLine=textReader.readLine())!=null)
		{
			numberOfLines++;
		}
		textReader.close();
		return numberOfLines;
	}
}
