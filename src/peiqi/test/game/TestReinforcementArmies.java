package game;

import static org.junit.Assert.*;

import java.util.LinkedList;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import risk.game.Continent;
import risk.game.Player;
import risk.game.RiskMap;
import risk.game.Territory;

public class TestReinforcementArmies {
	
	Player player = new Player("player");
	Continent continent = new Continent("continent", 5);
	LinkedList<Territory> territoryList = new LinkedList<Territory>();
	RiskMap map = RiskMap.getInstance();
	
	
	@BeforeClass
	public static void beforeTest() {
		System.out.println("Start to test calculation of number of reinforcement armies.");
	}

	@Test
	public void testReinforcementCalculation() {
		map.addContinent(continent);
		for (int i = 0; i < 14; i++) {
			Territory territory = new Territory();
			territory.setName(i + "");
			territory.setContinentName("continent");
			map.addTerritory(territory);
			player.addTerritory(territory);
		}
	
		player.reinforcement();
		assertEquals(9, player.getTotalArmy());
		System.out.println("Successfully test the calculation of number of reinforcement armies.");
	}

	@AfterClass
	public static void afterTest() {
		System.out.println("Finish testing calculation of number of reinforcement armies.");
		System.out.println();
	}
}
