package gameUI.components;

import model.Card;

import javax.swing.*;

/**
 * Manages the loading and caching of card images
 * Uses singleton pattern (only one instance)
 */
public class CardImageManager {
    // The single instance of CardImageManager
    private static CardImageManager instance;

    // Array of suit images (one for each suit)
    private ImageIcon[] suitImages;

    /**
     * Private constructor (for singleton pattern)
     */
    private CardImageManager() {
        // Initialize the suit images array (4 suits)
        suitImages = new ImageIcon[4];

        // Load all images
        loadImages();
    }

    /**
     * Get the singleton instance
     */
    public static CardImageManager getInstance() {
        // Create instance if it doesn't exist
        if (instance == null) {
            instance = new CardImageManager();
        }

        // Return the instance
        return instance;
    }

    /**
     * Load all card images
     */
    private void loadImages() {
        try {
            // Load each suit image
            suitImages[0] = new ImageIcon(getClass().getResource("/icons/cards/coins.png"));
            suitImages[1] = new ImageIcon(getClass().getResource("/icons/cards/chalices.png"));
            suitImages[2] = new ImageIcon(getClass().getResource("/icons/cards/swords.png"));
            suitImages[3] = new ImageIcon(getClass().getResource("/icons/cards/wands.png"));
        } catch (Exception e) {
            // Print error if any images fail to load
            System.out.println("Error loading card images: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Get image for a specific suit
     * @param suit The suit to get image for
     * @return The image for the specified suit
     */
    public ImageIcon getSuitImage(Card.Suit suit) {
        // Convert suit to index
        int index = suitToIndex(suit);

        // Return the image
        return suitImages[index];
    }

    /**
     * Convert a suit to an array index
     */
    private int suitToIndex(Card.Suit suit) {
        if (suit == Card.Suit.COINS) {
            return 0;
        } else if (suit == Card.Suit.CHALICES) {
            return 1;
        } else if (suit == Card.Suit.SWORDS) {
            return 2;
        } else { // WANDS
            return 3;
        }
    }

    /**
     * Get all suit images
     * @return An array mapping suits to their images
     */
    public Card.Suit[] getSuits() {
        // Create array of all suits
        Card.Suit[] suits = new Card.Suit[4];
        suits[0] = Card.Suit.COINS;
        suits[1] = Card.Suit.CHALICES;
        suits[2] = Card.Suit.SWORDS;
        suits[3] = Card.Suit.WANDS;
        return suits;
    }

    /**
     * Get all suit images
     */
    public ImageIcon[] getAllSuitImages() {
        // Create a copy of the images array
        ImageIcon[] result = new ImageIcon[4];
        for (int i = 0; i < 4; i++) {
            result[i] = suitImages[i];
        }
        return result;
    }
}