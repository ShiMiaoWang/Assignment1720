package gameUI;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Dialog for setting up players before the game starts
 */
public class PlayerSetupDialog extends JDialog {
    private List<JTextField> playerNameFields;
    private int numPlayers;
    private List<String> playerNames;
    private boolean confirmed;

    public PlayerSetupDialog(JFrame parent, int minPlayers, int maxPlayers) {
        super(parent, "Player Setup", true);

        playerNameFields = new ArrayList<>();
        playerNames = new ArrayList<>();
        confirmed = false;

        // Create components for player selection
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Player count selection
        JPanel countPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        countPanel.add(new JLabel("Number of Players:"));

        Integer[] playerCounts = new Integer[maxPlayers - minPlayers + 1];
        for (int i = 0; i < playerCounts.length; i++) {
            playerCounts[i] = minPlayers + i;
        }

        JComboBox<Integer> playerCountSelector = new JComboBox<>(playerCounts);
        countPanel.add(playerCountSelector);

        mainPanel.add(countPanel, BorderLayout.NORTH);

        // Player name fields
        JPanel namesPanel = new JPanel(new GridLayout(0, 1, 5, 5));
        JScrollPane scrollPane = new JScrollPane(namesPanel);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton okButton = new JButton("Start Game");
        JButton cancelButton = new JButton("Cancel");

        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Set initial player count
        numPlayers = minPlayers;
        updatePlayerFields(namesPanel);

        // Add event listeners
        playerCountSelector.addActionListener(e -> {
            numPlayers = (Integer) playerCountSelector.getSelectedItem();
            updatePlayerFields(namesPanel);
            pack();
        });

        okButton.addActionListener(e -> {
            if (validatePlayerNames()) {
                confirmed = true;
                setVisible(false);
            }
        });

        cancelButton.addActionListener(e -> {
            confirmed = false;
            setVisible(false);
        });

        // Set dialog properties
        setContentPane(mainPanel);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        pack();
        setLocationRelativeTo(parent);
    }

    private void updatePlayerFields(JPanel panel) {
        panel.removeAll();
        playerNameFields.clear();

        for (int i = 0; i < numPlayers; i++) {
            JPanel playerPanel = new JPanel(new BorderLayout());
            playerPanel.add(new JLabel("Player " + (i + 1) + ":"), BorderLayout.WEST);

            JTextField nameField = new JTextField(15);
            nameField.setText("Player " + (i + 1));
            playerNameFields.add(nameField);

            playerPanel.add(nameField, BorderLayout.CENTER);
            panel.add(playerPanel);
        }

        panel.revalidate();
        panel.repaint();
    }

    private boolean validatePlayerNames() {
        playerNames.clear();

        for (JTextField field : playerNameFields) {
            String name = field.getText().trim();

            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Player names cannot be empty",
                        "Validation Error",
                        JOptionPane.ERROR_MESSAGE);
                return false;
            }

            playerNames.add(name);
        }

        return true;
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public List<String> getPlayerNames() {
        return playerNames;
    }

    public int getPlayerCount() {
        return numPlayers;
    }
}