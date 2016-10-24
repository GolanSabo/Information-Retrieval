package il.ac.shenkar.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.text.*;
import javax.swing.*;

public class DisplayFrame extends JFrame implements ActionListener
{
	
	private int currentPage = 1;
	private JTextPane tPane;
	private JPanel textArea;
	private String [] text;
	private ArrayList<Page> pages;
	private JButton back;
	private JButton next;
	private String keyword;
	private JPanel bottomPanel;
	private JTextField changePage;
	private JLabel numberOfPages;
	
	public DisplayFrame(Link l)
	{
	
		keyword = l.getKeyword();
		textArea = new JPanel();
		tPane = new JTextPane();
		textArea.add(tPane);
		back = new JButton();
		next = new JButton();
		try {
			text = l.getDocumentContent();
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
		getPages();
		
	}
	
	public void createLinkFrame()
	{
		this.setPreferredSize(new Dimension(1000,380));
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		bottomPanel.setLayout(new FlowLayout());
		int counter = 0;
		
		Page temp = pages.get(currentPage-1);
		tPane.setText("");
		{
			ArrayList<String> words = temp.getData();
			for(String word:words)
			{
				counter+=word.length();
				if (counter>=125)
				{
					counter = word.length();
					appendToPane(tPane, "\n", Color.black);
					
				}
				if(word.equals(keyword))
				{
					appendToPane(tPane, word+ " ", Color.yellow);
				}
				else
				{
					appendToPane(tPane, word+ " ", Color.black);
				}
				counter++;
			}
			
		}
		
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
			createLinkFrame();
		}
		else if(event.getSource()==next)
		{
			currentPage++;
			createLinkFrame();
		}
		else if(event.getSource()==changePage)
		{
			currentPage = Integer.parseInt(changePage.getText());
			createLinkFrame();
		}
		
	}
}
