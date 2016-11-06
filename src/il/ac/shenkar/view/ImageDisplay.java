package il.ac.shenkar.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.print.PrinterException;
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
	private JPanel emptyPanelLeft;
	private JMenuBar menuBar;
	private JMenu menu;
	private JMenuItem item1;
	private JMenuItem item2;
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
		emptyPanelLeft = new JPanel();
		
		image.setToolTipText(title);
		image.addMouseListener(this);
		this.addMouseListener(this);
		menuBar = new JMenuBar();
		menu = new JMenu("Options");
		item1 = new JMenuItem("Details",KeyEvent.VK_D);
        KeyStroke ctrlDKeyStroke = KeyStroke.getKeyStroke("control D");
        item1.setAccelerator(ctrlDKeyStroke);
        item1.addActionListener(this);
        item2 = new JMenuItem("Print",KeyEvent.VK_P);
        KeyStroke ctrlPKeyStroke = KeyStroke.getKeyStroke("control P");
        item2.setAccelerator(ctrlPKeyStroke);
        item2.addActionListener(this);
        menu.add(item1);
        menu.add(item2);
        menuBar.add(menu);
        setJMenuBar(menuBar);
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
		emptyPanelLeft.setPreferredSize(new Dimension(50,100));

		emptyPanelTop.setPreferredSize(new Dimension(100,20));
		emptyPanelBottom.setPreferredSize(new Dimension(100,20));
		add(emptyPanelRight, BorderLayout.EAST);
		add(emptyPanelLeft, BorderLayout.WEST);

		add(emptyPanelTop, BorderLayout.NORTH);
		add(emptyPanelBottom, BorderLayout.SOUTH);
		
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
	public void actionPerformed(ActionEvent e) 
	{
		if(e.getSource()==item1)
		{
			JOptionPane.showMessageDialog(this,getDetails()
					, "File Details",JOptionPane.INFORMATION_MESSAGE);
		}
		else if(e.getSource()==item2)
		{
			printDocument();
		}
		
		
	}
	
	
	private void printDocument() {
		try {
			image.print();
		} catch (PrinterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
