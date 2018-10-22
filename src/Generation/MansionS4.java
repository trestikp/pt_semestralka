package Generation;

import java.awt.geom.Point2D;

/**
 * 
 * @author Ja
 *
 */
public class MansionS4 extends AMansion {
	
	/** Maximum number of pallets the mansion can accept per day */
	private final int MAX_PALLETS = 4;
	
	/**
	 * Constructor creating mansion of size 4 @position
	 * @param position of the mansion
	 */
	public MansionS4(Point2D position) {
		super(position);
	}

}
