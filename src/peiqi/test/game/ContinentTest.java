package game;

import static org.junit.Assert.*;

import java.awt.Point;
import java.util.LinkedList;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import risk.game.Continent;
import risk.game.Territory;

public class ContinentTest {
	String newName = "Africa";
	int newValue = 6;
	Continent continent = new Continent(newName, newValue);

	@BeforeClass
	public static void beforeTest() {
		System.out.println("Start to test Continent.");
	}
	
	@Test
	public void testSetName() {
		assertEquals(newName, continent.getName());
		System.out.println("Successfully set the name of continent with " + newName + ".");
	}
	
	@Test
	public void testaddTerritory() {
		String name = "Africa";
		String nameNew = "Africa1";
		Point location = new Point(100,100);
		Territory newTerritory = new Territory(name,location);
		
		continent.addTerritory(newTerritory);
		assertEquals(true, continent.getTerritoryMap().contains(name) );
		assertEquals(false, continent.getTerritoryMap().contains(nameNew) );
		System.out.println("Successfully add Territory! " );
	}
	
	@Test
	public void testremoveTerritory() {
		String name = "Africa";
		String nameNew = "Africa1";
		Point location = new Point(100,100);
		Point locationNew= new Point(200,200);
		Territory newTerritory = new Territory(name,location);
		Territory newTerritoryNew= new Territory(nameNew,locationNew);
		
		continent.addTerritory(newTerritory);
		continent.addTerritory(newTerritoryNew);
		assertEquals(true, continent.getTerritoryMap().contains(name) );
		assertEquals(true, continent.getTerritoryMap().contains(nameNew) );
		
		continent.removeTerritory(newTerritory);
		assertEquals(false, continent.getTerritoryMap().contains(name) );
		assertEquals(true, continent.getTerritoryMap().contains(nameNew) );
		
		System.out.println("Successfully remove Territory! " );
	}
	
	
	@Test
	public void testisOwned() {
		String name = "Africa";
		String nameNew = "Africa1";
		Point location = new Point(100,100);
		Point locationNew= new Point(200,200);
		Territory newTerritory = new Territory(name,location);
		Territory newTerritoryNew= new Territory(nameNew,locationNew);
		
		continent.addTerritory(newTerritory);
		continent.addTerritory(newTerritoryNew);
		
		LinkedList<String> territoryList = new LinkedList<String>();
		territoryList.push(name);
		territoryList.push(nameNew);
		
		assertEquals(true,continent.isOwned(territoryList));

		System.out.println("Successfully isOwned! " );
	}
	
	@AfterClass
	public static void afterTest() {
		System.out.println("Finish testing Continent.");
		System.out.println();
	}
}
