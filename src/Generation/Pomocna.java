package Generation;

public class Pomocna {



    private int[][] distanceMatrix;
    private int[][] timeMatrix;

    public Pomocna(int[][] dMatrix, int[][] tMatrix){
        this.distanceMatrix = dMatrix;
        this.timeMatrix = tMatrix;
    }
    public int[][] getDistanceMatrix() {
        return distanceMatrix;
    }

    public int[][] getTimeMatrix() {
        return timeMatrix;
    }
    
    public int mansionQuantity() {
    	return distanceMatrix.length;
    }

}
