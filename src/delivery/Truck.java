package delivery;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

import Generation.Path;
import simulation.Simulation;


public class Truck {
	public static final int MAX_LOAD = 6;
	public static final int UNLOAD_TIME_IN_MIN = 30;
	public static final int COST_PER_KM=25;
	
	public static Queue<Truck> launchableTrucks= new LinkedList<Truck>();
	public static ArrayList<Truck> trucksOnRoad= new ArrayList<Truck>() ;
	
	public static int count_of_trucks=0;
	public static int count_of_delivered_orders=0;
	
	public int numOfTruck;
	
	//TODO dodelat pro statistiky
	private int totalKm=0;
	private int totalTime=0;
	
	//TODO pro statistiky
	private int travelExpenses=0;
	
	
	private int momentalKM=0;
	private int neededTimeInMin=0;
	
	private int timeOfStartInMin=0;

	private LinkedList<Order> orders= new LinkedList<Order>();
	private int actualLoad;
	
	//private boolean isInHQ=true;
	
	

	public Truck() {
		count_of_trucks++;
		numOfTruck=count_of_trucks;
		Truck.launchableTrucks.add(this);
	}
	
	
	
	public void addOrder(Order o) {
		if(o==null)return;
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
			count_of_delivered_orders++;
			System.out.println("Nakladak c."+numOfTruck+" vylozil "+load+
					" palet v sidle c."+o.getSubscriber().ID+".");
		}
		else {
			throw new Exception("NO ORDERS LEFT!");
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
		
		
		neededTimeInMin+=((double)momentalKM/100);
		travelExpenses+=momentalKM*COST_PER_KM;
		
		totalKm+=momentalKM;
		totalTime+=neededTimeInMin;

		Truck t= Truck.launchableTrucks.poll();
		Truck.trucksOnRoad.add(t);
		
		System.out.println("Nakladak è."+numOfTruck+" zacina nakladat "+actualLoad+" palet.");
		
		
		
		
	}
	private int calcKM(int pointA, int pointB) {
		return (int)(((double)Simulation.distancePath[pointA][pointB].getValue()));
	}
	
	
	
	private void returnToHQ() {
		System.out.println("Nakladak c."+numOfTruck+" se vratil do HQ!");
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
	
	
	
}
