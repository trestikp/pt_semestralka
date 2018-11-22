package graphics;

import generation.AMansion;

import java.awt.Dimension;
import java.util.List;

import javax.swing.JFrame;

/**
 * Class creating the window of graphical interpretation
 */
public class Visualization{

    public static int minWidth = 1600;
    public static int minHeight = 900;

    private static List<AMansion> toPaint;

    public Visualization(List<AMansion> toPaint){
        this.toPaint = toPaint;
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame();

        frame.setTitle("The country");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        Graph g = new Graph(toPaint);
        g.setPreferredSize(new Dimension(minWidth, minHeight));
        frame.add(g);
        frame.pack();

        Dimension d = new Dimension(frame.getPreferredSize());

        frame.setMinimumSize(d);
    }

}
