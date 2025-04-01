package model;

/**
 * Represents a card in the Super Madiao game.
 */
public class Card {

	// Card suits
	public enum Suit {
		COINS, CHALICES, WANDS, SWORDS
	}

	// Card ranks
	public enum Rank {
		ONE(1), TWO(2), THREE(3), FOUR(4), FIVE(5), SIX(6);

		private final int value;

		Rank(int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}
	}

	private final Suit suit;      // Card suit
	private final Rank rank;      // Card rank
	private boolean isRevealed;  // Whether the card is visible

	/**
	 * Create a new card
	 */
	public Card(Suit suit, Rank rank) {
		this.suit = suit;
		this.rank = rank;
		this.isRevealed = false;  // Default to face down
	}

	// Get card suit
	public Suit getSuit() {
		return suit;
	}

	// Get card rank
	public Rank getRank() {
		return rank;
	}

	// Reveal card
	public void revealCard() {
		this.isRevealed = true;
	}

	// Hide card
	public void hideCard() {
		this.isRevealed = false;
	}

	// Check if card is revealed
	public boolean isRevealed() {
		return isRevealed;
	}

	@Override
	public String toString() {
		if (isRevealed) {
			return rank + " of " + suit;
		} else {
			return "Facedown Card";
		}
	}
}