package risk.game;

import java.util.LinkedList;
import java.util.Random;

/**
 * This class defines the hand cards of a player.
 */
public class Cards {

	public final static int INFANTRY = 0;
	public final static int CAVALRY = 1;
	public final static int CANNON = 2;
	
	private LinkedList<Integer> cards;
	
	/**
	 * Constructor for Card class
	 */
	public Cards() {
		cards = new LinkedList<Integer>();
	}
	
	/**
	 * method to add card.
	 * @param value of the card
	 */
	public void addCard(int value) {
		cards.add(value);
	}
	
	/**
	 * Randomly gets a card from three kinds of cards.
	 */
	public void getCard() {
		Random r = new Random();
		addCard(r.nextInt(3));
	}
	
	/**
	 * Gets all cards.
	 * @return all cards. 
	 */
	public LinkedList<Integer> getAllCards() {
		return cards;
	}
	
	/**
	 * Gets number of cards
	 * @return number of cards.
	 */
	public int getSize() {
		return cards.size();
	}
	
	/**
	 * Removes some cards from all cards.
	 * @param cards cards to be removed from all cards.
	 */
	public void removeCards(LinkedList<Integer> cards) {
		for (Integer card : cards) {
			this.cards.remove(card);
		}
	}
}
