package generation;

import java.awt.geom.Point2D;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import delivery.Order;

/**
 * The mansions
 */
public class Mansion extends AMansion{

	 public static final int OPENED_TIME_IN_MIN=120;
	 public static final int DEFAULT_START_OF_OPENING_IN_MIN = 480;
	 public static final int DEFAULT_END_OF_OPENING_IN_MIN = 1200;
	 public static final int ORDER_SIZE_LIMIT = 6;
	
    /** Size of the mansion */
    public int size;

    //DODELAT!!!
    /** name of the mansion */
    public String name;

    public int ID;
  
    
    public int openingTimeInMin;
    
    private int numOfGoodsToBeDelivered=0;
    
    
    private Queue<Order> actualOrder = new LinkedList<Order>();
    public LinkedList<Order> ordersDone= new LinkedList<Order>();
    
    /**
     * Constructor filling the position and size of mansion
     * @param pos position
     * @param size size
     */
    public Mansion(Point2D pos, int size, String name, int ID){
        super(pos);
        this.size = size;
        this.name = name;
        
        //n�hrada
        this.ID = ID;
    }
    
    
    public void orderToBeDelivered(Order o) {
    	actualOrder.add(o);
    	
    }    
    
    public void orderDelivered() {
    	if(actualOrder.isEmpty())return;
    	//TODO
    	System.out.println("Objednavka c."+actualOrder.peek().orderNum+" byla dorucena. "
    			+ "Probiha vyklad "+actualOrder.peek().getAmount()+" palet.");
    	ordersDone.add(actualOrder.poll());
    }

    public String mansionInfo(){
        String res = "";
        res += "ID: " + ID + "\n";
        res += "Name: " + name + "\n";
        res += "Location: [" + position.getX() + "," + position.getY() +"]\n";
        res += "Opening time: " + minToHour(openingTimeInMin) + "\n";
        res += "Distance to HQ: " + PathFinder.getDistancePaths()[0][ID].getValue() + "\n";
        return res;
    }

    public boolean canOrder() {
    	if(numOfGoodsToBeDelivered >= ORDER_SIZE_LIMIT) {
    		return false;
    	}
    	return true;
    }
    
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
            res = min + " min.";
        }
        return res;
    }
    
    private void resetGoodsLimit() {
    	this.numOfGoodsToBeDelivered=0;
    }
    
    public static void nextDay(List<AMansion> nodes) {
    	for(int i=1; i<nodes.size(); i++) {
    		((Mansion)nodes.get(i)).resetGoodsLimit();
    	}
    }
    
    
}
