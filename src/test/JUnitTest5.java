package test;

import static org.junit.Assert.*;

import java.io.File;
import java.nio.file.Paths;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import risk.io.RiskMapIO;


public class JUnitTest5 {

	RiskMapIO riskMapIO = new RiskMapIO();
	
	@BeforeClass
	public static void beforeTest() {
		System.out.println("Start to test validation of the map");
	}
	
	@Test
	public void testValidation() {
		try {
			riskMapIO.readFile("src/test/Africa_non_connected_graph.map");
		}
		catch (Exception e) {
			assertEquals("The map contained in the file is not a connected graph.", e.getMessage());
			System.out.println("Successfully test validation of \"Africa_non_connected_graph.map\" - " +e.getMessage());
		}
	}
	
	@AfterClass
	public static void afterTest() {
		System.out.println("Finish testing validation of the map");
	}
}
