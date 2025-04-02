package gameUI.components;

import model.Card;

import javax.swing.*;
import java.awt.*;

/**
 * Panel that displays a player's hand of cards
 */
public class HandPanel extends JPanel {

    // Array of card components in the hand
    private CardComponent[] cardComponents;
    private int componentCount;

    // Array to track which component represents which model card
    private Card[] modelCards;

    /**
     * Create a hand panel using cards and suit images
     *
     * @param cards      Array of cards in the player's hand
     * @param suitImages Array of card suit images
     */
    public HandPanel(Card[] cards, ImageIcon[] suitImages) {
        // Set layout
        setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
        setOpaque(false);

        // Get number of cards
        int cardCount = cards.length;

        // Initialize arrays
        cardComponents = new CardComponent[cardCount];
        modelCards = new Card[cardCount];
        componentCount = cardCount;

        // Create a component for each card
        for (int i = 0; i < cardCount; i++) {
            Card card = cards[i];

            // Get the image for this card's suit
            ImageIcon suitIcon = getSuitImageForCard(card, suitImages);

            // Create a card component
            CardComponent cardComponent = new CardComponent(suitIcon, card);

            // Add to our arrays
            cardComponents[i] = cardComponent;
            modelCards[i] = card;

            // Add to the panel
            add(cardComponent);
        }
    }

    /**
     * Get the right suit image for a card
     */
    private ImageIcon getSuitImageForCard(Card card, ImageIcon[] suitImages) {
        // Get the suit of this card
        Card.Suit cardSuit = card.getSuit();

        // Find the right image
        if (cardSuit == Card.Suit.COINS) {
            return suitImages[0];
        } else if (cardSuit == Card.Suit.CHALICES) {
            return suitImages[1];
        } else if (cardSuit == Card.Suit.SWORDS) {
            return suitImages[2];
        } else { // WANDS
            return suitImages[3];
        }
    }

    /**
     * Alternative constructor using just card icons
     *
     * @param cardIcons Array of card icons
     */
    public HandPanel(ImageIcon[] cardIcons) {
        // Set layout
        setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
        setOpaque(false);

        // Get number of cards
        int cardCount = cardIcons.length;

        // Initialize arrays
        cardComponents = new CardComponent[cardCount];
        modelCards = new Card[cardCount];
        componentCount = cardCount;

        // Create a component for each icon
        for (int i = 0; i < cardCount; i++) {
            ImageIcon icon = cardIcons[i];

            // Create a card component (no model reference)
            CardComponent cardComponent = new CardComponent(icon, null);

            // Add to our list
            cardComponents[i] = cardComponent;

            // Add to the panel
            add(cardComponent);
        }
    }

    /**
     * Get array of currently selected card components
     *
     * @return Array of selected card components
     */
    public CardComponent[] getSelectedCards() {
        // First count how many cards are selected
        int selectedCount = 0;
        for (int i = 0; i < componentCount; i++) {
            if (cardComponents[i].isSelected()) {
                selectedCount++;
            }
        }

        // Create array of exactly the right size
        CardComponent[] selected = new CardComponent[selectedCount];

        // Fill array with selected components
        int selectedIndex = 0;
        for (int i = 0; i < componentCount; i++) {
            CardComponent card = cardComponents[i];
            if (card.isSelected()) {
                selected[selectedIndex] = card;
                selectedIndex++;
            }
        }

        return selected;
    }

    /**
     * Get array of model cards that are currently selected
     *
     * @return Array of selected model cards
     */
    public Card[] getSelectedModelCards() {
        // Get all selected card components
        CardComponent[] selectedComponents = getSelectedCards();
        int selectedCount = selectedComponents.length;

        // Create array for model cards
        Card[] selectedCards = new Card[selectedCount];

        // For each selected component, get the corresponding model card
        for (int i = 0; i < selectedCount; i++) {
            CardComponent component = selectedComponents[i];

            // Find this component in our array
            for (int j = 0; j < componentCount; j++) {
                if (cardComponents[j] == component) {
                    // Found it - add the model card
                    selectedCards[i] = modelCards[j];
                    break;
                }
            }
        }

        return selectedCards;
    }

    /**
     * Remove cards from the hand panel
     *
     * @param toRemove Array of card components to remove
     */
    public void removeCards(CardComponent[] toRemove) {
        // Create new arrays of the right size
        int newCount = componentCount - toRemove.length;
        CardComponent[] newComponents = new CardComponent[newCount];
        Card[] newModelCards = new Card[newCount];

        // Copy only the cards we're keeping
        int newIndex = 0;
        for (int i = 0; i < componentCount; i++) {
            CardComponent current = cardComponents[i];

            // Check if this card should be removed
            boolean shouldRemove = false;
            for (int j = 0; j < toRemove.length; j++) {
                if (current == toRemove[j]) {
                    shouldRemove = true;
                    break;
                }
            }

            // If it shouldn't be removed, copy it
            if (!shouldRemove) {
                newComponents[newIndex] = current;
                newModelCards[newIndex] = modelCards[i];
                newIndex++;
            } else {
                // Remove from the panel
                remove(current);
            }
        }

        // Update our arrays
        cardComponents = newComponents;
        modelCards = newModelCards;
        componentCount = newCount;

        // Refresh the panel
        revalidate();
        repaint();
    }

    /**
     * Update hand with new cards
     *
     * @param cards      New array of cards
     * @param suitImages Array of card suit images
     */
    public void updateCards(Card[] cards, ImageIcon[] suitImages) {
        // Clear existing cards
        removeAll();

        // Get new count
        int newCount = cards.length;

        // Create new arrays
        cardComponents = new CardComponent[newCount];
        modelCards = new Card[newCount];
        componentCount = newCount;

        // Add each new card
        for (int i = 0; i < newCount; i++) {
            Card card = cards[i];

            // Get the image for this card's suit
            ImageIcon suitIcon = getSuitImageForCard(card, suitImages);

            // Create a card component
            CardComponent cardComponent = new CardComponent(suitIcon, card);

            // Add to our arrays
            cardComponents[i] = cardComponent;
            modelCards[i] = card;

            // Add to the panel
            add(cardComponent);
        }

        // Log debug info
        System.out.println("HandPanel updated: " + newCount + " cards");

        // Refresh the panel
        revalidate();
        repaint();
    }

    /**
     * Verify card mappings match player's actual hand
     *
     * @param playerHand The player's hand of cards
     * @return true if mappings are valid, false otherwise
     */
    public boolean verifyCardMappings(Card[] playerHand) {
        // Check if all components have valid card mappings
        for (int i = 0; i < componentCount; i++) {
            Card modelCard = modelCards[i];

            // Check if model card is null
            if (modelCard == null) {
                System.out.println("Warning: Component found without model card mapping");
                return false;
            }

            // Check if model card is in player's hand
            boolean cardInHand = false;
            for (int j = 0; j < playerHand.length; j++) {
                if (playerHand[j].equals(modelCard)) {
                    cardInHand = true;
                    break;
                }
            }

            if (!cardInHand) {
                System.out.println("Warning: Component maps to card not in player's hand");
                return false;
            }
        }

        // Check if all player cards have corresponding components
        for (int i = 0; i < playerHand.length; i++) {
            Card card = playerHand[i];

            boolean found = false;
            for (int j = 0; j < componentCount; j++) {
                if (card.equals(modelCards[j])) {
                    found = true;
                    break;
                }
            }

            if (!found) {
                System.out.println("Warning: Player has card without UI component");
                return false;
            }
        }

        // All checks passed
        return true;
    }
}