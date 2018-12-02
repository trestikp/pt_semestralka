package objects;

import java.awt.geom.Point2D;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import delivery.Order;
import functions.PathFinder;

/**
 * Class of the mansions
 * @author Pavel T�e�t�k and Tom� Ott
 */
public class Mansion extends AMansion{

    /** The length of opening time */
    public static final int OPENED_TIME_IN_MIN=120;
    /** Default start of opening time */
	public static final int DEFAULT_START_OF_OPENING_IN_MIN = 480;
	/** Default end of opening time */
	public static final int DEFAULT_END_OF_OPENING_IN_MIN = 1200;
	/** Limit of orders */
	private static final int ORDER_SIZE_LIMIT = 6;
	
    /** Size of the mansion */
    public int size;

    /** name of the mansion */
    public String name;

    /** iD of mansion */
    public int iD;
  
    /** Opening time of mansion */
    public int openingTimeInMin;

    /** Number of pallets to be delivered */
    private int numOfGoodsToBeDelivered=0;

    private int ordersMadeInADay= 0;

    /** Orders made */
    private Queue<Order> actualOrder = new LinkedList<Order>();
    /** Orders got */
    public LinkedList<Order> ordersDone= new LinkedList<Order>();
    
    /**
     * Constructor filling the position and size of mansion
     * @param pos position
     * @param size size
     */
    public Mansion(Point2D pos, int size, String name, int iD){
        super(pos);
        this.size = size;
        this.name = name;
        this.iD = iD;
    }

    /**
     * Method adds order to the order list
     * @param o order
     */
    public void orderToBeDelivered(Order o) {
    	actualOrder.add(o);
    	//System.out.println(o.getAmount());
    }    

    public void orderCreated(int amount) {
    	ordersMadeInADay++;
    	numOfGoodsToBeDelivered+=amount;
    	
    }
    
    /** Orders delivered */
    public void orderDelivered() {
    	if(actualOrder.isEmpty()){
    	    return;
        }
    	//TODO
    	System.out.println("Objednavka c."+actualOrder.peek().orderNum+" byla dorucena. "
    			+ "Probiha vyklad "+actualOrder.peek().getAmount()+" palet.");
    	ordersDone.add(actualOrder.poll());
    }

    /**
     * Method returns text info about mansion
     * @return text
     */
    public String mansionInfo(){
        String res = "";
        res += "ID: " + iD + "\n";
        res += "Name: " + name + "\n";
        res += "Size: " + size + "\n";
        res += "Can order: " + getCanOrderNumber() + " pallets\n";
        res += "Location: [" + position.getX() + "," + position.getY() +"]\n";
        res += "Opening time: " + minToHour(openingTimeInMin) + "\n";
        res += "Distance to HQ: " + PathFinder.getDistancePaths()[0][iD].getValue() + "\n";
        return res;
    }

    /**
     * Method testing if mansion can order
     * @return true or false
     */
    public boolean canOrder() {
    	if(numOfGoodsToBeDelivered >= ORDER_SIZE_LIMIT) {
    		return false;
    	} else {
            return true;
        }
    }

    /**
     * Method converting time in minutes to normal format
     * @param min
     * @return
     */
    private String minToHour(int min){
        String res = "";
        if(min > 60){
            int hours = min/60;
            int minutes = min%60;
            if(hours < 10){
                res += "0" + hours + ":";
            } else {
                res += hours + ":";
            }
            if(minutes < 10){
                res += "0" + minutes;
            } else {
                res += minutes;
            }
        } else {
            if(min < 10){
                res = /*min + " min.\n";*/"0:0" + min + "\n";
            }else {
                res = /*min + " min.\n";*/"0:" + min + "\n";
            }
        }
        return res;
    }

    /**
     * Resets number of goods mansion can order
     */
    private void resetGoodsLimit() {
    	this.numOfGoodsToBeDelivered=0;
    }

    /**
     * Moving to the next day of simulation
     * @param nodes List of mansions
     */
    public static void nextDay(List<AMansion> nodes) {
    	for(int i=1; i<nodes.size(); i++) {
    		((Mansion)nodes.get(i)).resetGoodsLimit();
            ((Mansion)nodes.get(i)).ordersMadeInADay = 0;
    	}
    }

    /**
     * Method returns number of pallets the mansion can still order
     * @return number of pallets
     */
    public int getCanOrderNumber(){
        int res = size-ordersMadeInADay;
        if(res < 0){
            System.out.println("Error");
            return res;
        } else {
            return res;
        }
    }
    
}