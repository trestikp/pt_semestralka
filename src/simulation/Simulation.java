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
	public int actualTimeinSec=19800;
	/**
	 * Pocet kolikrat uz bylo za den provedeno objednavani
	 */
	public int orderingTimesDone;
	/**
	 * Kdy naposled probihalo generovani objednavani
	 */
	public int lastCheck;
	
	/**
	 * distance/time path preference false=time true=distance
	 */
	public boolean distanceXorTime=false;
	
	private static Pomocna matrix;
	public static Path[][] distancePath;
	public static Path[][] timePath;
	
	private LinkedList<Order>[] ordersToDo=new LinkedList[6];
	
	
	//atributy vlakna
	 private boolean stoped;
	 private boolean endSim = false;
	
	
	//exponencialni rozdeleni
		private static final double LAMBDA=300;
	
	
	@SuppressWarnings("static-access")
	public Simulation(List<AMansion> g,Pomocna matrix,Path[][] distancePaths, Path[][] timePaths) {
		this.nodes=g;
		this.matrix=matrix;
		for(int i=0;i<ordersToDo.length;i++) {
			ordersToDo[i]=new LinkedList<Order>();
		}
		this.distancePath=distancePaths;
		this.timePath=timePaths;
	}
	 

	/**
	 * spusti simulaci
	 * @param dobaCekani doba trvani jedne minuty simulace v ms
	 */
	public void runSimulation(int dobaCekani) {
		//if (dobaCekani <= 0) dobaCekani=300;
		Thread vlaknoSimulace = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					
					
					stoped = false;
					while(true) {
						
					if (endSim) {
							return;
						}
						if (!stoped) {
							simulationStep();
							Thread.sleep(dobaCekani);
						}
					}
					}
				   catch (Exception ex) {
					   ex.printStackTrace();
				}
			}
		});
		vlaknoSimulace.start();
	}

	
	public void endSimulation() {
		endSim=true;
	}
	/**
	 * pozastavi simulaci
	 */
	public void pozastav() {
		stoped = true;
	}
	
	/**
	 * znovu spusti pozastavenou simulaci
	 */
	public void pokracuj() {
		stoped = false;
	}
	
	
	public void simulationStep() {
		if(actualTimeinSec>=SECONDS_IN_DAY)nextDay();
		
		
		orderTime();
		actualTimeinSec+=60;
		
		
		
	}
		
	
	private void orderTime() {
	///je mezi 6. hodinou ranní a 6. veèerní (èas objednávek)
	
		//kazdych 5 minut
		if(actualTimeinSec-lastCheck>=300) {
			lastCheck=actualTimeinSec;
			int hou=actualTimeinSec/3600;
			int min=(actualTimeinSec/60)%60;
			System.out.println("TIME= "+hou+":"+min);
			
			if(actualTimeinSec>=START_OF_ORDERING&&actualTimeinSec<Mansion.DEFAULT_END_OF_OPENING_IN_SEC) {
				
					if(actualTimeinSec<END_OF_ORDERING) {
						double chance= (((1/LAMBDA)*Math.exp(-orderingTimesDone/LAMBDA)))*1000;
						for(int i = 1; i < matrix.mansionQuantity(); i++) {
							int chancePoint = rand.nextInt(1000);
							if(chancePoint<=chance) {
								Mansion orderingMansion = (Mansion) nodes.get(i);
								int amouth= getRandomAmouth(orderingMansion.size);
								ordersToDo[amouth-1].add(new Order(orderingMansion,amouth));
								
							}
						}
						orderingTimesDone++;
					}
					System.out.println("Pocet objednavek "+getCountOfOrders());
					splitOrders();
					launchAllTrucks();
				}

				
				
			
			}

			
	}
	
	private int getCountOfOrders() {
		int result = 0;
		for(int i=0; i < ordersToDo.length; i++) {
			result+=ordersToDo[i].size();
		}
		return result;
	}
	
	private int getRandomAmouth(int size) {
		int chance=25;
		for(int i=1, addChance=25; i < size; i++, addChance-=5) {
			chance+=addChance;
		}
		int result= rand.nextInt(chance);
		
		if(result < 26)return 1;
		else if(result < 51)return 2;
		else if(result < 71)return 3;
		else if(result < 86)return 4;
		else if(result < 96)return 5;
		else return 6;
		
	}
	
	
	private void launchAllTrucks() {
		for(Truck t:Truck.launchableTrucks) {
			t.sendOnRoad(actualTimeinSec);
		}
	}

		
	private void splitOrders() {
		int actualTimeinMin=actualTimeinSec/60;
		for(int index=5;index>=0;index--) {
			
			int loadingTime = (index+1)*Truck.UNLOAD_TIME_IN_MIN;
			int aproxSetOnRoad= actualTimeinMin+loadingTime;
			for(int i=ordersToDo[index].size()-1;i>=0;i--) {
			
				//TODO error
				Mansion o = ordersToDo[index].get(i).getSubscriber();
				int aproxTime= aproxSetOnRoad + timePath[0][o.ID].value;
				
				System.out.println(aproxTime+"/"+(o.openingTimeInMin)
						+"-"+(o.openingTimeInMin+Mansion.OPENED_TIME_IN_MIN));
				//counting in minutes
				if((aproxTime) < (o.openingTimeInMin+Mansion.OPENED_TIME_IN_MIN)
						&& o.openingTimeInMin >= aproxTime) {
					assignOrderToTruck(ordersToDo[index].get(i));
					LinkedList<Order> list= null;
					//after unloading package
					int aproxTimeWhenDone = aproxTime+loadingTime;
					
					switch(index) {
						case 5: break;
						case 4:
							list=additionalOrders(1, o.ID,aproxTimeWhenDone);
							break;
						case 3:
							list=additionalOrders(2, o.ID,aproxTimeWhenDone);
							break;
						case 2:
							list=additionalOrders(3, o.ID,aproxTimeWhenDone);
							break;
						case 1:
							list=additionalOrders(4, o.ID,aproxTimeWhenDone);
							break;
						case 0:
							list=additionalOrders(1, o.ID,aproxTimeWhenDone);
							break;
					}
					list.stream().forEach(ord -> assignOrderToTruck(ord));
				}
			}

			
			
		}
	}
	
	private LinkedList<Order> additionalOrders(int neededSize, int pointID, int aproxtime) {
		LinkedList<Order> result= new LinkedList<Order>();
		//TODO otestovat 
		int orderIndex=neededSize-1;
		while(orderIndex >= 0 && neededSize > 0) {
			//bs to look for bigger orders if size is smaller 
			if(orderIndex >= neededSize) orderIndex=neededSize-1;
			//No orders left, lets try "smaller" orders
			if(ordersToDo[orderIndex].size() == 0) {
				orderIndex--;
				continue;
			}
		Order o=null;
			if(distanceXorTime) o=findClosePointWithOrder(pointID, orderIndex,distancePath, aproxtime);
			else o=findClosePointWithOrder(pointID, orderIndex,timePath, aproxtime);
		if(o == null) {
			orderIndex--;
			continue;
		}else {
			aproxtime+= (orderIndex+1)*Truck.UNLOAD_TIME_IN_MIN*2+timePath[pointID][o.getSubscriber().ID].value/60 ;
			pointID=o.getSubscriber().ID;
			result.add(o);
			neededSize-=(orderIndex+1);
		}
		

		}
		
		return result;
	}
	
	
	private Order findClosePointWithOrder(int startPoint,int orderSize,Path[][] path, int aproxtime) {

		int velikost=ordersToDo[orderSize].size();
		int minValue= Integer.MAX_VALUE;
		Order result=null;
		aproxtime+=Truck.UNLOAD_TIME_IN_MIN*(orderSize+1);
		
		for(int i=velikost-1;i>=0;i--) {
			Mansion m=ordersToDo[orderSize].get(i).getSubscriber();
			
			if(result==null||path[startPoint][m.ID].value<minValue) {

				if((aproxtime) < (m.openingTimeInMin+Mansion.OPENED_TIME_IN_MIN)
							&& m.openingTimeInMin >= aproxtime) {
					result=ordersToDo[orderSize].get(i);
				}
			}	
		}
		return result;
		
	}
	
	private void assignOrderToTruck(Order o) {
		if(o==null)return;
		
		System.out.println("assigning orders");
		if(Truck.launchableTrucks.isEmpty()) {
			Truck t=new Truck();
			//TODO Error
			t.addOrder(o);
			ordersToDo[o.getAmount()-1].remove(o);
			Truck.launchableTrucks.add(t);
		}
		
		else {
			Truck t=null;
			while(t==null||!Truck.launchableTrucks.isEmpty()) {
				t=Truck.launchableTrucks.peek();
				if(t.isLoaded()) {
					t.sendOnRoad(actualTimeinSec/60);
					Truck.launchableTrucks.poll();
				}else {
					t.addOrder(o);
					ordersToDo[o.getAmount()-1].remove(o);
				}
				
			}
		}
	}
	

	
	
	
	
	
	public void nextDay() {
		for(int i=1;i<nodes.size();i++) {
			((Mansion)nodes.get(i)).numOfDeliveredGoods=0;
		}
		actualTimeinSec=0;
		lastCheck=0;
		
		Order.NUM_OF_ALL_ORDERS=0;
		numDay++;
		System.out.println("Den è."+numDay);
		//TODO konec simulace?
		//if(numDay>3)stopSimulation();
		
	}
	
	
	
	
	
}
