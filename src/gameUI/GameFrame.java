package gameUI;

import gameUI.components.*;
import model.Card;
import model.Manager;
import model.Play;
import model.Player;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class GameFrame extends JFrame {

    private InfoPanel infoPanel;         // Information panel
    private HandPanel handPanel;         // Hand panel
    private JPanel playAreaPanel;        // Play area
    private Manager gameManager;         // Game manager
    private RankSelector rankSelector;   // Rank selector

    public GameFrame(Manager manager) {
        // Store game manager reference
        this.gameManager = manager;

        // Set window properties
        setTitle("SuperMadiao!");
        setSize(1152, 864);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Create main panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(34, 45, 65));

        // Info Panel (Top)
        infoPanel = new InfoPanel();
        mainPanel.add(infoPanel, BorderLayout.NORTH);

        // Play Area (Center)
        playAreaPanel = new JPanel(new FlowLayout());
        playAreaPanel.setPreferredSize(new Dimension(400, 200));
        playAreaPanel.setBackground(new Color(55, 66, 88));
        mainPanel.add(playAreaPanel, BorderLayout.CENTER);

        // Deck (Right)
        JPanel deckPanel = createPlaceholderPanel("Deck");
        mainPanel.add(deckPanel, BorderLayout.EAST);

        // Declaration Area (Left)
        rankSelector = new RankSelector();
        JPanel declarationPanel = new JPanel(new BorderLayout());
        declarationPanel.setBackground(new Color(55, 66, 88));
        declarationPanel.add(new JLabel("Declaration Area", SwingConstants.CENTER) {{
            setFont(new Font("Arial", Font.BOLD, 20));
            setForeground(Color.WHITE);
        }}, BorderLayout.NORTH);
        declarationPanel.add(rankSelector, BorderLayout.CENTER);
        mainPanel.add(declarationPanel, BorderLayout.WEST);

        // Bottom Panel (Hand and buttons)
        JPanel bottomPanel = new JPanel(new BorderLayout());

        // Player Hand (Top of bottom panel)
        Player currentPlayer = gameManager.getCurrentPlayer();
        CardImageManager imageManager = CardImageManager.getInstance();
        handPanel = new HandPanel(currentPlayer.getHand(), imageManager.getAllSuitImages());
        JScrollPane handScrollPane = new JScrollPane(handPanel);
        handScrollPane.setPreferredSize(new Dimension(400, 120));
        handScrollPane.setOpaque(false);
        handScrollPane.getViewport().setOpaque(false);
        handScrollPane.setBorder(null);
        bottomPanel.add(handScrollPane, BorderLayout.NORTH);

        // Buttons Panel (Bottom of bottom panel)
        JPanel buttonPanel = new JPanel(new FlowLayout());

        JButton challengeBtn = new JButton("Challenge");
        challengeBtn.setFont(new Font("Arial", Font.BOLD, 18));
        challengeBtn.addActionListener(e -> handleChallenge());
        buttonPanel.add(challengeBtn);

        JButton playBtn = new JButton("Play Selected Cards");
        playBtn.setFont(new Font("Arial", Font.BOLD, 18));
        playBtn.addActionListener(e -> handlePlayCards());
        buttonPanel.add(playBtn);

        JButton newGameBtn = new JButton("New Game");
        newGameBtn.setFont(new Font("Arial", Font.BOLD, 18));
        newGameBtn.addActionListener(e -> {
            int option = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to start a new game?",
                    "New Game", JOptionPane.YES_NO_OPTION);
            if (option == JOptionPane.YES_OPTION) {
                dispose();
                new Home();
            }
        });
        buttonPanel.add(newGameBtn);

        bottomPanel.add(buttonPanel, BorderLayout.SOUTH);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        setContentPane(mainPanel);
        setVisible(true);
    }

    // Handle card play
    private void handlePlayCards() {
        List<CardComponent> selectedComponents = handPanel.getSelectedCards();
        if (selectedComponents.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select at least one card to play.");
            return;
        }

        // Get the selected cards from the hand panel
        List<Card> selectedCards = handPanel.getSelectedModelCards();
        if (selectedCards.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Error retrieving card data.");
            return;
        }

        // Get current player
        Player currentPlayer = gameManager.getCurrentPlayer();
        String currentPlayerName = currentPlayer.getName();

        // Add debug logging
        System.out.println("Selected cards: " + selectedCards.size());
        System.out.println("Player hand: " + currentPlayer.getHand().size());

        // Verify mappings consistency
        boolean mappingsValid = handPanel.verifyCardMappings(currentPlayer.getHand());
        if (!mappingsValid) {
            JOptionPane.showMessageDialog(this, "UI and game data out of sync. Refreshing display.");
            updatePlayerHand(); // Refresh hand display
            return;
        }

        // Get declared rank from rank selector
        Card.Rank declaredRank = rankSelector.getSelectedRank();

        try {
            // Play the cards
            gameManager.playCards(gameManager.getCurrentPlayer(), selectedCards, declaredRank);

            // Update play area display
            updatePlayArea();

            // Remove cards from hand
            handPanel.removeCards(selectedComponents);

            // Update player info (turns have changed)
            Player newCurrentPlayer = gameManager.getCurrentPlayer();
            String newPlayerName = newCurrentPlayer.getName();
            updatePlayerInfo(newPlayerName, newCurrentPlayer.getHand().size());

            // Automatically handle turn change
            autoHandleTurnChange(currentPlayerName, newPlayerName);

            // Check for winner
            Player winner = gameManager.checkForWinner();
            if (winner != null) {
                JOptionPane.showMessageDialog(this, winner.getName() + " wins the game!");
                // Handle game end
            }
        } catch (IllegalArgumentException | IllegalStateException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
            // Refresh UI after error
            updatePlayerHand();
        }
    }

    // Automatically handle turn change with overlay notification
    private void autoHandleTurnChange(String currentPlayerName, String nextPlayerName) {
        // Create a semi-transparent overlay panel for the turn notification
        JPanel overlayPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Make the background semi-transparent
                g.setColor(new Color(0, 0, 0, 180));
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };

        overlayPanel.setLayout(new GridBagLayout());

        // Create a panel with the turn message
        JPanel messagePanel = new JPanel(new BorderLayout());
        messagePanel.setBackground(new Color(59, 89, 152));
        messagePanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        // Add title
        JLabel titleLabel = new JLabel("Turn Round!", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        messagePanel.add(titleLabel, BorderLayout.NORTH);

        // Add message
        JLabel messageLabel = new JLabel(currentPlayerName + " has played. It's " + nextPlayerName + "'s turn now!",
                SwingConstants.CENTER);
        messageLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        messageLabel.setForeground(Color.WHITE);
        messageLabel.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        messagePanel.add(messageLabel, BorderLayout.CENTER);

        overlayPanel.add(messagePanel);

        // Add the overlay to the glass pane of the frame
        setGlassPane(overlayPanel);
        overlayPanel.setVisible(true);

        // Create a timer to remove the overlay and update the display after a delay
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                // Need to use SwingUtilities.invokeLater to modify UI from non-EDT thread
                SwingUtilities.invokeLater(() -> {
                    // Hide the overlay
                    overlayPanel.setVisible(false);

                    // Update the hand display for the new player
                    updatePlayerHand();
                });
            }
        }, 2000); // 2 second delay
    }

    // Handle challenge
    private void handleChallenge() {
        Play lastPlay = gameManager.getPile().getLastPlay();
        if (lastPlay == null) {
            JOptionPane.showMessageDialog(this, "No cards to challenge!");
            return;
        }

        Player challenger = gameManager.getCurrentPlayer();
        String challengerName = challenger.getName();
        Player lastPlayer = lastPlay.getPlayer();

        boolean challengeSuccessful = gameManager.challengePlayer(challenger);

        StringBuilder message = new StringBuilder();
        if (challengeSuccessful) {
            // Determine the actual ranks of the cards
            StringBuilder actualRanks = new StringBuilder();
            List<Card> playedCards = lastPlay.getCards();
            for (int i = 0; i < playedCards.size(); i++) {
                if (i > 0) {
                    actualRanks.append(", ");
                }
                actualRanks.append(playedCards.get(i).getRank());
            }

            message.append("Challenge successful! ").append(lastPlayer.getName())
                    .append(" takes all cards.\nThe cards were ").append(actualRanks)
                    .append(" but declared as ").append(lastPlay.getDeclaredRank()).append(".");
        } else {
            message.append("Challenge failed! ").append(challenger.getName())
                    .append(" takes all cards.\nThe cards were indeed ")
                    .append(lastPlay.getDeclaredRank()).append("'s.");
        }

        // Show the challenge result
        JOptionPane.showMessageDialog(this, message.toString());

        // Update UI
        updatePlayArea();

        // Update round number
        updateRound(gameManager.getRoundNumber());

        // Update player info
        Player currentPlayer = gameManager.getCurrentPlayer();
        String newPlayerName = currentPlayer.getName();
        updatePlayerInfo(newPlayerName, currentPlayer.getHand().size());

        // Automatically handle turn change
        autoHandleTurnChange(challengerName, newPlayerName);

        // Check for winner
        Player winner = gameManager.checkForWinner();
        if (winner != null) {
            JOptionPane.showMessageDialog(this, winner.getName() + " wins the game!");
            // Handle game end
        }
    }

    // Update play area display
    private void updatePlayArea() {
        playAreaPanel.removeAll();

        Play lastPlay = gameManager.getPile().getLastPlay();
        if (lastPlay != null) {
            // Add a label to show the declared rank
            JLabel declaredRankLabel = new JLabel("Declared: " + lastPlay.getDeclaredRank());
            declaredRankLabel.setForeground(Color.WHITE);
            declaredRankLabel.setFont(new Font("Arial", Font.BOLD, 16));
            playAreaPanel.add(declaredRankLabel);

            // Add a separator
            JSeparator separator = new JSeparator(SwingConstants.HORIZONTAL);
            separator.setPreferredSize(new Dimension(playAreaPanel.getWidth() - 20, 2));
            playAreaPanel.add(separator);

            // Add cards
            for (Card card : lastPlay.getCards()) {
                CardImageManager imageManager = CardImageManager.getInstance();
                ImageIcon icon = imageManager.getSuitImage(card.getSuit());

                // Create a smaller version for the play area
                Image scaledImage = icon.getImage().getScaledInstance(40, 60, Image.SCALE_SMOOTH);
                ImageIcon scaledIcon = new ImageIcon(scaledImage);

                JLabel cardLabel = new JLabel(scaledIcon);
                cardLabel.setToolTipText(card.getRank() + " of " + card.getSuit());
                playAreaPanel.add(cardLabel);
            }
        } else {
            JLabel emptyLabel = new JLabel("No cards played yet");
            emptyLabel.setForeground(Color.WHITE);
            emptyLabel.setFont(new Font("Arial", Font.BOLD, 16));
            playAreaPanel.add(emptyLabel);
        }

        playAreaPanel.revalidate();
        playAreaPanel.repaint();
    }

    // Update player's hand display
    private void updatePlayerHand() {
        // Get the hand scroll pane from the bottom panel
        JPanel bottomPanel = (JPanel) getContentPane().getComponent(4); // Bottom panel is at index 4 in BorderLayout
        JScrollPane handScrollPane = (JScrollPane) bottomPanel.getComponent(0); // Hand is the first component

        // Get current player
        Player currentPlayer = gameManager.getCurrentPlayer();
        CardImageManager imageManager = CardImageManager.getInstance();

        // Create a new hand panel
        handPanel = new HandPanel(currentPlayer.getHand(), imageManager.getAllSuitImages());

        // Update the scroll pane
        handScrollPane.setViewportView(handPanel);

        // Refresh the UI
        handScrollPane.revalidate();
        handScrollPane.repaint();

        System.out.println("Player hand UI refreshed");
    }

    private JPanel createPlaceholderPanel(String text) {
        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(250, 100));
        panel.setBackground(new Color(55, 66, 88));
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 20));
        label.setForeground(Color.WHITE);
        panel.add(label);
        return panel;
    }

    // Update player information in the UI
    public void updatePlayerInfo(String playerName, int cardCount) {
        infoPanel.setPlayerName(playerName);
        infoPanel.setCardCount(cardCount);
    }

    // Update round information in the UI
    public void updateRound(int round) {
        infoPanel.setRound(round);
    }
}