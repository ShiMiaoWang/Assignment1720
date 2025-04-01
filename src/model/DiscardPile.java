package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Manages the discard pile in the game
 */
public class DiscardPile {

	private ArrayList<Card> cards;  // Discarded cards

	/**
	 * Create an empty discard pile
	 */
	public DiscardPile() {
		this.cards = new ArrayList<>();
	}

	/**
	 * Add cards to the discard pile
	 */
	public void addToPile(List<Card> newCards) {
		cards.addAll(newCards);
	}

	/**
	 * Get the top card from the discard pile
	 */
	public Card getTopCard() {
		if (!cards.isEmpty()) {
			return cards.get(cards.size() - 1);
		}
		return null;
	}

	/**
	 * Get all cards in the discard pile
	 */
	public List<Card> getCards() {
		return new ArrayList<>(cards);
	}

	/**
	 * Clear the discard pile and return the cards
	 */
	public List<Card> removeLastCards() {
		ArrayList<Card> removed = new ArrayList<>(cards);
		cards.clear();
		return removed;
	}

	/**
	 * Get the number of cards in the discard pile
	 */
	public int size() {
		return cards.size();
	}
}