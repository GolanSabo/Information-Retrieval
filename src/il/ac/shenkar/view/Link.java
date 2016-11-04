package il.ac.shenkar.view;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import il.ac.shenkar.Details.FileDetails;
import il.ac.shenkar.Functionality.SearchResult;
import il.ac.shenkar.controller.Controller.ResultType;
import il.ac.shenkar.pdf.utils.PDFHandler;
import il.ac.shenkar.pdf.utils.PDFReader;

/**
 * A Link view as shown by the main view
 */
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
	
	/**
	 * A constructor for the link 
	 * @param result - the result sent from the searcher via the controller
	 * @param _type - the type of document
	 */
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
			{
				description = get3Lines();
				fileDetails.setDescription(description);
				pane.setText(description);
			}
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
	
	/**
	 * A method that gets the first 3 lines of a document to be displayed in the description
	 * @return - The first 3 lines of a document
	 */
	public String get3Lines() 
	{
		StringBuilder textData;
		textData = new StringBuilder();
		if(fileDetails.getExtension().equals(".txt"))
		{
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
		}
		else if(fileDetails.getExtension().equals(".pdf"))
		{
			String text = PDFHandler.getText(getPath());
			textData.append(text.substring(0, 100));
		}
		return textData.toString();
	}
	
	/**
	 * A getter for the file path
	 * @return The file path
	 */
	public String getPath() {
		return path;
	}
	
	/**
	 * Gets the file details of the file
	 * @return The file details of the file
	 */
	public FileDetails getFileDetails() {
		return fileDetails;
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
	/**
	 * When the link is pressed - a new frame is initiated according to the type of link
	 */
	@Override
	public void mousePressed(MouseEvent e) {
		if(type == ResultType.DOCUMENT)
		{
			if(fileDetails.getExtension().equals(".txt"))
			{
				TextDocumentDisplay display = new TextDocumentDisplay(getFileDetails(),getLocations());
				display.setTitle(documentName.getText());
				display.createDisplay();
			}
			else if(fileDetails.getExtension().equals(".pdf"))
			{
				try {
					PDFReader p = new PDFReader(getPath());
					p.setVisible(true);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				/*
				PDFDocumentDisplay display = new PDFDocumentDisplay(getFileDetails(),getLocations());
				display.setTitle(documentName.getText());
				display.createDisplay();*/
			}
			
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
