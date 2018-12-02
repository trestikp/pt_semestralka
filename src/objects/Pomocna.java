package objects;

/**
 * Carrier of matrixes
 * @author Pavel Třeštík and Tomáš Ott
 */
public class Pomocna {



    private short[][] distanceMatrix;
    private short[][] timeMatrix;

    /**
     * Constructor creating the carrier
     * @param dMatrix distance matrix
     * @param tMatrix time matrix
     */
    public Pomocna(short[][] dMatrix, short[][] tMatrix){
        this.distanceMatrix = dMatrix;
        this.timeMatrix = tMatrix;
    }

    /**
     * Distance matrix getter
     * @return distance matrix
     */
    public short[][] getDistanceMatrix() {
        return distanceMatrix;
    }

    /**
     * Time matrix getter
     * @return time matrix
     */
    public short[][] getTimeMatrix() {
        return timeMatrix;
    }

    /**
     * Returns number of mansions of the matrixes
     * @return count of mansions
     */
    public int mansionQuantity() {
    	return distanceMatrix.length;
    }

}
