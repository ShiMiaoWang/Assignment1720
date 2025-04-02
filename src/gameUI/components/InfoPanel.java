package gameUI.components;

import javax.swing.*;
import java.awt.*;

/**
 * Panel that displays game information (round, player, card count)
 */
public class InfoPanel extends JPanel {
    // Labels for displaying information
    private JLabel roundLabel;
    private JLabel playerLabel;
    private JLabel cardCountLabel;

    /**
     * Create a new info panel
     */
    public InfoPanel() {
        // Set layout and background
        setLayout(new GridLayout(1, 3, 20, 0));
        setOpaque(false);

        // Create labels for each piece of information
        roundLabel = createLabel("Round: 1");
        playerLabel = createLabel("Player: -");
        cardCountLabel = createLabel("Cards Left: 0");

        // Add labels to panel
        add(roundLabel);
        add(playerLabel);
        add(cardCountLabel);
    }

    /**
     * Helper method to create a label with consistent style
     * @param text The text for the label
     * @return A styled JLabel
     */
    private JLabel createLabel(String text) {
        // Create label
        JLabel label = new JLabel(text);

        // Center the text
        label.setHorizontalAlignment(SwingConstants.CENTER);

        // Set text color and font
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Arial", Font.BOLD, 16));

        return label;
    }

    /**
     * Update the round number display
     * @param round Current round number
     */
    public void setRound(int round) {
        roundLabel.setText("Round: " + round);
    }

    /**
     * Update the player name display
     * @param name Current player name
     */
    public void setPlayerName(String name) {
        playerLabel.setText("Player: " + name);
    }

    /**
     * Update the card count display
     * @param count Number of cards in player's hand
     */
    public void setCardCount(int count) {
        cardCountLabel.setText("Cards Left: " + count);
    }
}