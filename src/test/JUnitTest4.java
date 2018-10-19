package test;

import static org.junit.Assert.*;

import java.io.File;
import java.nio.file.Paths;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import risk.io.RiskMapIO;


public class JUnitTest4 {

	RiskMapIO riskMapIO = new RiskMapIO();
	File file = new File("Africa_miss_continent.map");
	
	@BeforeClass
	public static void beforeTest() {
		System.out.println("Start to test reading a invalid file with three types of error.");
	}
	
	@Test
	public void testRead1() {
		try {
			riskMapIO.readFile("src/test/Africa_miss_continent.map");
		}
		catch (Exception e) {
			assertEquals("Continent of Angola: The Congo is invalid.", e.getMessage());
			System.out.println("Successfully test missing continent in \"Africa_miss_continent.map\" - " +e.getMessage());
		}	
	}
	
	@Test
	public void testRead2() {
		try {
			riskMapIO.readFile("src/test/Africa_miss_country.map");
		}
		catch (Exception e) {
			assertEquals("Adjacent country of Algeria: Morocco is invalid.", e.getMessage());
			System.out.println("Successfully test missing country in \"Africa_miss_country.map\" - " +e.getMessage());
		}
	}
	
	@Test
	public void testRead3() {
		try {
			riskMapIO.readFile("src/test/Africa_miss_connect.map");
		}
		catch (Exception e) {
			assertEquals("Libya and Egypt do not link to each other.", e.getMessage());
			System.out.println("Successfully test missing connection in \"Africa_miss_connect.map\" - " +e.getMessage());
		}
	}
	
	@Test
	public void testRead4() {
		try {
			riskMapIO.readFile("src/test/Africa_non_connected_graph.map");
		}
		catch (Exception e) {
			assertEquals("The map contained in the file is not a connected graph.", e.getMessage());
			System.out.println("Successfully test missing connection in \"Africa_non_connected_graph.map\" - " +e.getMessage());
		}
	}
	
	@AfterClass
	public static void afterTest() {
		System.out.println("Finish testing reading a invalid file with three types of error.");
	}
}
