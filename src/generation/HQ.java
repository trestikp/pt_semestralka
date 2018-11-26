package generation;

import java.awt.geom.Point2D;

/**
 * 
 * @author Ja
 *
 */
public class HQ extends AMansion{

	/**
	 * HQ is the main building of our company
	 * @param position of the HQ
	 */
	public HQ(Point2D position) {
		super(position);
	}

	public String HQInfo(){
		String res = "";
		res += "ID: 0\n";
		res += "Name: HQ\n";
		res += "Location: [" + position.getX() + "," + position.getY() +"]";
		return res;
	}
}
