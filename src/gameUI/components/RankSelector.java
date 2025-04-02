package gameUI.components;

import model.Card;

import javax.swing.*;
import java.awt.*;

/**
 * A component for selecting card ranks
 */
public class RankSelector extends JPanel {
    // Dropdown for selecting ranks
    private JComboBox rankSelector;

    /**
     * Create a new rank selector
     */
    public RankSelector() {
        // Set layout
        setLayout(new FlowLayout(FlowLayout.LEFT));

        // Set background color
        setBackground(new Color(55, 66, 88));

        // Create label
        JLabel label = new JLabel("Declare Rank:");
        label.setForeground(Color.WHITE);
        add(label);

        // Get all possible rank values
        Card.Rank[] allRanks = Card.Rank.values();

        // Create array of rank names
        String[] rankNames = new String[allRanks.length];

        // Fill array with rank names
        for (int i = 0; i < allRanks.length; i++) {
            rankNames[i] = allRanks[i].toString();
        }

        // Create dropdown with rank names
        rankSelector = new JComboBox(rankNames);

        // Add to panel
        add(rankSelector);
    }

    /**
     * Get the currently selected rank
     * @return The selected Card.Rank
     */
    public Card.Rank getSelectedRank() {
        // Get selected rank name
        String selectedRank = (String) rankSelector.getSelectedItem();

        // Get all possible rank values
        Card.Rank[] allRanks = Card.Rank.values();

        // Find the one that matches
        for (int i = 0; i < allRanks.length; i++) {
            if (allRanks[i].toString().equals(selectedRank)) {
                return allRanks[i];
            }
        }

        // Default to ONE if no match (shouldn't happen)
        return Card.Rank.ONE;
    }
}