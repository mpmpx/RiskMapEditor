package test;

import static org.junit.Assert.*;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import risk.game.Continent;
import risk.game.Country;
import risk.game.RiskMap;


public class JUnitTest3 {

	RiskMap map = new RiskMap();
	Country testCountry = new Country("China");
	Continent testContinent = new Continent("Asia", 5);
	
	public static void printMsg(String msg) {
		System.out.println(msg);
	}
	
	@BeforeClass
	public static void beforeTest() {
		printMsg("Start to test RiskMap.");
	}
	
	@Test
	public void testContinent() {
		map.addContinent(testContinent);
		assertEquals(testContinent, map.getContinentList().getFirst());
		printMsg("Successfully added a continent into the map.");
		map.removeContinent(testContinent.getName());
		assertEquals(0, map.getContinentList().size());
		printMsg("Successfully removed a continent from the map.");
	}
	
	@Test
	public void test2() {
		map.addCountry(testCountry);
		assertEquals(testCountry, map.getCountry(testCountry.getLocation()));
		printMsg("Successfully added a country into the map.");
		map.removeCountry(testCountry);
		assertEquals(0, map.getCountryList().size());
		printMsg("Successfully removed a country from the map.");
	}
	
	@AfterClass
	public static void afterTest() {
		printMsg("Finish testing RiskMap.");
		printMsg("");
	}
}
