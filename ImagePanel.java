package a38;

import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class ImagePanel extends JPanel {

    private Image image;

    public ImagePanel(String path) {
        image = new ImageIcon(path).getImage();
    }

    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);

        g.drawImage(
                image,
                0,
                0,
                getWidth(),
                getHeight(),
                this
        );
    }
}