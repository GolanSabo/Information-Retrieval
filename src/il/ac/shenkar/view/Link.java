package il.ac.shenkar.view;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.text.AttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

import il.ac.shenkar.Details.FileDetails;
import il.ac.shenkar.Functionality.SearchResult;
import il.ac.shenkar.controller.Controller.ResultType;



public class Link extends JPanel implements MouseListener
{
	private String keyword;
	private ArrayList<Integer> locations;
	private FileDetails fileDetails;
	private String path;
	private JLabel documentName;
	private JTextPane pane;
	private JPanel container;
	private ResultType type;
	private String description;
	
	
	public Link(SearchResult result, ResultType _type)
	{
		fileDetails = result.getFileDetails();
		type = _type;
		path=result.getFileDetails().getPath();
		documentName = new JLabel(result.getFileDetails().getDocumentName());
		documentName.setForeground(Color.blue);
		documentName.setAlignmentX(JLabel.LEFT);
		documentName.setFont(new Font(Font.SERIF,3,14));
		 pane = new JTextPane();
		 pane.setFont(new Font(Font.DIALOG_INPUT,0,14));
		 pane.setEditable(false);
		description = result.getFileDetails().getDescription();
		 container = new JPanel();
		 locations = new ArrayList<Integer>();
		locations = result.getLocations();
		createLinkPanel();
		
		 
	}
	
	

	public ArrayList<Integer> getLocations() {
		return locations;
	}

	public void setLocations(ArrayList<Integer> locations) {
		this.locations = locations;
	}
	public String getDocumentName() {
		return documentName.getText();
	}


	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	
	public void createLinkPanel()
	{
		setSize(200, 50);
		setLayout(new BorderLayout());
		container.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		setDescription();

		documentName.addMouseListener(this);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx=0;
		c.gridy = 0;
		container.add(documentName,c);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx=0;
		c.gridy = 1;
		c.insets=(new Insets(1,5,0,0));
		container.add(pane,c);
		add(container,BorderLayout.WEST);
		setVisible(true);
	}
	private void setDescription() {
		if(type==ResultType.DOCUMENT)
		{
			if(description.equals(""))
				pane.setText(get3Lines());
			else
				pane.setText(description);
		}
		else if(type==ResultType.IMAGE)
		{
			if(description.equals(""))
				pane.setText("Image");
		else
			pane.setText(description);
		}
	}




	private String get3Lines() {
		StringBuilder textData;
		textData = new StringBuilder();

		try {
			FileReader fr = new FileReader(path);
			BufferedReader textReader = new BufferedReader(fr);
			int numberOfLines = 3;
			for(int i=0;i<numberOfLines;i++)
			{
				String line = textReader.readLine();
				if(line!=null)
				{
					textData.append(line);
				textData.append('\n');
			}
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




	public String getPath() {
		return path;
	}




	public void setPath(String path) {
		this.path = path;
	}




	
	public FileDetails getFileDetails() {
		return fileDetails;
	}



	public void setFileDetails(FileDetails fileDetails) {
		this.fileDetails = fileDetails;
	}



	@Override
	public void mouseClicked(MouseEvent e) 
	{
		
		
	}
	@Override
	public void mouseEntered(MouseEvent e) {
		documentName.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
	}
	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mousePressed(MouseEvent e) {
		if(type == ResultType.DOCUMENT)
		{
			DocumentDisplay display = new DocumentDisplay(getFileDetails(),getLocations());
			display.setTitle(documentName.getText());
			display.createDisplay();
		}
		else if(type == ResultType.IMAGE)
		{
			ImageDisplay display = new ImageDisplay(this);
			display.setTitle(documentName.getText());
			display.createDisplay();
		}
		
		
	}
	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	
}
