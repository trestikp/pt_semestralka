package graf;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.*;
import java.util.Random;

import javax.swing.JPanel;

public class Graph extends JPanel {
	
	private static final long serialVersionUID = 1L;
	
	private Random rand = new Random();
	
	private final int GRID_NUMBER = 19;  //23
	private final int GRID_CENTER = (GRID_NUMBER + 1) / 2;
	
	private int grid_width;
	private int grid_height;
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		Graphics2D g2 = (Graphics2D) g;
		
		paintGraph(g2);
		
	}
	
	private void paintGraph(Graphics2D g2) {
		grid_width = (this.getWidth() - 40) / GRID_NUMBER;
		grid_height = (this.getHeight() - 40) / GRID_NUMBER;
		
		HlavniSidlo s = null;
		
		g2.setColor(Color.WHITE);
		g2.fillRect(0, 0, this.getWidth(), this.getHeight());
		
		g2.translate(20, 20);		
		
		Rectangle2D background = new Rectangle2D.Double(0, 0, this.getWidth()-40, this.getHeight()-40);
		
		g2.setColor(Color.BLACK);
		g2.fill(background);
		
		paintHlavniSidlo(g2, s);
	}
	

	private void paintHlavniSidlo(Graphics2D g2, HlavniSidlo sidlo) {
		int grid_width_start = grid_width * (GRID_CENTER - 1);
		int grid_height_start = grid_height * (GRID_CENTER - 1);
		int grid_width_end = grid_width_start + grid_width;
		int grid_height_end = grid_height_start + grid_height;
		
		int posX = rand.nextInt((grid_width_end - grid_width_start) - 1) + grid_width_start;
		int posY = rand.nextInt((grid_height_end - grid_height_start) -1) + grid_height_start;
		
		g2.setColor(Color.RED);
		g2.fill(new Ellipse2D.Double(posX, posY, 20, 20));
		//return new Ellipse2D.Double(posX, posY, 20, 20);
	}
	
}
