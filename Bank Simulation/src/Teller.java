
/*
 * Kyle Rogers
 * CS 2123, Data Structures
 * Event Driven Simulation
 * 
 * Dr. Wainwright
 */

import java.util.LinkedList;

public class Teller extends LinkedList<EventItem> {
	private double idleTime; // Stores the amount of time this teller was idle

	public Teller() {
		idleTime = 0;
	}
	
	// This method is used to increase the total idle time of this teller
	public void increaseIdle(int time) {
		idleTime += time;
	}
	
	// This method is used to return the value of idleTime
	public double getIdle() {
		return idleTime;
	}
}
