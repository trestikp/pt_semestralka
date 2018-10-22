package Generation;

import java.awt.geom.Point2D;

/**
 * 
 * @author Ja
 *
 */
public class MansionS1 extends AMansion {
	
	/** Maximum number of pallets the mansion can accept per day */
	private final int MAX_PALLETS = 1;
	
	/**
	 * Constructor creating mansion of size 1 @position
	 * @param position of the mansion
	 */
	public MansionS1(Point2D position) {
		super(position);
	}

}
