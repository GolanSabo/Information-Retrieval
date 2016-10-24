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

public class Link extends JPanel implements MouseListener
{
	private String keyword;
	private ArrayList<Integer> locations;
	private String path;
	private JLabel title;
	private JTextPane pane;
	private String documentName;
	private JPanel container;
	
	
	public Link(String _path,String _title, String _description, String word, 
			ArrayList<Integer> _locations)
	{
		
		keyword = word;
		 path=_path;
		 title = new JLabel(_title);
		 title.setForeground(Color.blue);
		 title.setAlignmentX(JLabel.LEFT);
		 title.setFont(new Font(Font.SERIF,3,14));
		 pane = new JTextPane();
		 pane.setFont(new Font(Font.DIALOG_INPUT,0,14));
		 pane.setEditable(false);
		 pane.setText(_description);
		 //pane.insertIcon(new ImageIcon("Back.png"));
		 container = new JPanel();
		 locations = new ArrayList<Integer>();
		 locations = _locations;
		createLinkPanel();
		
		 
	}
	
	

	
	public String getDocumentName() {
		return title.getText();
	}




	



	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public String getDescription( )
	{
		
		return pane.getText();
	}
	public void createLinkPanel()
	{
		setSize(200, 50);
		setLayout(new BorderLayout());
		container.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		title.addMouseListener(this);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx=0;
		c.gridy = 0;
		container.add(title,c);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx=0;
		c.gridy = 1;
		c.insets=(new Insets(1,5,0,0));
		container.add(pane,c);
		add(container,BorderLayout.WEST);
		setVisible(true);
	}
	public String getPath() {
		return path;
	}




	public void setPath(String path) {
		this.path = path;
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
	@Override
	public void mouseClicked(MouseEvent e) 
	{
		
		
	}
	@Override
	public void mouseEntered(MouseEvent e) {
		title.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
	}
	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mousePressed(MouseEvent e) {
		DisplayFrame display = new DisplayFrame(this);
		display.setTitle(title.getText());
		display.createLinkFrame();
		
	}
	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	
}
