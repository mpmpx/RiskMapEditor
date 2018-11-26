package risk.game;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Observable;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.Stack;

/**
 * The Game class simulates game process of the Risk game. It maintains status of game including 
 * current playing phase and player, and simulates game process and players' moves.
 */
public class Game extends Observable{
 	
	private Phase phase;
	private int playerNum;
	private Player[] players;
	private Player currentPlayer;
	private LinkedList<Player> playerQueue;
	private RiskMap map;
	private int exchangeBonusArmy;
	private PriorityQueue<Integer> attackerDice;
	private PriorityQueue<Integer> defenderDice;
	private Territory attacker;
	private Territory defender;
	private Player winner;
	
	/**
	 * Creates a game with initial exchange bonus armies with 5.
	 */
	public Game() {
		playerNum = 0;
		exchangeBonusArmy = 5;
		playerQueue = new LinkedList<Player>();
		attackerDice = new PriorityQueue<Integer>();
		defenderDice = new PriorityQueue<Integer>();
	}
	
	/**
	 * Sets number of players of this game;
	 * @param playerNum an integer that is to be set as number
	 * of players of this game.
	 */
	public void setPlayers (LinkedList<String> behaviours) {
		playerNum = behaviours.size();
		players = new Player[playerNum];
		for (int i = 0; i < playerNum; i++) {
			players[i] = new Player("player" + (i + 1));
			playerQueue.add(players[i]);
		}
		
		currentPlayer = players[0];
	}
	
	/**
	 * Returns number of players of this game.
	 * @return number of players of this game.
	 */
	public int getPlayerNum () {
		return playerNum;
	}
	
	/**
	 * Returns all players of this game.
	 * @return an array contains all players of this game.
	 */
	public Player[] getPlayers() {
		return players;
	}
	
	/**
	 * Sets the map of this game.
	 * @param map a map that is to be the map of this game.
	 */
	public void setMap(RiskMap map) {
		this.map = map;
	}
	
	/**
	 * Returns the map of this game.
	 * @return the map of this game.
	 */
	public RiskMap getMap() {
		return map;
	}
	
	public Player getWinner() {
		return winner;
	}
	
	/**
	 * Randomly distributes all territories to players. 
	 */
	public void distributeTerritories() {
		for (Territory territory : map.getTerritoryMap().values()) {
			Random r = new Random();
			Player player = players[r.nextInt(playerNum)];
			territory.setColor(player.getColor());
			territory.setOwner(player);
			player.addTerritory(territory);
		}
	}
	
	/**
	 * Starts the game. Create the phase and assign initial armies to players.
	 */
	public void start() {
		phase = new Phase();
		setInitialArmy();
		setChanged();
		notifyObservers();
	}
	
	/**
	 * Sets initial armies to players. Number of initial armies is based on
	 * number of players.
	 */
	public void setInitialArmy() {
		for (int i = 0; i < playerNum; i++) {
			switch (playerNum) {
				case 2 : {
					players[i].setUnsignedArmy(40);
					break;
				}
				case 3 : {
					players[i].setUnsignedArmy(35);
					break;
				}
				case 4 : {
					players[i].setUnsignedArmy(30);
					break;
				}
				case 5 : {
					players[i].setUnsignedArmy(25);
					break;
				}
				case 6 : {
					players[i].setUnsignedArmy(20);
					break;
				}
				default : {
					break;
				}
			}			
		}
	}
	
	/**
	 * Proceeds game to next phase.
	 */
	public void nextPhase() {
		int currentPhase = phase.getCurrentPhase();
		
		switch (phase.getCurrentPhase()) {
			case Phase.STARTUP : {
				if (currentPlayer == players[playerNum - 1]) {
					phase.nextPhase();
				}
				currentPlayer = nextPlayer();
				break;
			}
			case Phase.REINFORCEMENT : {
				phase.nextPhase();
				updateAttackableMap();
				break;
			}
			case Phase.ATTACK : {
				phase.nextPhase();
				attackerDice.clear();
				defenderDice.clear();
				updateReachableMap();
				break;
			}
			case Phase.FORTIFICATION : {
				currentPlayer.getNewCard();
				phase.nextPhase();
				currentPlayer = nextPlayer();
				break;
			}
			default : {
				break;
			}
		}
		
		if (phase.getCurrentPhase() == Phase.REINFORCEMENT) {
			currentPlayer.getReinforcement();
		}
		
		setChanged();
		notifyObservers();
	}
	
	/**
	 * Returns the current phase.
	 * @return the current phase.
	 */
	public int getCurrentPhase() {
		return phase.getCurrentPhase();
	}	
	
