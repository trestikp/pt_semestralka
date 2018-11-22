package Generation;

public class Pomocna {



    private short[][] distanceMatrix;
    private short[][] timeMatrix;

    public Pomocna(short[][] dMatrix, short[][] tMatrix){
        this.distanceMatrix = dMatrix;
        this.timeMatrix = tMatrix;
    }
    public short[][] getDistanceMatrix() {
        return distanceMatrix;
    }

    public short[][] getTimeMatrix() {
        return timeMatrix;
    }
    
    public int mansionQuantity() {
    	return distanceMatrix.length;
    }

}
