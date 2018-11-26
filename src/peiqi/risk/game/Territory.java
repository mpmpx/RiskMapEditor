package risk.game;

import java.awt.Color;
import java.awt.Point;
import java.util.LinkedList;

/**
 * This class represents a territory and maintains status and data including name, color, owner,
 * location, number of armies, shape and territory it belongs to.
 */
public class Territory {

	private String name;
	private Player owner;
	private String continentName;
	private Point location;
	private Color ownerColor;
	private int army;
	private LinkedList<Point> shape;
	
	/**
	 * Creates a territory.
	 */
	public Territory() {
		shape = new LinkedList<Point>();
		army = 0;
	}
	/**
	 * Creates a territory with a name and location coordinates
	 * @param name a string that is to be name of the territory.
	 * @param location a point that is to be the location of this territory.
	 */
	
	public Territory(String name, Point location) {
		this.name = name;
		continentName = null;
		this.location = location;
		army = 0;
	}
	
	/**
	 * Sets the name of the territory.
	 * @param name a string that is to be name of the territory.
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Returns the name of the territory.
	 * @return name of the territory.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Sets the owner of this territory.
	 * @param player a player that is to be the owner of this territory.
	 */
	public void setOwner(Player player) {
		this.owner = player;
	}
	
	/**
	 * Returns the owner of this territory.
	 * @return the owner of this territory.
	 */
	public Player getOwner() {
		return owner;
	}
	
	/**
	 * Sets name of the continent of this territory.
	 * @param continentName a string that is to be name of the continent of the territory.
	 */
	public void setContinentName(String continentName) {
		this.continentName = continentName;
	}
	
	/**
	 * Returns the name of the continent of the territory.
	 * @return name of the continent of the territory.
	 */
	public String getContinentName() {
		return continentName;
	}
	
	/**
	 * Sets the location of this territory with a point.
	 * @param location a point. The new location of the territory.
	 */
	public void setLocation(Point location) {
		this.location = location;
	}
	
	/**
	 * Sets the location of this territory with x and y coordinates.
	 * @param x x coordinate of the new location.
	 * @param y y coordinate of the new location.
	 */
	public void setLocation(int x, int y) {
		location = new Point(x, y);
	}
	
	/**
	 * Returns the location of this territory.
	 * @return the location of this territory.
	 */
	public Point getLocation() {
		return location;
	}
	
	/**
	 * Returns x coordinate of the location of this territory.
	 * @return x coordinate of the location of this territory.
	 */
	public int getX() {
		return location.x;
	}
	
	/**
	 * Returns y coordinate of the location of this territory.
	 * @return y coordinate of the location of this territory.
	 */
	public int getY() {
		return location.y;
	}
	
	/**
	 * Sets color of this territory.
	 * @param ownerColor a color that is to be the color of this territory.
	 */
	public void setColor(Color ownerColor) {
		this.ownerColor = ownerColor;
	}
	
	/**
	 * Returns color of this territory.
	 * @return color of this territory.
	 */
	public Color getColor() {
		return ownerColor;
	}
	
	/**
	 * Sets number of armies of this territory.
	 * @param armyNum an integer that is to be number of armies of this territory.
	 */
	public void setArmy(int armyNum) {
		this.army = armyNum;
	}
	
	/**
	 * Returns number of armies of this territory.
	 * @return number of armies of this territory.
	 */
	public int getArmy() {
		return army;
	}
	
	/**
	 * Adds armies to this territory.
	 * @param army an integer that is the number of armies to be added to this territory.
	 */
	public void addArmy(int army) {
		this.army += army;
	}
	
	/**
	 * Removes armies from this territory.
	 * @param army an integer that is the number of armies to be removed from this territory.
	 */
	public void removeArmy(int army) {
		this.army -= army;
	}
	
	/**
	 * Sets shape of this territory.
	 * @param shape a list of all x-y coordinates in this territory.
	 */
	public void setShape(LinkedList<Point> shape) {
		this.shape = shape;
	}
	
	/**
	 * Returns shape of this territory.
	 * @return list of all x-y coordinates in this territory.
	 */
	public LinkedList<Point> getShape() {
		return shape;
	}
	
	/**
	 * Checks whether the given location is in this territory.
	 * @param location a point that is used for checking.
	 * @return true if the given location is in the territory. Otherwise, false.
	 */
	public boolean contains(Point location) {
		return shape.contains(location);
	}
}
