package risk.game;

/**
 * Continent class manages Continent information(name and value) of a *.map file.
 * @author
 * @version 1
 */

public class Continent {

	/**
	 * name of a ontinent
	 * value of a continent
	 */
	private String name;
	private int value;

	/**
	 * Constructor of the class. Initialize all class variables.
	 */
	public Continent() {
		name = new String();
		value = 0;
	}

	/**
	 * Constructor of the class. Initialize with input.
	 */
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
