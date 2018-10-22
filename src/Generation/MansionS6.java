package Generation;

import java.awt.geom.Point2D;

/**
 * 
 * @author Ja
 *
 */
public class MansionS6 extends AMansion {
	
	/** Maximum number of pallets the mansion can accept per day */
	private final int MAX_PALLETS = 6;
	
	/**
	 * Constructor creating mansion of size 6 @position
	 * @param position of the mansion
	 */
	public MansionS6(Point2D position) {
		super(position);
	}

}
