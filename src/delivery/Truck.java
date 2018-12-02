package delivery;

import java.util.*;

import graphics.GUI;
import simulation.Simulation;

/**
 * Class of the Truck for deliveries
 * @author Pavel Třeštík and Tomáš Ott
 */
public class Truck {
	/** Constant of maximum allowed load */
	public static final int MAX_LOAD = 6;
	/** Constant of time needed to unload 1 pallet */
	public static final int UNLOAD_TIME_IN_MIN = 30;
	/** Constant cost per km */
	private static final int COST_PER_KM=25;

	/** Queue of the trucks that are in HQ ready to launch */
	public static Queue<Truck> launchableTrucks= new LinkedList<Truck>();
	/** List of the trucks that are on road */
	public static List<Truck> trucksOnRoad= new ArrayList<>() ;

	/** Total number of trucks */
	private static int count_of_trucks=0;
	
	/** Number of truck (id) */
	public int numOfTruck;
	
	//TODO dodelat pro statistiky
	/** Total time the truck spends on road in a day */
	private int totalTime=0;
	/** Total km the truck travels in a day */
	private int totalKm=0;
	/** Total count of pallets the truck delivers in a day */
	private int totalLoad=0;

	/** Total time the truck spends on road during whole simulation */
	private int totalRunTime = 0;
	/** Total km the truck travels during whole simulation */
	private int totalRunKm = 0;
	/** Total count of pallets truck delivers during whole simulation */
	private int totalRunLoad = 0;

	/** Length of current path */
	private int momentalKM=0;
	/** Time needed to travel designated path */
	private int neededTimeInMin=0;
	/** Time when the trucks sets on road */
	private int timeOfStartInMin=0;
	/** Load of the truck */
	private int actualLoad;

	
	/** Orders the truck is delivering */
	private LinkedList<Order> orders= new LinkedList<Order>();
	
	//private boolean isInHQ=true;


	/**
	 * Parameterless constructor, creating instance of a truck
	 */
	public Truck() {
		count_of_trucks++;
		numOfTruck=count_of_trucks;
		Truck.launchableTrucks.add(this);
	}


	/**
	 * Method adding order to a truck
	 * @param o order that is to be added
	 * @throws Exception
	 */
	public void addOrder(Order o) throws Exception {
		if(o==null){
			throw new Exception("Null pointer on adding Orders");
		}
		if(o.getAmount()<1){
			throw new Exception("Cannot add order with zero amount");
		}
		if(orders.isEmpty()) {
			momentalKM=calcKM(0, o.getSubscriber().iD);
		}
		else {
			momentalKM+=calcKM(orders.getLast().getSubscriber().iD, o.getSubscriber().iD);
		}
		orders.add(o);
		actualLoad+=o.getAmount();
		o.getSubscriber().orderToBeDelivered(o);
		
	}

	/**
	 * Method completes order
	 * @throws Exception
	 */
	private void completeOrder() throws Exception {
		if(!orders.isEmpty()) {
			Order o= orders.poll();
			int load=o.getAmount();
			this.actualLoad-=load;
			System.out.println("Truck n: "+numOfTruck+" unloaded "+load+
					" pallet in mansion n: "+o.getSubscriber().iD +".");
		}
		else {
			throw new Exception("NO ORDERS LEFT!");
		}
	}

	/**
	 * Method sends truck on road
	 * @param timeOfStartInMin time when the truck sets out
	 */
	public void sendOnRoad(int timeOfStartInMin) {
		if(orders.size()==0 || timeOfStartInMin <= 0 ) {
			System.out.println("Can't send on road without orders or with strange time!");
			return;
		}
		
		
		//Vypocitat potrebny cas
		this.timeOfStartInMin=timeOfStartInMin;
		neededTimeInMin= actualLoad*UNLOAD_TIME_IN_MIN*2;
		
		neededTimeInMin+=(int)((double)momentalKM/100.0);
		
		//STATISTIKY
		totalKm+=momentalKM;
		totalTime+=neededTimeInMin;
		totalLoad+=actualLoad;
		
		Truck t= Truck.launchableTrucks.poll();
		Truck.trucksOnRoad.add(t);
		
		System.out.println("Truck n: "+numOfTruck+" is starting to load "+actualLoad+" pallet(s).");
	}

	/**
	 * Method to calculate distance of the path
	 * @param pointA point of start
	 * @param pointB point of end
	 * @return distance of the path
	 */
	private int calcKM(int pointA, int pointB) {
		return (int)(((double)GUI.sim.distancePath[pointA][pointB].getValue()));
	}

	/**
	 * Method returning the truck back to HQ
	 */
	private void returnToHQ() {
		System.out.println("Truck n: "+numOfTruck+" has returned to HQ!");
		neededTimeInMin=0;
		timeOfStartInMin=-1;
		actualLoad=0;
		orders = new LinkedList<Order>();
		trucksOnRoad.remove(this);
		launchableTrucks.add(this);
	}

	/**
	 * Method used for finding out if truck has any orders to deliver
	 * @return true or false
	 */
	public boolean hasOrder() {
		return actualLoad>0||orders.size()>0;
	}

	/**
	 * Method tests if truck can load pallets
	 * @param size number of pallets to be loaded
	 * @return true or false
	 */
	public boolean canLoad(int size) {
		return this.actualLoad+size<7;
	}


