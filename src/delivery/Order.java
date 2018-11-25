package delivery;

import generation.Mansion;

public class Order {

	public static int NUM_OF_ALL_ORDERS=0;
	
	private Mansion subscriber;
	private int amount;
	public int orderNum=0;
	
	private int probableDeliveryInMin=0;
	
	
	public Order(Mansion sub, int am) {
		this.subscriber=sub;
		this.amount=am;
		NUM_OF_ALL_ORDERS++;
		this.orderNum=NUM_OF_ALL_ORDERS;
		System.out.println("Order n: "+orderNum+" for "+am+" pallet has been made by mansion n: " + subscriber.ID + "!");
	}
	
	public void setProbableTime(int timeInMin) {
		this.probableDeliveryInMin=timeInMin;
	}
	public int getProbableTime() {
		return this.probableDeliveryInMin;
	}
	
	
	
	public int getAmount() {
		return amount;
	}
	public Mansion getSubscriber() {
		return subscriber;
	}

	@Override
	public String toString(){
		return "	Order: " + orderNum + ", by mansion: " + subscriber.ID + ", amount: " + amount + " pallets";
	}
}
