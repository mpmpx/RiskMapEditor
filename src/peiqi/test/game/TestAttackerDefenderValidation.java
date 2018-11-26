package game;

import static org.junit.Assert.*;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import risk.game.Phase;
import risk.game.Territory;


public class TestAttackerDefenderValidation {
	
	Territory attacker;
	Territory defender;
	Phase phase;
	
	@BeforeClass
	public static void beforeTest() {
		System.out.println("Start to test attacker/defender validation.");
	}

	@Before
	public void before() {
		attacker = new Territory();
		defender = new Territory();
		phase = new Phase();

	}
	
	@Test
	public void testAttackerDefender() {
		
		phase.setAttack(attacker, defender);
		assertEquals(attacker, phase.getAttacker());
		assertEquals(defender, phase.getDefender());
		System.out.println("Successfully test attacker/defender validation.");
	}

	@AfterClass
	public static void afterTest() {
		System.out.println("Finish testing attacker/defender validation.");
		System.out.println();
	}
}
