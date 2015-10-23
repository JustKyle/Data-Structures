
/*
 * Kyle Rogers
 * CS 2123, Data Structures
 * Event Driven Simulation
 * 
 * Dr. Wainwright
 */

public class EventItem implements Comparable<EventItem> {
	public int time_of_day;
	public int service_time;
	public int type_of_event;

	public EventItem(int timeOfDay, int serviceTime, int typeOfEvent) {
		this.time_of_day = timeOfDay;
		this.service_time = serviceTime;
		this.type_of_event = typeOfEvent;
	}

	@Override
	// This method is used to compare the time of day values for EventItem
	// objects int the priority queue. This provides a method for the priority
	// queue to order EventItems based on their time of day values.
	public int compareTo(EventItem o) {
		if (o.time_of_day > this.time_of_day) {
			return -1;
		} else if (o.time_of_day < this.time_of_day) {
			return 1;
		}
		return 0;
	}
}
