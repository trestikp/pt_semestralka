package Generation;

import java.awt.geom.Point2D;

/**
 * The mansions
 */
public class Mansion extends AMansion{

    /** Size of the mansion */
    public int size;

    /**
     * Constructor filling the position and size of mansion
     * @param pos position
     * @param size size
     */
    public Mansion(Point2D pos, int size){
        super(pos);
        this.size = size;
    }
}