	/**
	 * Proceeds to next player's turn.
	 * @return
	 */
	private Player nextPlayer() {
		Player player = playerQueue.pop();
		playerQueue.add(player);
		return playerQueue.peek();
	}
	
	/**
	 * Returns the current player.
	 * @return the current player.
	 */
	public Player getCurrentPlayer() {
		return currentPlayer;
	}
	
	/**
	 * Reinforces given number of armies to given territory of the current player.
	 * @param territory the territory that is to be reinforced.
	 * @param armyNum the number of armies to be added to a territory.
	 */
	public void reinforce(Territory territory, int armyNum) {
		currentPlayer.reinforce(territory, armyNum);
		if (currentPlayer.getUnassignedArmy() == 0) {
			nextPhase();
		}
		setChanged();
		notifyObservers();
	}
	
	/**
	 * Sets up an attack with attacker and defender.
	 * @param attacker the territory commits to attack.
	 * @param defender the territory defends itself from an attack.
	 */
	public void setupAttack(Territory attacker, Territory defender) {
		this.attacker = attacker;
		this.defender = defender;
		setChanged();
		notifyObservers();
	}
	
	/**
	 * Returns attacking territory.
	 * @return attacking territory.
	 */
	public Territory getAttacker() {
		return attacker;
	}
	
	/**
	 * Returns defending territory.
	 * @return defending territory.
	 */
	public Territory getDefender() {
		return defender;
	}
	
