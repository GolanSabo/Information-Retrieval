package il.ac.shenkar.view;

import il.ac.shenkar.controller.Controller;
import il.ac.shenkar.errors.DuplicateNameException;
import il.ac.shenkar.errors.FilePathException;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.border.EtchedBorder;

public class SettingsContainer  extends JPanel implements ActionListener, MouseListener{
	final static int extraWindowWidth = 120;
	private JLabel lAuthor;
	private JTextField author;
	private JLabel lPath;
	private JTextField path;
	private JButton searchFile;
	private JFileChooser fc;
	private JTabbedPane tabbedPane;
	private JComboBox<String> documents;
	private JLabel lName;
	private JTextField name;
	private JLabel lSubject;
	private JTextField subject;
	private JLabel lDescription;
	private JTextPane description;
	private JScrollPane scroller;
	private JLabel lPublished;
	private JTextField date;
	private JButton go;
	private JLabel lChoose;
	private JRadioButton hidden;
	private JRadioButton visible;
	private ButtonGroup radioList;
	private JButton save; 
	private JPanel container;
	private JPanel card1;
	private JPanel card2;
	private JPanel card3;
	private JComboBox<String> batchTime;
	private JTextPane lBatchTime;
	private JTextPane explanation;
	private String[] batch = {"20 seconds","5 minutes","20 minutes","1 hour","24 hours"};
	private JButton saveBatch;
	private JPanel radioPanel;
	private JLabel currentVisibility;
	
	public SettingsContainer()
	{

		lAuthor = new JLabel("Author: ");
		author = new JTextField(20);
		author.setToolTipText("type in the author of the document");
		author.addActionListener(this);
		lPath = new JLabel("Enter file path: ");
		path = new JTextField(20);
		path.setToolTipText("please enter the file path or use the search button");
		path.addActionListener(this);
		fc = new JFileChooser("Load");
		searchFile = new JButton();
		searchFile.setIcon(new ImageIcon("Search.png"));
		searchFile.addActionListener(this);
		searchFile.setToolTipText("Search for file to load");
		documents = new JComboBox<String>(Controller.getInstance().getDocumentsNames());
		documents.addActionListener(this);
		documents.setToolTipText("Files in database");
		lName = new JLabel("Document name: ");
		name = new JTextField(20);
		name.setToolTipText("type in the document name");
		lSubject = new JLabel("Subject: ");
		subject = new JTextField(20);
		subject.setToolTipText("type in the subject of the document");
		lDescription = new JLabel("Description: ");
		description = new JTextPane();
		description.setToolTipText("type in a short description for the document");
		scroller = new JScrollPane(description);
		lPublished = new JLabel("Published date (MM/dd/yyyy): ");
		date = new JTextField(20);
		date.setToolTipText("type in the date in which the document was published");
		go = new JButton(new ImageIcon("Upload.png"));
		go.addActionListener(this);
		go.setToolTipText("stores the file in database");
		lChoose = new JLabel("Choose file: ");
		hidden = new JRadioButton();
		hidden.setText("Hidden");
		hidden.setToolTipText("marks the file as hidden");
		visible = new JRadioButton();
		visible.setText("Visible");
		visible.setToolTipText("marks the file as visible");
		radioList = new ButtonGroup();
		save = new JButton("save"); 
		save.setToolTipText("saves the privacy settings");
		save.addActionListener(this);
		batchTime = new JComboBox<String>(batch);
		batchTime.setToolTipText("Set the time for system to check for new files");
		lBatchTime = new JTextPane();
		lBatchTime.setText("Please select how often will the system"
				+ "\n look in database for new uploaded files");
		lBatchTime.setEditable(false);
		lBatchTime.setBackground(Color.lightGray);
		
		explanation = new JTextPane();
		explanation.setText("This tab is used to include/exclude "
				+ "\nfiles from database");
				
		explanation.setEditable(false);
		explanation.setBackground(Color.lightGray);
		saveBatch = new JButton("Update");
		saveBatch.addActionListener(this);
		saveBatch.setToolTipText("saves the batch time settings");
		card1 = new JPanel() {

			public Dimension getPreferredSize() {
				Dimension size = super.getPreferredSize();
				size.width += extraWindowWidth;
				return size;
			}
		};
		card1.setToolTipText("Store a new file in database");
		card2 = new JPanel() {

			public Dimension getPreferredSize() {
				Dimension size = super.getPreferredSize();
				size.width += extraWindowWidth;
				return size;
			}
		};
		card2.setToolTipText("Set file privacy");
		container = new JPanel() {

			public Dimension getPreferredSize() {
				Dimension size = super.getPreferredSize();
				size.width += extraWindowWidth;
				return size;
			}
		};
		radioPanel = new JPanel(new GridLayout(0, 1));
		
		card3 = new JPanel() {

			public Dimension getPreferredSize() {
				Dimension size = super.getPreferredSize();
				size.width += extraWindowWidth;
				return size;
			}
		};
		card3.setToolTipText("Set batch delay");
		tabbedPane = new JTabbedPane();
		currentVisibility = new JLabel();
		createSettings();
	}

