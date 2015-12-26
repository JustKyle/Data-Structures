
public class Table {
	
	// Set table size to 1019
	private int tableSize = 1019;
	private int[] table;
	
	// Store statistics on searches
	private double totalSuccessProbes = 0;
	private double totalUnProbes = 0;
	private double totalSuccessKeys = 0;
	private double totalUnKeys = 0;
	
	// Constructor, initialize table array with a size and fill with
	// null values
	public Table() {
		table = new int[tableSize];
		for (int i = 0; i < tableSize; i++) {
			table[i] = -1;
		}
	}
	
	// Insert into the tabel using Linear probing
	public void linearInsert(int n) {
		// Get the index of where the integer will be placed
		int key = getKey(n);
		
		
		if (isEmpty(key)) { // Insert if index is empty
			table[key] = n;
		}
		else { // Try to insert at each next index, in single increments
			while (!isEmpty(key)) {
				key++; // Increase index value by one
				if (key >= tableSize - 1) { // Wrap to the beginning of table
					key = 0;
				}
			}
			table[key] = n; // Where the loop ends is where integer is placed
		}
	}
	
	// Insert into the table using quadratic probing
	public void quadraticInsert(int n) {
		// Uses the getKey method to return the index of where the integer is
		// to be placed
		int key = getKey(n);
		
		// If the index is empty then place the integer
		if (isEmpty(key)) {
			table[key] = n;
		}
		// If the index is occupied, find next available index
		else {
			// Initialize variable to store number of iterations
			int iteration = 1;
			
			// Keep looping until every index has been checked or an empty
			// index has been found
			while(iteration <= tableSize) {
				// set the new index to key + iteration^2
				int newKey = key + (iteration*iteration);
				
				// if the key is greater than the table size wrap to beginning
				if (newKey >= tableSize) {
					newKey = newKey - tableSize;
					if (isEmpty(newKey)) { // check if index is empty
						table[newKey] = n;
						break;
					}
				}
				else if (isEmpty(newKey)){ // check if newKey index is empty 
					table[newKey] = n;
					break;
				}
				
				// If no empty index was found looking ahead, then look
				// behind in the table using key - iteration^2
				newKey = key - (iteration*iteration);
				
				// If the key is less than 0, wrap it to the end of the table
				if (newKey < 0) {
					newKey = newKey + tableSize;
					if (isEmpty(newKey)) { // check if index is empty
						table[newKey] = n;
						break;
					}
				}
				else if(isEmpty(newKey)) { // Check if newKey index is empty
					table[newKey] = n;
					break;
				}
				// Increase iteration count by 1
				iteration++;
			}
		}
	}
	
	// Determines initial index of an integer to be hashed into the table
	// and returns it.
	// key = num % tableSize
	public int getKey(int num) {
		return num%tableSize;
	}
	
	// Checks if a specified index in the table is empty
	// Empty indexes contain a value of -1
	public boolean isEmpty(int index) {
		if(table[index] == (-1)) {
			return true;
		}else{
			return false;
		}
	}

	public void searchStatsLinear(int[] all) {
		// Loops through integers 1 - 10,000 to find each within the table,
		// if the loop cannot find them then they do not exist in the table.
		for (int i = 0; i<all.length; i++) {
			// Create variable to store the number of probes for this key
			int probes = 1;
			
			// Variable used to end the while loop when either a key is found
			// or determined to be nonexistent
			boolean done = false;
			
			// Store the key returned from the getKey method
			int key = getKey(all[i]);
			
			// If the integer exists at the key, move on to next integer and
			// increase successful probes and successful keys by 1
			if (table[key] == all[i]) {
				totalSuccessProbes += probes;
				totalSuccessKeys++;
			}
			else {
				// Increase probes searched by one since initial key index
				// did not contain the integer
				probes++;
				// Enter while loop to do linear probing
				while (!done) {
					key++; // Increase the key index by 1
					if (key >= tableSize - 1) { // wrap index to beginning
						key = 0;
					}
					
					// Increase successful probes and keys by 1 if index
					// contains the integer searched for
					if (table[key] == all[i]) {
						totalSuccessProbes += probes;
						totalSuccessKeys++;
						done = true;
					}
					probes++; // Increase number of probes by 1
					// Integer is nonexistent in the table if search reaches
					// an empty index
					if (isEmpty(key)) {
						totalUnProbes += probes; // store number of probes
						totalUnKeys++; // store number of keys
						done = true; // exit while loop
					}
				}
			}
		}
		// Print the statistics for the load factor
		double loadFactor = totalSuccessKeys/tableSize;
		System.out.println("Load Factor: " + loadFactor);
		
		// Print statistics for the successful searches
		double aveSuccess = totalSuccessProbes/totalSuccessKeys;
		System.out.println(totalSuccessKeys + " Were successful.\nAve. probes: " + aveSuccess);
		
		// Print statistics for the unsuccessful searches
		double aveUn = totalUnProbes/totalUnKeys;
		System.out.println(totalUnKeys + " Were unsuccessful.\nAve probes: " + aveUn);
	}
	
