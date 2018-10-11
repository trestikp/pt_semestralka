package graf;

import java.awt.geom.Point2D;

public class OdberneSidlo extends Sidlo {

	public final int velikost;

	public OdberneSidlo(int velikost, Point2D pozice) {
		super(pozice);
		this.velikost = velikost;
	}
}
