package model;

/**
 * Manages game flow and rules
 */
public class Manager {
	// Game components
	private Player[] players;        // Array of players in the game
	private int playerCount;         // Number of players
	private Deck deck;               // Card deck
	private Pile pile;               // Play pile
	private DiscardPile discardPile; // Discard pile
	private int currentPlayerIndex;  // Current player index
	private int roundNumber;         // Current round number

	/**
	 * Create game manager and deal cards
	 */
	public Manager(Player[] players) {
		// Store players
		this.playerCount = players.length;
		this.players = new Player[playerCount];
		for (int i = 0; i < playerCount; i++) {
			this.players[i] = players[i];
		}

		// Initialize game components
		this.deck = new Deck();
		this.pile = new Pile();
		this.discardPile = new DiscardPile();

		// Set initial values
		this.currentPlayerIndex = 0;
		this.roundNumber = 1;

		// Deal cards to all players
		dealInitialCards();
	}

	/**
	 * Deal initial cards to all players
	 */
	private void dealInitialCards() {
		// Shuffle the deck
		deck.shuffle();

		// Keep dealing until deck is empty
		while (deck.size() > 0) {
			// Give one card to each player
			for (int i = 0; i < playerCount; i++) {
				// Stop if deck is empty
				if (deck.size() == 0) {
					break;
				}

				// Draw a card and give to player
				Card card = deck.draw();
				players[i].receiveCard(card);
			}
		}
	}

	/**
	 * Player plays cards
	 * @param player The player making the play
	 * @param cards The cards being played
	 * @param declaredRank The rank being declared
	 */
	public void playCards(Player player, Card[] cards, Card.Rank declaredRank) {
		// Check if it's this player's turn
		if (player != getCurrentPlayer()) {
			System.out.println("Not this player's turn");
			throw new IllegalStateException("Not your turn!");
		}

		// Check if player has these cards in their hand
		if (!player.hasAllCards(cards)) {
			// Add debug info
			System.out.println("Player cards: ");
			Card[] playerCards = player.getHand();
			for (int i = 0; i < playerCards.length; i++) {
				Card c = playerCards[i];
				System.out.println("  " + c.getRank() + " of " + c.getSuit());
			}

			System.out.println("Attempted cards: ");
			for (int i = 0; i < cards.length; i++) {
				Card c = cards[i];
				System.out.println("  " + c.getRank() + " of " + c.getSuit());
			}

			throw new IllegalArgumentException("You don't have these cards!");
		}

		// Check number of cards played (must be 1-4)
		if (cards.length < 1 || cards.length > 4) {
			throw new IllegalArgumentException("You must play 1-4 cards!");
		}

		// Execute the play
		player.playCard(cards);
		Play play = new Play(player, cards, declaredRank);
		pile.addPlay(play);

		// Move to next player
		nextPlayer();
	}

	/**
	 * Challenge another player
	 * @param challenger The player making the challenge
	 * @return true if challenge successful, false if failed
	 */
	public boolean challengePlayer(Player challenger) {
		// Get the last play
		Play lastPlay = pile.getLastPlay();

		// If no play to challenge, return false
		if (lastPlay == null) {
			return false;
		}

		// Get the player who made the last play
		Player lastPlayer = lastPlay.getPlayer();

		// Check if the last play was honest
		if (!lastPlay.matchesDeclaration()) {
			// Challenge successful - declared rank didn't match actual cards

			// Last player takes all cards from pile
			lastPlayer.receivePenalty(pile.getAllCards());

			// Clear the pile
			pile.clearPile();

			// After a challenge, the player who lost takes next turn
			setCurrentPlayer(lastPlayer);

			// Increment round number when pile is cleared
			roundNumber++;

			return true;
		} else {
			// Challenge failed - declared rank matched actual cards

			// Challenger takes all cards from pile
			challenger.receivePenalty(pile.getAllCards());

			// Clear the pile
			pile.clearPile();

			// After a challenge, the player who lost takes next turn
			setCurrentPlayer(challenger);

			// Increment round number when pile is cleared
			roundNumber++;

			return false;
		}
	}

	/**
	 * Check if any player has won
	 * @return The winning player, or null if no winner yet
	 */
	public Player checkForWinner() {
		// Check each player
		for (int i = 0; i < playerCount; i++) {
			// If player has no cards, they win
			if (players[i].hasNoCards()) {
				return players[i];
			}
		}

		// No winner yet
		return null;
	}

	/**
	 * Switch to the next player
	 */
	public void nextPlayer() {
		// Move to next player index, wrap around if needed
		currentPlayerIndex = (currentPlayerIndex + 1) % playerCount;
	}

	/**
	 * Get current player
	 */
	public Player getCurrentPlayer() {
		return players[currentPlayerIndex];
	}

	/**
	 * Set the current player directly
	 */
	public void setCurrentPlayer(Player player) {
		// Find the player in the array
		for (int i = 0; i < playerCount; i++) {
			if (players[i] == player) {
				currentPlayerIndex = i;
				break;
			}
		}
	}

	/**
	 * Get current round number
	 */
	public int getRoundNumber() {
		return roundNumber;
	}

	/**
	 * Get the play pile
	 */
	public Pile getPile() {
		return pile;
	}

	/**
	 * Get the discard pile
	 */
	public DiscardPile getDiscardPile() {
		return discardPile;
	}

	/**
	 * Get all players
	 */
	public Player[] getPlayers() {
		return players;
	}

	/**
	 * Get number of players
	 */
	public int getPlayerCount() {
		return playerCount;
	}
}