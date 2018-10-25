package Generation;

import java.io.File;
import java.util.List;

/**
 * Class for main testing - propably to be removed
 */
public class mainTest {

    //public static String fromFile = null;
	
	public static void main(String [] args) {
        String fileName = null;
        List<AMansion> toPaint;
	    /*
	    if(args.length > 0){
	        fromFile = args[0];
        }*/

	    if(args.length > 0){
	        fileName = args[0];
        }

        if(fileName == null){
            long start = System.nanoTime();
            Generator g = new Generator(2000);
            long end = System.nanoTime();
            System.out.println("\n Time: " + (end/1000000 -start/1000000) + "ms\n");
            toPaint = g.getMansions();
            //FileIO.exportToFile(toPaint);
        } else {
            long start = System.nanoTime();
            toPaint = FileIO.importFromFile(new File(fileName));
            long end = System.nanoTime();
            System.out.println("\n Time: " + (end/1000000 -start/1000000) + "ms\n");
        }



        Visualization v = new Visualization(toPaint);
        v.main(null);

		/*
		for(int i = 0; i < 20; i++){
            v = new Visualization();
            v.main(null);
        }*/

	}
	
}
