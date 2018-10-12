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
	private final int MARGIN=40;
	
	
	private int grid_width;
	private int grid_height;
	
	private Graphics2D g2;
	
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		this.g2 = (Graphics2D) g;
		
		paintGraph();
		
	}
	
	private void paintGraph() {
		grid_width = (this.getWidth() - MARGIN) / GRID_NUMBER;
		grid_height = (this.getHeight() - MARGIN) / GRID_NUMBER;
		
		HlavniSidlo s = null;
		
		g2.setColor(Color.WHITE);
		g2.fillRect(0, 0, this.getWidth(), this.getHeight());
		
		g2.translate(20, 20);		
		
		Rectangle2D background = new Rectangle2D.Double(0, 0, this.getWidth()-MARGIN, this.getHeight()-MARGIN);
		
		g2.setColor(Color.BLACK);
		g2.fill(background);
		
		paintGrid();
		paintHlavniSidlo(s);
	}
	
	/**
	 * Nakreslí møížku pro lepší orientaci.
	 * Ve finální verzi pøípadnì odstranit
	 */
	private void paintGrid() {
		Color def= g2.getColor();
		g2.setColor(Color.gray);
		
		for(int i=0;i<GRID_NUMBER-1;i++) {
			g2.drawLine(grid_width*(i+1), 0, grid_width*(i+1), getHeight()-MARGIN);
			g2.drawLine(0, grid_height*(i+1), getWidth()-MARGIN, grid_height*(i+1));
		}
		
		
		
		g2.setColor(def);
	}
	

	private void paintHlavniSidlo(HlavniSidlo sidlo) {
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
