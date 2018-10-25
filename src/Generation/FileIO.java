package Generation;

import java.awt.geom.Point2D;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Class used for exporting/importing information of mansions - to be removed?
 */
public class FileIO {
    public static void exportToFile(List<AMansion> col){
        try {
            PrintWriter pw = new PrintWriter(new File("vstup.txt"));
            pw.printf("%s;%d;%d\n", "HQ", (int) col.get(0).position.getX(), (int) col.get(0).position.getY());
            for(int i = 1; i < col.size(); i++){
                Mansion a = (Mansion) col.get(i);
                int size = a.size;
                double x = a.position.getX();
                double y = a.position.getY();
                pw.printf("%d;%d;%d\n",size, (int) x, (int) y);
            }
            pw.close();
        } catch (IOException e){
            System.out.println("IO Exception" + e.getMessage());
        }
    }

    public static List<AMansion> importFromFile(File f){
        List<AMansion> col = null;
        try{
            col = new ArrayList<>();
            Scanner sc = new Scanner(f);
            Point2D pos;
            while(sc.hasNext()){
                String line = sc.next().trim();
                String pLine[] = line.split(";");
                if(pLine[0].equals("HQ")){
                    pos = new Point2D.Double(Double.parseDouble(pLine[1]), Double.parseDouble(pLine[2]));
                    col.add(new HQ(pos));
                } else {
                    pos = new Point2D.Double(Double.parseDouble(pLine[1]), Double.parseDouble(pLine[2]));
                    col.add(new Mansion(pos, Integer.parseInt(pLine[0])));
                }
                sc.nextLine();
            }
            sc.close();
        } catch (IOException e){
            System.out.println("IO Exception: " + e.getMessage());
        }
        return col;
    }
}
