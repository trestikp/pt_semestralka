package graf;

import java.awt.Dimension;

import javax.swing.JFrame;

public class Screen{
	
	public static void main(String[] args) {
		JFrame frame = new JFrame();
		
		frame.setTitle("Okno");
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		frame.setMinimumSize(new Dimension(840, 640));
		
		Graph g = new Graph();
		g.setPreferredSize(new Dimension(840, 640));
		frame.add(g);
	}

}
