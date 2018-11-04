package Generation;

public class Pomocna {



    private int[][] distanceMatrix;
    private double[][] timeMatrix;

    public Pomocna(int[][] dMatrix, double[][] tMatrix){
        this.distanceMatrix = dMatrix;
        this.timeMatrix = tMatrix;
    }
    public int[][] getDistanceMatrix() {
        return distanceMatrix;
    }

    public double[][] getTimeMatrix() {
        return timeMatrix;
    }

}
