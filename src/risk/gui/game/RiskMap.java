package risk.game;

import java.awt.Dimension;
import java.awt.Point;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import javax.naming.SizeLimitExceededException;

/**
 * RiskMap class manages add or remove the element of a related list.
 */

public class RiskMap {

	//the max number of continent
	public static final int MAX_CONTINENT = 32;
	//the max number of country
	public static final int MAX_COUNTRY = 255;

	//the dimension of the map show
	private Dimension size;
	private HashMap<String, Continent> continentList;
	private HashMap<Point, Country> countryList;

	/**
	 * Constructor of the class. Initialize all class variables.
	 */
	public RiskMap() {
		continentList = new HashMap<String, Continent>();
		countryList = new HashMap<Point, Country>();
		size = new Dimension();
	}

	/**
	 * Set the size of the map with given width and height.
	 * @param newSize is a Dimension type which contains width and height.
	 */	
	public void setSize(Dimension newSize) {
		size.setSize(newSize);
	}

	/**
	 * Set the size of the map with given width and height.
	 * @param x is the width of the map.
	 * @param y is the height of the map.
	 */	
	public void setSize(int x, int y) {
		size.setSize(x, y);
	}

	/**
	 * Get the size of the map.
	 * @return size of the map stored in a Dimension type.
	 */	
	public Dimension getSize() {
		return size;
	}

	/**
	 * Method to add a new Continent to the continentList
	 * @param name   the name of the new continent
	 * @param value  the value of the new continent
	 * @return   false if the continentList is full, true if the
	 *           continentList is not full.
	 */
	public boolean addContinent(String name, int value) {
		if (continentList.size() > MAX_CONTINENT) {
			return false;
		}
		
		Continent newContinent = new Continent(name, value);
		continentList.put(newContinent.getName(), newContinent);
		return true;
	}

	/**
	 * Method to add a new Continent to the continentList
	 * @param continent a new continent
	 * @return   false if the continentList is full, true if the
 	 *           continentList is not full.
	 */
	public boolean addContinent(Continent continent) {
		if (continentList.size() > MAX_CONTINENT) {
			return false;
		}
		
		continentList.put(continent.getName(), continent);
		return true;
	}

	/**
	 * Method to remove a exist Continent from the continentList
	 * @param continentName  the name of continent to be removed
	 */
	public void removeContinent(String continentName) {
		for (Country country : countryList.values()) {
			if (country.getContinentName() == continentName) {
				country.setContinentName("unsigned");
			}
		}
		continentList.remove(continentName);
	}

	/**
	 * Method to get the continentList
	 * @return list an instance of ContinentList.
	 */
	public LinkedList<Continent> getContinentList() {
		LinkedList<Continent> list = new LinkedList<Continent>();
		for (Continent continent : continentList.values()) {
			list.add(continent);
		}
		
		return list;
	}

	/**
	 * Method to add a new country to the countryList
	 * @param country  a new country
	 * @return   false if the countrylist is full, true if the
	 *           countrylist is not full
	 */
	public boolean addCountry(Country country) {
		if (countryList.size() > MAX_COUNTRY) {
			return false;
		}
		
		countryList.put(country.getLocation(), country);
		return true;
	}

	/**
	 * Method to remove a exist country from the countryList
	 * @param country  the name of continent to be removed.
	 */
	public void removeCountry(Country country) {
		Country removedCountry = countryList.remove(country.getLocation());
		
		for (Country c : countryList.values()) {
			c.removeAdjacentCountry(removedCountry.getName());
		}
	}
	
	/**
	 * Get a country in the map according to the given location.
	 * @return a country in the map according to the given location.
	 */
	public Country getCountry(Point location) {
		return countryList.get(location);
	}

	/**
	 * Method to get the CountryList
	 * @return list an instance of CountryList.
	 */
	public LinkedList<Country> getCountryList() {
		LinkedList<Country> list = new LinkedList<Country>();
		for (Country country : countryList.values()) {
			list.add(country);
		}
		
		return list;
	}
}





