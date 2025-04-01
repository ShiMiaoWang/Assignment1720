package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Manages the play pile in the game
 */
public class Pile {
    private ArrayList<Play> plays;  // Executed plays

    /**
     * Create an empty pile
     */
    public Pile() {
        this.plays = new ArrayList<>();
    }

    /**
     * Add a play to the pile
     */
    public void addPlay(Play play) {
        plays.add(play);
    }

    /**
     * Get the last play
     */
    public Play getLastPlay() {
        if (!plays.isEmpty()) {
            return plays.get(plays.size() - 1);
        }
        return null;
    }

    /**
     * Get all cards in the pile
     */
    public List<Card> getAllCards() {
        ArrayList<Card> allCards = new ArrayList<>();
        for (Play play : plays) {
            allCards.addAll(play.getCards());
        }
        return allCards;
    }

    /**
     * Clear the pile
     */
    public void clearPile() {
        plays.clear();
    }

    /**
     * Get the top card of the pile
     */
    public Card getTopCard() {
        Play lastPlay = getLastPlay();
        if (lastPlay != null && !lastPlay.getCards().isEmpty()) {
            List<Card> cards = lastPlay.getCards();
            return cards.get(cards.size() - 1);
        }
        return null;
    }
}