package gameUI.components;

import model.Card;

import javax.swing.*;
import java.awt.*;

/**
 * A component for selecting card ranks
 */
public class RankSelector extends JPanel {
    private JComboBox<String> rankSelector;

    public RankSelector() {
        setLayout(new FlowLayout(FlowLayout.LEFT));
        setBackground(new Color(55, 66, 88));

        JLabel label = new JLabel("Declare Rank:");
        label.setForeground(Color.WHITE);
        add(label);

        // Create selector with all ranks
        String[] ranks = new String[Card.Rank.values().length];
        for (int i = 0; i < Card.Rank.values().length; i++) {
            ranks[i] = Card.Rank.values()[i].toString();
        }

        rankSelector = new JComboBox<>(ranks);
        add(rankSelector);
    }

    public Card.Rank getSelectedRank() {
        String selectedRank = (String) rankSelector.getSelectedItem();
        return Card.Rank.valueOf(selectedRank);
    }
}