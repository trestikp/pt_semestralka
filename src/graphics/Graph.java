package graphics;

import generation.AMansion;
import generation.Mansion;

import java.awt.*;
import java.awt.geom.*;
import java.util.List;

import javax.swing.JPanel;

/**
 * Class for graphical interpretation
 */
public class Graph extends JPanel {

    private static final long serialVersionUID = 1L;

    private List<AMansion> toPaint;

    public Graph(List<AMansion> toPaint){
        this.toPaint = toPaint;
    }

    /**
     * Painting on the screen
     * @param g 'the painter'
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;

        paintGraph(g2, toPaint);

    }

    /**
     * Paint the instances of mansions and HQ
     * @param g2 'the painter'
     */
    private void paintGraph(Graphics2D g2, List<AMansion> toPaint) {
        g2.setColor(Color.BLACK);
        g2.fillRect(0, 0, this.getWidth(), this.getHeight());
/*
        List<AMansion> toPaint;

        if(mainTest.fromFile == null){
            long start = System.nanoTime();
            Generator g = new Generator(2000, this.getWidth(), this.getHeight());
            long end = System.nanoTime();
            System.out.println("\n Time: " + (end/1000000 -start/1000000) + "ms\n");
            toPaint = g.getMansions();
            //FileIO.exportToFile(toPaint);
        } else {
            toPaint = FileIO.importFromFile(new File(mainTest.fromFile));
        }
*/

        g2.setColor(Color.RED);
        g2.fill(new Ellipse2D.Double((toPaint.get(0).position.getX()) - 10, (toPaint.get(0).position.getY()) - 10, 20, 20));

        for(int i = 1; i < toPaint.size(); i++){

            if(toPaint.get(i) instanceof Mansion){
                Mansion a = (Mansion) toPaint.get(i);

                switch(a.size){
                    case 6:
                        g2.setColor(new Color(0, 0, 255,255));
                        g2.fill(new Ellipse2D.Double((a.position.getX()) - 8, (a.position.getY()) - 8, 16, 16));
                        break;
                    case 5:
                        g2.setColor(/*new Color(0, 153, 255, 255)*/ Color.YELLOW);
                        g2.fill(new Ellipse2D.Double((a.position.getX()) - 7, (a.position.getY()) - 7, 14, 14));
                        break;
                    case 4:
                        g2.setColor(new Color(0, 255, 0, 255));
                        g2.fill(new Ellipse2D.Double((a.position.getX()) - 5, (a.position.getY()) - 5, 10, 10));
                        break;
                    case 3:
                        g2.setColor(new Color(255, 51, 204, 255));
                        g2.fill(new Ellipse2D.Double((a.position.getX()) - 4, (a.position.getY()) - 4, 8, 8));
                        break;
                    case 2:
                        g2.setColor(/*new Color(0, 255, 0, 255)*/ Color.BLUE);
                        g2.fill(new Rectangle2D.Double((a.position.getX()) - 3, (a.position.getY()) - 3, 6, 6));
                        //g2.fill(new Ellipse2D.Double((a.position.getX()) - 3, (a.position.getY()) - 3, 6, 6));
                        break;
                    case 1:
                        g2.setColor(/*new Color(0, 153, 255, 255)*/ Color.WHITE);
                        g2.fill(new Rectangle2D.Double((a.position.getX()) - 2, (a.position.getY()) - 2, 4, 4));
                        //g2.fill(new Ellipse2D.Double((a.position.getX()) - 2, (a.position.getY()) - 2, 4, 4));
                        break;
                }
            }
        }
    }

}
