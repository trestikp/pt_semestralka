package objects;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Class representing paths
 * @author Pavel Třeštík and Tomáš Ott
 */
public class Path {

	private List<Short> nodeIDs;
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
	public Path(List<Short> cest, short value){
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

	/**
	 * Adds node to the path
	 * @param ID id of node to be added
	 * @param value distance
	 */
	public void addNode(short ID, short value) {
		//System.out.println("P�ipisuju "+this.toString()+" bod: "+iD);
		nodeIDs.add(ID);
		this.value+=value;
	}
	
	/*public Path reversePath() {
		ArrayList<Integer> result=new ArrayList<Integer>(nodeIDs);
		Collections.reverse(result);
		return new Path(result,this.value);
	}*/

	/**
	 * Returns the IDs of path nodes
	 * @return List of nodes
	 */
	public List<Short> getNodeIDs() {
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


	public void setNodeIDs(List<Short> nodeIDs) {
		this.nodeIDs = nodeIDs;
	}

	/**
	 * Returns length of path
	 * @return distance
	 */
	public short getValue() {
		return value;
	}


	public void setValue(short value) {
		this.value = value;
	}


	/**
	 * Returns text representation of the path
	 * @return text
	 */
	@Override
	public String toString() {
		return nodeIDs.toString()+"["+value+"]";
		
	}
}
