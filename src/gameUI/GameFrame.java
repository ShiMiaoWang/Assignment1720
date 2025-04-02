package gameUI;

import gameUI.components.*;
import model.Card;
import model.Manager;
import model.Play;
import model.Player;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Main game window
 */
public class GameFrame extends JFrame {
    // Game UI components
    private InfoPanel infoPanel;         // Information panel
    private HandPanel handPanel;         // Hand panel
    private JPanel playAreaPanel;        // Play area
    private Manager gameManager;         // Game manager
    private RankSelector rankSelector;   // Rank selector

    /**
     * Create the game window
     * @param manager Game manager
     */
    public GameFrame(Manager manager) {
        // Store game manager reference
        this.gameManager = manager;

        // Set window properties
        setTitle("SuperMadiao!");
        setSize(1152, 864);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);  // Center on screen

        // Create main panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(new Color(34, 45, 65));

        // Add Info Panel (Top)
        infoPanel = new InfoPanel();
        mainPanel.add(infoPanel, BorderLayout.NORTH);

        // Add Play Area (Center)
        playAreaPanel = new JPanel();
        playAreaPanel.setLayout(new FlowLayout());
        playAreaPanel.setPreferredSize(new Dimension(400, 200));
        playAreaPanel.setBackground(new Color(55, 66, 88));
        mainPanel.add(playAreaPanel, BorderLayout.CENTER);

        // Add Deck Panel (Right)
        JPanel deckPanel = createPlaceholderPanel("Deck");
        mainPanel.add(deckPanel, BorderLayout.EAST);

        // Add Declaration Area (Left)
        JPanel declarationPanel = new JPanel();
        declarationPanel.setLayout(new BorderLayout());
        declarationPanel.setBackground(new Color(55, 66, 88));

        // Add title to declaration panel
        JLabel declarationTitle = new JLabel("Declaration Area");
        declarationTitle.setHorizontalAlignment(SwingConstants.CENTER);
        declarationTitle.setFont(new Font("Arial", Font.BOLD, 20));
        declarationTitle.setForeground(Color.WHITE);
        declarationPanel.add(declarationTitle, BorderLayout.NORTH);

        // Create and add rank selector
        rankSelector = new RankSelector();
        declarationPanel.add(rankSelector, BorderLayout.CENTER);

        // Add declaration panel to main panel
        mainPanel.add(declarationPanel, BorderLayout.WEST);

