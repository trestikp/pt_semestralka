package Generation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PathFinder {

	
	private static Path[][] distancePaths;
	private static Path[][] timePaths;
	
	private static int[][] pat;
	private static int[][] timepat;
	
	
	public static void pathFinding(int[][] pat, int[][] timepat) {
		PathFinder.pat=pat;
		PathFinder.timepat=timepat;
		for(int y=0;y<pat.length;y++) {
			for(int x=0;x<pat.length;x++) {
				if(pat[y][x]==-1)
				pat[y][x]=Integer.MAX_VALUE;
			}
		}
		Thread threadPathFinder = new Thread(new Runnable() {
			@Override
			public void run() {
					try {
	
						System.out.println("Casova matice");
						PathFinder.timePaths=pathFinding(timepat);
					}
				   catch (Exception ex) {
					   ex.printStackTrace();
				}
			}
		});
		threadPathFinder.start();
		
		
		
		System.out.println("Distancni matice");
		PathFinder.distancePaths=pathFinding(pat);
		
		while(threadPathFinder.isAlive());
	}
	
	
	private static Path[][] pathFinding(int[][] paths ) {
		 final int size= paths.length;
		
		Path[][] result= new Path[size][size];
		for(int y=0;y<size;y++) {
			//okopirovani (pravidlo symetrie)
			for(int x=0;x<y;x++) {
				if(result[x][y]!=null)
				result[y][x]=result[x][y].reversePath();
			}
			// z bodu x do x dojdu za 0 kroku
			result[y][y]=new Path(y,y,0);
			// vytvareni novych cest
			for(int x=y+1;x<size;x++) {
				if(paths[y][x]!=Integer.MAX_VALUE)
					result[y][x]=new Path(y,x,paths[y][x]);
			}
		}
		
		
		////////////////////////////////////////////////////////////
		//cesty z uzlu j
		for(int j=0;j<size;j++) {
			boolean[] done= new boolean[size];
			for(int x=0;x<j;x++) {
				done[x]=true;
			}
			//okopirovani (pravidlo symetrie)  --funguje
			for(int x=0;x<j;x++) {
				if(result[x][j]!=null) {
					if(result[j][x]==null||
					result[j][x].value>result[x][j].value) {
					result[j][x]=result[x][j].reversePath();
					}
				}
			}
			//cesty z vrcholu actualPoint (i prvku uz je hotovo) do ostatn�ch vrchol� --
			int actualPoint=j;
			for(int i=j;i<size;i++){
				
				//TODO
				//hled�n� nejmen��ho ohodnocen� pro dal�� krok
				int minimal=Integer.MAX_VALUE;
				for(int index=j; index<size;index++) {
					if(result[j][index]!=null && !done[index]) {
						if(minimal>result[j][index].value) {
							minimal=result[j][index].value;
							actualPoint=index;
						}
					}
				}
				done[actualPoint]=true;
				
				// u� zn�me nejkrat�� cesty k i prvk�m
				// te� porovnat jestli neexistuje krat�� cesta k x prvku
				for(int x=0;x<size;x++) {
					if(!done[x] && paths[actualPoint][x]!=Integer.MAX_VALUE) {
						if(result[j][x]==null||
								(result[j][actualPoint].value+paths[actualPoint][x])
								<result[j][x].value) {
							Path cest=result[j][actualPoint];
							result[j][x]=new Path(new ArrayList<Integer>(cest.nodeIDs),cest.value);
							result[j][x].addNode(x, paths[actualPoint][x]);
						}
					}
				}	
			}
		}
		
		
		return result;
	}


	public static Path[][] getDistancePaths() {
		return distancePaths;
	}



	public static Path[][] getTimePaths() {
		return timePaths;
	}

	
	
	
	
	
}
