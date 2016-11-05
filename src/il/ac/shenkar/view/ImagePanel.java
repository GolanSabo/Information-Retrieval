package il.ac.shenkar.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.border.EmptyBorder;

import javafx.scene.layout.Border;

/**
 * A class that represents image panel - to be used in image display
 */
public class ImagePanel extends JTextPane{

	//The image
    private BufferedImage image;

    public ImagePanel(String path) {
       try {                
          image = ImageIO.read(new File(path));
          this.setBorder(BorderFactory.createLineBorder(Color.black));
          this.setBackground(Color.LIGHT_GRAY);
          this.setEditable(false);
          this.setMaximumSize(getPanelSize());
       } catch (IOException ex) {
            
       }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int x = (this.getWidth()-image.getWidth())/2;
        int y = (this.getHeight()-image.getHeight())/2;

        g.drawImage(image, x, y, this);            
    }

    public Dimension getPanelSize()
    {
        return new Dimension(image.getWidth(),image.getHeight());

    }
	public BufferedImage getImage() {
		return image;
	}

	public void setImage(BufferedImage image) {
		this.image = image;
	}

}