	/**
	 * Method finds out state of trucks on road
	 * @param actualTimeInMin time of simulation
	 */
	public static void checkStateOnRoad(int actualTimeInMin) {

		
		//TODO
			for(int i=trucksOnRoad.size()-1; i >= 0; i--) {
			
				Truck t= trucksOnRoad.get(i);
				if(!t.orders.isEmpty() &&
						actualTimeInMin > t.orders.peek().getProbableTime()){
					try {
						//System.out.println(t.actualLoad);
						t.completeOrder();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				else if(t.timeOfStartInMin+t.neededTimeInMin<=actualTimeInMin || actualTimeInMin <   Simulation.START_OF_ORDERING_IN_MIN  ) {
					t.returnToHQ();
				}
				
			}
	}

	/**
	 * Getter for orders
	 * @return LinkedList of orders
	 */
	public LinkedList<Order> getOrders(){
		return orders;
	}
	
	/////STATISTICS

	/**
	 * Getter of km traveled in a day
	 * @return km
	 */
	private int getTotalKm() {
		return totalKm;
	}

	/**
	 * Getter for total time on road in a day
	 * @return time in min
	 */
	private int getTotalTimeOnRoad() {
		return totalTime;
	}

	/**
	 * Getter for total pallets delivered in a day
	 * @return num of pallets
	 */
	private int getTotalLoad() {
		return totalLoad;
	}

	/**
	 * Getter for total cost per day
	 * @return cost in kc
	 */
	private int getTotalExpense() {
		return this.getTotalKm()*COST_PER_KM;
	}

	/**
	 * Getter for time spent on road of all trucks
	 * @return time
	 */
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

	/**
	 * Getter for distance traveled by all trucks
	 * @return distance in km
	 */
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

	/**
	 * Getter for all pallets delivered by all trucks
	 * @return number of pallets
	 */
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

	/**
	 * Getter for total cost of all trucks
	 * @return cost in kc
	 */
	public static int getTotalMoneyOfAll(){
		int res = 0;

		for(Truck t: launchableTrucks){
			res += t.getTotalExpense();
		}
		for(Truck t: trucksOnRoad){
			res += t.getTotalExpense();
		}

		return res;
	}
	
	
	//// NEXT DAY

	/**
	 * Method moving the trucks to the next day of simulation
	 */
	public static void nextDay() {
		if(!launchableTrucks.isEmpty()) {
			System.out.println("Trucks are not in the HQ!!!");
		
			/*for(Truck t: trucksOnRoad) {
				t.returnToHQ();
			}*/
		}

		addToTotalStats();
		resetAllStats();
	}

	/**
	 * Write statistics when similation is ended
	 */
	public static void addToTotalStats(){
		for(Truck t: launchableTrucks) {
			t.totalRunKm += t.getTotalKm();
			t.totalRunLoad += t.getTotalLoad();
			t.totalRunTime += t.getTotalTimeOnRoad();
			//t.resetStats();
		}
		for(Truck t: trucksOnRoad) {
			t.totalRunKm += t.getTotalKm();
			t.totalRunLoad += t.getTotalLoad();
			t.totalRunTime += t.getTotalTimeOnRoad();
			//t.resetStats();
		}
	}

	private static void resetAllStats(){
		for(Truck t: launchableTrucks) {
			t.resetStats();
		}
		for(Truck t: trucksOnRoad) {
			t.resetStats();
		}
	}

	/**
	 * Resets daily stats of the simulation
	 */
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

	/**
	 * Returns information about all orders
	 * @return string information
	 */
	private String ordersToString(){
		String res = "";
		Iterator<Order> it = orders.iterator();
		while (it.hasNext()){
			res += it.next().toString()+"\n";
		}

		return res;
	}

	/**
	 * Supporting method converting time in minute to normal clock format
	 * @param min minutes
	 * @return visible format
	 */
	private String minToHour(int min){
		String res = "";
		if(min >= 60){
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
			if(min < 10){
				res = /*min + " min.\n";*/"0:0" + min + "\n";
			}else {
				res = /*min + " min.\n";*/"0:" + min + "\n";
			}
		}
		return res;
	}

	/**
	 *	Method returns text info about truck
	 * @return text info
	 */
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

	/**
	 * Method creates truck tag used in statistics
	 * @return String tag
	 */
	public String truckTag(){
		String res = "<truck> ";
		res += "Truck: " + this.numOfTruck + " delivered: " + getTotalLoad() + ".";
		res += " Traveled: " + getTotalKm() + " km in " + minToHour(getTotalTimeOnRoad()) + "\n";
		res += " </truck>\n";
		return res;
	}

	/**
	 * Method for writing stats from the whole simulation
	 * @return String of all stats
	 */
	public static String wholeSimStats(){
		String res = "<truckList>";
		for(Truck t: launchableTrucks){
			res += "<truck>";
			res += "Truck: " + t.numOfTruck + " delivered: " + t.totalRunLoad + ".";
			res += " Traveled: " + t.totalRunKm + " km in " + t.minToHour(t.totalRunTime);
			res += "</truck>\n";
		}
		for(Truck t: trucksOnRoad){
			res += "<truck>";
			res += "Truck: " + t.numOfTruck + " delivered: " + t.totalRunLoad + ".";
			res += " Traveled: " + t.totalRunKm + " km in " + t.minToHour(t.totalRunTime);
			res += "</truck>\n";
		}
		res += "</truckList>";
		return res;
	}
}
