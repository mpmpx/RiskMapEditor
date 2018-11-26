package game;

import static org.junit.Assert.*;

import java.awt.Color;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import risk.game.PlayerColor;

public class PlayerColorTest {

	@BeforeClass
	public static void beforeTest() {
		System.out.println("Start to test PlayerColorTest.");
	}
	
	@Test
	public void testnextColor() {
		PlayerColor.reset();
		assertEquals(Color.red, PlayerColor.nextColor());
		System.out.println("Successfully test set nextColor.");
	}
	

	@AfterClass
	public static void afterTest() {
		System.out.println("Finish testing nextColor.");
		System.out.println();
	}
}
