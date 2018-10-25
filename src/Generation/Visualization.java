package Generation;

import java.awt.Dimension;
import java.util.List;

import javax.swing.JFrame;

/**
 * Class creating the window of graphical interpretation
 */
public class Visualization{

    public static void main(String[] args) {
        JFrame frame = new JFrame();

        frame.setTitle("The country");
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setMinimumSize(new Dimension(1600, 900));

        Graph g = new Graph();
        g.setPreferredSize(new Dimension(1600, 900));
        frame.add(g);
    }

}
