package Generation;

import java.util.List;
import java.util.Random;

public class PathGenerator {

    private int[][] distanceMatrix;
    private List<AMansion> mansions;
    private Random rand;

    private PathGenerator(List<AMansion> mans){
        this.mansions = mans;
        distanceMatrix = new int[mans.size()][mans.size()];
        rand = new Random();
    }

    private void generatePaths(){

    }

}