        // Create Bottom Panel (Hand and buttons)
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BorderLayout());

        // Add Player Hand (Top of bottom panel)
        Player currentPlayer = gameManager.getCurrentPlayer();
        CardImageManager imageManager = CardImageManager.getInstance();

        // Create hand panel with player's cards
        Card[] playerCards = currentPlayer.getHand();
        ImageIcon[] suitImages = imageManager.getAllSuitImages();
        handPanel = new HandPanel(playerCards, suitImages);

        // Put hand panel in a scroll pane
        JScrollPane handScrollPane = new JScrollPane(handPanel);
        handScrollPane.setPreferredSize(new Dimension(400, 120));
        handScrollPane.setOpaque(false);
        handScrollPane.getViewport().setOpaque(false);
        handScrollPane.setBorder(null);
        bottomPanel.add(handScrollPane, BorderLayout.NORTH);

        // Add Buttons Panel (Bottom of bottom panel)
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());

        // Create Challenge button
        JButton challengeBtn = new JButton("Challenge");
        challengeBtn.setFont(new Font("Arial", Font.BOLD, 18));
        ChallengeButtonListener challengeListener = new ChallengeButtonListener();
        challengeBtn.addActionListener(challengeListener);
        buttonPanel.add(challengeBtn);

        // Create Play button
        JButton playBtn = new JButton("Play Selected Cards");
        playBtn.setFont(new Font("Arial", Font.BOLD, 18));
        PlayButtonListener playListener = new PlayButtonListener();
        playBtn.addActionListener(playListener);
        buttonPanel.add(playBtn);

        // Create New Game button
        JButton newGameBtn = new JButton("New Game");
        newGameBtn.setFont(new Font("Arial", Font.BOLD, 18));
        NewGameButtonListener newGameListener = new NewGameButtonListener();
        newGameBtn.addActionListener(newGameListener);
        buttonPanel.add(newGameBtn);

        // Add button panel to bottom panel
        bottomPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Add bottom panel to main panel
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        // Set the content pane
        setContentPane(mainPanel);

        // Show the window
        setVisible(true);
    }

    /**
     * Handle playing cards
     */
    private void handlePlayCards() {
        // Get selected card components
        CardComponent[] selectedComponents = handPanel.getSelectedCards();

        // Check if any cards are selected
        if (selectedComponents.length == 0) {
            JOptionPane.showMessageDialog(this, "Please select at least one card to play.");
            return;
        }

        // Get the selected model cards
        Card[] selectedCards = handPanel.getSelectedModelCards();
        if (selectedCards.length == 0) {
            JOptionPane.showMessageDialog(this, "Error retrieving card data.");
            return;
        }

        // Get current player info
        Player currentPlayer = gameManager.getCurrentPlayer();
        String currentPlayerName = currentPlayer.getName();

        // Add debug logging
        System.out.println("Selected cards: " + selectedCards.length);
        System.out.println("Player hand: " + currentPlayer.getCardCount());

        // Verify mappings consistency
        Card[] playerHand = currentPlayer.getHand();
        boolean mappingsValid = handPanel.verifyCardMappings(playerHand);
        if (!mappingsValid) {
            JOptionPane.showMessageDialog(this, "UI and game data out of sync. Refreshing display.");
            updatePlayerHand(); // Refresh hand display
            return;
        }

        // Get declared rank from rank selector
        Card.Rank declaredRank = rankSelector.getSelectedRank();

        try {
            // Play the cards
            gameManager.playCards(currentPlayer, selectedCards, declaredRank);

            // Update play area display
            updatePlayArea();

            // Remove cards from hand
            handPanel.removeCards(selectedComponents);

            // Update player info (turns have changed)
            Player newCurrentPlayer = gameManager.getCurrentPlayer();
            String newPlayerName = newCurrentPlayer.getName();
            int newPlayerCardCount = newCurrentPlayer.getCardCount();
            updatePlayerInfo(newPlayerName, newPlayerCardCount);

            // Automatically handle turn change
            autoHandleTurnChange(currentPlayerName, newPlayerName);

            // Check for winner
            Player winner = gameManager.checkForWinner();
            if (winner != null) {
                String winMessage = winner.getName() + " wins the game!";
                JOptionPane.showMessageDialog(this, winMessage);
                // Handle game end
            }
        } catch (Exception e) {
            // Show error message
            JOptionPane.showMessageDialog(this, e.getMessage());

            // Refresh UI after error
            updatePlayerHand();
        }
    }

    /**
     * Display turn change notification and update UI
     */
    private void autoHandleTurnChange(String currentPlayerName, String nextPlayerName) {
        // Create message to display
        String message = currentPlayerName + " has played. It's " + nextPlayerName + "'s turn now!";

        // Show dialog to user
        String title = "Turn Round!";
        JOptionPane.showMessageDialog(this, message, title, JOptionPane.INFORMATION_MESSAGE);

        // Update the hand display for the new player
        updatePlayerHand();
    }

    /**
     * Handle challenge action
     */
    private void handleChallenge() {
        // Get the last play
        Play lastPlay = gameManager.getPile().getLastPlay();

        // Check if there's a play to challenge
        if (lastPlay == null) {
            JOptionPane.showMessageDialog(this, "No cards to challenge!");
            return;
        }

        // Get challenger and last player
        Player challenger = gameManager.getCurrentPlayer();
        String challengerName = challenger.getName();
        Player lastPlayer = lastPlay.getPlayer();

        // Execute the challenge
        boolean challengeSuccessful = gameManager.challengePlayer(challenger);

        // Create message to show result
        String message;

        if (challengeSuccessful) {
            // Challenge succeeded - determine actual card ranks
            Card[] playedCards = lastPlay.getCards();
            String actualRanks = getCardRanksText(playedCards);

            message = "Challenge successful! " + lastPlayer.getName() +
                    " takes all cards.\nThe cards were " + actualRanks +
                    " but declared as " + lastPlay.getDeclaredRank() + ".";
        } else {
            // Challenge failed
            message = "Challenge failed! " + challenger.getName() +
                    " takes all cards.\nThe cards were indeed " +
                    lastPlay.getDeclaredRank() + "'s.";
        }

        // Show the challenge result
        JOptionPane.showMessageDialog(this, message);

        // Update UI
        updatePlayArea();

        // Update round number
        int roundNumber = gameManager.getRoundNumber();
        updateRound(roundNumber);

        // Update player info
        Player currentPlayer = gameManager.getCurrentPlayer();
        String newPlayerName = currentPlayer.getName();
        int cardCount = currentPlayer.getCardCount();
        updatePlayerInfo(newPlayerName, cardCount);

        // Automatically handle turn change
        autoHandleTurnChange(challengerName, newPlayerName);

        // Check for winner
        Player winner = gameManager.checkForWinner();
        if (winner != null) {
            String winMessage = winner.getName() + " wins the game!";
            JOptionPane.showMessageDialog(this, winMessage);
            // Handle game end
        }
    }

    /**
     * Get text showing all card ranks
     */
    private String getCardRanksText(Card[] cards) {
        // Build string of card ranks
        String result = "";

        for (int i = 0; i < cards.length; i++) {
            // Add separator if not first card
            if (i > 0) {
                result = result + ", ";
            }

            // Add this card's rank
            result = result + cards[i].getRank();
        }

        return result;
    }

    /**
     * Update play area display
     */
    private void updatePlayArea() {
        // Clear existing components
        playAreaPanel.removeAll();

        // Get the last play
        Play lastPlay = gameManager.getPile().getLastPlay();

        if (lastPlay != null) {
            // Add a label to show the declared rank
            JLabel declaredRankLabel = new JLabel("Declared: " + lastPlay.getDeclaredRank());
            declaredRankLabel.setForeground(Color.WHITE);
            declaredRankLabel.setFont(new Font("Arial", Font.BOLD, 16));
            playAreaPanel.add(declaredRankLabel);

            // Add a separator
            JSeparator separator = new JSeparator(SwingConstants.HORIZONTAL);
            int separatorWidth = playAreaPanel.getWidth() - 20;
            separator.setPreferredSize(new Dimension(separatorWidth, 2));
            playAreaPanel.add(separator);

            // Add each card
            Card[] cards = lastPlay.getCards();
            for (int i = 0; i < cards.length; i++) {
                Card card = cards[i];

                // Get image for this card's suit
                CardImageManager imageManager = CardImageManager.getInstance();
                ImageIcon icon = imageManager.getSuitImage(card.getSuit());

                // Create a smaller version for the play area
                Image originalImage = icon.getImage();
                Image scaledImage = originalImage.getScaledInstance(40, 60, Image.SCALE_SMOOTH);
                ImageIcon scaledIcon = new ImageIcon(scaledImage);

                // Create label for this card
                JLabel cardLabel = new JLabel(scaledIcon);
                String tooltipText = card.getRank() + " of " + card.getSuit();
                cardLabel.setToolTipText(tooltipText);
                playAreaPanel.add(cardLabel);
            }
        } else {
            // No cards played yet
            JLabel emptyLabel = new JLabel("No cards played yet");
            emptyLabel.setForeground(Color.WHITE);
            emptyLabel.setFont(new Font("Arial", Font.BOLD, 16));
            playAreaPanel.add(emptyLabel);
        }

        // Refresh the panel
        playAreaPanel.revalidate();
        playAreaPanel.repaint();
    }

    /**
     * Update player's hand display
     */
    private void updatePlayerHand() {
        // Get the hand scroll pane from the bottom panel
        JPanel bottomPanel = (JPanel) getContentPane().getComponent(4); // Bottom panel is at index 4
        JScrollPane handScrollPane = (JScrollPane) bottomPanel.getComponent(0); // Hand is the first component

        // Get current player
        Player currentPlayer = gameManager.getCurrentPlayer();
        CardImageManager imageManager = CardImageManager.getInstance();

        // Create a new hand panel with player's cards
        Card[] playerCards = currentPlayer.getHand();
        ImageIcon[] suitImages = imageManager.getAllSuitImages();
        handPanel = new HandPanel(playerCards, suitImages);

        // Update the scroll pane
        handScrollPane.setViewportView(handPanel);

        // Refresh the UI
        handScrollPane.revalidate();
        handScrollPane.repaint();

        System.out.println("Player hand UI refreshed");
    }

    /**
     * Create a placeholder panel with text
     * @param text The text to display
     * @return A styled panel
     */
    private JPanel createPlaceholderPanel(String text) {
        // Create panel
        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(250, 100));
        panel.setBackground(new Color(55, 66, 88));

        // Create and add label
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 20));
        label.setForeground(Color.WHITE);
        panel.add(label);

        return panel;
    }

    /**
     * Update player information in the UI
     * @param playerName Current player's name
     * @param cardCount Number of cards in player's hand
     */
    public void updatePlayerInfo(String playerName, int cardCount) {
        infoPanel.setPlayerName(playerName);
        infoPanel.setCardCount(cardCount);
    }

    /**
     * Update round information in the UI
     * @param round Current round number
     */
    public void updateRound(int round) {
        infoPanel.setRound(round);
    }

    /**
     * Listener for Challenge button
     */
    private class ChallengeButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            handleChallenge();
        }
    }

    /**
     * Listener for Play button
     */
    private class PlayButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            handlePlayCards();
        }
    }

    /**
     * Listener for New Game button
     */
    private class NewGameButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            // Show confirmation dialog
            String message = "Are you sure you want to start a new game?";
            String title = "New Game";
            int option = JOptionPane.showConfirmDialog(
                    GameFrame.this,
                    message,
                    title,
                    JOptionPane.YES_NO_OPTION
            );

            // If user confirms, start new game
            if (option == JOptionPane.YES_OPTION) {
                // Close this window
                dispose();

                // Show home screen
                new Home();
            }
        }
    }
}