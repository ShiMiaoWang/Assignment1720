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

		private int value;

		// Constructor for Rank enum
		Rank(int value) {
			this.value = value;
		}

		// Get the numerical value of this rank
		public int getValue() {
			return value;
		}
	}

	// Card properties
	private Suit suit;      // Card suit (COINS, CHALICES, etc.)
	private Rank rank;      // Card rank (ONE, TWO, etc.)
	private boolean isRevealed;  // Whether the card is visible

	/**
	 * Create a new card
	 */
	public Card(Suit suit, Rank rank) {
		this.suit = suit;
		this.rank = rank;
		this.isRevealed = false;  // Cards start face down by default
	}

	// Get the suit of this card
	public Suit getSuit() {
		return suit;
	}

	// Get the rank of this card
	public Rank getRank() {
		return rank;
	}

	// Turn the card face up (reveal it)
	public void revealCard() {
		this.isRevealed = true;
	}

	// Turn the card face down (hide it)
	public void hideCard() {
		this.isRevealed = false;
	}

	// Check if the card is face up (revealed)
	public boolean isRevealed() {
		return isRevealed;
	}

	// Get a string representation of the card
	public String toString() {
		if (isRevealed) {
			return rank + " of " + suit;
		} else {
			return "Facedown Card";
		}
	}

	// Check if two cards are the same
	public boolean equals(Object obj) {
		// Check if comparing with itself
		if (this == obj) {
			return true;
		}

		// Check if comparing with null
		if (obj == null) {
			return false;
		}

		// Check if comparing with different type
		if (getClass() != obj.getClass()) {
			return false;
		}

		// Cast and compare properties
		Card other = (Card) obj;
		if (this.suit == other.suit && this.rank == other.rank) {
			return true;
		} else {
			return false;
		}
	}

	// Generate a hash code for HashMap/HashSet
	public int hashCode() {
		int result = 31;
		result = result * suit.hashCode();
		result = result + rank.hashCode();
		return result;
	}
}