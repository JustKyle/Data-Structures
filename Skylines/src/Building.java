
public class Building {
	public int start;
	private int height;
	private int end;
	
	// Create Building object with a starting point, height, and end point
	public Building(int L, int H, int R) {
		this.start = L;
		this.height = H;
		this.end = R;
	}
	
	// Building object used for final skyline
	public Building(int L, int H) {
		this.start = L;
		this.height = H;
	}
	
	// returns starting point of the building
	public int getStart() {
		return this.start;
	}
	
	// returns height of the building
	public int getHeight() {
		return this.height;
	}
	
	// returns end point of the building
	public int getEnd() {
		return this.end;
	}
}
