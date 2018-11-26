package game;

import static org.junit.Assert.*;

import java.awt.Point;
import java.util.HashMap;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import risk.game.Player;
import risk.game.Territory;


public class TestStartup {

	Player player;
	HashMap<String, Territory> territoryMap;
	
	@BeforeClass
	public static void beforeTest() {
		System.out.println("Start to test validation of startup phase.");
	}

	@Before
	public void before() {
		player = new Player("player");
		player.setUnsignedArmy(20);
		player.addTerritory(new Territory("a", new Point(0, 1)));
		player.addTerritory(new Territory("b", new Point(0, 2)));
		territoryMap = player.getTerritoryMap();
	}
	
	@Test
	public void testStartup() {
		
		player.addArmy(territoryMap.get("a").getName(), 12);
		player.addArmy(territoryMap.get("b").getName(), 8);
		
		assertEquals(0, player.getUnassignedArmy());
		System.out.println("Successfully test validation of startup phase.");
	}

	@AfterClass
	public static void afterTest() {
		System.out.println("Finish testing validation of startup phase.");
		System.out.println();
	}
}
