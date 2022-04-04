package megaMan;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * Handles all movement for objects, including collisions by using rectangular bound intersections
 */
public abstract class DynamicObj extends Obj{
	private static final long serialVersionUID = 1L;
	protected double g = 2;

	protected volatile byte move = 0; //-1 left, 1 right, 0 stop
	protected volatile boolean jump = false;
	protected int jumps = 0;
	protected double xVel = 0, yVel = 0; //up is positive yVel
	protected boolean colliding = false;
	
	protected double acc;
	protected double dAcc;
	protected int maxJumps;
	
	public DynamicObj(int x, int y, int w, int h) {
		super(x,y,w,h);
		
	}
	public abstract void draw(Graphics2D g2d);
	public abstract void damage(int amt);
	public void physics(ArrayList<StaticObj> statics) { //should be called every frame
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
	}
}//end class