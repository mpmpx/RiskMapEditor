package risk.game;

import java.awt.Color;
import java.awt.Point;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * This class maintains all of the data and functionality that a player would
 * have. 
 */
public class Player{

	private String name;
	private Color color;
	private Cards cards;
	
	private HashMap<String, Territory> territoryMap;
	private HashMap<Continent, Boolean> controlledContinent;
	private int ownedTerritoryNum;
	private int totalArmy;
	private int unassignedArmy;
	private int freeArmy;
	
	private HashMap<String, LinkedList<Territory>> attackableMap;
	private HashMap<String, LinkedList<Territory>> reachableMap;
	
	private boolean hasConquered;
	
	/**
	 * Creates a player with name.
	 * 
	 * @param name a string that is to be the player's name.
	 */
	public Player(String name) {
		this.name = name;
		color = PlayerColor.nextColor();
		cards = new Cards();
		territoryMap = new HashMap<String, Territory>();
		controlledContinent = new HashMap<Continent, Boolean>();
		reachableMap = new HashMap<String, LinkedList<Territory>>();
		attackableMap = new HashMap<String, LinkedList<Territory>>();
		ownedTerritoryNum = 0;
		totalArmy = 0;
		unassignedArmy = 0;
		freeArmy = 0;
		hasConquered = false;
	}

	/**
	 * Sets the color which represents this player.
	 * @param color the color which represents this player.
	 */
	public void setColor(Color color) {
		this.color = color;
	}
	
	/**
	 * Returns the color of this player.
	 * @return the color of this player.
	 */
	public Color getColor() {
		return color;
	}
	
	/**
	 * Returns the player's name.
	 * @return the player's name.
	 */
	
	public String getName() {
		return name;
	}
	

	/**
	 * Sets controlled continents of this player.
	 * @param continentList a list of continents that is to be set
	 * as controlled continents of this player.
	 */
	public void setContinents(LinkedList<Continent> continentList) {
		for (Continent continent : continentList) {
			controlledContinent.put(continent, false);
		}
	}
	
	/**
	 * Sets unassigned armies to this player.
	 * @param army the number of army that is to be set as 
	 * unassigned armies of this player.
	 */
	public void setUnsignedArmy(int army) {
		unassignedArmy = army;
		totalArmy += army;
		for (Territory territory : territoryMap.values()) {
			territory.addArmy(1);
			totalArmy++;
		}
	}
	
	/**
	 * This player gets reinforcement. The number of reinforcement is based on
	 * the number of controlled territories and continents of this player.
	 */
	public void getReinforcement() {
		unassignedArmy = 0;
		freeArmy = 0;

		freeArmy = (int) Math.max(Math.floor(ownedTerritoryNum / 3), 3);
		unassignedArmy  += freeArmy;
		
		for (Continent continent : controlledContinent.keySet()) {
			if (controlledContinent.get(continent)) {
				unassignedArmy += continent.getValue();
			}
		}
		
		totalArmy += unassignedArmy;
	}
	
	/**
	 * Reinforces armies to the given territory.
	 * @param territory the territory that is to be reinforced by armies.
	 * @param armyNum the number of armies that is to be assigned to the given
	 * territory.
	 */
	public void reinforce(Territory territory, int armyNum) {
		territory.addArmy(armyNum);
		unassignedArmy -= armyNum;
	}

	/**
	 * Update attackable hash map.
	 * @param attackableMap the hash map that is to be updated.
	 */
	public void updateAttackableMap(HashMap<String, LinkedList<Territory>> attackableMap) {
		this.attackableMap = attackableMap;
	}
	
	/**
	 * Returns attackable hash map.
	 * @return attackable hash map.
	 */
	public HashMap<String, LinkedList<Territory>> getAttackableMap() {
		return attackableMap;
	}
	
	/**
	 * Attacks a territory of enemy by one of territory.
	 * @param attacker the attacking territory.
	 * @param defender the enemy's attacked territory.
	 * @param attackerCasualties attacker's casualties.
	 * @param defenderCasualties defender's casualties.
	 */
	public void attack(Territory attacker, Territory defender, int attackerCasualties, int defenderCasualties) {
		killArmy(attacker.getName(), attackerCasualties);		
		defender.getOwner().killArmy(defender.getName(), defenderCasualties);
		
		if (defender.getArmy() == 0) {
			defender.getOwner().removeTerritory(defender);
			defender.setColor(color);
			defender.setOwner(this);
			addTerritory(defender);
			hasConquered = true;
		}
		
	}
	
	/**
	 * Updates controlled continents.
	 */
	private void updateControlledContinent() {
		LinkedList<String> territoryList = new LinkedList<String>();
		controlledContinent.clear();
		
		for (Territory territory : territoryMap.values()) {
			territoryList.add(territory.getName());
		}
		
		for (Continent continent : controlledContinent.keySet()) {
			if (continent.isOwned(territoryList)) {
				controlledContinent.replace(continent, true);
			}
		}
	}
	
