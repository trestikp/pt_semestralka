package Generation;

import java.awt.geom.Point2D;
import java.util.Random;

/**
 * 
 * @author Ja
 *
 */
public class Generator {

	//Marginy mozna budou chtit zmenit podle toho jak to bude vypadat, tohle jsem navrhl jen tak co me napadlo
	/** Margin around HQ. There can't be a mansion of size 5 or 6 within this margin. */
	private final int HQ_MS56_MARGIN = 200;
	/** Margin around mansions of size 5 and 6. Restricting other mansions and HQ. */
	private final int MS56_MARGIN = 100;
	/** Margin around mansions of size 3 and 4. Restricting mansions with size 3< */
	private final int MS34_MARGIN = 50;
	/** Margin around mansions of size 1 and 2. Restricting mansions with size 1< */
	private final int MS12_MARGIN = 10;
	/** Margin from the edges of the window. */
	private final int WINDOW_MARGIN = 10;
	
	private final int HQ_GENERATOR_MARGIN = 100;
	
	/** Random number generator */
	private Random rand;
	
	/** Number of mansions to generate. Number should be between <500, 2000>. Basic @value 500.  */
	private int numberOfMansions = 500;
	
	/** Number of mansions of size 1 */
	private int numberOfMS1 = 0;
	/** Number of mansions of size 2 */
	private int numberOfMS2 = 0;
	/** Number of mansions of size 3 */
	private int numberOfMS3 = 0;
	/** Number of mansions of size 4 */
	private int numberOfMS4 = 0;
	/** Number of mansions of size 5 */
	private int numberOfMS5 = 0;
	/** Number of mansions of size 6 */
	private int numberOfMS6 = 0;
	
	//prozatim hodnoty prirazene konstruktorem
	private int windowWidth;
	private int windowHeight;
	
	/**
	 * Constructor for generator. Used to get the number of mansions to generate.
	 * @param numberOfMansions that is to be generated.
	 */
	public Generator(int numberOfMansions, int wX, int wY) {
		if(numberOfMansions < 500 || numberOfMansions > 2000) {
			throw new IllegalArgumentException("Wrong number of mansions! Please enter a number between 500 and 2000!");
		}
		this.numberOfMansions = numberOfMansions;
		
		this.windowWidth = wX;
		this.windowHeight = wY;
		
		rand = new Random();
		countNumberOfMansionSizes();
	}
	
	/**
	 * Method generates the position of the HQ.
	 * @param windowWidth - width of the window HQ is painted on
	 * @param windowHeight - height of the window HQ is painted on
	 * @return
	 */
	public AMansion generateHQ(/*int windowWidth, int windowHeight*/) {
		int fromX = ((windowWidth / 2) - (HQ_GENERATOR_MARGIN/2));
		//int toX = ((windowWidth / 2) + (HQ_GENERATOR_MARGIN/2));
		int fromY = ((windowHeight / 2) - (HQ_GENERATOR_MARGIN/2));
		//int toY = ((windowHeight / 2) + (HQ_GENERATOR_MARGIN/2));
		
		int x = rand.nextInt(HQ_GENERATOR_MARGIN + 1) + fromX;
		int y = rand.nextInt(HQ_GENERATOR_MARGIN + 1) + fromY;
		
		Point2D.Double pos = new Point2D.Double(x, y);
		
		return new HQ(pos);
	}
	
	/**
	 * Method counts how many mansions of each size to create.
	 */
	private void countNumberOfMansionSizes() {
			//jina moznost jak to udelat je v konstantim case proste udelat random z cisla vsech mansion. napr 3 az 5 procent z xxx(500-2000) budou S6
		int a = 0;
		for(int i = 0; i < numberOfMansions; i++) {
			a = rand.nextInt(100) + 1;
				//to si skoro rika o switch...
			if(a <= 5) {
				numberOfMS6++;
				continue;
			}
			if(a > 5 && a <= 15) {
				numberOfMS5++;
				continue;
			}
			if(a > 15 && a <= 30) {
				numberOfMS4++;
				continue;
			}
			if(a > 30 && a <= 50) {
				numberOfMS3++;
				continue;
			}
			if(a > 50 && a <= 75) {
				numberOfMS2++;
				continue;
			}
			if(a > 75 && a <= 100) numberOfMS1++;
		}
	}
	
	public AMansion[] generateMansionsSize6() {
		AMansion[] res = new AMansion[numberOfMS6];
		Point2D pos = null;
		MansionS6 mans = null;
		
		for(int i = 0; i < numberOfMS6; i++) {
			pos = generatePosition();
			mans = new MansionS6(pos);
			//if()
		}
		
		return null;
	}
	
	private Point2D generatePosition(/*int windowWidth, int windowHeight*/) {
		int x = rand.nextInt(windowWidth - (2 * WINDOW_MARGIN)) + WINDOW_MARGIN;
		int y = rand.nextInt(windowHeight - (2 * WINDOW_MARGIN)) + WINDOW_MARGIN;
		
		return new Point2D.Double(x, y);
	}
}
