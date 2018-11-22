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
		System.out.println("Objedn�vka �."+orderNum+" pro "+am+" palet byla vytvo�ena!");
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
	
	
}
