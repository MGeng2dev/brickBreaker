package brickBreaker;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

public class MapGenerator {
	public int map[][]; // 2D array, contains all bricks
	public int brickWidth;
	public int brickHeight;
	
	// Constructor
	public MapGenerator(int row, int col) {  // how many rows & cols should be generated for # of bricks
		map = new int[row][col];
		for (int i = 0; i < map.length; i++) {
			for(int j = 0; j < map[0].length; j++) {
				map[i][j] = 1; // 1 means there's a brick at that position in the array
			}
		}
		
		brickWidth = 540 / col;
		brickHeight = 150 / row;
	}
	
	// Draw bricks where there is a value of 1
	public void draw(Graphics2D g) {
		for (int i = 0; i < map.length; i++) {
			for(int j = 0; j < map[0].length; j++) {
				if(map[i][j] > 0) {
					// draw brick
					g.setColor(Color.white);
					g.fillRect(j * brickWidth + 80, i * brickHeight + 50, brickWidth, brickHeight);
					
					// create border around bricks
					g.setStroke(new BasicStroke(3));
					g.setColor(Color.black);
					g.drawRect(j * brickWidth + 80, i * brickHeight + 50, brickWidth, brickHeight);
				}
			}
		}
		
	}
	
	public void setBrickValue(int value, int row, int col) {
		map[row][col] = value;
	}
	
}
