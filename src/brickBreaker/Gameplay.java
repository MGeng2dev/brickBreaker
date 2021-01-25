package brickBreaker;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
//import java.util.Timer;
import javax.swing.Timer;

import javax.swing.JPanel;

public class Gameplay extends JPanel implements KeyListener, ActionListener {
	private boolean play = false; // when game starts, it shouldn't play by itself
	private int score = 0; // starting score
	
	private int totalBricks = 21;
	
	private Timer timer;
	private int delay = 8;
	
	private int playerX = 310; // starting position of slider
	
	private int ballposX = 120; // starting position of ball
	private int ballposY = 350;
	private int ballXdir = -1; 
	private int ballYdir = -2;
	
	private MapGenerator map;
	
	public Gameplay() { // constructor
		map = new MapGenerator(3, 7); // create map of bricks
		
		addKeyListener(this);
		setFocusable(true);
		setFocusTraversalKeysEnabled(false);
		timer = new Timer(delay, this);
		timer.start();
	}
	
	public void paint(Graphics g) { // draw different objects
		// background
		g.setColor(Color.black);
		g.fillRect(1, 1, 692, 592);
		
		// map
		map.draw((Graphics2D)g);
		
		// borders
		g.setColor(Color.yellow);
		g.fillRect(0, 0, 3, 592);
		g.fillRect(0, 0, 692, 3);
		g.fillRect(692, 0, 3, 592);
		
		
		// score
		g.setColor(Color.white);
		g.setFont(new Font("serif", Font.BOLD, 25));
		g.drawString("" + score,  590,  30); // where to draw
		
		// slider
		g.setColor(Color.green);
		g.fillRect(playerX,  550,  100,  8);
		
		// ball
		g.setColor(Color.yellow);
		g.fillOval(ballposX,  ballposY,  20, 20); // changes as position moves
		
		// detect game over
		if (totalBricks <= 0) {
			play = false;
			ballXdir = 0;
			ballYdir = 0;
			g.setColor(Color.red);
			g.setFont(new Font("serif", Font.BOLD, 30));
			g.drawString("You Won!",  260,  300);
			
			g.setFont(new Font("serif", Font.BOLD, 20));
			g.drawString("Press Enter to Restart",  230,  350);
		}
		
		// check for end of game
		if(ballposY > 570) {
			play = false;
			ballXdir = 0;
			ballYdir = 0;
			g.setColor(Color.red);
			g.setFont(new Font("serif", Font.BOLD, 30));
			g.drawString("Game Over, Score: " + score,  190,  300);
			
			g.setFont(new Font("serif", Font.BOLD, 20));
			g.drawString("Press Enter to Restart",  230,  350);
		}
		
		g.dispose(); // don't know
	}
	

	@Override
	public void actionPerformed(ActionEvent e) {
		timer.start();
		if (play) {  // if we have pressed right or left arrow key
			// detect intersection with slider
			if (new Rectangle(ballposX, ballposY, 20, 20).intersects(new Rectangle(playerX, 550, 100, 8))) {
				ballYdir = -ballYdir;
			}
			
			// iterate thru each brick
			A: for(int i = 0; i < map.map.length; i++) { // first map is object (above), second map is 2D array (in MapGenerator)
				for(int j = 0; j < map.map[0].length; j++) {
					if (map.map[i][j] > 0) { // if brick is there
						int brickX = j * map.brickWidth + 80;
						int brickY = i * map.brickHeight + 50;
						int brickWidth = map.brickWidth;
						int brickHeight = map.brickHeight;
						
						// rectangle around brick
						Rectangle rect = new Rectangle(brickX, brickY, brickWidth, brickHeight);
						Rectangle ballRect = new Rectangle(ballposX, ballposY, 20, 20);  // around ball to detect intersection
						Rectangle brickRect = rect;
						
						// if ball hits brick
						if(ballRect.intersects(brickRect)) {
							map.setBrickValue(0,  i,  j);
							totalBricks--;
							score += 5;
							
							if (ballposX + 19 <= brickRect.x || ballposX + 1 >= brickRect.x + brickRect.width) {
								ballXdir = -ballXdir;
							}
							else {
								ballYdir = -ballYdir;
							}
							
							break A;
						}
					}
				}
			}
			
			// detect if ball is touching border
			ballposX += ballXdir;
			ballposY += ballYdir;
			// left border
			if(ballposX < 0) {
				ballXdir = -ballXdir;
			}
			// top border
			if(ballposY < 0) {
				ballYdir = -ballYdir;
			}
			// right border
			if(ballposX > 670) {
				ballXdir = -ballXdir;
			}
			
		}
		
		repaint(); // function recalls paint method and draws everything again
		
	}

	// We don't need these but have to keep in code to work
	@Override
	public void keyTyped(KeyEvent e) {}
	@Override
	public void keyReleased(KeyEvent e) {}

	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
			// make sure doesn't go outside panel
			if(playerX >= 600) {
				playerX = 600;
			}
			else {
				moveRight(); // method call
			}
		}
		if(e.getKeyCode() == KeyEvent.VK_LEFT) {
			// make sure doesn't go outside panel
			if(playerX < 10) {
				playerX = 10;
			}
			else {
				moveLeft(); // method call
			}
		}
		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			if(!play) { // game over or finished
				play = true;
				ballposX = 120;
				ballposY = 350;
				ballXdir = -1;
				ballYdir = -2;
				playerX = 350; // default position
				score = 0;
				totalBricks = 21;
				map = new MapGenerator(3, 7);
				
				repaint();
			}
		}
		
	}
	
	public void moveRight() {
		play = true;
		playerX += 20; // move 20 pixels to the right when right arrow clicked
		
	}
	
	public void moveLeft() {
		play = true;
		playerX -= 20; // moves 20 pixels to the left when left arrow clicked
	}

	

}
