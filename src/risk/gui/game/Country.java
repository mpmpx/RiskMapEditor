package risk.game;

import java.awt.Point;
import java.util.LinkedList;

/**
 * Country class manages Country information of a *.map file.
 */

public class Country {
	public static final int MAX_ADJACENT_COUNTRIES = 10;
	private String name;
	//continent of which that Country belongs to
	private String continent;
	private Point location;
	//all the adjacent country list
	private LinkedList<Point> adjacentCountryList;

	/**
	 * Constructor of the class. Initialize all class variables.
	 */
	public Country() {
		location = new Point();
		adjacentCountryList = new LinkedList<Point>();
	}

	/**
	 * Constructor of the class. Initialize with name.
	 */
	public Country(String name) {
		this.name = name;
		location = new Point();
		adjacentCountryList = new LinkedList<Point>();
	}

	/**
	 * Constructor of the class. Initialize with location.
	 */
	public Country(Point location) {
		this.location = location;
		adjacentCountryList = new LinkedList<Point>();
	}
	
	/**
	 * Set the name of the country.
	 * @param name is the name to be assigned to the country.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Get the name of the country.
	 * @return name of the country.
	 */	
	public String getName() {
		return name;
	}

	/**
	 * Set the continent of the country.
	 * @param name is the name of the continent to be assigned to the country.
	 */	
	public void setContinentName(String name) {
		continent = name;
	}

	/**
	 * Get the name of the continent of the country.
	 * @return the name of the continent of the country.
	 */	
	public String getContinentName() {
		return continent;
	}

	/**
	 * Set the location of the country with x and y coordinates.
	 * @param x is the x coordinate of the location.
	 * @param y is the y coordinate of the location.
	 */	
	public void setLocation(int x, int y) {
		location = new Point(x, y);
	}

	/**
	 * Set the location of the country with a point.
	 * @param location is the location to be assigned to the country.
	 */	
	public void setLocation(Point location) {
		this.location = location;
	}

	/**
	 * Get the location of the country.
	 * @return the location of the country.
	 */	
	public Point getLocation() {
		return location;
	}
	
	/**
	 * Get the x coordinate of the location of the country.
	 * @return the x coordinate of the location of the country.
	 */
	public int getX() {
		return location.x;
	}

	/**
	 * Get the y coordinate of the location of the country.
	 * @return the y coordinate of the location of the country.
	 */	
	public int getY() {
		return location.y;
	}

	/**
	 * Method to add a new location to country list.
	 * @param location new location
	 */
	public void addAdjacentCountry(Point location) {
		if (!adjacentCountryList.contains(location)) {
			adjacentCountryList.add(location);
		}
	}

	/**
	 * Method to remove a exist Country from the AdjacentCountry list
	 * @param country  the name of Country to be removed
	 * @return true if the adjacent country is successfully removed. Otherwise, false. 
	 */
	public boolean removeAdjacentCountry(String name) {
		return adjacentCountryList.remove(name);
	}
	
	/**
	 * Get the list of the adjacent countries of the country.
	 * @return list of the adjacent countries of the country.
	 */
	public LinkedList<Point> getAdjacentCountryList() {
		return adjacentCountryList;
	}
}