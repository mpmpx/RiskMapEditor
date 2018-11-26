package game;

import static org.junit.Assert.*;

import java.awt.Point;
import java.util.LinkedList;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import risk.game.Continent;
import risk.game.RiskMap;
import risk.game.Territory;

public class RiskMapTest {
	
	RiskMap map = RiskMap.getInstance();
	Continent testContinent = new Continent("Asia", 5);
	
	@BeforeClass
	public static void beforeTest() {
		System.out.println("Start to test RiskMapTest.");
	}
	
	
	@Test
	public void testaddContinent() {
		map.clear();
		Continent newContinent = new Continent("Afric", 5);
		
		map.getContinentMap().put(testContinent.getName(), testContinent);
		map.getContinentMap().put(newContinent.getName(), newContinent);
		
		boolean bstate1 = map.getContinentMap().containsKey("Asia");
		boolean bstate2 = map.getContinentMap().containsKey("Afric");
		
		assertEquals(true, bstate1);
		assertEquals(true, bstate2);
		System.out.println("Successfully test addContinent.");
	}
	
	@Test
	public void testaddTerritoryry() {
		String name = "Africa";
		String nameNew = "Africa1";
		Point location = new Point(100,100);
		Point locationNew= new Point(200,200);
		Territory newTerritory = new Territory(name,location);
		Territory newTerritoryNew= new Territory(nameNew,locationNew);
		
		map.addTerritory(newTerritory);
		map.addTerritory(newTerritoryNew);
		
		boolean bstate1 = map.getTerritoryMap().containsKey("Africa");
		boolean bstate2 = map.getTerritoryMap().containsKey("Africa1");
		
		
		assertEquals(true, bstate1);
		assertEquals(true, bstate2);
		System.out.println("Successfully test addTerritory.");
	}
	
	@Test
	public void testupdateTerritory() {
		String name = "Africa";
		String nameNew = "Africa1";
		Point location = new Point(100,100);
		Point locationNew= new Point(200,200);
		Territory newTerritory = new Territory(name,location);
		Territory newTerritoryNew= new Territory(nameNew,locationNew);
		
		map.addTerritory(newTerritory);
		
		map.updateTerritory(newTerritoryNew);
		
		boolean bstate2 = map.getTerritoryMap().containsKey("Africa1");
	
		assertEquals(true, bstate2);
		System.out.println("Successfully test updateTerritory.");
	}
	
	@Test
	public void testaddLink() {
		String name = "Africa";
		String nameNew = "Africa1";
		Point location = new Point(100,100);
		Territory newTerritory = new Territory(name,location);
		
		map.addTerritory(newTerritory);
		
		assertEquals(0, map.getEdgeMap().size());
		LinkedList<String> adjacentList = new LinkedList<String>();
		adjacentList.add(nameNew);
	
		map.addLink(nameNew,adjacentList);
		assertEquals(1, map.getEdgeMap().size());
		
		System.out.println("Successfully test addLink.");
	}

	
	@AfterClass
	public static void afterTest() {
		System.out.println("Finish testing RiskMapTest.");
		System.out.println();
	}
}
