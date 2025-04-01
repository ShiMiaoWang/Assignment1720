package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a player's play
 */
public class Play {
    private Player player;          // Player who played
    private ArrayList<Card> cards;    // Cards played
    private Card.Rank declaredRank;  // Declared rank

    /**
     * Create a new play
     */
    public Play(Player player, List<Card> cards, Card.Rank declaredRank) {
        this.player = player;
        this.cards = new ArrayList<>();

        // Add all cards and make them visible
        for (Card card : cards) {
            card.revealCard();
            this.cards.add(card);
        }

        this.declaredRank = declaredRank;
    }

    /**
     * Get the player who played
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Get the played cards
     */
    public List<Card> getCards() {
        return new ArrayList<>(cards);
    }

    /**
     * Get the declared rank
     */
    public Card.Rank getDeclaredRank() {
        return declaredRank;
    }

    /**
     * Check if actual cards match the declared rank
     */
    public boolean matchesDeclaration() {
        for (Card card : cards) {
            if (card.getRank() != declaredRank) {
                return false;
            }
        }
        return true;
    }
}