package Generation;

import java.awt.geom.Point2D;

/**
 * 
 * @author Ja
 *
 */
public class MansionS2 extends AMansion {
	
	/** Maximum number of pallets the mansion can accept per day */
	private final int MAX_PALLETS = 2;
	
	/**
	 * Constructor creating mansion of size 2 @position
	 * @param position of the mansion
	 */
	public MansionS2(Point2D position) {
		super(position);
	}

}
