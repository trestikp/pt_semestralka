package Generation;

import java.awt.*;
import java.awt.geom.Point2D;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
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
                String ID = a.ID;
                pw.printf("%d;%d;%d;%s\n",size, (int) x, (int) y, ID);
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
                    col.add(new Mansion(pos, Integer.parseInt(pLine[0]), pLine[3]));
                }
                sc.nextLine();
            }
            sc.close();
        } catch (IOException e){
            System.out.println("IO Exception: " + e.getMessage());
        }
        return col;
    }

    public static void exportMatrix(int distanceMatrix[][]){
        try {
            PrintWriter pw = new PrintWriter(new File("distanceMatrix.txt"));
            for(int i = 0; i < distanceMatrix.length; i++){
                for(int j = 0; j < distanceMatrix.length; j++){
                    pw.print(distanceMatrix[i][j] + " ; ");
                }
                pw.println();
            }
            pw.close();
        } catch (IOException e){
            System.out.println(e.getMessage());
        }
    }

    public static void matrixResults(File f){
        try{
            Scanner sc = new Scanner(f);
            String line = null;
            int count = 0, a, max = 0, counter = 0;
            while (sc.hasNext()){
                line = sc.nextLine().trim();
                String[] pLine = line.split(";");
                for(int i = 0; i < pLine.length; i++){
                    a = Integer.parseInt(pLine[i].trim());
                    if(a != -1 || a != 0){
                        if(a > max){
                            max = a;
                        }
                    }
                }
                //sc.nextLine();
            }
            System.out.println("Max: "+max);
        } catch (IOException e){
            System.out.println(e.getMessage());
        }
    }
}
