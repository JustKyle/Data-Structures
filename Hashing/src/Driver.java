import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Scanner;

public class Driver {
	
	// Produce an array of the keys to be inserted
	public static int[] makeKeysArray(int size) {
		
		// Creates an ArrayList to be used for storing 1 - 10,000
		ArrayList<Integer> list = new ArrayList<Integer>();
		// Creates an array to hold the keys to be insterted
		int[] keys = new int[size];
		
		// Adds integers 1 - 10,000 to ArrayList list
		for(int i = 0; i < 10000; i++) {
			list.add(i + 1);
		}
		Collections.shuffle(list); // shuffles the positions of all integers in the ArrayList
		
		// Adds the first 611/815 integers to the keys array
		for (int i = 0; i<size; i++) {
			keys[i] = list.get(i);
		}
		return keys; // Return the array of keys
	}
	
	// Inserts the keys into the table using the Table object and linear probing
	public static Table linearTable(int[] keys, Table table) {
		for (int i = 0; i<keys.length; i++) {
			table.linearInsert(keys[i]);
		}
		return table; // Returns the finished table
	}
	
	// Inserts the keys into the table using the Table object and quadratic probing
	public static Table quadraticTable(int[] keys, Table table) {
		for (int i = 0; i<keys.length; i++) {
			table.quadraticInsert(keys[i]);
		}
		return table; // Returns the finished table
	}
	
	public static void main(String[] args) {
		
		// Initialize Scanner, create table, and create keyArraySize variable
		Scanner input = new Scanner(System.in);
		Table table = new Table();
		int keyArraySize;
		
		// Prompt user for choice between a hash table using linear or quadratic probint
		System.out.print("Enter 0 for linear probing or 1 for Quadratic probing: ");
		int probeChoice = input.nextInt();
		// Prompt user for choice of how full the table is to be
		System.out.print("Enter 0 for an 60% full table or 1 for an 80% full table: ");
		int fullChoice = input.nextInt();
		
		// Seat value of keyArraySize based on fullness choice
		if(fullChoice == 0) {
			keyArraySize = 611; // 60%
		}
		else {
			keyArraySize = 815; // 80%
		}
		
		// Create array all to store all integers 1 - 10,000
		int[] all = new int[10000];
		for (int i = 0; i<all.length; i++) {
			all[i] = i+1;
		}
		
		// Build hash table based on user choice
		if (probeChoice == 0) { // Linear probing
			Table hashTable = linearTable(makeKeysArray(keyArraySize),table);
			
			hashTable.searchStatsLinear(all);
		}
		else { // Quadratic probing
			Table hashTable = quadraticTable(makeKeysArray(keyArraySize),table);
			
			hashTable.searchStatsQuadratic(all);
			
		}
		// System.out.println(table.toString());
	}
}
