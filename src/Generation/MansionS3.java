package Generation;

import java.awt.geom.Point2D;

/**
 * 
 * @author Ja
 *
 */
public class MansionS3 extends AMansion {
	
	/** Maximum number of pallets the mansion can accept per day */
	private final int MAX_PALLETS = 3;
	
	/**
	 * Constructor creating mansion of size 3 @position
	 * @param position of the mansion
	 */
	public MansionS3(Point2D position) {
		super(position);
	}

}
