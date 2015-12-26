import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Driver {

	// This method creates an array of "Spikes" from the initial Skyline array of Buildings
	// and creates the final skyline, merging the shorter buildings with the taller ones
	// when they are at the same location.
	public static int[] toSpikes(ArrayList<Building> initSkyline, int size) {
		// Initialize array to store the spikes
		int[] skyline = new int[size];

		// Start for loop to iterate over the initial Skyline array
		for (int i = 0; i < initSkyline.size(); i++) {
			// Get Building values from the Building at index i in the array
			int start = initSkyline.get(i).getStart() - 1;
			int height = initSkyline.get(i).getHeight();
			int end = initSkyline.get(i).getEnd() - 1;

			// Start for loop to turn the building into spikes
			for (int j = start; j < end; j++) {
				// Replaces a spike at j if the new spike has greater height
				if (height > skyline[j]) {
					skyline[j] = height;
				}
			}
		}
		return skyline;
	}

	// Condenses the spike array into a skyline
	public static ArrayList<Building> inductiveSkyline(int[] skyline) {
		// Find the height of the first building
		int tempHeight = skyline[0];
		// Store the number of times the height changes to use as index in the ArrayList
		int numChanges = 0;
		// Store the starting index of the change in height to use ad start point of building
		int tempIdx = 0;
		
		// Create an array to store the buildings in the format of 
		// (Start point, height, start point of next, height of next,...)
		ArrayList<Building> condensedSkyline = new ArrayList<Building>();
		// Start for loop to condense the rest of the buildings
		for (int i = 0; i < skyline.length; i++) {
			if (tempHeight != skyline[i]) {
				// This is to add the first building, if the height has changed 0 times
				if (numChanges == 0) {
					condensedSkyline.add(numChanges, new Building(1, tempHeight));
					// Set the temp height to the height of the next building
					tempHeight = skyline[i];
					// Increase the number of changes
					numChanges++;
					// Set the starting point of the new building
					tempIdx = i;
				}
				// Add each other building to the array
				else {
					condensedSkyline.add(numChanges, new Building(tempIdx + 1, tempHeight));
					tempHeight = skyline[i];
					numChanges++;
					tempIdx = i;
				}
			}
		}
		// Add the final height which brings the last building down to 0
		condensedSkyline.add(new Building(skyline.length, 0));
		return condensedSkyline;
	}

	public static void toString(ArrayList<Building> condensedSkyline) {
		String finalSkyline = "(";

		// Adds each value of start and height to the string
		for (int i = 0; i < condensedSkyline.size(); i++) {
			if (i == condensedSkyline.size() - 1) {
				finalSkyline += condensedSkyline.get(i).getStart() + ", " + condensedSkyline.get(i).getHeight() + ")";
			} else {
				finalSkyline += condensedSkyline.get(i).getStart() + ", " + condensedSkyline.get(i).getHeight() + " ,";
			}
		}
		// Prints the string representing the skyline
		System.out.println(finalSkyline);
	}

	public static ArrayList<Building> dncSkyline(ArrayList<Building> initSkyline, int low, int high) {
		// Create ArrayList to hold final skyline
		ArrayList<Building> skyline = new ArrayList<Building>();
		
		// If there is only one building in the initSkyline array
		if (low == high) {
			// Create a building object to hold the only building in the array
			Building bldg = initSkyline.get(low);
			
			// Add the building to the array in the format of (building(position, height),...)
			skyline.add(new Building(bldg.getStart(), bldg.getHeight()));
			skyline.add(new Building(bldg.getEnd(), 0));
		}
		// If the array contains more than 1 building
		else if (initSkyline.size() > 1) {
			// Find the index of the center of the array
			int center = (low + high)/2;
			
			// Split the array into two halves at the center
			ArrayList<Building> skyLow = dncSkyline(initSkyline, low, center);
			ArrayList<Building> skyHigh = dncSkyline(initSkyline, center + 1, high);
			
			// Merge the two halves and set the skyline array to the merge
			skyline = merge(skyLow, skyHigh);
		}
		// Return the final Skyline
		return skyline;
	}
	
	public static ArrayList<Building> merge(ArrayList<Building> skyLow, ArrayList<Building> skyHigh) {
		// Create an array to hold the merged skyline
		ArrayList<Building> finalSkyline = new ArrayList<Building>();
		
		// Used for tracking the current index of the array while in the while loop
		int idxLow = 0;
		int idxHigh = 0;
		
		// Used for saving the height and determining the height of the building being added
		int lowHeight = 0;
		int highHeight = 0;
		
		// Saves the current height in the array
		int currHeight = 0;
		
		// Saves the current building start point in the array
		int currStart = 0;
		
		// Start while loop to merge the two arrays
		while (idxLow < skyLow.size() && idxHigh < skyHigh.size()) {
			// Retrieve and store the buildings at the current index of each array
			Building bldgA = skyLow.get(idxLow);
			Building bldgB = skyHigh.get(idxHigh);
			
			// If building A comes before Building B
			if (bldgA.getStart() <= bldgB.getStart()) {
				currStart = bldgA.getStart(); // Set current array index to start of A
				lowHeight = bldgA.getHeight();// Set current array height to height of A
				idxLow++;					  // Increase current index in Low array by 1
			}
			// If building B comes before Building A
			if (bldgA.getStart() >= bldgB.getStart()) {
				currStart = bldgB.getStart();  // Set current array index to start of B
				highHeight = bldgB.getHeight();// Set current array height to height of B
				idxHigh++;					   // Increase current index in High array by 1
			}
			// Compare the low and high array heights and determine which is the tallest
			int maxHeight = lowHeight > highHeight ? lowHeight : highHeight;
			// If the current height is not equal to the max height
			if (maxHeight != currHeight) {
				currHeight = maxHeight; // Set current height to the max height
				// Add a new building with current position and height
				finalSkyline.add(new Building(currStart, currHeight));
			}
			// the low index is larger than its size exit the loop
			if (idxLow >= skyLow.size()) {
				break;
			}
		}
		// Add the remaining buildings in low to the skyline
		while (idxLow < skyLow.size()) {
			finalSkyline.add(skyLow.get(idxLow));
			idxLow++;
		}
		// Add the remaining buildings in high to the skyline
		while (idxHigh < skyHigh.size()) {
			finalSkyline.add(skyHigh.get(idxHigh));
			idxHigh++;
		}
		// Return the finished skyline
		return finalSkyline;
	}
	
	public static void main(String args[]) {

		Scanner input = new Scanner(System.in);
		
		// Create ArrayList to hold initial skyline building data from file
		ArrayList<Building> initSkyline = new ArrayList<Building>(); 

		// Prompt for choice between inductive or DaC
		System.out.println("Welcome to the Skyline generator!");
		System.out.print("Enter 1 for Induction or 2 for Divide-and-Conquer: ");
		int choice = input.nextInt();

		// Get filename for dataset
		System.out.print("Enter the number for the dataset you would like to use: ");
		int set = input.nextInt();

		input.close();
		
		// Initialize variable for the size of the skyline array
		int size = 0;
		
		// Attempt to open and parse dataset file
		try {
			FileReader file = new FileReader("Sky" + set + ".txt");
			Scanner tokens = new Scanner(file);

			// Start while loop to iterate through the file
			while (tokens.hasNext()) {
				int start = tokens.nextInt();
				int height = tokens.nextInt();
				int end = tokens.nextInt();
				if (end > size) {
					size = end;
				}
				// Add each building to the initSkyline array
				initSkyline.add(new Building(start, height, end));
			}
			tokens.close();

		}
		// catch exception for file not found
		catch (FileNotFoundException e) {
			System.out.println("File Not Found Exception: " + e
					+ "\nPlease enter a valid file name.");
		}
		// Do the inductive skyline for choice 1
		if (choice == 1) {
			System.out.println("Inductive:");
			toString(inductiveSkyline(toSpikes(initSkyline, size)));
		} 
		// Do the DnC skyline for choice 2
		else if (choice == 2) {
			System.out.println("Divide and Conquer:");
			toString(dncSkyline(initSkyline, 0, initSkyline.size() - 1));
		}
		// Print this if neither 1 or 2 is chosen
		else {
			System.out.println("You're not very good at following directions. Try again.");
		}
	}
}
