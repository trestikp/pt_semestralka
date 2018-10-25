package Generation;

import java.util.List;
import java.util.Random;

public class PathGenerator {

    private int[][] distanceMatrix;
    private List<AMansion> mansions;
    private Random rand;

    public PathGenerator(List<AMansion> mans){
        this.mansions = mans;
        distanceMatrix = new int[mans.size()][mans.size()];
        rand = new Random();

        //System.out.println(distanceMatrix.length);

        //long start = System.nanoTime();
        prepareMatrix();
        //long end = System.nanoTime();
        //System.out.println("\n Preparation time: " + (end/1000000 - start/1000000) + "ms\n");
    }

    private void prepareMatrix(){
        for(int i = 0; i < distanceMatrix.length; i++){
            for(int j = 0; j < distanceMatrix.length; j++){
                if(i == j){
                    distanceMatrix[i][j] = 0;
                } else {
                    distanceMatrix[i][j] = -1;
                }
            }
        }
    }

    public int[][] generatePaths(){
        int x;
        double distance;
        for(int j = 0; j < distanceMatrix.length/2; j++){
            for(int i = 0; i < 200; i++){
                x = rand.nextInt(mansions.size());
                if(j == x){
                    x = rand.nextInt(mansions.size());
                    i--;
                } else {
                    distance = (mansions.get(j).getDistance(mansions.get(x)) / Generator.multiplier);
                    distanceMatrix[j][x] = (int) distance;  //mozne pomerne znatelne ztraty z pretypovani na double
                    distanceMatrix[x][j] = (int) distance;
                }
            }
        }

        return distanceMatrix;
    }
}
