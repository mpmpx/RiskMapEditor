package test;

import static org.junit.Assert.*;

import java.awt.Point;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import risk.game.Country;


public class JUnitTest2 {

	private Country country = new Country();
	private String testName = "China";
	private Point testLocation = new Point(200, 300);
	private String testContinent = "Asia";
	private Point testAdjacentCountry = new Point(100, 300);
	
	public static void printMsg(String msg) {
		System.out.println(msg);
	}
	
	@BeforeClass
	public static void beforeTest() {
		printMsg("Start to test Country.");
	}
	
	@Test
	public void testName() {
		country.setName(testName);
		assertEquals(testName, country.getName());
		printMsg("Successfully set the name to the country with " + testName + ".");
	}
	
	@Test
	public void testLocation() {
		country.setLocation(testLocation);
		assertEquals(testLocation, country.getLocation());
		printMsg("Successfully set the location to the country with (" + testLocation.x + ", " + testLocation.y + ").");
	}
	
	@Test
	public void testContinent() {
		country.setContinentName(testContinent);
		assertEquals(testContinent, country.getContinentName());
		printMsg("Succesfully set the continent to the country with " + testContinent + ".");
	}
	
	@Test
	public void testAdjacentCountry() {
		country.addAdjacentCountry(testAdjacentCountry);
		assertEquals(testAdjacentCountry, country.getAdjacentCountryList().getFirst());
		printMsg("Successfully added a country as an adjacent country of the country.");
	}
	
	@AfterClass
	public static void afterTest() {
		printMsg("Finish testing Country.");
		printMsg("");
	}
}
