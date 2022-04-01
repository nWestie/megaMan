package megaMan;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class Goumba extends DynamicObj {
	
	protected static BufferedImage[] rImgs; //only keep 1 copy of left and right
	protected static BufferedImage[] lImgs;
	protected BufferedImage[] imgs; //the actual images used, points to either lImgs or rImgs
	
	double animInd = 0; //index of animation
	protected boolean squashed = false;
	protected boolean traction = false;
	public Goumba(int x, int y, int move) {
		super(x, y, 18, 18);
		if(move!=0)move/=Math.abs(move);
		if(lImgs!=null)return;//only add images once
		rImgs = new BufferedImage[4];
		lImgs = new BufferedImage[4];
		try {
			BufferedImage image, imageR;
			image = ImageIO.read(ClassLoader.getSystemResource("enemy_sheet.PNG"));//need to update
			imageR = ImageIO.read(ClassLoader.getSystemResource("enemy_sheet(right).png"));
			for(int i = 0; i < rImgs.length; i++) {
				rImgs[i] = imageR.getSubimage((3-i)*20, 0, width, height);
				lImgs[i] = image.getSubimage(i*20, 0, width, height);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		imgs = move>0 ? rImgs : lImgs;

		this.move = (byte)move;
		maxJumps = 0;
		xVel = -20;
		dAcc = .15;
		acc = 1.3;
	}
	@Override
	public void draw(Graphics2D g2d) {
//		g2d.drawString(String.format("%d",xVel),x, y-10);
		g2d.setColor(Color.black);
		if(gameBoard.showBounds) g2d.draw(this);
		if(squashed)
			g2d.drawImage(imgs[2], null,x,y);
		else if(move !=0)
			g2d.drawImage(imgs[(int)animInd], null, x, y);
		else {
			animInd = 1;
			g2d.drawImage(imgs[2], null,x,y);
			return;
		}
		animInd += .3;
		if((int)animInd>1) animInd = 0;
	}
	@Override
	public void damage(int amt) {
		squashed = true;
		height = 13;
		acc = 0;
	}

}
