package functions;
 import java.util.ArrayList;

import objects.Path;

/**
 * Class finding paths of the graph
 * @author Pavel Trestik a Tomas Ott
 */
public class PathFinder {
 	
	private static Path[][] distancePaths;
	private static Path[][] timePaths;


	/**
	 * The main method finding the paths
	 * @param pat distance matrix
	 * @param timepat time matrix
	 */
	public static void pathFinding(short[][] pat, short[][] timepat) {
		
		/*for(int y=0;y<pat.length;y++) {
			for(int x=0;x<pat.length;x++) {
				if(pat[y][x]==-1)
				pat[y][x]=Integer.MAX_VALUE;
			}
		}*/
		Thread threadPathFinder = new Thread(new Runnable() {
			@Override
			public void run() {
					try {
	
						System.out.println("Time paths");
						PathFinder.timePaths=pathFinding(timepat);
						System.out.println("Time Paths done");
					}
				   catch (Exception ex) {
					   ex.printStackTrace();
				}
			}
		});
		threadPathFinder.start();
		
		System.out.println("Distance paths");
		PathFinder.distancePaths=pathFinding(pat);
		System.out.println("Distance Paths done");
		try {
			while(threadPathFinder.isAlive()) {
				
					Thread.sleep(50);
				
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	
	private static Path[][] pathFinding(short[][] paths ) {
		 final int size= paths.length;
		
		 Path[][] result= PathsInitialization(paths);
		
		
		////////////////////////////////////////////////////////////
		//cesty z uzlu j
		for(short j=0;j<size;j++) {
			boolean[] done= new boolean[size];
			for(int x=0;x<j;x++) {
				done[x]=true;
			}
			//okopirovani (pravidlo symetrie)  --funguje
			for(short x=0;x<j;x++) {
				if(result[x][j]!=null) {
					if(result[j][x]==null||
					result[j][x].getValue()>result[x][j].getValue()) {
					result[j][x]=new Path(result[x][j]);
					}
				}
			}
			//cesty z vrcholu actualPoint (i prvku uz je hotovo) do ostatnich vrcholu --
			dijkstraAlgorithm(result, done, j, paths);
		}
		
		
		return result;
	}
	
	
	private static Path[][] PathsInitialization(short[][] paths) {
		final int size= paths.length;
		Path[][] result= new Path[size][size];
		
		
		for(short y=0;y<size;y++) {
			//okopirovani (pravidlo symetrie)
			for(short x=0;x<y;x++) {
				if(result[x][y]!=null) {
					//reversed Path
					result[y][x]=new Path(result[x][y]);
				}
			}
			// z bodu x do x dojdu za 0 kroku
			result[y][y]=new Path(y,y,(short)0);
			// vytvareni novych cest
			for(short x=(short) (y+1);x<size;x++) {
				if(paths[y][x]!=0)
					result[y][x]=new Path(y,x,paths[y][x]);
			}
		}
		return result;
		
		
	}
	
	private static void dijkstraAlgorithm(Path[][] result, boolean[] done, short j, short[][] paths) {
		final int size= result.length;
		int actualPoint=j;
		for(short i=j;i<size;i++){
			
			//TODO
			//hledani nejmensiho ohodnoceni pro dalsi krok
			short minimal=0;
			for(short index=j; index<size;index++) {
				if(result[j][index]!=null && !done[index]) {
					if(minimal==0 || minimal>result[j][index].getValue()) {
						minimal=result[j][index].getValue();
						actualPoint=index;
					}
				}
			}
			done[actualPoint]=true;
			
			// uz zname nejkratsi cestu 
			// ted porovnat jestli neexistuje kratsi cesta k x prvku
			for(short x=0;x<size;x++) {
				if(!done[x] && paths[actualPoint][x]!=0) {
					if(result[j][x]==null|| //(result[j][x].getValue()>0 &&
							(result[j][actualPoint].getValue()+paths[actualPoint][x])
							<result[j][x].getValue()) {
						Path cest=result[j][actualPoint];
						result[j][x]=new Path(new ArrayList<Short>(cest.getNodeIDs()),cest.getValue());
						result[j][x].addNode(x, paths[actualPoint][x]);
					}
				}
			}	
		}
	}

	/**
	 * Distance paths getter
	 * @return distance paths matrix
	 */
 	public static Path[][] getDistancePaths() {
		return distancePaths;
	}

	/**
	 * Time paths getter
	 * @return time paths matrix
	 */
 	public static Path[][] getTimePaths() {
		return timePaths;
	}
 	
	
	
	
	
}