/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package picbrowserj;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import static java.awt.Image.SCALE_SMOOTH;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

/**
 *
 * @author homeadmin
 */
public class Canvas extends JPanel {
    private Image image;
    public Canvas() {
        super();
        image = new ImageIcon("resources/None.png").getImage();
    }
    Image image1;
    public void showImage(String Path) {
        image1 = new ImageIcon(Path).getImage();
        rescaleImage();
    }
    public void rescaleImage() {
        if (image1==null) return;
        //scale image but maintain ratio
        float height1=image1.getHeight(null);
        float height2 =getHeight();
        float width1 = image1.getWidth(null);
        float width2= getWidth();
        if (height2/height1 < width2/width1) {
            image = image1.getScaledInstance(-1,(int)height2, SCALE_SMOOTH);
        } else {
            image = image1.getScaledInstance((int)width2, -1, SCALE_SMOOTH);
        }
        
        //AffineTransform affineTransform = AffineTransform.getScaleInstance(0.5, 0.5);
        //affineTransform.filter(image1,image2);
        this.repaint(); 
    }
    protected void paintComponent(Graphics graphics) {
        Graphics g = graphics.create();

        //Draw in our entire space, even if isOpaque is false.
        g.setColor(Color.WHITE);
        //g.fillRect(0, 0, image == null ? getWidth() : image.getWidth(this),
        //                 image == null ? getHeight() : image.getHeight(this));
        g.fillRect(0, 0, getWidth() ,getHeight());
        if (image != null) {
            //Draw image at its natural size of 125x125.
            g.drawImage(image, 0, 0, this);
        }

        //Add a border, red if picture currently has focus
        if (isFocusOwner()) {
            g.setColor(Color.RED);
        } else {
            g.setColor(Color.BLACK);
        }
        g.drawRect(0, 0, image == null ? 125 : image.getWidth(this),
                         image == null ? 125 : image.getHeight(this));
        g.dispose();
    }
    
}
