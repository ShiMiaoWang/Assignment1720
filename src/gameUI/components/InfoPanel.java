package gameUI.components;

import javax.swing.*;
import java.awt.*;

public class InfoPanel extends JPanel {
    private JLabel roundLabel;
    private JLabel playerLabel;
    private JLabel cardCountLabel;

    public InfoPanel() {
        // Set layout and background
        setLayout(new GridLayout(1, 3, 20, 0));
        setOpaque(false);

        // Create labels
        roundLabel = createLabel("Round: 1");
        playerLabel = createLabel("Player: -");
        cardCountLabel = createLabel("Cards Left: 0");

        // Add to panel
        add(roundLabel);
        add(playerLabel);
        add(cardCountLabel);
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Arial", Font.BOLD, 16));
        return label;
    }

    public void setRound(int round) {
        roundLabel.setText("Round: " + round);
    }

    public void setPlayerName(String name) {
        playerLabel.setText("Player: " + name);
    }

    public void setCardCount(int count) {
        cardCountLabel.setText("Cards Left: " + count);
    }
}