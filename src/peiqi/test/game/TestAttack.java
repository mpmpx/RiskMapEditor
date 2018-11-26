package game;

import static org.junit.Assert.*;

import java.nio.file.Paths;
import java.util.HashMap;
import java.util.LinkedList;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import risk.controller.ReadFileController;
import risk.game.Player;
import risk.game.RiskMap;
import risk.game.Territory;


public class TestAttack {
	
	RiskMap map;
	ReadFileController controller;
	String rootDir;
	Player player;
	Player player2;
	HashMap<String, Territory> territoryMap;
	HashMap<String, LinkedList<Territory>> result;
	
	@BeforeClass
	public static void beforeTest() {
		System.out.println("Start to test validation of attack phase.");
	}

	@Before
	public void before() {
		map = RiskMap.getInstance();
		controller = new ReadFileController();
		rootDir = (Paths.get("").toAbsolutePath().toString());
		player = new Player("player");
		player2 = new Player("player2");
		result = new HashMap<String, LinkedList<Territory>>();
		
		try {
			controller.readFile(rootDir +"\\src\\test\\Africa.map.");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		territoryMap = map.getTerritoryMap();
		player.addTerritory(territoryMap.get("Mozambique"));
		player.addArmy("Mozambique", 10);

		player2.addTerritory(territoryMap.get("Madagascar"));
		player2.addArmy("Madagascar", 10);

		territoryMap = map.getTerritoryMap();
		LinkedList<Territory> list  = new LinkedList<Territory>();
		list.add(territoryMap.get("Mozambique"));
		result.put("Madagascar", list);
		
	}
	
	@Test
	public void testAttackPhase() {
		player2.attack();
		
		assertEquals(result, player2.getAttackableMap());
		System.out.println("Successfully test validation of attack phase.");
	}

	@AfterClass
	public static void afterTest() {
		System.out.println("Finish testing validation of attack phase.");
		System.out.println();
	}
}