	/**
	 * Update the reachable hash map.
	 * @param reachableMap the reachable hash map.
	 */
	public void updateReachableMap(HashMap<String, LinkedList<Territory>> reachableMap) {
		this.reachableMap = reachableMap;
	}

	/**
	 * Returns the reachable hash map.
	 * @return the reachable hash map.
	 */
	public HashMap<String, LinkedList<Territory>> getReachableMap() {
		return reachableMap;
	}
	
	/**
	 * Moves armies from one territory to another one in fortification phase.
	 * @param departureTerritory moves armies from this territory.
	 * @param arrivalTerritory moves armies to this territory.
	 * @param armyNum the number of armies that are to be moved.
	 */
	public void fortify(Territory departureTerritory, Territory arrivalTerritory, int armyNum) {
		departureTerritory.removeArmy(armyNum);
		arrivalTerritory.addArmy(armyNum);

		if (hasConquered) {
			cards.getCard();
			hasConquered = false;
		}
	}
	

	/**
	 * Adds a new territory.
	 * @param territory the territory that is to be added.
	 */
	public void addTerritory(Territory territory) {
		territoryMap.put(territory.getName(), territory);
		ownedTerritoryNum++;
		updateControlledContinent();
	}
	
	/**
	 * Removes a territory.
	 * @param territory the territory that is to be removed.
	 */
	public void removeTerritory(Territory territory) {
		totalArmy -= territory.getArmy();
		territoryMap.remove(territory.getName());
		ownedTerritoryNum--;
		updateControlledContinent();
	}
	
	/**
	 * Returns the territory based on given coordinates.
	 * @param x x coordinate of the territory.
	 * @param y y coordinate of the territory.
	 * @return the territory with given x and y coordinates.
	 */
	public Territory getTerritory(int x, int y) {
		for (Territory territory : territoryMap.values()) {
			if (territory.getShape().contains(new Point(x, y))) {
				return territory;
			}
			
		}
		return null;
	}
	
	/**
	 * Returns the hash map contains all territory controlled by this player.
	 * @return territoryMap the territory hash map.
	 */
	public HashMap<String, Territory> getTerritoryMap() {
		return territoryMap;
	}
	
	/**
	 * Returns number of unassigned armies.
	 * @return number of unassigned armies. 
	 */
	public int getUnassignedArmy() {
		return unassignedArmy;
	}
	
	/**
	 * Returns the number of total armies.
	 * @return the number of total armies.
	 */
	public int getTotalArmy() {
		return totalArmy;
	}
	
	/**
	 * Returns the number of armies given by system in the reinforcement phase.
	 * @return the number of armies given by system in the reinforcement phase.
	 */
	public int getFreeArmy() {
		return freeArmy;
	}
	
	/**
	 * Returns the hash map contains the controlled continents.
	 * @return the hash map contains the controlled continents.
	 */
	public HashMap<Continent, Boolean> getControlledContinent() {
		return controlledContinent;
	}
	
	/**
	 * Adds armies to a territory.
	 * @param territory the territory that adds some armies.
	 * @param armyNum the number of armies that are to be added to the given territory.
	 */
	public void addArmy(String territory, int armyNum) {
		territoryMap.get(territory).addArmy(armyNum);
		unassignedArmy -= armyNum;
	}
	
	/**
	 * Removes armies from a territory.
	 * @param territory the territory which removes some armies.
	 * @param armyNum the number of armies that are to be removed from the given territory.
	 */
	public void removeArmy(String territory, int armyNum) {
		territoryMap.get(territory).removeArmy(armyNum);
	}
	
	/**
	 * Removes armies from a territory and deducts the total armies.
	 * @param territory the territory that removes armies.
	 * @param army the number of armies that are to be removed from the given territory.
	 */
	public void killArmy(String territory, int army) {
		territoryMap.get(territory).removeArmy(army);
		totalArmy -= army;
	}
	
	/**
	 * Exchange hand cards and receives bonus armies.
	 * @param exchangeCards cards that are to be exchanged.
	 * @param exchangeBonusArmy the number of armies assigned to this player.
	 */
	public void exchangeCards(LinkedList<Integer> exchangeCards, int exchangeBonusArmy) {
		cards.removeCards(exchangeCards);
		totalArmy += exchangeBonusArmy;
		unassignedArmy += exchangeBonusArmy;
	}
	
	/**
	 * Returns player's hand cards.
	 * @return player's hand cards.
	 */
	public Cards getCardSet() {
		return cards;
	}
	
	/**
	 * Gets a new card.
	 */
	public void getNewCard() {
		if (hasConquered) {
			cards.getCard();
		}
		hasConquered = false;
	}
}
