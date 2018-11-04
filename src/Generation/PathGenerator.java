package Generation;

import java.awt.*;
import java.io.File;
import java.util.List;
import java.util.Random;

public class PathGenerator {

    private int MAX_SPEED = 120;

    private int[][] distanceMatrix;
    private int[][] timeMatrix;
    private List<AMansion> mansions;
    private Random rand;
    private int pathType; //3 typy, 1 - 100%; 2 - 80%; 3 - 60% (speed @100% - 120km/h)

    public PathGenerator(List<AMansion> mans){
        this.mansions = mans;
        distanceMatrix = new int[mans.size()][mans.size()];
        timeMatrix = new int[mans.size()][mans.size()];
        rand = new Random();

        //System.out.println(distanceMatrix.length);

        //long start = System.nanoTime();
        prepareMatrix();
        //long end = System.nanoTime();
        //System.out.println("\n Preparation time: " + (end/1000000 - start/1000000) + "ms\n");
        generatePaths();
    }

    public PathGenerator(File f){
        getPathsFromFile(f);
    }

    private void prepareMatrix(){
        for(int i = 0; i < distanceMatrix.length; i++){
            for(int j = 0; j < distanceMatrix.length; j++){
                if(i == j){
                    distanceMatrix[i][j] = 0;
                } else {
                    distanceMatrix[i][j] = -1;
                }
                timeMatrix[i][j] = -2;
            }
        }
    }

    public int[][] generatePaths(){
        int x;
        int type;
        double distance;
        for(int j = 0; j < distanceMatrix.length; j++){
            for(int i = 0; i < 200; i++){
                x = rand.nextInt(mansions.size());
                type = rand.nextInt(4)+1;
                if(j == x){
                    x = rand.nextInt(mansions.size());
                    i--;
                } else {
                    if(distanceMatrix[j][x] > 0){
                        x = rand.nextInt(mansions.size());
                        i--;
                    } else {
                        distance = (mansions.get(j).getDistance(mansions.get(x)) / Generator.multiplier);
                        distanceMatrix[j][x] = (int) distance;  //mozne pomerne znatelne ztraty z pretypovani na double
                        distanceMatrix[x][j] = (int) distance;

                        switch (type){
                            case 1: timeMatrix[j][x] = (int) Math.round((distance / MAX_SPEED) * 3600);
                                    timeMatrix[x][j] = (int) Math.round((distance / MAX_SPEED) * 3600);
                                break;
                            case 2: timeMatrix[j][x] = (int) Math.round((distance / (MAX_SPEED * 0.8)) * 3600);
                                    timeMatrix[x][j] = (int) Math.round((distance / (MAX_SPEED * 0.8)) * 3600);
                                break;
                            case 3: timeMatrix[j][x] = (int) Math.round((distance / (MAX_SPEED * 0.6)) * 3600);
                                    timeMatrix[x][j] = (int) Math.round((distance / (MAX_SPEED * 0.6)) * 3600);
                                break;
                                default: timeMatrix[j][x] = (int) Math.round((distance / MAX_SPEED) * 3600);
                                    timeMatrix[x][j] = (int) Math.round((distance / MAX_SPEED) * 3600);
                        }
                    }
                }
            }
        }

        return distanceMatrix;
    }

    public void getPathsFromFile(File f){
        Pomocna p = FileIO.importMatrix(f);
        distanceMatrix = p.getDistanceMatrix();
        timeMatrix = p.getTimeMatrix();
    }

    public int[][] getDistanceMatrix(){
        return distanceMatrix;
    }

    public int[][] getTimeMatrix(){
        return  timeMatrix;
    }

}
