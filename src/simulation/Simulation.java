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
	
	private int numDay=0;
	
	
	
	
	private Random rand= new Random();
	
			
	public List<AMansion> nodes;
	public int actualTimeinSec=0;
	public double simStepInMs=1000;
	public double timeCoef=0;
	
	
	private int[][]  edges;
	public static Path[][] path;
	
	private Queue<Truck> availableTrucks;
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
		
		orderTime();
		for(int i=0;i<300;i+=timeCoef) {
			
			//TODO prekresli se funkce vizualizace
			
			actualTimeinSec+= (int)timeCoef;
			if(actualTimeinSec>SECONDS_IN_DAY)nextDay();
			if(i%60==0) {
				try {
					Thread.sleep( (long)simStepInMs  );
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
			
		}
			
		
		
	}
	
	private void orderTime() {
	///je mezi 6. hodinou ranní a 6. veèerní (èas objednávek)
	
		
		
			if(actualTimeinSec>START_OF_ORDERING&&actualTimeinSec<END_OF_ORDERING) {
				for(int i=(int) (((1/lambda)*Math.exp(-Order.NUM_OF_ALL_ORDERS*lambda))*edges.length);i>0;i--) {
					//TODO dodelat pravdepodobnost objednavky palet
					Mansion orderingMansion= (Mansion) nodes.get(rand.nextInt(edges.length)+1);
					int amouth= rand.nextInt(orderingMansion.size-1);
					ordersToDo[amouth].add(new Order(orderingMansion,amouth+1));
				}
				
			
			}

			splitOrders();
			launchAllTrucks();
			
	}
	
	
	private void launchAllTrucks() {
		for(Truck t:Truck.launchableTrucks) {
			t.sendOnRoad(actualTimeinSec);
		}
	}

		
	private void splitOrders() {
		
		
		
		for(int index=5;index>=0;index--) {
			
			for(int i=ordersToDo[index].size()-1;i>=0;i--) {
				Mansion o = ordersToDo[index].get(i).getSubscriber();
				int aproxTime=actualTimeinSec*60+calcTravelTime( 0, o.ID);
				

				if((aproxTime) < (o.openingTimeInMin+Mansion.OPENED_TIME_IN_MIN)
						&& o.openingTimeInMin >= aproxTime) {
					assignOrderToTruck(ordersToDo[5].get(i));
					LinkedList<Order> list= null;
					switch(index) {
						case 5: break;
						case 4:
							list=additionalOrders(1, o.ID);
							break;
						case 3:
							list=additionalOrders(2, o.ID);
							break;
						case 2:
							list=additionalOrders(3, o.ID);
							break;
						case 1:
							list=additionalOrders(4, o.ID);
							break;
						case 0:
							list=additionalOrders(1, o.ID);
							break;
					}
					list.stream().forEach(ord -> assignOrderToTruck(ord));
				}
			}

			
			
		}
	}
	private int calcTravelTime(int pointA, int pointB) {
		return (int)((((double)Simulation.path[pointA][pointB].value)/100)*Truck.MINUTES_NEEDED_FOR_100KM);
	}	
	
	private LinkedList<Order> additionalOrders(int neededSize, int pointID) {
		LinkedList<Order> result= new LinkedList<Order>();
		//TODO otestovat + dodelat osetreni dodavaciho okna
		int orderIndex=neededSize-1;
		while(orderIndex>=0&&neededSize>0) {
			if(orderIndex>=neededSize)orderIndex=neededSize-1;
			if(ordersToDo[orderIndex].size()==0) {
				orderIndex--;
				continue;
			}
			
		Order o=findClosePointWithOrder(pointID, orderIndex);
		pointID=o.getSubscriber().ID;
		result.add(o);
		neededSize-=(orderIndex+1);
		}
		
		return result;
	}
	
	
	private void assignOrderToTruck(Order o) {
		if(o==null)return;
		
		if(availableTrucks.isEmpty()) {
			Truck t=new Truck();
			t.addOrder(o);
			ordersToDo[o.getAmount()-1].remove(o);
			Truck.launchableTrucks.add(t);
			//TODO
		}
		
		else {
			//TODO
		}
	}
	
	
	private Order findClosePointWithOrder(int startPoint,int orderSize) {

		int velikost=ordersToDo[orderSize-1].size();
		int minValue= Integer.MAX_VALUE;
		Order result=null;
		
		for(int i=velikost-1;i>=0;i--) {
			Mansion m=ordersToDo[orderSize-1].get(i).getSubscriber();
			
			if(result==null||path[startPoint][m.ID].value<minValue) {
				result=ordersToDo[orderSize-1].get(i);
			}
		}
		if(result!=null)ordersToDo[orderSize-1].remove(result);
		return result;
		
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
