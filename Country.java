package risk.game;

import java.awt.Point;
import java.util.LinkedList;

/**
 * Country class manages Country information of a *.map file.
 * @author
 * @version 1
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
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public void setContinentName(String name) {
		continent = name;
	}
	
	public String getContinentName() {
		return continent;
	}
	
	public void setLocation(int x, int y) {
		location = new Point(x, y);
	}
	
	public void setLocation(Point location) {
		this.location = location;
	}
	
	public Point getLocation() {
		return location;
	}
	
	public int getX() {
		return location.x;
	}
	
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
	 */
	public boolean removeAdjacentCountry(String name) {
		return adjacentCountryList.remove(name);
	}
	
	public LinkedList<Point> getAdjacentCountryList() {
		return adjacentCountryList;
	}
}