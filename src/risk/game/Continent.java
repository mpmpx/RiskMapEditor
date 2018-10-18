package risk.game;

public class Continent {

	private String name;
	private int value;
	
	public Continent() {
		name = new String();
		value = 0;
	}
	
	public Continent(String name, int value) {
		this.name = name;
		this.value = value;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setValue(int value) {
		this.value = value;
	}
	
	public String getName() {
		return name;
	}
	
	public int getValue() {
		return value;
	}
}
