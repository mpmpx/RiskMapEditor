package risk.game;

/**
 * Continent class manages Continent information(name and value) of a *.map file.
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
	
	/**
	 * Set the name of the continent.
	 * @param name is the name to be assigned.
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Set the value of the continent.
	 * @param value is the value of the continent
	 */
	public void setValue(int value) {
		this.value = value;
	}
	
	/**
	 * Get the name of the continent.
	 * @return the name of the continent.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Get the value of the continent.
	 * @return the value of the continent.
	 */
	public int getValue() {
		return value;
	}
}
