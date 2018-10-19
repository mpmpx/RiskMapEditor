package test;

import static org.junit.Assert.*;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;


public class JUnitTestTemplate {

	public static void printMsg(String msg) {
		System.out.println(msg);
	}
	
	@BeforeClass
	public static void beforeTest() {
		printMsg("Start to test.");
	}
	
	@Test
	public void test1() {
		printMsg("");
	}
	
	@Test
	public void test2() {
		printMsg("");
	}
	
	@AfterClass
	public static void afterTest() {
		printMsg("Finish testing.");
		printMsg("");
	}
}
