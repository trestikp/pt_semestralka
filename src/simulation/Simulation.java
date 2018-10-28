package simulation;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

import Generation.*;
import delivery.Order;
import delivery.Truck;

public class Simulation {

	private static final int SECONDS_IN_DAY= 86400;
	private static final int START_OF_ORDERING=21600;
	private static final int END_OF_ORDERING=64800;
	
	private static final int[] NUM_OF_MORNING_ORDERS= {50,150,300};
	private int numDay=0;
	
	private static final int MINUTES_NEEDED_FOR_100KM=70;
	
	
	
	private Random rand= new Random();
	
	private int numOfOrders=0;
			
	public List<AMansion> nodes;
	public int actualTimeinSec=0;
	public double simStepInMs=1000;
	public double timeCoef=0;
	
	
	private int[][]  edges;
	public Path[][] path;
	
	private Queue<Truck> availableTrucks;
	private LinkedList<Truck> launchableTrucks;
	private ArrayList<Order>[] ordersToDo=new ArrayList[6];
	
	
	//exponencialni rozdeleni
		private int lambda=300;
	
	
	public Simulation(List<AMansion> g,int[][] edge, int simStep,Path[][] completePaths) {
		this.nodes=g;
		this.simStepInMs=simStep;
		this.edges=edges;
		for(int i=0;i<ordersToDo.length;i++) {
			ordersToDo[i]=new ArrayList<Order>();
		}
		this.path=completePaths;
		
	}
	 
	
	public void startSimulation() {
		timeCoef=simStepInMs/1000;
	}
	
	
	public void stopSimulation() {
		timeCoef=0;
	}
	
	public void simulationStep() {
		if(timeCoef==0)return;
		
		///je mezi 6. hodinou ranní a 6. veèerní (èas objednávek)
		
		if(actualTimeinSec>START_OF_ORDERING&&actualTimeinSec<END_OF_ORDERING) {
			for(int i=(int) (((1/lambda)*Math.exp(-Order.NUM_OF_ALL_ORDERS*lambda))*edges.length);i>0;i--) {
				Mansion orderingMansion= (Mansion) nodes.get(rand.nextInt(edges.length)+1);
				//TODO dodìlat pravdìpodobnosti 
				int amouth= rand.nextInt(orderingMansion.size-1);
				ordersToDo[amouth].add(new Order(orderingMansion,amouth+1));
			}
			
		
		}

		splitOrders();
		launchAllTrucks();
		
		actualTimeinSec+= (int)timeCoef;
		if(actualTimeinSec>SECONDS_IN_DAY)nextDay();
	}
	
	
	private void launchAllTrucks() {
		for(Truck t:launchableTrucks) {
			t.sendOnRoad(actualTimeinSec);
		}
	}


	private void splitOrders() {
		
		for(int i=0;i<ordersToDo[5].size();i++) {
			
			Mansion o = ordersToDo[5].get(i).getSubscriber();
			
			// momentální èas + doba cesty + nákladání + vykládaní
			int aproxTime=actualTimeinSec*60+neededTimeToTravelInMin(0,o.ID)+Truck.UNLOAD_TIME_IN_MIN*12;
			
			if((aproxTime) < (o.openingTimeInMin+Mansion.OPENED_TIME_IN_MIN)
					&& o.openingTimeInMin >= aproxTime) {
				assignOrderToTruck(ordersToDo[5].get(i),aproxTime);
				ordersToDo[5].remove(i);
				
			}
			
		}
		
		
		//TODO
		
		
	}
	
	
	private void assignOrderToTruck(Order o, int neededTime) {
		if(availableTrucks.isEmpty()) {
			Truck t=new Truck();
			t.addOrder(o, neededTime);
			launchableTrucks.add(t);
		}
		
		else {
			//TODO
		}
	}
	
	
	
	private int neededTimeToTravelInMin(int pointA, int pointB) {
		return (int)(((double)path[pointA][pointB].value)/100.0*MINUTES_NEEDED_FOR_100KM);
	}
	
	
	public void nextDay() {
		for(int i=1;i<nodes.size();i++) {
			((Mansion)nodes.get(i)).numOfOrderedGoods=0;
		}
		actualTimeinSec=0;
		System.out.println("Den è."+numDay);
		
		Order.NUM_OF_ALL_ORDERS=0;
		numDay++;
		//TODO konec simulace?
		if(numDay>3)stopSimulation();
		
	}
	
	
	
	
	
}
