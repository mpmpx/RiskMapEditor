package risk.game;

import java.awt.Point;
import java.util.LinkedList;

public class Country {
	public static final int MAX_ADJACENT_COUNTRIES = 10;
	private String name;
	private String continent;
	private Point location;
	private LinkedList<Point> adjacentCountryList;
	
	public Country() {
		location = new Point();
		adjacentCountryList = new LinkedList<Point>();
	}

	public Country(String name) {
		this.name = name;
		location = new Point();
		adjacentCountryList = new LinkedList<Point>();
	}
	
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
	
	public void addAdjacentCountry(Point location) {
		if (!adjacentCountryList.contains(location)) {
			adjacentCountryList.add(location);
		}
	}
	
	public boolean removeAdjacentCountry(String name) {
		return adjacentCountryList.remove(name);
	}
	
	public LinkedList<Point> getAdjacentCountryList() {
		return adjacentCountryList;
	}
}