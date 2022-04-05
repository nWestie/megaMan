package megaMan;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;

public class MenuPtr extends Obj{
	protected volatile Point cPos = new Point(0,0);
	public MenuPtr() {
		super(0,0,1,1);
	}

	@Override
	public void draw(Graphics2D g2d) {
		g2d.setColor(Color.white);
		g2d.fillOval(x,y,5,5);
	}

}
