package generation;

import functions.FileIO;
import objects.AMansion;
import objects.Pomocna;

import java.io.File;
import java.util.List;
import java.util.Random;

public class PathGenerator {

    private int MAX_SPEED = 120;
    private int TIME=60;
    
    
    private short[][] distanceMatrix;
    private short[][] timeMatrix;
    private List<AMansion> mansions;
    private Random rand;
    private int pathType; //3 typy, 1 - 100%; 2 - 80%; 3 - 60% (speed @100% - 120km/h)

    public PathGenerator(List<AMansion> mans){
        this.mansions = mans;
        distanceMatrix = new short[mans.size()][mans.size()];
        timeMatrix = new short[mans.size()][mans.size()];
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
                    distanceMatrix[i][j] = 0;
                }
                timeMatrix[i][j] = 0;
            }
        }
    }

    public void generatePaths(){
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
                        continue;
                    } else {
                        distance = (mansions.get(j).getDistance(mansions.get(x)) / Generator.multiplier);
                        distanceMatrix[j][x] = (short) distance;  //mozne pomerne znatelne ztraty z pretypovani na double
                        distanceMatrix[x][j] = (short) distance;

                        switch (type){
		                    case 1: timeMatrix[j][x] = (short) Math.round((distance / MAX_SPEED) * TIME);
		                        timeMatrix[x][j] = (short) Math.round((distance / MAX_SPEED) * TIME);
		                        break;
		                    case 2: timeMatrix[j][x] = (short) Math.round((distance / (MAX_SPEED * 0.8)) * TIME);
		                    	timeMatrix[x][j] = (short) Math.round((distance / (MAX_SPEED * 0.8)) * TIME);
                            	break;
		                    case 3: timeMatrix[j][x] = (short) Math.round((distance / (MAX_SPEED * 0.6)) * TIME);
                            	timeMatrix[x][j] = (short) Math.round((distance / (MAX_SPEED * 0.6)) * TIME);
                            	break;
		                    default: timeMatrix[j][x] = (short) Math.round((distance / MAX_SPEED) * TIME);
		                    	timeMatrix[x][j] = (short) Math.round((distance / MAX_SPEED) * TIME);
                        }
                    }
                }
            }
        }
    }

    public void getPathsFromFile(File f){
        Pomocna p = FileIO.importMatrix(f);
        distanceMatrix = p.getDistanceMatrix();
        timeMatrix = p.getTimeMatrix();
    }

    public short[][] getDistanceMatrix(){
        return distanceMatrix;
    }

    public short[][] getTimeMatrix(){
        return  timeMatrix;
    }

}
