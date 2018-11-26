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


public class TestFortification {
	
	RiskMap map;
	ReadFileController controller;
	String rootDir;
	Player player;
	HashMap<String, Territory> territoryMap;
	HashMap<String, LinkedList<Territory>> result;
	
	@BeforeClass
	public static void beforeTest() {
		System.out.println("Start to test validation of fortification phase.");
	}

	@Before
	public void before() {
		map = RiskMap.getInstance();
		map.clear();
		controller = new ReadFileController();
		rootDir = (Paths.get("").toAbsolutePath().toString());
		player = new Player("player");
		result = new HashMap<String, LinkedList<Territory>>();
		
		try {
			controller.readFile(rootDir +"\\src\\test\\Africa.map.");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		territoryMap = map.getTerritoryMap();
		player.addTerritory(territoryMap.get("Egypt"));
		player.addTerritory(territoryMap.get("North Sudan"));
		player.addTerritory(territoryMap.get("South Sudan"));
		
		player.addArmy("Egypt", 10);
		player.addArmy("North Sudan", 10);
		player.addArmy("South Sudan", 10);

		territoryMap = map.getTerritoryMap();
		LinkedList<Territory> list1  = new LinkedList<Territory>();
		list1.add(territoryMap.get("North Sudan"));
		list1.add(territoryMap.get("South Sudan"));
		result.put("Egypt", list1);
		
		LinkedList<Territory> list2  = new LinkedList<Territory>();
		list2.add(territoryMap.get("South Sudan"));
		list2.add(territoryMap.get("Egypt"));
		result.put("North Sudan", list2);
		
		LinkedList<Territory> list3  = new LinkedList<Territory>();
		list3.add(territoryMap.get("North Sudan"));
		list3.add(territoryMap.get("Egypt"));
		result.put("South Sudan", list3);
	}
	
	@Test
	public void testFortification() {
		player.fortification();
		
		assertEquals(result, player.getReachableMap());
		System.out.println("Successfully test validation of fortification phase.");
	}

	@AfterClass
	public static void afterTest() {
		System.out.println("Finish testing validation of fortification phase.");
		System.out.println();
	}
}
