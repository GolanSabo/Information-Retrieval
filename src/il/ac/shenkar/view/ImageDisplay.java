package il.ac.shenkar.view;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;


public class ImageDisplay extends JFrame implements ActionListener, ILinkDisplay
{
	private ImagePanel image;
	public ImageDisplay(Link l)
	{
		image = new ImagePanel(l.getPath());
	}

	@Override
	public void createDisplay() 
	{
		setLayout(new BorderLayout());
		add(image,BorderLayout.CENTER);
		setVisible(true);
		pack();

	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}
