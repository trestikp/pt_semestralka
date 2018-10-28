package delivery;

import Generation.Mansion;

public class Order {

	public static int NUM_OF_ALL_ORDERS=0;
	
	private Mansion subscriber;
	private int amount;
	public int orderNum=0;
	public Order(Mansion sub, int am) {
		this.subscriber=sub;
		this.amount=am;
		NUM_OF_ALL_ORDERS++;
		this.orderNum=NUM_OF_ALL_ORDERS;
		System.out.println("Objednávka è."+orderNum+" pro "+am+" palet byla doruèena!");
	}
	
	
	public int getAmount() {
		return amount;
	}
	public Mansion getSubscriber() {
		return subscriber;
	}
	
	
}
