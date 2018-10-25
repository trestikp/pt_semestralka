package Generation;

import java.awt.geom.Point2D;

/**
 * The mansions
 */
public class Mansion extends AMansion{

    /** Size of the mansion */
    public int size;

    /** ID of the mansion */
    public String ID;

    /**
     * Constructor filling the position and size of mansion
     * @param pos position
     * @param size size
     */
    public Mansion(Point2D pos, int size, String ID){
        super(pos);
        this.size = size;
        this.ID = ID;
    }
}
