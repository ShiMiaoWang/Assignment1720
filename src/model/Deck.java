package model;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Manages the deck of cards in the Super Madiao game
 */
public class Deck {

	// Store all cards
	private ArrayList<Card> cards;

	/**
	 * Create and shuffle a new deck
	 */
	public Deck() {
		cards = new ArrayList<>();

		// Add all cards
		for (Card.Suit suit : Card.Suit.values()) {
			cards.add(new Card(suit, Card.Rank.ONE));
			cards.add(new Card(suit, Card.Rank.TWO));
			cards.add(new Card(suit, Card.Rank.THREE));
			cards.add(new Card(suit, Card.Rank.FOUR));
			cards.add(new Card(suit, Card.Rank.FIVE));
			cards.add(new Card(suit, Card.Rank.SIX));
		}

		// Shuffle
		shuffle();
	}

	/**
	 * Shuffle the deck
	 */
	public void shuffle() {
		Collections.shuffle(cards);
	}

	/**
	 * Draw a card
	 */
	public Card draw() {
		if (!cards.isEmpty()) {
			return cards.remove(0);
		}
		return null;
	}

	/**
	 * Get remaining card count
	 */
	public int size() {
		return cards.size();
	}
}