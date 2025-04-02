package model;

/**
 * Manages the discard pile in the game
 */
public class DiscardPile {

	private Card[] cards;     // Array of discarded cards
	private int cardCount;    // Number of cards in pile

	/**
	 * Create an empty discard pile
	 */
	public DiscardPile() {
		// Can store up to 24 cards (entire deck)
		this.cards = new Card[24];
		this.cardCount = 0;
	}

	/**
	 * Add cards to the discard pile
	 */
	public void addToPile(Card[] newCards) {
		// Add each new card to the pile
		for (int i = 0; i < newCards.length; i++) {
			// Make sure we don't exceed array bounds
			if (cardCount < cards.length) {
				cards[cardCount] = newCards[i];
				cardCount++;
			}
		}
	}

	/**
	 * Get the top card from the discard pile
	 * @return The top card, or null if pile is empty
	 */
	public Card getTopCard() {
		// Check if there are any cards
		if (cardCount == 0) {
			return null;
		} else {
			// Return the top card (last in the array)
			return cards[cardCount - 1];
		}
	}

	/**
	 * Get all cards in the discard pile
	 * @return Array of all cards in the pile
	 */
	public Card[] getCards() {
		// Create a new array with exact size needed
		Card[] result = new Card[cardCount];

		// Copy cards from pile to result
		for (int i = 0; i < cardCount; i++) {
			result[i] = cards[i];
		}

		return result;
	}

	/**
	 * Clear the discard pile and return the cards
	 */
	public Card[] removeAllCards() {
		// Get all cards
		Card[] allCards = getCards();

		// Clear the pile
		cardCount = 0;

		// Return the cards that were removed
		return allCards;
	}

	/**
	 * Get the number of cards in the discard pile
	 */
	public int getCardCount() {
		return cardCount;
	}
}