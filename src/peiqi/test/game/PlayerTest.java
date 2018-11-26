package game;

import static org.junit.Assert.*;

import java.awt.Point;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import risk.game.Player;
import risk.game.Territory;

public class PlayerTest {
	
	String name = "lili";
	Player newzPlayer = new Player(name);
	
	@BeforeClass
	public static void beforeTest() {
		System.out.println("Start to test Player.");
	}
	
	@Test
	public void testsetUnsignedArmy() {
		int num = 10;
		newzPlayer.setUnsignedArmy(num);
		assertEquals(num, newzPlayer.getUnassignedArmy());
		System.out.println("Successfully test setUnsignedArmy.");
	}
	
	@Test
	public void testgetTotalArmy() {
		int num = 10;
		newzPlayer.setUnsignedArmy(num);
		newzPlayer.setUnsignedArmy(num);
		assertEquals(num+num, newzPlayer.getTotalArmy());
		System.out.println("Successfully test getTotalArmy.");
	}
	
	@Test
	public void testaddExchangeBonusArmy() {
		int num = 10;

		newzPlayer.setUnsignedArmy(num);
		newzPlayer.addExchangeBonusArmy(num);
		
		assertEquals(num+num, newzPlayer.getTotalArmy());
		assertEquals(num+num, newzPlayer.getUnassignedArmy());
		System.out.println("Successfully test addExchangeBonusArmy.");
	}
	
	@Test
	public void testgetReinforcement() {
		newzPlayer.reinforcement();
		assertEquals(3, newzPlayer.getTotalArmy());
		assertEquals(3, newzPlayer.getUnassignedArmy());
		System.out.println("Successfully test getReinforcement.");
	}
	
	@Test
	public void testaddArmy() {
		String name = "Africa";
		String nameNew = "Africa1";
		Point location = new Point(100,100);
		Point locationNew= new Point(200,200);
		Territory newTerritory = new Territory(name,location);
		Territory newTerritoryNew= new Territory(nameNew,locationNew);
		
		int num = 5;

		newzPlayer.addTerritory(newTerritory);
		newzPlayer.addTerritory(newTerritoryNew);
		
		newzPlayer.setUnsignedArmy(num);
		assertEquals(5, newzPlayer.getUnassignedArmy());
		newzPlayer.addArmy(name, 2);		
		
		assertEquals(3, newzPlayer.getUnassignedArmy());
		
		System.out.println("Successfully test addArmy.");
	}
	
	@Test
	public void testaddTerritory() {
		String name = "Africa";
		String nameNew = "Africa1";
		Point location = new Point(100,100);
		Point locationNew= new Point(200,200);
		Territory newTerritory = new Territory(name,location);
		Territory newTerritoryNew= new Territory(nameNew,locationNew);

		newzPlayer.addTerritory(newTerritory);
		newzPlayer.addTerritory(newTerritoryNew);

		assertEquals(true, newzPlayer.getTerritoryMap().containsKey(name));
		assertEquals(true, newzPlayer.getTerritoryMap().containsKey(nameNew));
	
		System.out.println("Successfully test addTerritory.");
	}
	
	@Test
	public void testremoveTerritory() {
		String name = "Africa";
		String nameNew = "Africa1";
		Point location = new Point(100,100);
		Point locationNew= new Point(200,200);
		Territory newTerritory = new Territory(name,location);
		Territory newTerritoryNew= new Territory(nameNew,locationNew);

		newzPlayer.addTerritory(newTerritory);
		newzPlayer.addTerritory(newTerritoryNew);
		

		assertEquals(true, newzPlayer.getTerritoryMap().containsKey(name));
		assertEquals(true, newzPlayer.getTerritoryMap().containsKey(nameNew));
		
		assertEquals(2, newzPlayer.getTerritoryMap().size());
		
		newzPlayer.removeTerritory(newTerritoryNew);
		
		assertEquals(1, newzPlayer.getTerritoryMap().size());
		
		assertEquals(true, newzPlayer.getTerritoryMap().containsKey(name));
		assertEquals(false, newzPlayer.getTerritoryMap().containsKey(nameNew));
	
		System.out.println("Successfully test removeTerritory.");
	}
	
	@AfterClass
	public static void afterTest() {
		System.out.println("Finish testing Player.");
		System.out.println();
	}
}
