package Generation;

import java.awt.geom.Point2D;

/**
 * The mansions
 */
public class Mansion extends AMansion{

	 public static final int OPENED_TIME_IN_MIN=120;
	   
	
    /** Size of the mansion */
    public int size;

    //DODELAT!!!
    /** name of the mansion */
    public String name;

    public int ID;
  
    
    public int openingTimeInMin;
    
    public int numOfOrderedGoods=0;
    
    
    /**
     * Constructor filling the position and size of mansion
     * @param pos position
     * @param size size
     */
    public Mansion(Point2D pos, int size, String name, int ID){
        super(pos);
        this.size = size;
        this.name = name;
        
        //n�hrada
        this.ID = ID;
    }
}
