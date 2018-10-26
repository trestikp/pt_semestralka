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
        String fileName2 = null;
        List<AMansion> toPaint;
        int numberToGenerate = 2000; //defaultne se generuje 2000 prvku, pokud se zada vstupni soubor, tak todle ignoruje a nacita ze souboru
        PathGenerator p;
        Generator g;

	    if(args.length >= 1){
	        numberToGenerate = Integer.parseInt(args[0]);
        }
        if(args.length >= 2){
            fileName = args[1];
        }
        if(args.length >= 3){
            fileName2 = args[2];
        }

        if(fileName == null){
            long start = System.nanoTime();
            g = new Generator(numberToGenerate);
            long end = System.nanoTime();
            System.out.println("\n Generating mansions time: " + (end/1000000 -start/1000000) + "ms\n");
            toPaint = g.getMansions();
            //FileIO.exportToFile(toPaint);
        } else {
            long start = System.nanoTime();
            toPaint = FileIO.importFromFile(new File(fileName));
            long end = System.nanoTime();
            System.out.println("\n Reading mansions input time: " + (end/1000000 -start/1000000) + "ms\n");
        }

        if(fileName2 == null){
            long start = System.nanoTime();
            p = new PathGenerator(toPaint);
            long end = System.nanoTime();
            System.out.println("\n Generating matrix time: " + (end/1000000 -start/1000000) + "ms\n");
            //FileIO.exportMatrix(p.getMatrix());
        } else {
            long start = System.nanoTime();
            p = new PathGenerator(new File(fileName2));
            long end = System.nanoTime();
            System.out.println("\n Reading matrix input time: " + (end/1000000 -start/1000000) + "ms\n");
        }

        //FileIO.matrixResults(new File("distanceMatrix.txt"));

        Visualization v = new Visualization(toPaint);
        v.main(null);

		/*
		for(int i = 0; i < 20; i++){
            v = new Visualization();
            v.main(null);
        }*/

	}
	
}
