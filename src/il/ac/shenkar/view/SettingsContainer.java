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

public class SettingsContainer  extends JPanel implements ActionListener{
	final static int extraWindowWidth = 120;
	private JLabel lAuthor;
	private JTextField author;
	private JLabel lPath;
	private JTextField path;
	private JButton searchFile;
	private JFileChooser fc;
	private JTabbedPane tabbedPane;
	private JComboBox documents;
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
	private JComboBox batchTime;
	private JTextPane lBatchTime;
	private JTextPane explanation;
	private String[] batch = {"20 seconds","5 minutes","20 minutes","1 hour","24 hours"};
	private JButton saveBatch;
	
	public SettingsContainer()
	{

		lAuthor = new JLabel("Author: ");
		author = new JTextField(20);
		author.addActionListener(this);
		lPath = new JLabel("Enter file path: ");
		path = new JTextField(20);
		path.addActionListener(this);
		fc = new JFileChooser("Load");
		searchFile = new JButton();
		searchFile.setIcon(new ImageIcon("Search.png"));
		searchFile.addActionListener(this);
		documents = new JComboBox(Controller.getInstance().getDocumentsNames());
		lName = new JLabel("Document name: ");
		name = new JTextField(20);
		lSubject = new JLabel("Subject: ");
		subject = new JTextField(20);
		lDescription = new JLabel("Description: ");
		description = new JTextPane();
		scroller = new JScrollPane(description);
		lPublished = new JLabel("Published date (MM/dd/yyyy): ");
		date = new JTextField(20);
		go = new JButton(new ImageIcon("Upload.png"));
		go.addActionListener(this);
		lChoose = new JLabel("Choose file: ");
		hidden = new JRadioButton();
		hidden.setText("Hidden");
		visible = new JRadioButton();
		visible.setText("Visible");
		radioList = new ButtonGroup();
		save = new JButton("save"); 
		save.addActionListener(this);
		batchTime = new JComboBox(batch);
		lBatchTime = new JTextPane();
		lBatchTime.setText("Please select how often will the system"
				+ "\n look in database for new uploaded files");
		lBatchTime.setEditable(false);
		lBatchTime.setBackground(Color.lightGray);
		
		explanation = new JTextPane();
		explanation.setText("This field is used to include/exclude "
				+ "\nfiles from database");
				
		explanation.setEditable(false);
		explanation.setBackground(Color.lightGray);
		saveBatch = new JButton("Update");
		saveBatch.addActionListener(this);
		createSettings();
	}

	private void addComponentToPane(Container pane) {

		tabbedPane = new JTabbedPane();
		//Create the "cards".
		card1 = new JPanel() {

			public Dimension getPreferredSize() {
				Dimension size = super.getPreferredSize();
				size.width += extraWindowWidth;
				return size;
			}
		};
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



		card2 = new JPanel() {

			public Dimension getPreferredSize() {
				Dimension size = super.getPreferredSize();
				size.width += extraWindowWidth;
				return size;
			}
		};

		container = new JPanel() {

			public Dimension getPreferredSize() {
				Dimension size = super.getPreferredSize();
				size.width += extraWindowWidth;
				return size;
			}
		};
		container.setLayout(new GridBagLayout());

		c.gridx = 0;
		c.gridy = 0;

		c.fill = GridBagConstraints.HORIZONTAL;
		container.add(explanation,c);
		
		c.gridx = 0;
		c.gridy = 1;

		c.fill = GridBagConstraints.HORIZONTAL;
		container.add(lChoose,c);

		c.gridx = 1;
		c.gridy = 1;


		container.add(documents,c);
		JPanel radioPanel = new JPanel(new GridLayout(0, 1));
		radioList.add(visible);
		radioList.add(hidden);
		radioPanel.add(visible);
		radioPanel.add(hidden);
		visible.addActionListener(this);
		hidden.addActionListener(this);
		
		
		c.gridx = 1;
		c.gridy = 3;
		// c.fill = GridBagConstraints.HORIZONTAL;
		container.add(radioPanel, c);
		c.gridx = 0;
		c.gridy = 4;
		c.fill = GridBagConstraints.NONE;
		container.add(save,c);
		
		card2.add(container);
		

		card3 = new JPanel() {

			public Dimension getPreferredSize() {
				Dimension size = super.getPreferredSize();
				size.width += extraWindowWidth;
				return size;
			}
		};
		
		card3.add(lBatchTime);
		card3.add(batchTime);
		card3.add(saveBatch);
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
			documents = new JComboBox(Controller.getInstance().getDocumentsNames());


		}
		else if(e.getSource()==hidden )
		{

			JOptionPane.showMessageDialog(this,"Marking a file as hidden will exclude it from future searches!"
					, "Warning",JOptionPane.WARNING_MESSAGE);


		}
		else if(e.getSource()==save )
		{
			String docName = (String)documents.getSelectedItem();
			Controller.getInstance().changeVisibility(docName,visible.isSelected());
		}
		else if(e.getSource()==saveBatch)
		{
			
			Controller.getInstance().setBatch((String)batchTime.getSelectedItem());
		}

		repaint();

	}

	private void createSettings()
	{
		setPreferredSize(new Dimension(650,400));
		addComponentToPane(this);
		setVisible(true);


	}




}
