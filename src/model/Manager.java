package model;

import java.util.List;

/**
 * Manages game flow and rules
 */
public class Manager {
	private List<Player> players;        // All players
	private Deck deck;                   // Card deck
	private Pile pile;                   // Play pile
	private DiscardPile discardPile;     // Discard pile
	private int currentPlayerIndex = 0;  // Current player index
	private int roundNumber = 1;         // Current round number

	/**
	 * Create game manager and deal cards
	 */
	public Manager(List<Player> players) {
		this.players = players;
		this.deck = new Deck();
		this.pile = new Pile();
		this.discardPile = new DiscardPile();
		dealInitialCards();
	}

	/**
	 * Deal initial cards
	 */
	private void dealInitialCards() {
		deck.shuffle();
		while (deck.size() > 0) {
			for (Player player : players) {
				if (deck.size() == 0)
					break;
				player.receiveCard(deck.draw());
			}
		}
	}

	/**
	 * Player plays cards
	 */
	public void playCards(Player player, List<Card> cards, Card.Rank declaredRank) {
		// Check if it's this player's turn
		if (player != getCurrentPlayer()) {
			throw new IllegalStateException("Not your turn!");
		}

		// Check if player has these cards
		if (!player.getHand().containsAll(cards)) {
			throw new IllegalArgumentException("You don't have these cards!");
		}

		// Check number of cards played
		if (cards.size() < 1 || cards.size() > 4) {
			throw new IllegalArgumentException("You must play 1-4 cards!");
		}

		// Execute the play
		player.playCard(cards);
		Play play = new Play(player, cards, declaredRank);
		pile.addPlay(play);
		nextPlayer();
	}

	/**
	 * Challenge another player
	 */
	public boolean challengePlayer(Player challenger) {
		Play lastPlay = pile.getLastPlay();
		if (lastPlay == null)
			return false; // No play to challenge

		Player lastPlayer = lastPlay.getPlayer();

		if (!lastPlay.matchesDeclaration()) { // Challenge successful
			lastPlayer.receivePenalty(pile.getAllCards());
			pile.clearPile();

			// After a challenge, the player who lost takes next turn
			setCurrentPlayer(lastPlayer);

			// Increment round number when pile is cleared
			roundNumber++;

			return true;
		} else { // Challenge failed
			challenger.receivePenalty(pile.getAllCards());
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
	 */
	public Player checkForWinner() {
		for (Player player : players) {
			if (player.getHand().isEmpty())
				return player;
		}
		return null;
	}

	/**
	 * Switch to the next player
	 */
	public void nextPlayer() {
		currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
	}

	/**
	 * Get current player
	 */
	public Player getCurrentPlayer() {
		return players.get(currentPlayerIndex);
	}

	/**
	 * Set the current player directly
	 */
	public void setCurrentPlayer(Player player) {
		for (int i = 0; i < players.size(); i++) {
			if (players.get(i) == player) {
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

	public Pile getPile() {
		return pile;
	}

	public DiscardPile getDiscardPile() {
		return discardPile;
	}
}