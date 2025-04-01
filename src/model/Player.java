package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a player in the game
 */
public class Player {

	private String name;         // Player name
	private ArrayList<Card> hand;  // Player's hand of cards

	/**
	 * Create a new player
	 */
	public Player(String name) {
		this.name = name;
		this.hand = new ArrayList<>();
	}

	/**
	 * Get player name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Get player's hand
	 */
	public List<Card> getHand() {
		return new ArrayList<>(hand);
	}

	/**
	 * Receive a card
	 */
	public void receiveCard(Card card) {
		card.revealCard();  // Visible to the player
		hand.add(card);
	}

	/**
	 * Play a set of cards
	 */
	public void playCard(List<Card> cards) {
		hand.removeAll(cards);
	}

	/**
	 * Receive penalty cards
	 */
	public void receivePenalty(List<Card> cards) {
		hand.addAll(cards);
	}
}