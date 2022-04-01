package megaMan;

import java.awt.Graphics2D;
import java.awt.Rectangle;

public abstract class Obj extends Rectangle {
	public Obj(int x, int y, int w, int h) {
		super(x,y,w,h);
	}

	public abstract void draw(Graphics2D g2d);
}
