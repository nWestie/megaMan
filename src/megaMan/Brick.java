package megaMan;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class Brick extends StaticObj{
	private static final long serialVersionUID = 1L;
	protected static BufferedImage img;
	protected static final int w = 33, h = 28;
	/**
	 * Adds brick, on brick-sized grid. To place at specific pixel, use freeBrick
	 */
	public Brick(int x, int y, boolean snap) {
		super(x,y,w,h);
		if(snap) {
			x = (int)((x/w)*w);
			y = (int)((y/h)*h);
		}
		//load img if needed
		if(img == null) {
			try {
				img = ImageIO.read(ClassLoader.getSystemResource("singlebrick.png"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println("Img Error");
				e.printStackTrace();
			}
		}
	}
	public void draw(Graphics2D g2d) {
		g2d.drawImage(img, null, (int)getX(), (int)getY());
//		g2d.setColor(Color.green);
//		g2d.draw(this);
	}
	
}
