package Generation;

/**
 * Class for main testing - propably to be removed
 */
public class mainTest {

    public static String fromFile = null;
	
	public static void main(String [] args) {

	    if(args.length > 0){
	        fromFile = args[0];
        }

        Visualization v = new Visualization();
        v.main(null);

		/*
		for(int i = 0; i < 20; i++){
            v = new Visualization();
            v.main(null);
        }*/

	}
	
}
