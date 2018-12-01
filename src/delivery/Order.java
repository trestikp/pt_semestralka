package delivery;

import objects.Mansion;

/**
 * Class of order
 * @author Pavel Třeštík and Tomáš Ott
 */
public class Order {

	/** Number of all orders */
	public static int numberOfAllOrders =0;

	/** Instance of the subscriber of the order */
	private Mansion subscriber;
	/** Amount of pallets ordered */
	private int amount;
	/** Number(id) of order */
	public int orderNum=0;
	/** Expected time of delivery in min */
	private int probableDeliveryInMin=0;

	/**
	 * Constructor of the order
	 * @param sub mansion creating the order
	 * @param am amount of pallets to be ordered
	 */
	public Order(Mansion sub, int am) {
		this.subscriber=sub;
		this.amount=am;
		numberOfAllOrders++;
		this.orderNum= numberOfAllOrders;
		System.out.println("Order n: "+orderNum+" for "+am+" pallet has been made by mansion n: " + subscriber.ID + "!");
	}

	/**
	 * Method sets probable time of delivery
	 * @param timeInMin expected new time
	 */
	public void setProbableTime(int timeInMin) {
		this.probableDeliveryInMin=timeInMin;
	}

	/**
	 * Method returns probable time of delivery
	 * @return time in min
	 */
	public int getProbableTime() {
		return this.probableDeliveryInMin;
	}


	/**
	 * Getter of the amount of pallets
	 * @return amount
	 */
	public int getAmount() {
		return amount;
	}

	/**
	 * Getter of the mansion creating order
	 * @return mansion
	 */
	public Mansion getSubscriber() {
		return subscriber;
	}

	/**
	 * Method to give information about the order in text from.
	 * @return string
	 */
	@Override
	public String toString(){
		return "	Order: " + orderNum + ", by mansion: " + subscriber.ID + ", amount: " + amount + " pallets";
	}
}
