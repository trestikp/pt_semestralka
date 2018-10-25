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

	/** Array of all mansions. */
	private AMansion[] mansions = new AMansion[numberOfMansions]; //mozna lepsi kolekce?
	/** The current end of @mansions */
	private int currentMansionsEnd = 0;

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
		System.out.printf(pomocnejVypis());
	}
	
	/**
	 * Method generates the position of the HQ.
	 * @return
	 */
	public void generateHQ(/*int windowWidth, int windowHeight*/) {
		int fromX = ((windowWidth / 2) - (HQ_GENERATOR_MARGIN/2));
		//int toX = ((windowWidth / 2) + (HQ_GENERATOR_MARGIN/2));
		int fromY = ((windowHeight / 2) - (HQ_GENERATOR_MARGIN/2));
		//int toY = ((windowHeight / 2) + (HQ_GENERATOR_MARGIN/2));
		HQ res = null;
		
		int x = rand.nextInt(HQ_GENERATOR_MARGIN + 1) + fromX;
		int y = rand.nextInt(HQ_GENERATOR_MARGIN + 1) + fromY;
		
		Point2D.Double pos = new Point2D.Double(x, y);

		res = new HQ(pos);
		mansions[currentMansionsEnd] = res;
		currentMansionsEnd++;
		//return res;
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
			if(a <= 3) {
				numberOfMS6++;
				continue;
			}
			if(a > 3 && a <= 8) {
				numberOfMS5++;
				continue;
			}
			if(a > 8 && a <= 18) {
				numberOfMS4++;
				continue;
			}
			if(a > 18 && a <= 38) {
				numberOfMS3++;
				continue;
			}
			if(a > 38 && a <= 63) {
				numberOfMS2++;
				continue;
			}
			if(a > 63 && a <= 100) numberOfMS1++;
		}
	}
	
	public void generateMansionsSize6() {
		//AMansion[] res = new AMansion[numberOfMS6];
		Point2D pos = generatePosition();
		MansionS6 mans = null;
		
		for(int i = currentMansionsEnd; i < numberOfMS6; i++) {
			mans = new MansionS6(pos);
			for(int a = 0; a < currentMansionsEnd; a++){
				if(mansions[a] instanceof HQ || mansions[a] instanceof MansionS6 || mansions[a] instanceof MansionS5){
					if(mansions[a].getDistance(mans) < HQ_MS56_MARGIN){
						pos = generatePosition();
						break;
					}
				}
			}
		}
		
		//return res;
	}

	/**
	 * Generator of position.
	 * @return
	 */
	private Point2D generatePosition(/*int windowWidth, int windowHeight*/) {
		int x = rand.nextInt(windowWidth - (2 * WINDOW_MARGIN)) + WINDOW_MARGIN;
		int y = rand.nextInt(windowHeight - (2 * WINDOW_MARGIN)) + WINDOW_MARGIN;
		
		return new Point2D.Double(x, y);
	}

	//pak vymazat
	private String pomocnejVypis(){
		return String.format("1: %s\n2: %s\n3: %s\n4: %s\n5: %s\n6: %s", numberOfMS1, numberOfMS2, numberOfMS3, numberOfMS4, numberOfMS5, numberOfMS6);
	}
}
