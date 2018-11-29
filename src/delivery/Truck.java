package delivery;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

import simulation.Simulation;


public class Truck {
	public static final int MAX_LOAD = 6;
	public static final int UNLOAD_TIME_IN_MIN = 30;
	public static final int COST_PER_KM=25;
	
	public static Queue<Truck> launchableTrucks= new LinkedList<Truck>();
	public static ArrayList<Truck> trucksOnRoad= new ArrayList<>() ;
	
	public static int count_of_trucks=0;
	
	
	public int numOfTruck;
	
	//TODO dodelat pro statistiky
	private int totalTime=0;
	private int totalKm=0;
	private int totalLoad=0;
	
	private int momentalKM=0;
	private int neededTimeInMin=0;
	private int timeOfStartInMin=0;
	private int actualLoad;
	
	private LinkedList<Order> orders= new LinkedList<Order>();
	
	//private boolean isInHQ=true;
	
	

	public Truck() {
		count_of_trucks++;
		numOfTruck=count_of_trucks;
		Truck.launchableTrucks.add(this);
	}
	
	
	
	public void addOrder(Order o) throws Exception {
		if(o==null)throw new Exception("Null pointer on adding Orders");
		if(o.getAmount()<1)throw new Exception("Cannot add order with zero amount");
		if(orders.isEmpty()) {
			momentalKM=calcKM(0, o.getSubscriber().ID);
		}
		else {
			momentalKM+=calcKM(orders.getLast().getSubscriber().ID, o.getSubscriber().ID);
		}
		orders.add(o);
		actualLoad+=o.getAmount();
		o.getSubscriber().orderToBeDelivered(o);
		
	}
	
	public void completeOrder() throws Exception {
		if(!orders.isEmpty()) {
			Order o= orders.poll();
			int load=o.getAmount();
			this.actualLoad-=load;
			System.out.println("Truck n: "+numOfTruck+" unloaded "+load+
					" pallet in mansion n: "+o.getSubscriber().ID+".");
		}
		else {
			throw new Exception("NO ORDERS LEFT!");
		}
	}
	
	public void sendOnRoad(int timeOfStartInMin) {
		if(orders.size()==0 || timeOfStartInMin <= 0) {
			System.out.println("Can't send on road without orders or with strange time!");
			return;
		}
		
		//Vypocitat potrebny cas
		this.timeOfStartInMin=timeOfStartInMin;
		neededTimeInMin= actualLoad*UNLOAD_TIME_IN_MIN*2;
		
		neededTimeInMin+=((double)momentalKM/100);
		
		//STATISTIKY
		totalKm+=momentalKM;
		totalTime+=neededTimeInMin;
		totalLoad+=actualLoad;
		
		Truck t= Truck.launchableTrucks.poll();
		Truck.trucksOnRoad.add(t);
		
		System.out.println("Truck n: "+numOfTruck+" is starting to load "+actualLoad+" pallet(s).");
		
		
		
		
	}
	private int calcKM(int pointA, int pointB) {
		return (int)(((double)Simulation.distancePath[pointA][pointB].getValue()));
	}
	
	
	
	private void returnToHQ() {
		System.out.println("Truck n: "+numOfTruck+" has returned to HQ!");
		neededTimeInMin=0;
		timeOfStartInMin=-1;
		actualLoad=0;
		
		trucksOnRoad.remove(this);
		launchableTrucks.add(this);
	}

	public boolean hasOrder() {
		return actualLoad>0||orders.size()>0;
	}
	
	public boolean canLoad(int size) {
		return this.actualLoad+size<7;
	}
	
	

	public static void checkStateOnRoad(int actualTimeInMin) {

		
		//TODO
			for(int i=0; i < trucksOnRoad.size(); i++) {
				Truck t= trucksOnRoad.get(i);
				if(!t.orders.isEmpty() &&
						actualTimeInMin > t.orders.peek().getProbableTime()){
					try {
						t.completeOrder();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				else if(t.timeOfStartInMin+t.neededTimeInMin<=actualTimeInMin) {
					t.returnToHQ();
				}
				
			}
	}
	
	/////STATISTICS
	
	public int getTotalKm() {
		return totalKm;
	}
	public int getTotalTimeOnRoad() {
		return totalTime;
	}
	public int getTotalLoad() {
		return totalLoad;
	}
	public int getTotalExpense() {
		return this.getTotalKm()*COST_PER_KM;
	}
	
	
	public static int gettimeOfAllTrucksOnRoad() {
	int result=0;
		
		for(Truck t: launchableTrucks) {
			result+= t.getTotalTimeOnRoad();
		}
		for(Truck t: trucksOnRoad) {
			result+= t.getTotalTimeOnRoad();
		}
		
		return result;
	}
	
	public static int getmileageOfAllTrucks() {
		int result=0;
		
		for(Truck t: launchableTrucks) {
			result+= t.getTotalKm();
		}
		for(Truck t: trucksOnRoad) {
			result+= t.getTotalKm();
		}
		
		return result;
	}
	public static int getTotalLoadOfAllTrucks() {
		int result=0;
		
		for(Truck t: launchableTrucks) {
			result+= t.getTotalLoad();
		}
		for(Truck t: trucksOnRoad) {
			result+= t.getTotalLoad();
		}
		
		return result;
	}
	
	
	//// NEXT DAY
	
	
	public static void nextDay() {
		if(!launchableTrucks.isEmpty()) {
			System.out.println("Trucks are not in the HQ!!!");
		
			for(Truck t: trucksOnRoad) {
				t.returnToHQ();
			}
		}
		for(Truck t: launchableTrucks) {
			t.resetStats();
		}
	}
	
	
	private void resetStats() {

		totalTime=0;
		totalKm=0;
		totalLoad=0;
		
		
		momentalKM = 0;
		neededTimeInMin = 0;
		timeOfStartInMin = 0;
		actualLoad = 0;
		
		if(!orders.isEmpty()) {
			orders.removeFirst();
		}
	}
	
	/////////////////////STRING

	private String ordersToString(){
		String res = "";
		Iterator<Order> it = orders.iterator();
		while (it.hasNext()){
			res += it.next().toString()+"\n";
		}

		return res;
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
				res += "0" + minutes + "\n";
			} else {
				res += minutes + "\n";
			}
		} else {
			res = min + " min.\n";
		}
		return res;
	}
	
	public String infoAboutTruck(){
		String res = "Truck id: " + this.numOfTruck + "\n";
		res += "Load : " + actualLoad + " pallets\n";
		res += "Orders: " + ordersToString() + "\n";
		res += "Time needed: " + minToHour(neededTimeInMin);
		res += "Time of start: " + minToHour(timeOfStartInMin);
		res += "Total distance: " + momentalKM + " km\n";
		res += "Travel expenses: " + momentalKM*COST_PER_KM + " Kc\n";

		return res;
	}
	
}
