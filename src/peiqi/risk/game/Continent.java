package risk.game;

import java.util.HashMap;
import java.util.LinkedList;

/**
 * This class defines a continent which contains its name, continent value
 * and all territories which belong to it.
 */
public class Continent {

	private String name;
	private int value;
	private LinkedList<String> territoryList;
	
	/**
	 * constructor method with parameters.
	 * @param name a string string that is to be this continent's name.
	 * @param value an integer that is to be this continent's value.
	 */
	public Continent(String name, int value) {
		this.name = name;
		this.value = value;
		territoryList = new LinkedList<String>();
	}
	
	/**
	 * Gets the name of this continent.
	 * @return continent's name.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Gets the value of this continent.
	 * @return continent's value.
	 */
	public int getValue() {
		return value;
	}
	
	/**
	 * Adds a new territory for this continent if it has not
	 * been added to this continent.
	 * @param newTerritory the territory to be added.
	 */
	public void addTerritory(Territory newTerritory) {
		if (!territoryList.contains(newTerritory.getName())) {
			territoryList.add(newTerritory.getName());	
		}
	}
	
	/**
	 * Removes a territory from this continent.
	 * @param territory the territory to be removed from this continent.
	 */
	public void removeTerritory(Territory territory) {
		territoryList.remove(territory.getName());
	}
	
	/**
	 * Returns the territory list which contains all territories.
	 * @return territoryList the territory list which contains all territories.
	 */
	public LinkedList<String> getTerritoryList() {
		return territoryList;
	}
	
	/**
	 * Checks whether given list of territories contains all territories of this continent.
	 * @param territoryList a list of territories that is to be checked.
	 * @return true if given list of territories contains all territories of this continent.
	 * Otherwise, return false;
	 */
	public boolean isOwned(LinkedList<String> territoryList) {
		HashMap<String, Boolean> visited = new HashMap<String, Boolean>();
		
		for (String territory : this.territoryList) {
			visited.put(territory, false);
		}
		
		for (String territory : territoryList) {
			visited.replace(territory, true);
		}

		return !visited.containsValue(false);
	}
	
}

