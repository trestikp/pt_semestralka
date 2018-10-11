package graf;

public class Cesta {

	public final int ohodnoceni;
	public final Bod2D pocatek,konec;
	
	
	public Cesta(Bod2D pocatek,Bod2D konec, int ohodnoceni) {
		this.ohodnoceni=ohodnoceni;
		this.pocatek=pocatek;
		this.konec=konec;
	}
	
	
}
