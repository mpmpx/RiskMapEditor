package risk.game.strategy;

import java.io.Serializable;
import java.util.LinkedList;

import risk.game.Player;
import risk.game.Territory;

public class CheaterStrategy implements Strategy, Serializable {
	
	private Player player;
	private Behavior behavior;
	
	public CheaterStrategy(Player player) {
		this.player = player;
		behavior = Behavior.CHEATER;
	}
	
	@Override
	public void startup() {
		while (player.getUnassignedArmy() != 0) {
			Territory randomTerritory = player.getRandomTerritory();
			player.placeUnassignedArmy(randomTerritory, 1);
		}
	}

	@Override
	public void reinforce() {
		for (Territory territory : player.getTerritoryMap().values()) {
			player.setUnsignedArmy(territory.getArmy());
			player.placeUnassignedArmy(territory, player.getUnassignedArmy());
		}
	}

	@Override
	public void attack() {
		player.updateAttackableMap();
		
		for (LinkedList<Territory> attackableList : player.getAttackableMap().values()) {
			for (Territory territory : attackableList) {
				if (territory.getOwner() != player) {
					territory.getOwner().removeTerritory(territory);
					territory.setOwner(player);
					territory.setArmy(1);
					territory.setColor(player.getColor());
					player.addTerritory(territory);
				}
			}
		}
	}

	@Override
	public void fortify() {
		player.updateAttackableMap();
		for (Territory territory : player.getTerritoryMap().values()) {
			for (Territory adjacent : territory.getAdjacent().values()) {
				if (adjacent.getOwner() != player) {
					player.setUnsignedArmy(territory.getArmy()); 
					player.placeUnassignedArmy(territory, player.getUnassignedArmy());
					continue;
				}
			}
		}
	}

	@Override
	public Behavior getBehavior() {
		return behavior;
	}

}
