package model;

/**
 * Manages the pile where cards are played during the game
 */
public class Pile {
    private Play[] plays;   // Array of plays that have been made
    private int playCount;  // Number of plays

    /**
     * Create an empty pile
     */
    public Pile() {
        // Can store up to 100 plays (more than enough for a game)
        this.plays = new Play[100];
        this.playCount = 0;
    }

    /**
     * Add a play to the pile
     */
    public void addPlay(Play play) {
        plays[playCount] = play;
        playCount++;
    }

    /**
     * Get the most recent play
     * @return The last play, or null if pile is empty
     */
    public Play getLastPlay() {
        // Check if there are any plays
        if (playCount == 0) {
            return null;
        } else {
            // Return the most recent play (last in the array)
            return plays[playCount - 1];
        }
    }

    /**
     * Get all cards in the pile
     * @return Array of all cards from all plays
     */
    public Card[] getAllCards() {
        // First count total number of cards
        int totalCards = 0;
        for (int i = 0; i < playCount; i++) {
            totalCards += plays[i].getCardCount();
        }

        // Create array to hold all cards
        Card[] allCards = new Card[totalCards];

        // Fill array with cards from all plays
        int cardIndex = 0;
        for (int i = 0; i < playCount; i++) {
            Play play = plays[i];
            Card[] playCards = play.getCards();

            for (int j = 0; j < playCards.length; j++) {
                allCards[cardIndex] = playCards[j];
                cardIndex++;
            }
        }

        return allCards;
    }

    /**
     * Clear all plays from the pile
     */
    public void clearPile() {
        // Just reset the play count (no need to clear array)
        playCount = 0;
    }

    /**
     * Get the top card of the pile
     * @return The top card, or null if pile is empty
     */
    public Card getTopCard() {
        // Get the last play
        Play lastPlay = getLastPlay();

        // If there is no last play, return null
        if (lastPlay == null) {
            return null;
        }

        // Get cards from the last play
        Card[] cards = lastPlay.getCards();

        // If there are no cards, return null
        if (cards.length == 0) {
            return null;
        }

        // Return the last card from the last play
        return cards[cards.length - 1];
    }

    /**
     * Get number of plays in pile
     */
    public int getPlayCount() {
        return playCount;
    }
}