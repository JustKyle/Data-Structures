/*
 * Kyle Rogers
 * CS 2123, Data Structures
 * Event Driven Simulation
 * 
 * Dr. Wainwright
 */

import java.util.Scanner;
import java.util.Random;
import java.util.PriorityQueue;
import java.util.LinkedList;

public class BankSimulation {
	public static int clock;
	public static int numCashiers;
	public static int arrivalMean;
	public static int arrivalVariance;
	public static int serviceMean;
	public static int serviceVariance;
	public static int timeLimit;

	public static int customerCount;
	public static int totalInterArrivalTime;
	public static int totalServiceTime;
	public static int maxQueueLength;
	public static int maxCustomerWaitTime;
	public static int totalCustomerWaitTime;
	
	public static int report;

	public static Teller[] tellers = { new Teller(), new Teller(),
			new Teller(), new Teller(), new Teller(), new Teller(),
			new Teller(), new Teller(), new Teller() };

	// This method is used to seed the simulation by creating random integers
	// based on mean arrival time, arrival time variance, mean service time, and
	// the service time variance
	public static int uniform(int mean, int variant, Random rand) {
		int small = mean - variant;
		int range = 2 * variant + 1;
		return small + rand.nextInt(range);
	}

	// This method is used to find the teller that has the lowest number of
	// nodes in its queue
	public static int shortestLine() {
		int shortest = 0;
		for (int i = 1; i < numCashiers; i++) {
			if (tellers[i].size() < tellers[shortest].size()) {
				shortest = i;
			}
		}
		return shortest;
	}

	public static void main(String[] args) {
		Scanner input = new Scanner(System.in);
		Random rand = new Random();
		PriorityQueue<EventItem> eventQueue = new PriorityQueue<EventItem>();

		clock = 0;
		customerCount = 0;
		maxQueueLength = 0;
		maxCustomerWaitTime = 0;
		totalCustomerWaitTime = 0;
		totalInterArrivalTime = 0;
		totalServiceTime = 0;
		report = 1;

		System.out.println("Welcome to the Bank Teller Event Driven Simulation");
		System.out.print("Enter the number of cashiers on duty: ");
		numCashiers = input.nextInt();

		System.out.print("Enter the Mean and Variance for the inter-arrival\nMean: ");
		arrivalMean = input.nextInt();
		System.out.print("Variance: ");
		arrivalVariance = input.nextInt();

		System.out.print("Enter the Mean and Variance for the customer service time\nMean: ");
		serviceMean = input.nextInt();
		System.out.print("Variance: ");
		serviceVariance = input.nextInt();

		System.out.print("Finally, Enter the time limit of this simulation: ");
		timeLimit = input.nextInt();
		
		System.out.println("------------------------------------------------------");

		// Create the first Arrival node
		int initTOD = uniform(arrivalMean, arrivalVariance, rand);
		int initServTime = uniform(serviceMean, serviceVariance, rand);
		eventQueue.add(new EventItem(initTOD, initServTime, -1));

		// Update the total Inter-arrival time and total Service time
		totalInterArrivalTime += initTOD;
		totalServiceTime += initServTime;

		// Begin the Simulation
		while (clock < timeLimit) {
			EventItem temp = eventQueue.poll(); // Remove the first node from
												// the Event Queue
			for (int i = 0; i < numCashiers; i++) { // Update the idle time for
													// all empty queues
				if (tellers[i].size() == 0) {
					tellers[i].increaseIdle(temp.time_of_day - clock);
				}
			}
			clock = temp.time_of_day; // Update the clock
			if (temp.type_of_event == -1) { // If temp is an arrival node
				int shortest = shortestLine(); // Get index of shortest teller queue
				tellers[shortest].add(temp); // add the temp node to the
											 // shortest teller queue

				if (tellers[shortest].size() == 1) { // If new node is only node
													 // in teller queue
					int serviceTime = tellers[shortest].peek().service_time;

					// create a departure node for temp node
					eventQueue.add(new EventItem(clock + serviceTime,
							serviceTime, shortest)); 
				}
				// calculate new arrival time
				int newArrivalTime = clock + uniform(arrivalMean, arrivalVariance, rand);
				// calculate new service time
				int newServiceTime = uniform(serviceMean, serviceVariance, rand);
				// Create new Arrival Node
				eventQueue.add(new EventItem(newArrivalTime, newServiceTime, -1));

				// Update the total inter-arrival and service time
				totalInterArrivalTime += newArrivalTime - clock;
				totalServiceTime += newServiceTime;

				// Update the value for Maximum queue length
				for (int i = 0; i < numCashiers; i++) {
					if (maxQueueLength < tellers[i].size()) {
						maxQueueLength = tellers[i].size();
					}
				}
			}
			// If temp is a departure node
			else if (temp.type_of_event >= 0) { // If temp is a Departure node

				customerCount += 1; // Update Customer Count

				// Store the index of the teller the temp node is departing from
				int tellerNum = temp.type_of_event; 

				// Find wait time for this customer
				int waitTime = clock - (tellers[tellerNum].peek().time_of_day + temp.service_time); 
				
				// Store this wait time if greater than previous wait times
				if (waitTime > maxCustomerWaitTime) {
					maxCustomerWaitTime = waitTime;
				}
				totalCustomerWaitTime += waitTime; // update total wait time

				tellers[tellerNum].remove(); // remove departing node from teller
				
				// create new arrival node for next customer if queue not empty
				if (tellers[tellerNum].size() != 0) {
					int departServ = tellers[tellerNum].peek().service_time;
					int departTOD = clock + departServ;
					eventQueue.add(new EventItem(departTOD, departServ,tellerNum));
				}
			}
			// Print the number of items in the event queue and the number of
			// customers waiting at each teller every ~500 time units
			if (clock > 400 && (clock % (500*report) == 0 || clock % (500*report) <= 5)) {
				System.out.println("Items in Event Queue: " + eventQueue.size());
				for (int i = 0; i < numCashiers; i++) {
					System.out.println("Number of Customers in teller #" + i
							+ ": " + tellers[i].size());
				}
				System.out.println("Clock time: " + clock);
				System.out.println("------------------------------------------------------");
				report++;
			}
		}

		System.out.println("(1) The total number of customers processed. "
				+ customerCount);

		double aveInterArrival = totalInterArrivalTime / customerCount;
		System.out.println("(2) The average inter-arrival time. "
				+ aveInterArrival);

		double aveServTime = totalServiceTime / customerCount;
		System.out.println("(3) The average service time. " + aveServTime);

		double aveWaitTime = totalCustomerWaitTime / customerCount;
		System.out.println("(4) The average wait time per customer. "
				+ aveWaitTime);

		System.out
				.println("(5) Percent of idle time for each of the cashiers:");
		double totalIdle = 0;
		for (int i = 0; i < numCashiers; i++) {
			totalIdle += tellers[i].getIdle();
		}
		for (int i = 0; i < numCashiers; i++) {
			double percentIdle = (tellers[i].getIdle() / totalIdle) * 100;
			System.out.println("\tTeller #" + i + ": " + percentIdle + "%");
		}

		System.out.println("(6) Maximum Customer Wait Time: "
				+ maxCustomerWaitTime);

		System.out.println("(7) The maximum queue length of any customer queue: "
						+ maxQueueLength);

		int numLeft = 0;
		for (int i = 0; i < numCashiers; i++) {
			numLeft += tellers[i].size();
		}
		System.out.println("(8) The total number of people left in the queues at the end of the simulation: "
						+ numLeft);
	}
}
