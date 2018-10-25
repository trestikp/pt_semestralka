package Generation;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 
 * @author Ja
 *
 */
public class Generator {

	//Marginy mozna budou chtit zmenit podle toho jak to bude vypadat, tohle jsem navrhl jen tak co me napadlo
	/** Margin around HQ. There can't be a mansion of size 5 or 6 within this margin. */
	private final int MARGIN_50 = 50;
	/** Margin around mansions of size 5 and 6. Restricting other mansions and HQ. */
	private final int MARGIN_25 = 25;
	/** Margin around mansions of size 3 and 4. Restricting mansions with size 3< */
	private final int MARGIN_15 = 15;
	/** Margin around mansions of size 1 and 2. Restricting mansions with size 1< */
	private final int MARGIN_10 = 10;
	/** Margin from the edges of the window. */
	private final int WINDOW_MARGIN = 10;
	
	private final int HQ_GENERATOR_MARGIN = 100;
	
	/** Random number generator */
	private Random rand;
	
	/** Number of mansions to generate. Number should be between <500, 2000>. Basic @value 500.  */
	private int numberOfMansions;
	
	/** Number of mansions of size 1 */
	private int numberOfSize1 = 0;
	/** Number of mansions of size 2 */
	private int numberOfSize2 = 0;
	/** Number of mansions of size 3 */
	private int numberOfSize3 = 0;
	/** Number of mansions of size 4 */
	private int numberOfSize4 = 0;
	/** Number of mansions of size 5 */
	private int numberOfSize5 = 0;
	/** Number of mansions of size 6 */
	private int numberOfSize6 = 0;

	/** Collection of the mansions */
    private List<AMansion> mansionsCol; //mozna pole rychlejsi, nechce se mi srat s polem

	//prozatim hodnoty prirazene konstruktorem
    /** Width of the window the mansions are supposed to generate in */
	private static int windowWidth = Visualization.minWidth;
	/** Height of the window the mansions are supposed to generate in */
	private static int windowHeight = Visualization.minHeight;

	/** Multiplier used to count the px distance to a "real" distance */
	public static double multiplier = Math.max(windowHeight, windowWidth) / 500;
	
	/**
	 * Constructor for generator. Used to get the number of mansions to generate.
	 * @param numberOfMansions that is to be generated.
     * //@param wX width of the window
     * //@param wY height of the window
	 */
	public Generator(int numberOfMansions/*, int wX, int wY*/) {
		if(numberOfMansions < 500 || numberOfMansions > 2000) {
			throw new IllegalArgumentException("Wrong number of mansions! Please enter a number between 500 and 2000!");
		}
		this.numberOfMansions = numberOfMansions;
		
		//this.windowWidth = wX;
		//this.windowHeight = wY;

        mansionsCol = new ArrayList<>(numberOfMansions + 1);
		
		rand = new Random();
		countNumberOfMansionSizes();
		generateHQ();
		generateSize6();
		generateSize5();
		generateSize4();
		generateSize3();
		generateSize2();
		generateSize1();
		System.out.printf(pomocnejVypis());
		System.out.printf("\nCol: " + mansionsCol.size());//taky pomocne
	}
	
	/**
	 * Method counts how many mansions of each size to create.
	 */
	private void countNumberOfMansionSizes() {
			//jina moznost jak to udelat je v konstantim case proste udelat random z cisla vsech mansion. napr 3 az 5 procent z xxx(500-2000) budou S6
            // a nakonec odecist od zbytku a zbytek budou s1
		int a = 0;
		for(int i = 0; i < numberOfMansions; i++) {
			a = rand.nextInt(100) + 1;
				//to si skoro rika o switch...
			if(a <= 3) {
				numberOfSize6++;
				continue;
			}
			if(a > 3 && a <= 8) {
				numberOfSize5++;
				continue;
			}
			if(a > 8 && a <= 18) {
				numberOfSize4++;
				continue;
			}
			if(a > 18 && a <= 38) {
				numberOfSize3++;
				continue;
			}
			if(a > 38 && a <= 63) {
				numberOfSize2++;
				continue;
			}
			if(a > 63 && a <= 100) numberOfSize1++;
		}
	}



	/**
	 * Generator of position.
	 * @return Point2D position
	 */
	private Point2D generatePosition() {
		int x = rand.nextInt(windowWidth - (2 * WINDOW_MARGIN)) + WINDOW_MARGIN;
		int y = rand.nextInt(windowHeight - (2 * WINDOW_MARGIN)) + WINDOW_MARGIN;
		
		return new Point2D.Double(x, y);
	}

	private double getDistance(Point2D p1, Point2D p2){
        double res = 0;
        double x = Math.abs(p1.getX() - p2.getX());
        double y = Math.abs(p1.getY() - p2.getY());

        res = Math.sqrt((x * x) + (y * y));

        return res;
    }

