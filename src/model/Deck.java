package model;

/**
 * Manages the deck of cards in the Super Madiao game
 */
public class Deck {

	// Store all cards in the deck (maximum 24 cards: 4 suits × 6 ranks)
	private Card[] cards;
	private int cardCount;

	/**
	 * Create and shuffle a new deck with all cards
	 */
	public Deck() {
		// Create array to hold all cards (4 suits × 6 ranks = 24 cards)
		cards = new Card[24];
		cardCount = 0;

		// Create all ranks for COINS
		cards[cardCount++] = new Card(Card.Suit.COINS, Card.Rank.ONE);
		cards[cardCount++] = new Card(Card.Suit.COINS, Card.Rank.TWO);
		cards[cardCount++] = new Card(Card.Suit.COINS, Card.Rank.THREE);
		cards[cardCount++] = new Card(Card.Suit.COINS, Card.Rank.FOUR);
		cards[cardCount++] = new Card(Card.Suit.COINS, Card.Rank.FIVE);
		cards[cardCount++] = new Card(Card.Suit.COINS, Card.Rank.SIX);

		// Create all ranks for CHALICES
		cards[cardCount++] = new Card(Card.Suit.CHALICES, Card.Rank.ONE);
		cards[cardCount++] = new Card(Card.Suit.CHALICES, Card.Rank.TWO);
		cards[cardCount++] = new Card(Card.Suit.CHALICES, Card.Rank.THREE);
		cards[cardCount++] = new Card(Card.Suit.CHALICES, Card.Rank.FOUR);
		cards[cardCount++] = new Card(Card.Suit.CHALICES, Card.Rank.FIVE);
		cards[cardCount++] = new Card(Card.Suit.CHALICES, Card.Rank.SIX);

		// Create all ranks for WANDS
		cards[cardCount++] = new Card(Card.Suit.WANDS, Card.Rank.ONE);
		cards[cardCount++] = new Card(Card.Suit.WANDS, Card.Rank.TWO);
		cards[cardCount++] = new Card(Card.Suit.WANDS, Card.Rank.THREE);
		cards[cardCount++] = new Card(Card.Suit.WANDS, Card.Rank.FOUR);
		cards[cardCount++] = new Card(Card.Suit.WANDS, Card.Rank.FIVE);
		cards[cardCount++] = new Card(Card.Suit.WANDS, Card.Rank.SIX);

		// Create all ranks for SWORDS
		cards[cardCount++] = new Card(Card.Suit.SWORDS, Card.Rank.ONE);
		cards[cardCount++] = new Card(Card.Suit.SWORDS, Card.Rank.TWO);
		cards[cardCount++] = new Card(Card.Suit.SWORDS, Card.Rank.THREE);
		cards[cardCount++] = new Card(Card.Suit.SWORDS, Card.Rank.FOUR);
		cards[cardCount++] = new Card(Card.Suit.SWORDS, Card.Rank.FIVE);
		cards[cardCount++] = new Card(Card.Suit.SWORDS, Card.Rank.SIX);

		// Shuffle the cards
		shuffle();
	}

	/**
	 * Shuffle the deck (randomize card order)
	 */
	public void shuffle() {
		// Shuffle by swapping pairs of cards many times
		for (int i = 0; i < 100; i++) {
			// Pick two random positions
			int pos1 = (int) (Math.random() * cardCount);
			int pos2 = (int) (Math.random() * cardCount);

			// Swap the cards at these positions
			Card temp = cards[pos1];
			cards[pos1] = cards[pos2];
			cards[pos2] = temp;
		}
	}

	/**
	 * Draw a card from the top of the deck
	 * @return The top card, or null if deck is empty
	 */
	public Card draw() {
		// Check if there are any cards left
		if (cardCount <= 0) {
			return null;
		}

		// Get the top card
		Card topCard = cards[0];

		// Shift all other cards one position forward
		for (int i = 0; i < cardCount - 1; i++) {
			cards[i] = cards[i + 1];
		}

		// Decrease card count
		cardCount--;

		// Return the top card
		return topCard;
	}

	/**
	 * Get the number of cards left in the deck
	 */
	public int size() {
		return cardCount;
	}
}