package graf;

import java.awt.geom.Point2D;

public class Cesta {

	public final int ohodnoceni;
	public final Point2D pocatek, konec;

	public Cesta(Point2D pocatek, Point2D konec, int ohodnoceni) {
		this.ohodnoceni = ohodnoceni;
		this.pocatek = pocatek;
		this.konec = konec;
	}

}
