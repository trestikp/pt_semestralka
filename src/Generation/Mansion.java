package Generation;

import java.awt.geom.Point2D;
import java.util.LinkedList;
import java.util.Queue;

import delivery.Order;

/**
 * The mansions
 */
public class Mansion extends AMansion{

	 public static final int OPENED_TIME_IN_MIN=120;
	 public static final int DEFAULT_START_OF_OPENING_IN_SEC = 480*60;
	 public static final int DEFAULT_END_OF_OPENING_IN_SEC = 1200*60;

	
    /** Size of the mansion */
    public int size;

    //DODELAT!!!
    /** name of the mansion */
    public String name;

    public int ID;
  
    
    public int openingTimeInMin;
    
    public int numOfDeliveredGoods=0;
    
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
    	numOfDeliveredGoods+=actualOrder.peek().getAmount();
    	ordersDone.add(actualOrder.poll());
    }
    
}
