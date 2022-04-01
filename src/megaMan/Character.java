package megaMan;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class Character extends DynamicObj{
	private static final long serialVersionUID = 1L;
	
	protected static BufferedImage[] rImgs;
	protected static BufferedImage[] lImgs;
	double animInd = 0; //index of animation
	protected boolean lastDir = true;
	protected int lives = 5;
	
	public Character() {
		super(400, 200, 31, 33);
		acc = 6;
		dAcc = .2;
		maxJumps = 2;
		
		if(lImgs!=null)return;//only add images once
		int[] rpos = 	{0, 107, 139, 159, 183, 215, 249, 277, 300, 328, 359, 393};
		int[] rwidth = 	{32, 32, 20, 24, 32, 34, 28, 23, 28, 31, 34, 31};
		int[] lpos = 	{818,713,693,669,637,603,576,552,524,493,459,428};
        int[] lwidth =	{32, 32 ,20 ,24 ,32 ,34 ,27 ,24 ,28 ,31 ,34 ,31};
        rImgs = new BufferedImage[rpos.length];
		lImgs = new BufferedImage[lpos.length];
		try {
			BufferedImage image;
			image = ImageIO.read(ClassLoader.getSystemResource("megaman.png"));
			for(int i = 0; i < rImgs.length; i++) {
				rImgs[i] = image.getSubimage(rpos[i], 8, rwidth[i], height);
				lImgs[i] = image.getSubimage(lpos[i], 8, lwidth[i], height);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	@Override
	public void draw(Graphics2D g2d) {
		g2d.setColor(Color.black);
		if(gameBoard.showBounds) g2d.draw(this);
		switch(move) {
		case 1:
			g2d.drawImage(rImgs[(int)animInd], null, x, y);
			break;
		case -1:
			g2d.drawImage(lImgs[(int)animInd], null, x,y);
			break;
		case 0:
			animInd = 1;
			g2d.drawImage(lastDir?rImgs[0]:lImgs[0], null,x,y);
			return;
		default:
			move = 0;
		}
		animInd += .8;
		if(animInd>=rImgs.length) animInd = 6;
	}
	@Override
	public void physics(ArrayList<StaticObj> statics) {
		throw new RuntimeException("For ego character, must include dynamics for physics");
	}
	public void physics(ArrayList<StaticObj> statics, ArrayList<DynamicObj> dynamics) {
		//x movement
		xVel += move*acc;
//		xVel += xVel>=dAcc?-dAcc:(xVel<=-dAcc?dAcc:-xVel);
//		xVel = Math.max(-maxVel,Math.min(xVel, maxVel));
		xVel = (int)(xVel*(1-dAcc));
		translate((int)xVel,0);
		Rectangle temp;
		for(StaticObj obj:statics) {//if now colliding, undo movement
			temp = this.intersection(obj);
			if(!temp.isEmpty()) {
				translate((xVel>0?-1:1)*(int)temp.getWidth(),0);//check this line
				xVel=0;
				colliding = true;
			}
		}
		for(DynamicObj obj:dynamics) {//if now colliding, undo movement
			temp = this.intersection(obj);
			if(!temp.isEmpty()) {
				translate((xVel>0?-1:1)*(int)temp.getWidth(),0);//check this line
				if(obj instanceof Goumba && ((Goumba)obj).squashed) {
					obj.xVel = xVel*2;
					xVel /= -2;
				} else {
					damage(1);						
					xVel = (xVel>0?-1:1)*10;
				}
				this.move = 0;
				colliding = true;
			}
		}
		if(jump) {
			jump = false;
			if(jumps<maxJumps) {
				yVel = 20;
				jumps++;
			}
		}
		yVel -= g;
		yVel = Math.max(-30,yVel);
		translate(0,(int)-yVel);
		for(StaticObj obj:statics) {//if now colliding, undo movement
			temp = this.intersection(obj);
			if(!temp.isEmpty()) {
				if(yVel>0) //hit on roof
					translate(0,(int)temp.getHeight());
				else{ //hit on ground
					translate(0,-(int)temp.getHeight());
					jumps=0;
				}
				yVel=0;
			}
		}
		for(DynamicObj obj:dynamics) {//if now colliding, undo movement
			temp = this.intersection(obj);
			if(!temp.isEmpty()) {
				translate(0,(yVel>0?1:-1)*(int)temp.getHeight());//check this line
				if(obj instanceof Goumba && !((Goumba)obj).squashed) {
					obj.damage(1);
					yVel = 15;
				}
			}
		}
		if(this.y > 2000)
			gameBoard.dieFlag = true;
	}
	@Override
	public void damage(int amt) {
		lives -= amt;
	}
}