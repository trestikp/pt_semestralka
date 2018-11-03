package delivery;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

import Generation.Path;
import simulation.Simulation;


public class Truck {
	public static final int MAX_LOAD = 6;
	public static final int UNLOAD_TIME_IN_MIN = 30;
	public static final int COST_PER_KM=25;
	public static final int MINUTES_NEEDED_FOR_100KM=70;
	
	public static Queue<Truck> launchableTrucks= new LinkedList<Truck>();
	public static LinkedList<Truck> trucksOnRoad= new LinkedList<Truck>() ;
	
	public static int count_of_trucks=0;
	
	public int numOfTruck;
	
	private int totalKm=0;
	private int totalTime=0;
	
	
	private int travelExpenses=0;
	private int momentalKM=0;
	private int neededTimeInMin=0;
	
	private int timeOfStartInMin=0;

	private LinkedList<Order> orders= new LinkedList<Order>();
	private int actualLoad;
	
	//private boolean isInHQ=true;
	
	
	//TODO kalkulace souøadnic
	//public Point2D coords
	

	public Truck() {
		count_of_trucks++;
		numOfTruck=count_of_trucks;
	}
	
	
	
	public void addOrder(Order o) {
		if(o==null)return;
		orders.add(o);
		actualLoad+=o.getAmount();
	}
	
	public void completeOrder() {
		int load=orders.poll().getAmount();
		if(orders.isEmpty()) {
			System.out.println("Náklaïák è."+numOfTruck+" vyložil "+load+" palet.");
		}
	}
	
	public void sendOnRoad(int timeOfStartInMin) {
		if(orders.size()==0) {
			System.out.println("Nelze vyslat na cestu bez objednavek!");
			return;
		}
		
		//Vypocitat potrebny cas
		this.timeOfStartInMin=timeOfStartInMin;
		neededTimeInMin= actualLoad*UNLOAD_TIME_IN_MIN*2;
		Order o=orders.peek();
		Order o2=null;
		momentalKM+= calcKM(0, o.getSubscriber().ID);
		for(Iterator<Order> it=orders.iterator();it.hasNext();) {
			o2=it.next();
			momentalKM+=calcKM(o.getSubscriber().ID, o2.getSubscriber().ID);
			o=o2;
		}
		
		neededTimeInMin+=((double)momentalKM/100)*MINUTES_NEEDED_FOR_100KM;
		travelExpenses+=momentalKM*COST_PER_KM;
		
		totalKm+=momentalKM;
		totalTime+=neededTimeInMin;
		
		System.out.println("Nakladak è."+numOfTruck+" zacina nakladat "+actualLoad+" palet.");
		//TODO nejdriv nalozit naklad
		/*for(int i=0;i<road.length;i++) {
			travelExpenses+=road[i].value*COST_PER_KM;
		}*/
		
		
		
	}
	private int calcKM(int pointA, int pointB) {
		return (int)(((double)Simulation.path[pointA][pointB].value));
	}	
	
	
	
	private void returnToHQ() {
		neededTimeInMin=0;
		timeOfStartInMin=-1;
		actualLoad=0;
		launchableTrucks.add(this);
	}
	
	/*public boolean isLoadable(Order or) {
		if(actualLoad+or.getAmount()>MAX_LOAD)
			return false;
		else return true;
	}*/
	
	/*public boolean isAvailable() {
		return isInHQ;
	}*/
	
	public boolean isLoaded() {
		return actualLoad>4||orders.size()>3;
	}
	
	
	

	public static void checkStateOnRoad(int actualTimeInMin) {

		
		//TODO
		
		for(Truck t: trucksOnRoad) {
			if(t.timeOfStartInMin+t.neededTimeInMin>=actualTimeInMin)t.returnToHQ();
			
			int time= actualTimeInMin-t.timeOfStartInMin;
			//TODO if()
			
			
		}
		
		
		
	}
	
	
	
}
