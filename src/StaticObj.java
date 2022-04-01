package megaMan;

import java.awt.Graphics2D;
import java.awt.Rectangle;

public abstract class StaticObj extends Obj{
	private static final long serialVersionUID = 1L;
	/**
	 * Adds brick, on brick-sized grid. To place at specific pixel, use freeBrick
	 */
	public StaticObj(int x, int y, int w, int h) {
		super(x,y,w,h);
	}
	public abstract void draw(Graphics2D g2d);	
}
