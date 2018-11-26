package game;

import static org.junit.Assert.*;

import java.util.LinkedList;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import risk.game.Continent;
import risk.game.Phase;
import risk.game.Player;
import risk.game.RiskMap;
import risk.game.Territory;


public class TestConqueringValidation {
	
	RiskMap map;
	Territory t1;
	Territory t2;
	Player attacker;
	Player defender;
	Phase phase;
	
	
	@BeforeClass
	public static void beforeTest() {
		System.out.println("Start to test validation of conquering.");
	}

	@Before
	public void before() {
		map = RiskMap.getInstance();
		map.addContinent(new Continent("continent", 5));

		
		t1 = new Territory();
		t1.setArmy(0);
		t1.setContinentName("continent");
		t1.setName("attacker");
		map.addTerritory(t1);
		attacker = new Player("attacker");
		t1.setOwner(attacker.getColor());
		attacker.addTerritory(t1);
		attacker.setUnsignedArmy(1);
		attacker.addArmy("attacker", 1);
		
		t2 = new Territory();
		t2.setArmy(0);
		t2.setContinentName("continent");
		t2.setName("defender");
		map.addTerritory(t2);
		defender = new Player("defender");
		t2.setOwner(defender.getColor());
		defender.addTerritory(t2);
		defender.setUnsignedArmy(1);
		defender.addArmy("defender", 1);
		
		Player[] players = new Player[] {attacker, defender};
		
		phase = new Phase();
		phase.addPlayers(players);
		phase.initialize();
		phase.setAttack(t1, t2);
		
		map.addLink(t1.getName(), new LinkedList<String>());
		map.addLink(t2.getName(), new LinkedList<String>());
		
	}
	
	@Test
	public void testConquering() {
		
		phase.conquerTerritory(2);
		// make sure a player is removed.
		assertEquals(phase.getPlayers().length, 1);
		
		// make sure defender is removed.
		assertEquals(phase.getPlayers()[0].getName(), "attacker");
		
		// make sure defender territory belongs to attacker.
		assertEquals(map.getTerritoryMap().get("defender").getOwner(), attacker.getColor());
		System.out.println("Successfully test validation of conquering.");
	}

	@AfterClass
	public static void afterTest() {
		System.out.println("Finish testing validation of conquering.");
		System.out.println();
	}
}
