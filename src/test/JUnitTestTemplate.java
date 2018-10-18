package test;

import static org.junit.Assert.*;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;


public class JUnitTestTemplate {

	@BeforeClass
	public static void beforeTest() {
		System.out.println("");
	}
	
	@Test
	public void test1() {
		System.out.println("");
	}
	
	@Test
	public void test2() {
		System.out.println("");
	}
	
	@AfterClass
	public static void afterTest() {
		System.out.println("");
		System.out.println();
	}
}
