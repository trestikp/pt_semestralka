package Generation;

import java.util.ArrayList;
import java.util.Collections;

public class Path {

	public ArrayList<Integer> nodeIDs;
	public int value;
	
	public Path(int firstNodeID,int secondNodeID, int value ) {
		nodeIDs=new ArrayList<Integer>();
		nodeIDs.add(firstNodeID);
		nodeIDs.add(secondNodeID);
		this.value=value;
	}
	public Path(ArrayList<Integer> nodeIDs, int value) {
		this.nodeIDs=nodeIDs;
		this.value=value;
	}
	
	public void addNode(int ID, int value) {
		//System.out.println("Pøipisuju "+this.toString()+" bod: "+ID);
		nodeIDs.add(ID);
		this.value+=value;
	}
	
	public Path reversePath() {
		ArrayList<Integer> result=new ArrayList<Integer>(nodeIDs);
		Collections.reverse(result);
		return new Path(result,this.value);
	}
	
	@Override
	public String toString() {
		return nodeIDs.toString()+"["+value+"]";
		
	}
	
}