    /**
     * Method generates the position of the HQ and adds it to the 0th index of List.
     */
    public void generateHQ() {
        int fromX = ((windowWidth / 2) - (HQ_GENERATOR_MARGIN/2));
        int fromY = ((windowHeight / 2) - (HQ_GENERATOR_MARGIN/2));
        HQ res = null;

        int x = rand.nextInt(HQ_GENERATOR_MARGIN + 1) + fromX;
        int y = rand.nextInt(HQ_GENERATOR_MARGIN + 1) + fromY;

        Point2D.Double pos = new Point2D.Double(x, y);

        res = new HQ(pos);
        mansionsCol.add(res);
    }

    /**
     * Generates mansions of size 6.
     */
    public void generateSize6(){
        Point2D pos;
        AMansion man = null;

        for(int i = 0; i < numberOfSize6; i++){
            pos = generatePosition();
            for(int j = 1; j < mansionsCol.size(); j++){
                if(getDistance(pos, mansionsCol.get(0).position) < MARGIN_50 || getDistance(pos, mansionsCol.get(j).position) < MARGIN_25) {
                    pos = generatePosition();
                    j = 1;
                }
            }
            man = new Mansion(pos, 6,"Mansion" + mansionsCol.size());
            mansionsCol.add(man);
        }
    }

    /**
     * Generates mansions of size 5 with the same margins as size 6.
     */
    public void generateSize5(){
        Point2D pos;
        AMansion man = null;

        for(int i = 0; i < numberOfSize5; i++){
            pos = generatePosition();

            for(int j = 1; j < mansionsCol.size(); j++){ //ma stejne marginy jako size 6, mohlo by se sloucit, ale pokud by jsme chteli ty marginy zmensit tak je to zvlast
                if(getDistance(pos, mansionsCol.get(0).position) < MARGIN_50 || getDistance(pos, mansionsCol.get(j).position) < MARGIN_25) {
                    pos = generatePosition();
                    j = 1;
                }
            }
            man = new Mansion(pos, 5,"Mansion" + mansionsCol.size());
            mansionsCol.add(man);
        }
    }

    /**
     * Generates mansions of size 4.
     */
    public void generateSize4(){
        Point2D pos;
        AMansion man = null;

        for(int i = 0; i < numberOfSize4; i++){
            pos = generatePosition();

            for(int j = 1; j < mansionsCol.size(); j++){ //ma trochu jine marginy nez size 6 a 5
                if(getDistance(pos, mansionsCol.get(0).position) < MARGIN_25 || getDistance(pos, mansionsCol.get(j).position) < MARGIN_15) {
                    pos = generatePosition();
                    j = 1;
                }
            }
            man = new Mansion(pos, 4,"Mansion" + mansionsCol.size());
            mansionsCol.add(man);
        }
    }

    /**
     * Generates mansions of size 3 with the same margins as size 4.
     */
    public void generateSize3(){
        Point2D pos;
        AMansion man = null;

        for(int i = 0; i < numberOfSize3; i++){
            pos = generatePosition();

            for(int j = 1; j < mansionsCol.size(); j++){ //zase stejne marginy jako 4, dalo by se spojit
                if(getDistance(pos, mansionsCol.get(0).position) < MARGIN_25 || getDistance(pos, mansionsCol.get(j).position) < MARGIN_15) {
                    pos = generatePosition();
                    j = 1;
                }
            }
            man = new Mansion(pos, 3,"Mansion" + mansionsCol.size());
            mansionsCol.add(man);
        }
    }

    /**
     * Generates mansions of size 2.
     */
    public void generateSize2(){
        Point2D pos;
        AMansion man = null;

        for(int i = 0; i < numberOfSize2; i++){
            pos = generatePosition();

            for(int j = 1; j < mansionsCol.size(); j++){ //
                if(getDistance(pos, mansionsCol.get(0).position) < MARGIN_15 || getDistance(pos, mansionsCol.get(j).position) < MARGIN_10) {
                    pos = generatePosition();
                    j = 1;
                }
            }
            man = new Mansion(pos, 2,"Mansion" + mansionsCol.size());
            mansionsCol.add(man);
        }
    }

    /**
     * Generates mansions of size 1 with the same margins as size 2.
     */
    public void generateSize1(){
        Point2D pos;
        AMansion man = null;

        for(int i = 0; i < numberOfSize1; i++){
            pos = generatePosition();

            for(int j = 1; j < mansionsCol.size(); j++){ //
                if(getDistance(pos, mansionsCol.get(0).position) < MARGIN_15 || getDistance(pos, mansionsCol.get(j).position) < MARGIN_10) {
                    pos = generatePosition();
                    j = 1;
                }
            }
            man = new Mansion(pos, 1,"Mansion" + mansionsCol.size());
            mansionsCol.add(man);
        }
    }

	//pak vymazat
	private String pomocnejVypis(){
		return String.format("1: %s\n2: %s\n3: %s\n4: %s\n5: %s\n6: %s", numberOfSize1, numberOfSize2, numberOfSize3, numberOfSize4, numberOfSize5, numberOfSize6);
	}

    /**
     * Returns the List of mansions.
     * @return Collection of mansions.
     */
	public List<AMansion> getMansions(){
        return mansionsCol;
    }
}
