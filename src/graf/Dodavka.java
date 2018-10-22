package graf;

public class Dodavka {

	private static final int MAX_LOAD = 6;
	private static final int UNLOAD_TIME_IN_MIN = 30;

	private int nakladyNaCestu;
	private int dobaStartu;
	private int dobaCesty;

	private OdberneSidlo cilCesty;

	public Dodavka(OdberneSidlo cil) {
		this.cilCesty = cil;
	}

}
