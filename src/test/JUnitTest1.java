package test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import risk.game.Continent;

public class JUnitTest1 {
	
	private Continent continent = new Continent();
	private String newName = "Africa";
	private int newValue = 6;
	
	@BeforeClass
	public static void beforeTest() {
		System.out.println("Start to test Continent");
	}
	
	@Test
	public void testSetName() {
		continent.setName("Africa");
		assertEquals(newName, continent.getName());
		System.out.println("Successfully set the name of continent with " + newName + ".");
	}
	
	@Test
	public void testSetValue() {
		continent.setValue(6);
		assertEquals(newValue, continent.getValue());
		System.out.println("Successfully set the value of continent as " + newValue + ".");
	}
	
	@AfterClass
	public static void afterTest() {
		System.out.println("Finish testing Continent");
		System.out.println();
	}
}
