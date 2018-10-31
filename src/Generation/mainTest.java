package Generation;

import java.io.File;
import java.util.List;
import java.util.Scanner;

import simulation.Simulation;

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


		/*
		for(int i = 0; i < 20; i++){
            v = new Visualization();
            v.main(null);
        }*/
        

        //testovac� pras�rna
        int[][] pole = new int[8][];
        int[] testPole={0,4,-1,-1,-1,3,-1,-1};
        pole[0]=testPole;
        int[] testPole1={4,0,1,-1,2,-1,2,-1};
        pole[1]=testPole1;
        int[]  testPole2={-1,1,0,3,-1,4,-1,4};
        pole[2]=testPole2;
        int[]  testPole3={-1,-1,3,0,-1,-1,3,-1};
        pole[3]=testPole3;
        int[] testPole4={-1,2,-1,-1,0,3,-1,-1};
        pole[4]=testPole4;
        int[]  testPole5={3,-1,4,-1,3,0,5,-1};
        pole[5]=testPole5;
        int[]  testPole6={-1,2,-1,3,-1,5,0,2};
        pole[6]=testPole6;
        int[]  testPole7={-1,-1,4,-1,-1,-1,2,0};
        pole[7]=testPole7;

        
        System.out.println("Path generating");
        long start = System.nanoTime();
        PathFinder fn= new PathFinder(pole);
        Path[][] paths=fn.pathFinding();
        long end = System.nanoTime();
        

        for(int y=0;y<paths.length;y++) {
        	for(int x=0;x<paths.length;x++) {
        		System.out.print("(");
        		if(paths[y][x]!=null)
        			if(paths[y][x].nodeIDs!=null)
        		for(int i=0;i<paths[y][x].nodeIDs.size();i++) {
        			if(paths[y][x]==null)System.out.print("0");
        			else System.out.print(paths[y][x].nodeIDs.get(i)+",");
        		}
        		if(paths[y][x]!=null)System.out.print("["+paths[y][x].value+"]");
        		System.out.print("),");
        	}
        	System.out.println();
        }
        
        
        
        System.out.println("Path generation time: "+ (end/1000000000 -start/1000000000) +"s\\n");
        System.out.println("----------------------------");
        
        Scanner sc= new Scanner(System.in);
        /*
        System.out.println("Zadejte �as (v ms) odpov�daj�c� jedn� minut� simulace: ");
        int simStep= sc.nextInt();
        
        
        Simulation sim= new Simulation(toPaint,p.getMatrix(),simStep);
        sim.startSimulation();
        while(true) {
        	
        	
        	
        	
        }*/
        

        Visualization v = new Visualization(toPaint);
        v.main(null);

	}
	
}