	/**
	 * Checks whether the current player is able to commit to attack.
	 * @return true if the current player is able to commit to attack.
	 * Otherwise, false.
	 */
	public boolean checkAttackPhase() {
		for (String territory : currentPlayer.getAttackableMap().keySet()) {
			if (!currentPlayer.getAttackableMap().get(territory).isEmpty()) {
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Attacking territory attacks defending territory. 
	 * @param attackerDiceNum number of dice used by attacker.
	 * @param defenderDiceNum number of dice used by defender.
	 */
	public void attack(int attackerDiceNum, int defenderDiceNum) {
		int attackerCasualties = 0;
		int defenderCasualties = 0;
		
		Player defenderPlayer = defender.getOwner();
		
		attackerDice = rollDice(attackerDiceNum);
		defenderDice = rollDice(defenderDiceNum);
	
		PriorityQueue<Integer> attackerDiceCopy = new PriorityQueue<Integer>(Collections.reverseOrder());
		attackerDiceCopy.addAll(attackerDice);
		PriorityQueue<Integer> defenderDiceCopy = new PriorityQueue<Integer>(Collections.reverseOrder());
		defenderDiceCopy.addAll(defenderDice);
	
		while (!attackerDiceCopy.isEmpty() && !defenderDiceCopy.isEmpty()) {
			if (attackerDiceCopy.remove() > defenderDiceCopy.remove()) {
				defenderCasualties++;
			} 
			else {
				attackerCasualties++;
			}
		}
		
		currentPlayer.attack(attacker, defender, attackerCasualties, defenderCasualties);
		
		if (defender.getOwner() == currentPlayer) {
			attacker = null;
			defender = null;
		} else {
			updateAttackableMap();
			if (checkAttackPhase() == false) {
				nextPhase();
				return;
			}
		}
		
		if (defenderPlayer.getTotalArmy() == 0) {
			int index = 0;
			playerNum--;
			Player[] newPlayers = new Player[playerNum];
			
			for (Player player : players) {
				if (player != defenderPlayer) {
					newPlayers[index++] = player;
				}
			}
			players = newPlayers;
			playerQueue.remove(defenderPlayer);
		}
		
		setChanged();
		notifyObservers();
		
		if (players.length == 1) {
			winner = currentPlayer;
		}
	}
	
	/**
	 * Rolls some dice and returns the result.
	 * @param diceNum the number of dice.
	 * @return a priority queue contains the results of rolling.
	 */
	private PriorityQueue<Integer> rollDice(int diceNum) {
		Random r = new Random();
		PriorityQueue<Integer> dice = new PriorityQueue<Integer>();
		for (int i = 0; i < diceNum; i++) {
			dice.add(r.nextInt(6) + 1);
		}
		
		return dice;
	}

	/**
	 * Returns results of dice rolling by attacker.
	 * @return results of dice rolling by attacker.
	 */
	public PriorityQueue<Integer> getAttackerDice() {
		return attackerDice;
	}
	
	/**
	 * Returns results of dice rolling by defender.
	 * @return results of dice rolling by defender.
	 */
	public PriorityQueue<Integer> getDefenderDice() {
		return defenderDice;
	}
	
	/**
	 * Updates the hash map containing all enemies' territories that the current
	 * player can attack.
	 */
	public void updateAttackableMap() {
		HashMap<String, LinkedList<Territory>> attackableMap = new HashMap<String, LinkedList<Territory>>();

		for (Territory territory : currentPlayer.getTerritoryMap().values()) {
			attackableMap.put(territory.getName(), getAttackableList(territory));
		}

		currentPlayer.updateAttackableMap(attackableMap);
		setChanged();
		notifyObservers();
	}
	
	/**
	 * Returns a list of territories which is able to be attacked by the given territory.
	 * @param territory a list of territories which is able to be attacked by this territory
	 * will be returned.
	 * @return attackableList a list of territories which is able to be attacked by the 
	 * given territory.
	 */
	private LinkedList<Territory> getAttackableList(Territory territory) {
		LinkedList<Territory> attackableList = new LinkedList<Territory>();
		HashMap<String, LinkedList<String>> edgeMap = map.getEdgeMap();
		
		if (territory.getArmy() > 1) {
			for (String adjacent : edgeMap.get(territory.getName())) {
				Territory adjacentTerritory = map.getTerritoryMap().get(adjacent);
				if (!currentPlayer.getTerritoryMap().containsKey(adjacentTerritory.getName())) {
					attackableList.add(map.getTerritoryMap().get(adjacent));
				}
			}	
		}
		return attackableList;
	}
	
	/**
	 * Moves armies from the attacking territory to the recently conquered territory.
	 * @param attacker the attacking territory.
	 * @param defender the recently conquered territory.
	 * @param armyNum number of armies that is to be moved from the attacking territory
	 * to the conquered territory.
	 */
	public void conquer(Territory attacker, Territory defender, int armyNum) {
		currentPlayer.addArmy(defender.getName(), armyNum);
		currentPlayer.removeArmy(attacker.getName(), armyNum);
		updateAttackableMap();
		
		if (checkAttackPhase() == false) {
			nextPhase();
		}
	}
	
	/**
	 * The current player moves armies from one territory to another territory.
	 * @param departureTerritory moves armies from this territory.
	 * @param arrivalTerritory moves armies to this territory.
	 * @param armyNum number of armies that is to be moved.
	 */
	public void fortify(Territory departureTerritory, Territory arrivalTerritory, int armyNum) {
		currentPlayer.fortify(departureTerritory, arrivalTerritory, armyNum);
		setChanged();
		notifyObservers();
	}
	
	/**
	 * Update the hash map contains all territories which is able to be connected
	 * to a territory by a path.
	 */
	public void updateReachableMap() {
		HashMap<String, LinkedList<Territory>> reachableMap = new HashMap<String, LinkedList<Territory>>();
		
		for (Territory territory : map.getTerritoryMap().values()) {
			reachableMap.put(territory.getName(), getReachableList(territory));
		}
		
		currentPlayer.updateReachableMap(reachableMap);
	}
	
	/**
	 * Returns a list of territories which are able to be connected by the given territory by a path.
	 * @param territory a list of territories which are able to be connected by this territory by a path
	 * will be returned.
	 * @return reachableList a list of territories which are able to be connected by the given 
	 * territory by a path.
	 */
	private LinkedList<Territory> getReachableList(Territory territory) {
		LinkedList<Territory> reachableList = new LinkedList<Territory>();
		Stack<Territory> stack = new Stack<Territory>();
		HashMap<String, LinkedList<String>> edgeMap = map.getEdgeMap();
		HashMap<String, Territory> territoryMap = currentPlayer.getTerritoryMap();
		
		if (territory.getArmy() > 1) {
			for (String adjacent : edgeMap.get(territory.getName())){
				if (territoryMap.containsKey(adjacent)) {
					stack.push(territoryMap.get(adjacent));
				}
			}
		
			while (!stack.isEmpty()) {
				Territory currentTerritory = stack.pop();
				reachableList.add(currentTerritory);
			
				for (String adjacent : edgeMap.get(currentTerritory.getName())){
					if (territoryMap.containsKey(adjacent)) {
						if (!adjacent.equals(territory.getName()) && !reachableList.contains(territoryMap.get(adjacent))) {
							stack.push(territoryMap.get(adjacent));
						}
					}
				}
			}
		}
		
		return reachableList;
	}
	
	/**
	 * The current player exchanges a set of cards and receives bonus armies.
	 * @param cards a set of hand cards that are to be exchanged.
	 */
	public void exchangeCards(LinkedList<Integer> cards) {
		currentPlayer.exchangeCards(cards, exchangeBonusArmy);
		exchangeBonusArmy += 5;
		setChanged();
		notifyObservers();
	}
	
	/**
	 * Returns current number of exchange bonus armies.
	 * @return current number of exchange bonus armies.
	 */
	public int getExchangeBonusArmy() {
		return exchangeBonusArmy;
	}
}
