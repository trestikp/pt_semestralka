package Generation;

import java.awt.geom.Point2D;

/**
 * 
 * @author Ja
 *
 */
public class MansionS5 extends AMansion {
	
	/** Maximum number of pallets the mansion can accept per day */
	private final int MAX_PALLETS = 5;
	
	/**
	 * Constructor creating mansion of size 5 @position
	 * @param position of the mansion
	 */
	public MansionS5(Point2D position) {
		super(position);
	}

}
