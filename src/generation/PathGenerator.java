package generation;

import functions.FileIO;
import objects.AMansion;
import objects.Pomocna;

import java.io.File;
import java.util.List;
import java.util.Random;

/**
 * Class for generating edges between mansions.
 * @author Pavel Třeštík and Tomáš Ott
 */
public class PathGenerator {

    /** Constant of the max speed a truck can travel */
    private final int MAX_SPEED = 120;
    /** Constant travel time */
    private final int TIME=60;
    
    /** Distance edges of the graph */
    private short[][] distanceMatrix;
    /** Time edges of the graph */
    private short[][] timeMatrix;
    /** Mansions to which edges are generated */
    private List<AMansion> mansions;
    /** Attribute of random */
    private Random rand;

    /**
     * Constructor for edges generator
     * @param mans collection of mansions
     */
    public PathGenerator(List<AMansion> mans){
        this.mansions = mans;
        distanceMatrix = new short[mans.size()][mans.size()];
        timeMatrix = new short[mans.size()][mans.size()];
        rand = new Random();

        //long start = System.nanoTime();
        prepareMatrix();
        //long end = System.nanoTime();
        //System.out.println("\n Preparation time: " + (end/1000000 - start/1000000) + "ms\n");
        generatePaths();
    }

    /**
     * Overloaded constructor getting edges from file
     * @param f file to read from
     */
    public PathGenerator(File f){
        getPathsFromFile(f);
    }

    /**
     * Method that prepares the distance and time matrixes before data are generated to them
     */
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

    /**
     * Method that generates the edges. Generates both distance and time
     */
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
                        distanceMatrix[j][x] = (short) distance;
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

    /**
     * Method reading data from file
     * @param f file with data
     */
    public void getPathsFromFile(File f){
        Pomocna p = FileIO.importMatrix(f);
        distanceMatrix = p.getDistanceMatrix();
        timeMatrix = p.getTimeMatrix();
    }

    /**
     * Distance matrix getter.
     * @return matrix of distance edges
     */
    public short[][] getDistanceMatrix(){
        return distanceMatrix;
    }

    /**
     * Time matrix getter
     * @return matrix of time edges
     */
    public short[][] getTimeMatrix(){
        return  timeMatrix;
    }

}
