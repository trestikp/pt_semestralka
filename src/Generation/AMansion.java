package Generation;

import java.awt.geom.Point2D;

/**
 * 
 * @author Ja
 *
 */
public abstract class AMansion {
	
	
	public static final int UNLOADING_WINDOW_TIME_IN_MIN=120;
	
	
	
	
	/** Position of the mansion */
	protected Point2D position;
	
	/**
	 * Constructor creating an instance of mansion on set postion
	 * @param position of the mansion
	 */
	public AMansion(Point2D position) {
		this.position = position;
	}
	
	/**
	 * Distance between 2 mansions
	 * @param mansion
	 * @return
	 */
	public double getDistance(AMansion mansion) { //mela by se vyuzit pri generovani cest
		double res = 0;
		double x = Math.abs(this.position.getX() - mansion.position.getX());
		double y = Math.abs(this.position.getY() - mansion.position.getY());
		
		res = Math.sqrt((x * x) + (y * y));
		
		return res;
	}
	
}
