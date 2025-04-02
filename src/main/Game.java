package main;

import gameUI.AudioManager;
import gameUI.GameFrame;
import gameUI.Home;
import model.Manager;
import model.Player;

/**
 * Main game class that connects the model and UI
 */
public class Game {
    private Manager gameManager;
    private GameFrame gameFrame;
    private Player[] players;

    /**
     * Create a new game with the specified players
     * @param playerNames Array of player names
     */
    public Game(String[] playerNames) {
        // Get number of players
        int playerCount = playerNames.length;

        // Initialize players array
        players = new Player[playerCount];

        // Create a Player object for each name
        for (int i = 0; i < playerCount; i++) {
            String name = playerNames[i];
            players[i] = new Player(name);
        }

        // Create game manager with players
        gameManager = new Manager(players);

        // Create game interface and pass manager
        gameFrame = new GameFrame(gameManager);

        // Continue playing background music, start if not already playing
        try {
            AudioManager audioManager = AudioManager.getInstance();
            audioManager.playBackgroundMusic("/audio/background_music.wav");
            audioManager.setVolume(0.05f); // Set volume to 5%
        } catch (Exception e) {
            System.out.println("Failed to play background music: " + e.getMessage());
            e.printStackTrace();
        }

        // Update UI display
        updateUI();
    }

    /**
     * Update UI display with current game state
     */
    private void updateUI() {
        // Get current player
        Player currentPlayer = gameManager.getCurrentPlayer();

        // Update player information
        gameFrame.updatePlayerInfo(
                currentPlayer.getName(),
                currentPlayer.getCardCount()
        );

        // Update round information
        gameFrame.updateRound(gameManager.getRoundNumber());
    }

    /**
     * Start a new game with specified player names
     * @param playerNames Array of player names
     */
    public static void startNewGame(String[] playerNames) {
        new Game(playerNames);
    }

    /**
     * Program entry point
     * @param args Command line arguments (not used)
     */
    public static void main(String[] args) {
        // Show home page
        new Home();
    }
}