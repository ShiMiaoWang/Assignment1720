package gameUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Dialog for setting up players before the game starts
 */
public class PlayerSetupDialog extends JDialog {
    // Input fields for player names
    private JTextField[] playerNameFields;

    // Number of players selected
    private int numPlayers;

    // List of final player names
    private String[] playerNames;

    // Whether dialog was confirmed or canceled
    private boolean confirmed;

    /**
     * Create a new player setup dialog
     * @param parent The parent frame
     * @param minPlayers Minimum number of players
     * @param maxPlayers Maximum number of players
     */
    public PlayerSetupDialog(JFrame parent, int minPlayers, int maxPlayers) {
        // Call parent constructor
        super(parent, "Player Setup", true);

        // Initialize variables
        playerNames = new String[maxPlayers];
        confirmed = false;

        // Create main panel with border spacing
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Create player count selection panel
        JPanel countPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel countLabel = new JLabel("Number of Players:");
        countPanel.add(countLabel);

        // Create dropdown with player count options
        Integer[] playerCounts = new Integer[maxPlayers - minPlayers + 1];
        for (int i = 0; i < playerCounts.length; i++) {
            playerCounts[i] = minPlayers + i;
        }

        JComboBox playerCountSelector = new JComboBox(playerCounts);
        countPanel.add(playerCountSelector);

        // Add count panel to main panel
        mainPanel.add(countPanel, BorderLayout.NORTH);

        // Create panel for player name fields
        final JPanel namesPanel = new JPanel(new GridLayout(0, 1, 5, 5));
        JScrollPane scrollPane = new JScrollPane(namesPanel);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Create buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton okButton = new JButton("Start Game");
        JButton cancelButton = new JButton("Cancel");

        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Set initial player count
        numPlayers = minPlayers;
        playerNameFields = new JTextField[maxPlayers];
        updatePlayerFields(namesPanel);

        // Create player count selection handler
        playerCountSelector.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Get selected count
                JComboBox source = (JComboBox) e.getSource();
                Integer selection = (Integer) source.getSelectedItem();
                numPlayers = selection.intValue();

                // Update player name fields
                updatePlayerFields(namesPanel);

                // Resize dialog to fit content
                pack();
            }
        });

        // Create OK button handler
        okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Validate input
                if (validatePlayerNames()) {
                    confirmed = true;
                    setVisible(false);
                }
            }
        });

        // Create Cancel button handler
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                confirmed = false;
                setVisible(false);
            }
        });

        // Set dialog properties
        setContentPane(mainPanel);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        pack();
        setLocationRelativeTo(parent);
    }

    /**
     * Update the player name fields based on selected count
     */
    private void updatePlayerFields(JPanel panel) {
        // Clear existing fields
        panel.removeAll();

        // Create fields for each player
        for (int i = 0; i < numPlayers; i++) {
            // Create panel for this player
            JPanel playerPanel = new JPanel(new BorderLayout());

            // Add label
            JLabel nameLabel = new JLabel("Player " + (i + 1) + ":");
            playerPanel.add(nameLabel, BorderLayout.WEST);

            // Create text field if needed
            if (playerNameFields[i] == null) {
                playerNameFields[i] = new JTextField(15);
                playerNameFields[i].setText("Player " + (i + 1));
            }

            // Add text field
            playerPanel.add(playerNameFields[i], BorderLayout.CENTER);

            // Add to main panel
            panel.add(playerPanel);
        }

        // Refresh the panel
        panel.revalidate();
        panel.repaint();
    }

    /**
     * Validate player names and store them
     * @return true if all names are valid, false otherwise
     */
    private boolean validatePlayerNames() {
        // Check each name field
        for (int i = 0; i < numPlayers; i++) {
            String name = playerNameFields[i].getText().trim();

            // Check if name is empty
            if (name.length() == 0) {
                JOptionPane.showMessageDialog(this,
                        "Player names cannot be empty",
                        "Validation Error",
                        JOptionPane.ERROR_MESSAGE);
                return false;
            }

            // Add valid name to list
            playerNames[i] = name;
        }

        // All names are valid
        return true;
    }

    /**
     * Check if dialog was confirmed
     */
    public boolean isConfirmed() {
        return confirmed;
    }

    /**
     * Get the player names entered
     */
    public String[] getPlayerNames() {
        // Create array of exact size needed
        String[] result = new String[numPlayers];

        // Copy names
        for (int i = 0; i < numPlayers; i++) {
            result[i] = playerNames[i];
        }

        return result;
    }

    /**
     * Get the selected player count
     */
    public int getPlayerCount() {
        return numPlayers;
    }
}