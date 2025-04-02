package model;

/**
 * Represents a player in the game
 */
public class Player {

	private String name;        // Player name
	private Card[] hand;        // Player's hand of cards
	private int cardCount;      // Number of cards in hand

	/**
	 * Create a new player with given name
	 */
	public Player(String name) {
		this.name = name;
		// A player can have at most 24 cards (entire deck)
		this.hand = new Card[24];
		this.cardCount = 0;
	}

	/**
	 * Get player name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Get player's hand of cards
	 * @return Array of cards in player's hand
	 */
	public Card[] getHand() {
		// Create a new array with exact size needed
		Card[] result = new Card[cardCount];

		// Copy cards from hand to result
		for (int i = 0; i < cardCount; i++) {
			result[i] = hand[i];
		}

		return result;
	}

	/**
	 * Get the number of cards in hand
	 */
	public int getCardCount() {
		return cardCount;
	}

	/**
	 * Add a card to player's hand
	 */
	public void receiveCard(Card card) {
		// Make the card visible to the player
		card.revealCard();

		// Add the card to player's hand
		hand[cardCount] = card;
		cardCount++;
	}

	/**
	 * Remove specific cards from player's hand
	 */
	public void playCard(Card[] cardsToPlay) {
		// Find and remove each card
		for (int i = 0; i < cardsToPlay.length; i++) {
			Card cardToRemove = cardsToPlay[i];

			// Find this card in the hand
			boolean found = false;
			int foundAt = -1;

			for (int j = 0; j < cardCount; j++) {
				if (hand[j].equals(cardToRemove)) {
					found = true;
					foundAt = j;
					break;
				}
			}

			// If found, remove it by shifting all cards after it
			if (found) {
				for (int j = foundAt; j < cardCount - 1; j++) {
					hand[j] = hand[j + 1];
				}
				cardCount--;
			}
		}
	}

	/**
	 * Add cards to player's hand (as penalty)
	 */
	public void receivePenalty(Card[] cards) {
		// Add each card to hand
		for (int i = 0; i < cards.length; i++) {
			receiveCard(cards[i]);
		}
	}

	/**
	 * Check if player has a specific card
	 */
	public boolean hasCard(Card card) {
		for (int i = 0; i < cardCount; i++) {
			if (hand[i].equals(card)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Check if player has all the specified cards
	 */
	public boolean hasAllCards(Card[] cards) {
		for (int i = 0; i < cards.length; i++) {
			if (!hasCard(cards[i])) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Check if player has no cards left
	 */
	public boolean hasNoCards() {
		return cardCount == 0;
	}
}