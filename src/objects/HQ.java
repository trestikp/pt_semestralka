package objects;

import java.awt.geom.Point2D;

/**
 * 
 * Class representing the HQ
 * @author Pavel Třeštík and Tomáš Ott
 */
public class HQ extends AMansion{

	/**
	 * HQ is the main building of our company
	 * @param position of the HQ
	 */
	public HQ(Point2D position) {
		super(position);
	}

	/**
	 * Method returns text info about HQ
	 * @return text
	 */
	public String HQInfo(){
		String res = "";
		res += "ID: 0\n";
		res += "Name: HQ\n";
		res += "Location: [" + position.getX() + "," + position.getY() +"]";
		return res;
	}
}
