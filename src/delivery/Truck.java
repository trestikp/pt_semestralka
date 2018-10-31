package delivery;

import java.util.Queue;

import Generation.Path;


public class Truck {
	public static final int MAX_LOAD = 6;
	public static final int UNLOAD_TIME_IN_MIN = 30;
	public static final int COST_PER_KM=25;
	
	
	public static int COUNT_OF_TRUCKS=0;
	
	public int numOfTruck;
	
	private int travelExpenses;
	private int timeOfStartInMin;
	private int timeNeeded;

	private Queue<Order> orders;
	private int actualLoad;
	private int neededTimeInMin;
	
	private boolean isInHQ=true;
	
	

	public Truck() {
		COUNT_OF_TRUCKS++;
		numOfTruck=COUNT_OF_TRUCKS;
	}
	
	
	
	public void addOrder(Order o,int neededTime) {
		orders.add(o);
		actualLoad+=o.getAmount();
	}
	
	public void completeOrder() {
		orders.poll();
		if(orders.isEmpty()) {
			System.out.println("Náklaïák è."+numOfTruck+" se vrací do HQ.");
		}
	}
	
	public void sendOnRoad(int timeOfStartInMin) {
		System.out.print("Náklaïák è."+numOfTruck+" se vydává na cestu. ");
		
		
		this.timeOfStartInMin=timeOfStartInMin;
		//TODO
		/*for(int i=0;i<road.length;i++) {
			travelExpenses+=road[i].value*COST_PER_KM;
		}*/
		
		
		
	}
	
	
	public boolean isLoadable(Order or) {
		if(orders.stream().mapToInt(o-> o.getAmount()).sum()+or.getAmount()>MAX_LOAD)
			return false;
		else return true;
	}
	
	public boolean isAvailable() {
		return isInHQ;
	}
	public boolean isLoaded() {
		return actualLoad>4||orders.size()>3;
	}
	
	
	
	
	
	
	
}
