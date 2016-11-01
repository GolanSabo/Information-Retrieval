package il.ac.shenkar.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.text.*;

import il.ac.shenkar.Details.FileDetails;

import javax.swing.*;

public class TextDocumentDisplay extends JFrame implements ActionListener, MouseListener
	,ILinkDisplay, ITextDocument
{
	
	private int currentPage = 1;
	private JTextPane tPane;
	private JPanel textArea;
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
	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	private String author;
	private String subject;
	private String description;
	private String date;
	private String title;
	private JPanel detailsPanel;
	private JButton showDetails;
	public TextDocumentDisplay(FileDetails fd, ArrayList<Integer> _locations)
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
				appendToPane(tPane, word+ " ", Color.yellow);
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
		
		this.setPreferredSize(new Dimension(1000,380));
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		//bottomPanel.setLayout(new FlowLayout());
		int charCounter = 0;
				
		//tPane.setEditable(false);
		scrollpane = new JScrollPane(tPane);
		scrollpane.setMaximumSize(new Dimension(400,380));
		textArea.add(scrollpane);
		add(textArea,BorderLayout.CENTER);
		
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
		*/detailsPanel.add(showDetails);
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
		}
		else if(event.getSource()==showDetails)
		{
			JOptionPane.showMessageDialog(this,detailsPane.getText()
					, "File Details",JOptionPane.INFORMATION_MESSAGE);
		}*/
		
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
