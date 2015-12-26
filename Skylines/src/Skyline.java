
import java.util.ArrayList;

public class Skyline {
	private ArrayList<Building> skyline = new ArrayList<Building>();
	
	public void add(Building bldg) {
		skyline.add(bldg);
	}
	
	public Building get(int index) {
		return skyline.get(index);
	}
	
	public int size() {
		return skyline.size();
	}
}
