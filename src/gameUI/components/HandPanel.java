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

        revalidate();
        repaint();
    }
}