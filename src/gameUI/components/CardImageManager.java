package gameUI.components;

import model.Card;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Manages the loading and caching of card images
 */
public class CardImageManager {
    private static CardImageManager instance;
    private Map<Card.Suit, ImageIcon> suitImages;

    private CardImageManager() {
        suitImages = new HashMap<>();
        loadImages();
    }

    public static CardImageManager getInstance() {
        if (instance == null) {
            instance = new CardImageManager();
        }
        return instance;
    }

    // Load all card images
    private void loadImages() {
        try {
            // Load suit images
            suitImages.put(Card.Suit.COINS, new ImageIcon(getClass().getResource("/icons/cards/coins.png")));
            suitImages.put(Card.Suit.CHALICES, new ImageIcon(getClass().getResource("/icons/cards/chalices.png")));
            suitImages.put(Card.Suit.SWORDS, new ImageIcon(getClass().getResource("/icons/cards/swords.png")));
            suitImages.put(Card.Suit.WANDS, new ImageIcon(getClass().getResource("/icons/cards/wands.png")));
        } catch (Exception e) {
            System.err.println("Error loading card images: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Get image for a specific suit
    public ImageIcon getSuitImage(Card.Suit suit) {
        return suitImages.get(suit);
    }

    // Get all suit images
    public Map<Card.Suit, ImageIcon> getAllSuitImages() {
        return new HashMap<>(suitImages);
    }
}