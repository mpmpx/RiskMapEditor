package game;

import static org.junit.Assert.*;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import risk.game.Cards;

import org.junit.Test;

public class CardsTest {

	private Cards newCards = new Cards();
	
	public static void printMsg(String msg) {
		System.out.println(msg);
	}
	
	@BeforeClass
	public static void beforeTest() {
		printMsg("Start to test.");
		System.out.println("Start to test Cards.");
	}
	
	@Test
	public void testgetSizeTest() {	
		newCards.addCard(2);
		newCards.addCard(4);

		int newValue = newCards.getSize();
		int num  = 2;
		assertEquals(num, newValue); 
		System.out.println("Successfully get the cards size " + newValue + "."); 
	}
	
	@Test
	public void testremoveCards() {	
		newCards.getCard();   
		newCards.removeCards(newCards.getAllCards());
		assertEquals(0, newCards.getAllCards().size()); 
		System.out.println("Successfully removeCards. " ); 
	}
	
	
	@AfterClass
	public static void afterTest() {
		printMsg("Finish testing cards.");
		printMsg("");
	}	
	

}
