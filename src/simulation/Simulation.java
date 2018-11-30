package simulation;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import delivery.Order;
import delivery.Truck;
import objects.AMansion;
import objects.Mansion;
import objects.Path;
import objects.Pomocna;

public class Simulation {

	private static final int MINUTES_IN_DAY = 1440;
	private static final int START_OF_ORDERING_IN_MIN = 360;
	private static final int END_OF_ORDERING_IN_MIN = 1080;
	
	private int numDay=1;
	
	
	
	
	private Random rand= new Random();
	
			
	public List<AMansion> nodes;
	private int actualTimeinMin=330;
	/**
	 * Pocet kolikrat uz bylo za den provedeno objednavani
	 */
	private int orderingTimesDone;
	/**
	 * Kdy naposled probihalo generovani objednavani
	 */
	private int lastCheckInMin;
	
	/**
	 * distance/time path preference false=time true=distance
	 */
	public boolean distanceXorTime=false;
	
	private static Pomocna matrix;
	public static Path[][] distancePath;
	public static Path[][] timePathInMin;
	
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
		this.timePathInMin=timePaths;
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
	public void pauseSim() {
		stoped = true;
	}
	
	/**
	 * znovu spusti pozastavenou simulaci
	 */
	public void resumeSim() {
		stoped = false;
	}
	
	
	public void simulationStep() {
		if(actualTimeinMin>=MINUTES_IN_DAY)nextDay();
		

		if(actualTimeinMin-lastCheckInMin>=5) {
			orderTime();
			//What is going on the road drivers?
			if(!Truck.trucksOnRoad.isEmpty()) {
				Truck.checkStateOnRoad(actualTimeinMin);
			}
		}
		actualTimeinMin++;
		
		
	
		
	}
		
	
	private void orderTime() {
	///je mezi 6. hodinou rann� a 6. ve�ern� (�as objedn�vek)
	
		//kazdych 5 minut
			
			lastCheckInMin=actualTimeinMin;
			System.out.print("TIME= "+minToHour(actualTimeinMin));
			
			if(actualTimeinMin>=START_OF_ORDERING_IN_MIN&&actualTimeinMin< Mansion.DEFAULT_END_OF_OPENING_IN_MIN) {
				
					if(actualTimeinMin<END_OF_ORDERING_IN_MIN) {
						double chance= (((1/LAMBDA)*Math.exp(-orderingTimesDone/LAMBDA)))*1000;
						for(int i = 1; i < matrix.mansionQuantity(); i++) {
							if(((Mansion)nodes.get(i)).canOrder()){
								int chancePoint = rand.nextInt(1000);
								if(chancePoint<=chance) {
									Mansion orderingMansion = (Mansion) nodes.get(i);
									int amouth= getRandomAmouth(orderingMansion.size);
									ordersToDo[amouth-1].add(new Order(orderingMansion,amouth));
									
							}
							}
						}
						orderingTimesDone++;
					}
					System.out.println("Number of orders: "+getCountOfOrders());
					splitOrders();
					launchAllTrucks();
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
		for(int i = 0; i<Truck.launchableTrucks.size(); i++) {
			
			if(Truck.launchableTrucks.peek().hasOrder()) {
				Truck.launchableTrucks.peek().sendOnRoad(actualTimeinMin);
			}
			else {
				break;
			}
		}
	}

	private void splitOrders() throws IndexOutOfBoundsException  {
		
		for(int index=5;index>=0;index--) {
			
			int loadingTime = (index+1)*Truck.UNLOAD_TIME_IN_MIN;
			int aproxSetOnRoad= actualTimeinMin+loadingTime;
			
			if(ordersToDo[index].isEmpty())continue;
			for(int ind=ordersToDo[index].size()-1;ind>=0;ind--) {
			
				//TODO error
				if(ind >= ordersToDo[index].size()) {
					ind = ordersToDo[index].size()-1;
				}
				Mansion o = ordersToDo[index].get(ind).getSubscriber();
				//TODO jakto ze je hodnota zaporna?
				int aproxTime= aproxSetOnRoad + timePathInMin[0][o.ID].getValue();
				
				//System.out.println(aproxTime+"/"+(o.openingTimeInMin)
				//		+"-"+(o.openingTimeInMin+Mansion.OPENED_TIME_IN_MIN));
				//counting in minutes
				if((aproxTime) < (o.openingTimeInMin+Mansion.OPENED_TIME_IN_MIN)
						&& o.openingTimeInMin <= aproxTime) {
					//System.out.println("yes");
					Order o1 = ordersToDo[index].get(ind);
					assignOrderToTruck(o1);
					LinkedList<Order> list= null;
					//after unloading package
					int aproxTimeWhenDone = aproxTime+loadingTime;
					o1.setProbableTime(aproxTimeWhenDone);
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
							list=additionalOrders(5, o.ID,aproxTimeWhenDone);
							break;
					}
					if(list!=null) {
						list.stream().forEach(ord -> assignOrderToTruck(ord));
					}
					if(ordersToDo[index].isEmpty()) {
						break;
					}
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
			else o=findClosePointWithOrder(pointID, orderIndex,timePathInMin, aproxtime);
		if(o == null) {
			orderIndex--;
			continue;
		}else {
			aproxtime+= (orderIndex+1)*Truck.UNLOAD_TIME_IN_MIN*2+timePathInMin[pointID][o.getSubscriber().ID].getValue() ;
			o.setProbableTime(aproxtime);
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
			
			if(result==null||path[startPoint][m.ID].getValue()<minValue) {

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
		
		//System.out.println("assigning orders");
		if(Truck.launchableTrucks.isEmpty()) {
			Truck t=new Truck();
			try {
				t.addOrder(o);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			ordersToDo[o.getAmount()-1].remove(o);

		}
		
		else {
			while(!Truck.launchableTrucks.isEmpty()) {
				Truck t=Truck.launchableTrucks.peek();
				if(t.canLoad(o.getAmount())) {
					try {
						t.addOrder(o);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					ordersToDo[o.getAmount()-1].remove(o);
					return;
				}else {
					t.sendOnRoad(actualTimeinMin);
				}
				
			}
			if(Truck.launchableTrucks.isEmpty()) {
				assignOrderToTruck(o);
			}
		}
	}
	

	
	
	
	
	
	public void nextDay() {
		Mansion.nextDay(nodes);
		Truck.nextDay();
		actualTimeinMin=0;
		lastCheckInMin=0;
		
		Order.NUM_OF_ALL_ORDERS=0;
		numDay++;
		System.out.println("Day "+numDay);
		//TODO konec simulace?
		//if(numDay>3)stopSimulation();
		
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
	
	
	
}
