package generation;

import java.util.ArrayList;
import java.util.Collections;

public class Path {

	private ArrayList<Short> nodeIDs;
	private short value;
	private boolean reversed=false;
	
	/**
	 * Absolutely new Path
	 * @param firstNodeID
	 * @param secondNodeID
	 * @param value
	 */
	public Path(short firstNodeID,short secondNodeID, short value ) {
		nodeIDs=new ArrayList<Short>();
		nodeIDs.add(firstNodeID);
		nodeIDs.add(secondNodeID);
		this.value=value;
	}
	/**
	 * New longer path
	 * @param cest
	 * @param value
	 */
	public Path(ArrayList<Short> cest, short value){
		this.nodeIDs=cest;
		this.value=value;
	}
	
	
	/**
	 * reversed Path
	 * @param pat
	 */
	public Path(Path pat) {
		this.nodeIDs=pat.nodeIDs;
		this.value=pat.value;
		this.reversed=true;
	}
	
	public void addNode(short ID, short value) {
		//System.out.println("P�ipisuju "+this.toString()+" bod: "+ID);
		nodeIDs.add(ID);
		this.value+=value;
	}
	
	/*public Path reversePath() {
		ArrayList<Integer> result=new ArrayList<Integer>(nodeIDs);
		Collections.reverse(result);
		return new Path(result,this.value);
	}*/
	

	public ArrayList<Short> getNodeIDs() {
		if(reversed) {
			//TODO mozne zpomaleni
			ArrayList<Short> result=new ArrayList<Short>(nodeIDs);
			Collections.reverse(result);
			return result;
		}
		else {
			return nodeIDs;
		}
	}
	public void setNodeIDs(ArrayList<Short> nodeIDs) {
		this.nodeIDs = nodeIDs;
	}
	public short getValue() {
		return value;
	}
	public void setValue(short value) {
		this.value = value;
	}
	
	
	
	@Override
	public String toString() {
		return nodeIDs.toString()+"["+value+"]";
		
	}
}
