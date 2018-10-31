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
                String name = a.name;
                int ID = a.ID;
                pw.printf("%d;%d;%d;%s;%d\n",size, (int) x, (int) y, name, ID);
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
                    col.add(new Mansion(pos, Integer.parseInt(pLine[0]), pLine[3], Integer.parseInt(pLine[4])));
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
                    pw.print(distanceMatrix[i][j] + ";");
                }
                pw.println();
            }
            pw.close();
        } catch (IOException e){
            System.out.println(e.getMessage());
        }
    }

    public static int[][] importMatrix(File f){
        int[][] distanceMatrix = null;
        try{
            int length = 0;
            int count = 0;
            Scanner sc = new Scanner(f);
            while(sc.hasNext()){
                length++;
                sc.nextLine();
            }
            sc.close();
            sc = new Scanner(f);
            distanceMatrix = new int[length][length];
            String line;
            String[] pLine;
            length = 0;
            while (sc.hasNext()) {
                line = sc.nextLine().trim();
                pLine = line.split(";");
                for(int i = 0; i < pLine.length; i++){
                    distanceMatrix[length][i] = Integer.parseInt(pLine[i]);
                    if(Integer.parseInt(pLine[i]) > 0){
                        count++;
                    }
                }
                //System.out.println("Line: " + length + " - paths: " + count);
                count = 0;
                length++;
            }
            sc.close();
        } catch (IOException e){
            System.out.println(e.getMessage());
        }
        return  distanceMatrix;
    }
}
