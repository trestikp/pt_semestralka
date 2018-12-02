package functions;

import delivery.Truck;
import objects.AMansion;
import objects.HQ;
import objects.Mansion;
import objects.Pomocna;
import simulation.Simulation;

import java.awt.geom.Point2D;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;

/**
 * Class used for exporting/importing information of the program
 * @author Pavel Třeštík and Tomáš Ott
 */
public class FileIO {

    /**
     * Method exports generated mansions to a file.
     * @param col collection of mansions to be exported.
     * @param dir directory where the file is to be created.
     */
    public static void exportToFile(List<AMansion> col, File dir){
        try {
            File f = new File(dir.getPath() + "/mansions.txt");
            if(f.exists()){
                f.delete();
            }
            PrintWriter pw = new PrintWriter(f);
            pw.printf("%s;%d;%d\n", "HQ", (int) col.get(0).position.getX(), (int) col.get(0).position.getY());
            for(int i = 1; i < col.size(); i++){
                Mansion a = (Mansion) col.get(i);
                int size = a.size;
                double x = a.position.getX();
                double y = a.position.getY();
                String name = a.name;
                int ID = a.iD;
                pw.printf("%d;%d;%d;%s;%d\n",size, (int) x, (int) y, name, ID);
            }
            pw.close();
            if(!f.setReadOnly()){
                System.out.println("Failed to make file read only.");
            }
        } catch (IOException e){
            System.out.println("IO Exception" + e.getMessage());
        }
    }

    /**
     * Method imports mansions from a file
     * @param f file from which mansions are imported
     * @return collection of imported mansions
     */
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

    /**
     * Method exports distance and time matrixes to one file.
     * @param distanceMatrix distance matrix to be exported
     * @param timeMatrix time matrix to be exported
     * @param dir the export target location
     */
    public static void exportMatrix(short distanceMatrix[][], short timeMatrix[][], File dir){
        try {
            File f = new File(dir.getPath() + "/matrixes.txt");
            if(f.exists()){
                f.delete();
            }
            PrintWriter pw = new PrintWriter(f);
            for(int i = 0; i < distanceMatrix.length; i++){
                for(int j = 0; j < distanceMatrix.length; j++){
                    pw.print(distanceMatrix[i][j] + "!" + timeMatrix[i][j] + ";");
                }
                pw.println();
            }
            pw.close();
            if(!f.setReadOnly()){
                System.out.println("Failed to make file read only.");
            }
        } catch (IOException e){
            System.out.println(e.getMessage());
        }
    }

    /**
     * Method imports distance and time matrix from a file
     * @param f file from which matrixes are imported
     * @return returns a carrier of matrixes
     */
    public static Pomocna importMatrix(File f){
    	short[][] distanceMatrix = null;
    	short[][] timeMatrix = null;
        try{
            int length = 0;
            //int count = 0;
            Scanner sc = new Scanner(f);
            while(sc.hasNext()){
                length++;
                sc.nextLine();
            }
            sc.close();
            sc = new Scanner(f);
            distanceMatrix = new short[length][length];
            timeMatrix = new short[length][length];
            String line;
            String[] pLine;
            String[] aLine;
            length = 0;
            while (sc.hasNext()) {
                line = sc.nextLine().trim();
                pLine = line.split(";");
                for(int i = 0; i < pLine.length; i++){
                    aLine = pLine[i].split("!");
                    distanceMatrix[length][i] = (short) Integer.parseInt(aLine[0]);
                    timeMatrix[length][i] = (short)Integer.parseInt(aLine[1]);
                    /*if(Integer.parseInt(aLine[0]) > 0){
                        //count++;
                        //nebyl by prazdny, slouzi pro testovaci ucely
                    }*/
                }
                //System.out.println("Line: " + length + " - paths: " + count);
                //count = 0;
                length++;
            }
            sc.close();
        } catch (IOException e){
            System.out.println(e.getMessage());
        }

        Pomocna p = new Pomocna(distanceMatrix, timeMatrix);
        return  p;
    }

    /**
     * Method writes statistics to a file.
     * @param launchable queue of truck that are in HQ
     * @param onRoad arraylist of truck that are on road
     * @param sim instance of simulation
     * @param dir target directory of the statistics
     */
    public static void writeStatistics(Queue<Truck> launchable, List<Truck> onRoad, Simulation sim, File dir){
        try{
            File f;
            if(dir == null){
                f = new File("statistics.xml");
            } else {
                f = new File(dir.getPath() + "/statistics.xml");
            }
            PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(f, true)));
            //pw.print("<simulation>\n");

            pw.print("<day>\n");
            pw.print("<daycount>");
            pw.println("Day: " + sim.getDay());
            pw.print("</daycount>\n");
            pw.print("<pallets> ");
            pw.print("The company delivered: " + Truck.getTotalLoadOfAllTrucks() + " pallets.");
            pw.print(" </pallets>\n");
            pw.print("<business> ");
            pw.print("The company's vehicles traveled: " + Truck.getmileageOfAllTrucks() + " km, resulting in total price: " +
                    Truck.getTotalMoneyOfAll() + " kc.");
            pw.print(" </business>\n");
            pw.print("<truckList>\n");
            for(Truck t: launchable){
                pw.print(t.truckTag());
            }
            for(Truck t: onRoad){
                pw.print(t.truckTag());
            }
            pw.print(" </truckList>\n");
            pw.print("</day>\n");

            //pw.print("</simulation>\n");
            pw.close();
            System.out.println("Writing statistics.");
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * Method write the start of statistics file
     * @param dir target directory of the statistic file
     */
    public static void startStatistics(File dir){
        File f;
        if(dir == null){
            f = new File("statistics.xml");
        } else {
            f = new File(dir.getPath() + "/statistics.xml");
        }
        try {
            PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(f, true)));
            pw.print("<simulation>\n");
            pw.close();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * Method write the end of statistics file
     * @param dir target directory of the statistic file
     */
    public static void endStatistics(File dir){
        File f;
        if(dir == null){
            f = new File("statistics.xml");
        } else {
            f = new File(dir.getPath() + "/statistics.xml");
        }
        try {
            PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(f, true)));
            pw.print("\n</simulation>");
            pw.close();
        } catch (IOException e){
            e.printStackTrace();
        }
        f.setReadOnly();
    }

    /**
     * Method writes summarized statistics of the whole simulation
     * @param dir target directory of the statistic file
     */
    public static void wholeSimAllTruckStats(File dir){
        File f;
        if(dir == null){
            f = new File("statistics.xml");
        } else {
            f = new File(dir.getPath() + "/statistics.xml");
        }
        try {
            PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(f, true)));
            pw.print("\n" + Truck.wholeSimStats());
            pw.close();
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