	private void addComponentToPane(Container pane) {

		
		
		
		card1.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.insets  = new Insets(5,0,5,43);
		c.fill = GridBagConstraints.NONE;
		card1.add(searchFile,c);
		
		c.gridx = 1;
		c.gridy = 0;
		c.insets  = new Insets(5,43,5,0);
		c.fill = GridBagConstraints.HORIZONTAL;
		card1.add(lPath,c);
		c.gridx = 2;
		c.gridy = 0;
		c.insets  = new Insets(5,0,5,0);
		c.fill = GridBagConstraints.HORIZONTAL;
		card1.add(path,c);

		c.gridx = 1;
		c.gridy = 1;
		c.fill = GridBagConstraints.NONE;
		card1.add(lName,c);

		c.gridx = 2;
		c.gridy = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		card1.add(name,c);

		c.gridx = 1;
		c.gridy = 2;
		c.fill = GridBagConstraints.NONE;
		card1.add(lAuthor,c);

		c.gridx = 2;
		c.gridy = 2;
		c.fill = GridBagConstraints.HORIZONTAL;
		card1.add(author,c);

		c.gridx = 1;
		c.gridy = 3;
		c.fill = GridBagConstraints.NONE;
		card1.add(lSubject,c);

		c.gridx = 2;
		c.gridy = 3;
		c.fill = GridBagConstraints.HORIZONTAL;
		card1.add(subject,c);

		c.gridx = 1;
		c.gridy = 4;
		c.fill = GridBagConstraints.NONE;
		card1.add(lDescription,c);

		c.gridx = 2;
		c.gridy = 4;
		c.fill = GridBagConstraints.HORIZONTAL;
		description.setPreferredSize(new Dimension(100,100));
		card1.add(scroller,c);

		c.gridx = 1;
		c.gridy = 8;
		c.fill = GridBagConstraints.NONE;
		card1.add(lPublished,c);

		c.gridx = 2;
		c.gridy = 8;
		c.fill = GridBagConstraints.HORIZONTAL;
		card1.add(date,c);

		c.gridx = 2;
		c.gridy = 9;
		c.fill = GridBagConstraints.NONE;
		card1.add(go,c);
		
		container.setLayout(new GridBagLayout());

		c.gridx = 1;
		c.gridy = 0;

		c.fill = GridBagConstraints.HORIZONTAL;
		container.add(explanation,c);
		
		c.gridx = 0;
		c.gridy = 1;

		c.insets  = new Insets(5,5,5,5);
		container.add(lChoose,c);

		c.gridx = 1;
		c.gridy = 1;
		

		container.add(documents,c);
		c.gridx = 2;
		c.gridy = 1;
		currentVisibility.setText("Currently: " + 
		Controller.getInstance().checkVisibility((String)documents.getSelectedItem()));
		container.add(currentVisibility,c);

		
		
		radioList.add(visible);
		radioList.add(hidden);
		radioPanel.add(visible);
		radioPanel.add(hidden);
		visible.addActionListener(this);
		hidden.addActionListener(this);
		
		
		c.gridx = 1;
		c.gridy = 3;
		container.add(radioPanel, c);
		c.gridx = 2;
		c.gridy = 4;
		c.fill = GridBagConstraints.NONE;
		container.add(save,c);
		
		card2.add(container);
	
		card3.add(lBatchTime);
		card3.add(batchTime);
		card3.add(saveBatch);
		tabbedPane.addMouseListener(this);
		tabbedPane.addTab("Load File", card1);
		tabbedPane.addTab("Privacy", card2);
		tabbedPane.add("Administrator", card3);
		pane.add(tabbedPane, BorderLayout.NORTH);
	}
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		if(e.getSource()==searchFile)
		{

			int returnVal = fc.showOpenDialog(this);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File file = fc.getSelectedFile();
				path.setText(file.getPath());
			} 

		}
		else if(e.getSource()==go)
		{
			try {
				Controller.getInstance().storeInDatabase(path.getText(),name.getText(),author.getText(),
						subject.getText(),description.getText(),date.getText());
				documents.addItem((String)name.getText());
				path.setText("");
				name.setText("");
				author.setText("");
				subject.setText("");
				description.setText("");
				date.setText("");
			} catch (IOException e1) {
				JOptionPane.showMessageDialog(this,"Could not store file in database!\n"
						, "IO Error",JOptionPane.ERROR_MESSAGE);

			} catch (DuplicateNameException err) {
				JOptionPane.showMessageDialog(this,"The documnet name already exists in database!\n"
						+ "Please try a different name", "Duplicate Name Error",JOptionPane.ERROR_MESSAGE);

			} catch (ParseException e1) {
				JOptionPane.showMessageDialog(this,e1.getMessage() + "\n" 
						+ "Please make sure the date format is MM/dd/yyyy", "Date Format Error",JOptionPane.ERROR_MESSAGE);

			} catch (FilePathException e1) {
				JOptionPane.showMessageDialog(this,e1.getMessage() + "\n" 
						+ "Please make sure the file path is correct", "File Path Error",JOptionPane.ERROR_MESSAGE);

			}
			
			createSettings();


		}
		else if(e.getSource()==hidden )
		{

			JOptionPane.showMessageDialog(this,"Marking a file as hidden will exclude it from future searches!"
					, "Warning",JOptionPane.WARNING_MESSAGE);


		}
		else if(e.getSource()==save )
		{
			if(!visible.isSelected() && !hidden.isSelected())
				return;
			String docName = (String)documents.getSelectedItem();
			Controller.getInstance().changeVisibility(docName,visible.isSelected());
			currentVisibility.setText("Currently: " + 
					Controller.getInstance().checkVisibility((String)documents.getSelectedItem()));

			
		}
		else if(e.getSource()==saveBatch)
		{
			
			Controller.getInstance().setBatch((String)batchTime.getSelectedItem());
		}
		else if(e.getSource()==documents)
		{
			currentVisibility.setText("Currently: " + 
		Controller.getInstance().checkVisibility((String)documents.getSelectedItem()));

		}

		repaint();

	}

	private void createSettings()
	{
		setPreferredSize(new Dimension(650,400));
		addComponentToPane(this);
		setVisible(true);


	}

	@Override
	public void mouseClicked(MouseEvent e) {
				
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
	public void mousePressed(MouseEvent e) {
		if(e.getSource()==tabbedPane && e.getButton()==MouseEvent.BUTTON3)
		{
			if(tabbedPane.getSelectedIndex()==0)
			{
				JOptionPane.showMessageDialog(this,"This tab is used to load a file to \n"
					+ "the search engine database."
					, "Load File",JOptionPane.INFORMATION_MESSAGE);
			}
			else if(tabbedPane.getSelectedIndex()==1)
			{
				JOptionPane.showMessageDialog(this,"This tab is used set the file\n"
					+ "visibility."
					, "Privacy",JOptionPane.INFORMATION_MESSAGE);
			}
			else if(tabbedPane.getSelectedIndex()==2)
			{
				JOptionPane.showMessageDialog(this,"This tab is used to set how frequent  \n"
					+ "will the Engine look for new uploads to the database."
					, "Administrator",JOptionPane.INFORMATION_MESSAGE);
			}
		}

		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}




}
