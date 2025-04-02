package model;

/**
 * Represents a play made by a player (cards played and rank declared)
 */
public class Play {
    private Player player;         // Player who made this play
    private Card[] cards;          // Cards that were played
    private Card.Rank declaredRank; // The rank that was declared
    private int cardCount;         // Number of cards played

    /**
     * Create a new play
     * @param player The player who made the play
     * @param cards The cards that were played
     * @param declaredRank The rank that was declared
     */
    public Play(Player player, Card[] cards, Card.Rank declaredRank) {
        this.player = player;
        this.declaredRank = declaredRank;

        // Store a copy of the cards
        this.cardCount = cards.length;
        this.cards = new Card[cardCount];

        // Add each card and make it face up
        for (int i = 0; i < cardCount; i++) {
            cards[i].revealCard();  // Make card visible
            this.cards[i] = cards[i];
        }
    }

    /**
     * Get the player who made this play
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Get the cards that were played
     * @return Array of cards in the play
     */
    public Card[] getCards() {
        // Create a new array with exact size needed
        Card[] result = new Card[cardCount];

        // Copy cards from play to result
        for (int i = 0; i < cardCount; i++) {
            result[i] = cards[i];
        }

        return result;
    }

    /**
     * Get number of cards in this play
     */
    public int getCardCount() {
        return cardCount;
    }

    /**
     * Get the rank that was declared
     */
    public Card.Rank getDeclaredRank() {
        return declaredRank;
    }

    /**
     * Check if the actual cards match the declared rank
     * @return true if all cards have the declared rank, false otherwise
     */
    public boolean matchesDeclaration() {
        // Check each card
        for (int i = 0; i < cardCount; i++) {
            // If any card doesn't match the declared rank, return false
            if (cards[i].getRank() != declaredRank) {
                return false;
            }
        }

        // All cards matched the declared rank
        return true;
    }
}