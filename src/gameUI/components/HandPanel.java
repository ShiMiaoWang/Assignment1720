package gameUI.components;

import model.Card;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HandPanel extends JPanel {

    private ArrayList<CardComponent> cardComponents;
    private Map<CardComponent, Card> componentToCardMap;

    public HandPanel(List<Card> cards, Map<Card.Suit, ImageIcon> suitImages) {
        // Set layout
        setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
        setOpaque(false);

        cardComponents = new ArrayList<>();
        componentToCardMap = new HashMap<>();

        // Create all card components
        for (Card card : cards) {
            ImageIcon suitIcon = suitImages.get(card.getSuit());
            CardComponent cardComponent = new CardComponent(suitIcon, card);
            cardComponents.add(cardComponent);
            componentToCardMap.put(cardComponent, card);
            add(cardComponent);
        }
    }

    // Alternative constructor that takes ImageIcons directly (for backward compatibility)
    public HandPanel(List<ImageIcon> cardIcons) {
        // Set layout
        setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
        setOpaque(false);

        cardComponents = new ArrayList<>();
        componentToCardMap = new HashMap<>();

        // Create all card components
        for (ImageIcon icon : cardIcons) {
            CardComponent card = new CardComponent(icon, null); // No model card reference
            cardComponents.add(card);
            add(card);
        }
    }

    // Get selected cards
    public List<CardComponent> getSelectedCards() {
        List<CardComponent> selected = new ArrayList<>();
        for (CardComponent card : cardComponents) {
            if (card.isSelected()) {
                selected.add(card);
            }
        }
        return selected;
    }

    // Get selected model cards
    public List<Card> getSelectedModelCards() {
        List<Card> selectedCards = new ArrayList<>();
        for (CardComponent component : getSelectedCards()) {
            Card modelCard = componentToCardMap.get(component);
            if (modelCard != null) {
                selectedCards.add(modelCard);
            }
        }
        return selectedCards;
    }

    // Remove cards
    public void removeCards(List<CardComponent> toRemove) {
        for (CardComponent card : toRemove) {
            cardComponents.remove(card);
            componentToCardMap.remove(card);
            remove(card);
        }
        revalidate();
        repaint();
    }

    // Update hand with new cards
    public void updateCards(List<Card> cards, Map<Card.Suit, ImageIcon> suitImages) {
        // Clear existing cards
        removeAll();
        cardComponents.clear();
        componentToCardMap.clear();

        // Add new cards
        for (Card card : cards) {
            ImageIcon suitIcon = suitImages.get(card.getSuit());
            CardComponent cardComponent = new CardComponent(suitIcon, card);
            cardComponents.add(cardComponent);
            componentToCardMap.put(cardComponent, card);
            add(cardComponent);
        }

        // Log debug info
        System.out.println("HandPanel updated: " + cards.size() + " cards");

        revalidate();
        repaint();
    }

    // Verify card mappings match player's actual hand
    public boolean verifyCardMappings(List<Card> playerHand) {
        // Check if all components have valid card mappings
        for (CardComponent component : cardComponents) {
            Card modelCard = componentToCardMap.get(component);
            if (modelCard == null) {
                System.out.println("Warning: Component found without model card mapping");
                return false;
            }

            if (!playerHand.contains(modelCard)) {
                System.out.println("Warning: Component maps to card not in player's hand");
                return false;
            }
        }

        // Check if all player cards have corresponding components
        for (Card card : playerHand) {
            boolean found = false;
            for (CardComponent component : cardComponents) {
                if (card.equals(componentToCardMap.get(component))) {
                    found = true;
                    break;
                }
            }

            if (!found) {
                System.out.println("Warning: Player has card without UI component");
                return false;
            }
        }

        return true;
    }
}