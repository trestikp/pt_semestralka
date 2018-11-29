package generation;
 import simulation.Simulation;
 import graphics.Visualization;
 import graphics.Graph;

import java.io.File;
import java.util.List;
import java.util.Scanner;
 /**
 * Class for main testing - propably to be removed
 */
public class mainTest {
     //public static String fromFile = null;
	
	public static void main(String [] args) {
        String fileName = null;
        String fileName2 = null;
        List<AMansion> toPaint;
        int numberToGenerate = 2000; //defaultne se generuje 1000 prvku, pokud se zada vstupni soubor, tak todle ignoruje a nacita ze souboru
        PathGenerator p;
        Generator g;
         //TODO Zeptat se na vstupni soubory namisto prikazove radky
        
        
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
            //FileIO.exportMatrix(p.getDistanceMatrix(), p.getTimeMatrix());
        } else {
            long start = System.nanoTime();
            p = new PathGenerator(new File(fileName2));
            long end = System.nanoTime();
            System.out.println("\n Reading matrix input time: " + (end/1000000 -start/1000000) + "ms\n");
        }
         //FileIO.matrixResults(new File("distanceMatrix.txt"));
       
        /////
        /*
         Visualization v = new Visualization(toPaint);
        v.main(null);
*/
        
         //int[][] distanceMarix = p.getDistanceMatrix();
        //int[][] timeMatrix = p.getTimeMatrix();
         /* tak tohle je moje testovaci prasarna :D
        for(int i = 0; i < 10; i++){
            for(int j = 0; j < 10; j++){
                System.out.print(distanceMarix[i][j] + "    ");
            }
            System.out.println("");
        }
         System.out.println("--------");
         for(int i = 0; i < 10; i++){
            for(int j = 0; j < 10; j++){
                System.out.print(timeMatrix[i][j] + "    ");
            }
            System.out.println("");
        }
        */

       //testovac� prasarna
        // short[][] pole = getTestMatrix();
        
        
        System.out.println("Path generating");
        long start = System.nanoTime();
        PathFinder.pathFinding(p.getDistanceMatrix(),p.getTimeMatrix());
        //  PathFinder.pathFinding(pole, pole);
        long end = System.nanoTime();
        System.out.println("Path generation time: "+ (end/1000000000 -start/1000000000) +"s\\n");
        System.out.println("----------------------------");
 
       //  printBothPathArrays(); // vypis
        
        
        
        
       //TODO udelat uzivatelske rozhrani v cmd a rozdelit main do vice mensich method
        
        
        Scanner sc= new Scanner(System.in);
        System.out.println("Zadejte cas odpovidajici (v ms) odpovidajici jedne minute simulace: ");
        int simStep= sc.nextInt();
        sc.close();
        Simulation sim= new Simulation(toPaint,new Pomocna(p.getDistanceMatrix(),p.getTimeMatrix()),
        								PathFinder.getDistancePaths(),PathFinder.getTimePaths());
        sim.runSimulation(simStep);
       while(true) {
          /* System.out.println("Ukoncit aplikaci? (Y/N)");
           char c=sc.nextLine().charAt(0);
           if(c=='y'||c=='Y') {
        	   sim.endSimulation();
        	   return;
           }*/
       }
        
 	}
	/**
	 * This method serves only for testing correct function of Path generating
	 * @return  distance matrix
	 */
	private static short[][] getTestMatrix(){
			short[][] pole = new short[8][];
	        short[] testPole={0,4,0,0,0,3,0,0};
	       pole[0]=testPole;
	       short[] testPole1={4,0,1,0,2,0,2,0};
	       pole[1]=testPole1;
	       short[]  testPole2={0,1,0,3,0,4,0,4};
	       pole[2]=testPole2;
	       short[]  testPole3={0,0,3,0,0,0,3,0};
	       pole[3]=testPole3;
	       short[] testPole4={0,2,0,0,0,3,0,0};
	       pole[4]=testPole4;
	       short[]  testPole5={3,0,4,0,3,0,5,0};
	       pole[5]=testPole5;
	       short[]  testPole6={0,2,0,3,0,5,0,2};
	       pole[6]=testPole6;
	       short[]  testPole7={0,0,4,0,0,0,2,0};
	       pole[7]=testPole7;
	       return pole;
	}
	
	private static void printBothPathArrays() {
    	printPaths(PathFinder.getDistancePaths());
   		System.out.println();
    	printPaths(PathFinder.getTimePaths());
	}
	
	/**
	 * Method prints Paths
	 * @param paths desired array of Paths
	 */
	private static void printPaths(Path[][] paths) {
		 for(int y=0;y<PathFinder.getTimePaths().length;y++) {
	        	for(int x=0;x<paths.length;x++) {
	        		System.out.print("(");
	        		if(paths[y][x]!=null)
	        			if(paths[y][x].getNodeIDs()!=null)
	        		for(int i=0;i<paths[y][x].getNodeIDs().size();i++) {
	        			if(paths[y][x]==null)System.out.print("0");
	        			else System.out.print(paths[y][x].getNodeIDs().get(i)+",");
	        		}
	        		if(paths[y][x]!=null)System.out.print("["+paths[y][x].getValue()+"]");
	        		System.out.print("),");
	        	}
	        	System.out.println();
	        }
	}
	
}