	public void searchStatsQuadratic(int[] all) {
		// Loops through integers 1 - 10,000 to find each within the table,
		// if the loop cannot find them then they do not exist in the table.
		for (int i = 0; i<all.length; i++) {
			// Create variable to store the number of probes for this key
			int probes = 1;
			
			// Store the key returned from the getKey method
			int key = getKey(all[i]);
			
			// If the integer exists at the key, move on to next integer and
			// increase successful probes and successful keys by 1
			if (table[key] == all[i]) {
				totalSuccessProbes += probes;
				totalSuccessKeys++;
			}
			else {
				// Create variable to store the number of iterations
				int iteration = 1;
				probes++; // Increase the number of probes executed by one
				
				while(true) { // Begin while loop to search for keys
					// set the new index to key + iteration^2
					int newKey = key + (iteration*iteration);
					
					// Wrap key to beginning if greater than tableSize
					if (newKey >= tableSize) {
						newKey = newKey - tableSize;
						if (table[newKey] == all[i]) {
							// Successful if the integer is present at index
							totalSuccessProbes += probes;
							totalSuccessKeys++;
							break;
						}
						else if (isEmpty(newKey)) {
							// Unsuccessful if empty
							totalUnProbes += probes; // store number of probes
							totalUnKeys++; // store number of keys
							break;
						}
					}
					else if (table[newKey] == all[i]){ 
						totalSuccessProbes += probes;
						totalSuccessKeys++;
						break;
					}
					else if (isEmpty(newKey)) {
						totalUnProbes += probes; // store number of probes
						totalUnKeys++; // store number of keys
						break;
					}
					probes++; // Increase the number of probes by 1
					
					// Change key to search in opposite direction
					newKey = key - (iteration*iteration);
					if (newKey < 0) {
						newKey = newKey + tableSize;
						if (table[newKey] == all[i]) {
							totalSuccessProbes += probes;
							totalSuccessKeys++;
							break;
						}
						else if (isEmpty(newKey)) {
							totalUnProbes += probes; // store number of probes
							totalUnKeys++; // store number of keys
							break;
						}
					}
					else if (table[newKey] == all[i]){ 
						totalSuccessProbes += probes;
						totalSuccessKeys++;
						break;
					}
					else if (isEmpty(newKey)) {
						totalUnProbes += probes; // store number of probes
						totalUnKeys++; // store number of keys
						break;
					}
					iteration++;
					probes++;
				}
			}
		}
		// Print the statistics for the load factor
		double loadFactor = totalSuccessKeys/tableSize;
		System.out.println("Load Factor: " + loadFactor);
				
		// Print statistics for the successful searches
		double aveSuccess = totalSuccessProbes/totalSuccessKeys;
		System.out.println(totalSuccessKeys + " Were successful.\nAve. probes: " + aveSuccess);
		
		// Print statistics for the unsuccessful searches
		double aveUn = totalUnProbes/totalUnKeys;
		System.out.println(totalUnKeys + " Were unsuccessful.\nAve probes: " + aveUn);
	}

}
