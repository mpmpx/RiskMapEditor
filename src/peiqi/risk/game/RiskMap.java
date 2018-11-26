package risk.game;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * This class represents a map of Risk game. It maintains an image of the map and 
 * all continents, territories and links.
 */

public class RiskMap implements Serializable{
	public final static int MAX_CONTINENT = 32;
	public final static int MAX_COUNTRY = 255;
	public final static int MAX_ADJACENT_COUNTRIES = 10;
	
	private String imagePath;
	private HashMap<String, Continent> continentMap;
	private HashMap<String, Territory> territoryMap;
	private HashMap<String, LinkedList<String>> edgeMap;

	/**
	 * Creates a map of Risk.
	 * 
	 */
	public RiskMap() {
		continentMap = new HashMap<String, Continent>();
		territoryMap = new HashMap<String, Territory>();
		edgeMap = new HashMap<String, LinkedList<String>>();
	}
	
	/**
	 * Clears all variables of the class.
	 */
	public void clear() {
		continentMap.clear();
		territoryMap.clear();
		edgeMap.clear();
	}
	
	/**
	 * Resets the territory.
	 */
	public void reset() {
		for (Territory territory : territoryMap.values()) {
			territory.setOwner(null);
			territory.setColor(null);
			territory.setArmy(0);
		}
	}
	
	/**
	 * Sets the image of the map.
	 * @param imagePath the path of the image file.
	 */
	public void setImage(String imagePath) {
		this.imagePath = imagePath;
	}
	
	/**
	 * Returns the path of map image.
	 * @return the path of map image.
	 */
	public String getImagePath() {
		return imagePath;
	}
	
	/**
	 * Adds a new continent.
	 * @param newContinent a continent that is to be added.
	 */
	public void addContinent(Continent newContinent) {
		continentMap.put(newContinent.getName(), newContinent);
	}
	
	/**
	 * Returns the hash map contains all territories.
	 * @return continentMap the hash map contains all territories.
	 */
	public HashMap<String, Continent> getContinentMap() {
		return continentMap;
	}
	
	/**
	 * Adds a new territory.
	 * @param territory a territory that is to be added.
	 */
	public void addTerritory(Territory territory) {
		territoryMap.put(territory.getName(), territory);
	}
	
	/**
	 * Updates the territory information.
	 * @param territory the territory that is to be updated.
	 */
	public void updateTerritory(Territory territory) {
		territoryMap.replace(territory.getName(), territory);
	}
	
	/**
	 * Returns the territory map.
	 * @return he territory map.
	 */
	public HashMap<String, Territory> getTerritoryMap() {
		return territoryMap;
	}
	
	/**
	 * Adds links of a territory.
	 * @param territory add links to this territory.
	 * @param adjacentList a list of territories link to the given territory.
	 */
	public void addLink(String territory, LinkedList<String> adjacentList) {
		edgeMap.put(territory, adjacentList);
	}
	
	/**
	 * Adds a hash map contains all links.
	 * @param newEdgeMap a hash map contains all links.
	 */
	public void addLink(HashMap<String, LinkedList<String>> newEdgeMap) {
		this.edgeMap = newEdgeMap;
	}
	
	/**
	 * Returns the hash map contains all links.
	 * @return the hash map contains all links.
	 */
	public HashMap<String, LinkedList<String>> getEdgeMap() {
		return edgeMap;
	}
}
