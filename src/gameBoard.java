package megaMan;

import javax.imageio.ImageIO;
import javax.swing.*;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.image.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class gameBoard extends JPanel {	
	static final boolean showBounds = false;
	static int fps = 20;
	static int tStep = 1000/fps;
	static int hScrollBound = 270;
	static int vScrollBound = 250;
	
	static boolean exitFlag = false;
	static boolean dieFlag = false;
	static Rectangle viewBounds;
	//variables
	protected Character man;
	protected BufferedImage heart;
	
	//each dynamic obj handles collisions with static world individually
	//Interactions between dynamic objs
	protected ArrayList<StaticObj> statics = new ArrayList<>();
	protected ArrayList<DynamicObj> dynamics = new ArrayList<>();
	protected volatile Obj newObj;
	protected static int xScroll=0;
	protected static int yScroll=0;
	public gameBoard() throws IOException {
		man = new Character();
		heart = ImageIO.read(ClassLoader.getSystemResource("heart.png"));
		newObj = new Brick(6, 6);
//		statics.add((StaticObj)newObj);
		//Listener
		addKeyListener(new KeyInput());
		setFocusable(true);
		MouseEvents mouse = new MouseEvents();
		addMouseListener(mouse);
		addMouseMotionListener(mouse);
	}//end constructor
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D)g;
		g2d.translate(-xScroll, -yScroll);
		if(showBounds) {
			g2d.drawLine(hScrollBound+xScroll,yScroll,hScrollBound+xScroll,viewBounds.height+yScroll);
			g2d.drawLine(xScroll+viewBounds.width-hScrollBound,yScroll,xScroll+viewBounds.width-hScrollBound,viewBounds.height+yScroll);	
			g2d.drawLine(xScroll, vScrollBound+yScroll,xScroll+viewBounds.width,vScrollBound+yScroll);
			g2d.drawLine(xScroll, yScroll+viewBounds.height-vScrollBound,xScroll+viewBounds.width, yScroll+viewBounds.height-vScrollBound);
		}

		newObj.draw(g2d);
		g2d.setColor(new Color(0x88FFFFFF, true));
		g2d.fillRect(newObj.x, newObj.y, newObj.width, newObj.height);
		
		for(StaticObj o:statics)o.draw(g2d); 
		for(DynamicObj e:dynamics)e.draw(g2d); 
		man.draw(g2d);
		
		//draw overlay
		g2d.translate(xScroll, yScroll);
		for(int i = 0; i < man.lives; i++)
			g2d.drawImage(heart,null,15+23*i,15);
	}//end paintComponent
	
	private void run() {
		buildEnv();
		long nextLoop;
		while(true) {
			nextLoop = System.currentTimeMillis()+tStep;
			for(int i = 0; i<dynamics.size();i++) {
				DynamicObj e = dynamics.get(i);
				e.physics(statics);
				if(!viewBounds.intersects(e)) {
					dynamics.remove(i);
					i--;
				}	
			}
			man.physics(statics, dynamics);
			
			if(man.getX()>xScroll+viewBounds.width-hScrollBound)
				xScroll = (int)(man.getX() - viewBounds.width + hScrollBound);
			else if(man.getX()<xScroll+hScrollBound)
				xScroll = (int)(man.getX() - hScrollBound);
			if(man.getY()<yScroll+vScrollBound)
				yScroll =(int)(man.getY()-vScrollBound);
			else if(man.getY()>yScroll+viewBounds.height-vScrollBound)
				yScroll = (int)(man.getY()+vScrollBound-viewBounds.height);
			
			repaint();
			if(dieFlag) {
				repaint();
				break;
			}
			while(System.currentTimeMillis()<nextLoop);
		}
		System.out.println("finished");
	}
	public static void main(String[] args) throws IOException {
		viewBounds = new Rectangle(0,0,1200,800);

		JFrame frame = new JFrame();
		frame.setTitle("Megaman");
		frame.setSize(viewBounds.width, viewBounds.height);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		frame.setLocationRelativeTo(null);
		
		gameBoard game = new gameBoard();
		Container container = frame.getContentPane();
		container.add(game);
		frame.setVisible(true);
		game.menu();
	}//end main
	private void menu() {
		run();
	}


	private void buildEnv() {
		
		int ct = 0;
		try {
			File myObj = new File("src/megaMan/genEnv");
			Scanner inp = new Scanner(myObj);
			while (inp.hasNextLine()) {
				ct++;
				switch(inp.next()) {
				case "B":
					statics.add(new Brick(inp.nextInt(), inp.nextInt()));
					break;
//				case "G":
//					System.out.printf("Goumba(%d, %d, %d)\n", inp.nextInt(), inp.nextInt(), inp.nextInt());
//					break;
				default:
					throw new IllegalArgumentException(String.format("Syntax Error on genEnv line %d", ct));
				}
		    }
		    inp.close();
	    } catch (FileNotFoundException e) {
	    	System.out.println(String.format("Error on line", ct));
	    	e.printStackTrace();
	    }
		

		dynamics.add(new Goumba(800, 500, -1));
	}
	private class KeyInput extends KeyAdapter{
		@Override
		public void keyPressed(KeyEvent e) {
			switch(e.getKeyCode()) {
			case KeyEvent.VK_RIGHT:
				man.move = 1;
				break;
			case KeyEvent.VK_LEFT:
				man.move = -1;
				break;
			case KeyEvent.VK_UP:
				man.jump = true;
				break;
			case KeyEvent.VK_P:
				
			}
		}//end keyPressed
		@Override
		public void keyReleased(KeyEvent e) {
			if(e.getKeyCode() == KeyEvent.VK_RIGHT)
			{
				man.move = 0;
				man.lastDir = true;
			}
			if(e.getKeyCode() == KeyEvent.VK_LEFT)
			{
				man.move = 0;
				man.lastDir = false;
			}
		}
	}
	private class MouseEvents extends MouseAdapter{
		@Override
		public void mouseMoved(MouseEvent e) {
			if(newObj instanceof Brick) {
				newObj.x = (int)((xScroll+e.getX())/newObj.width)*newObj.width;
				newObj.y = (int)(yScroll+e.getY())/newObj.height*newObj.height;
//				System.out.printf("(%d, %d)");
			}else {				
				newObj.x = xScroll+e.getX()-newObj.width/2;
				newObj.y = yScroll+e.getY()-newObj.height/2;
			}
			repaint();
		}
		@Override
		public void mouseClicked(MouseEvent e) {
			if(newObj instanceof StaticObj) {
				System.out.printf("B %d %d\n", newObj.x/newObj.width, newObj.y/newObj.height);
				for(StaticObj o:statics) {
					if(newObj.equals(o)) {
						statics.remove(o);
						return;
					}
				}
				statics.add((StaticObj)newObj);
			}
			if(newObj instanceof DynamicObj)dynamics.add((DynamicObj)newObj);
			newObj = (Obj) newObj.clone();
		}
	}
}//end class
