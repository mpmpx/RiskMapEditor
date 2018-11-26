package game;

import static org.junit.Assert.*;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import risk.game.Phase;
import risk.game.Player;
import risk.game.Territory;

import org.junit.Test;

public class PhaseTest {

	Phase newPhase = new Phase();
	
	public static void printMsg(String msg) {
		System.out.println(msg);
	}
	
	@BeforeClass
	public static void beforeTest() {
		printMsg("Start to test.");
	}
	
	@Test
	public void testaddPlayers() {	
		Player[] players = new Player[5];
		newPhase.addPlayers(players);
		
		assertSame(players, newPhase.getPlayers()); 
		System.out.println("Successfully test addPlayers."); 
	}
	
	@Test
	public void testsetAttack() {	
		Territory attacker = new Territory();
		Territory defender = new Territory();
		
		newPhase.setAttack(attacker, defender);
		
		assertSame(attacker, newPhase.getAttacker()); 
		assertSame(defender, newPhase.getDefender()); 
		System.out.println("Successfully test setAttack."); 
	}
	
	@Test
	public void testsetAttackerAndDefender() {	
		Territory attacker = new Territory();
		Territory defender = new Territory();
	
		newPhase.setAttacker(attacker); 
		newPhase.setDefender(defender);
		
		assertSame(attacker, newPhase.getAttacker()); 
		assertSame(defender, newPhase.getDefender()); 
		System.out.println("Successfully test setAttacker And setDefender."); 
	}
	
	@AfterClass
	public static void afterTest() {
		printMsg("Finish testing cards.");
		printMsg("");
	}	
	

}
