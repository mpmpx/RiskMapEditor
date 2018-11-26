package controller;

import static org.junit.Assert.*;

import java.nio.file.Paths;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import risk.controller.ReadFileController;


public class TestReadInvalidMap {

	ReadFileController controller = new ReadFileController();
	String rootDir = (Paths.get("").toAbsolutePath().toString());
	
	@BeforeClass
	public static void beforeTest() {
		System.out.println("Start to test reading invalid files with three types of error.");
	}
	
	@Test
	public void testRead1() {
		try {
			controller.readFile(rootDir +"\\src\\test\\Africa_miss_continent.map.");
		}
		catch (Exception e) {
			assertEquals("Continent of Angola: The Congo is invalid.", e.getMessage());
			System.out.println("Successfully test missing continent in \"Africa_miss_continent.map\" - " +e.getMessage());
		}	
	}
	
	@Test
	public void testRead2() {
		try {
			controller.readFile(rootDir + "\\src\\test\\Africa_miss_country.map");
		}
		catch (Exception e) {
			assertEquals("Adjacent territory of Algeria: Morocco is invalid.", e.getMessage());
			System.out.println("Successfully test missing country in \"Africa_miss_country.map\" - " +e.getMessage());
		}
	}
	
	@Test
	public void testRead3() {
		try {
			controller.readFile(rootDir + "\\src\\test\\Africa_miss_connect.map");
		}
		catch (Exception e) {
			assertEquals("Libya and Egypt do not link to each other.", e.getMessage());
			System.out.println("Successfully test missing connection in \"Africa_miss_connect.map\" - " +e.getMessage());
		}
	}
	
	
	@AfterClass
	public static void afterTest() {
		System.out.println("Finish testing reading a invalid file with three types of error.");
		System.out.println();
	}
}
