package main;

import gameUI.AudioManager;
import gameUI.GameFrame;
import gameUI.Home;
import model.Manager;
import model.Player;

import java.util.ArrayList;
import java.util.List;

public class Game {
    private Manager gameManager;
    private GameFrame gameFrame;
    private List<Player> players;

    public Game(List<String> playerNames) {
        // Initialize players
        players = new ArrayList<>();
        for (String name : playerNames) {
            players.add(new Player(name));
        }

        // Create game manager
        gameManager = new Manager(players);

        // Create game interface and pass manager
        gameFrame = new GameFrame(gameManager);

        // Continue playing background music, start if not already playing
        try {
            AudioManager.getInstance().playBackgroundMusic("/audio/background_music.wav");
            AudioManager.getInstance().setVolume(0.05f); // Set volume to 5%
        } catch (Exception e) {
            System.err.println("Failed to play background music: " + e.getMessage());
            e.printStackTrace();
        }

        // Update UI display
        updateUI();
    }

    // Update UI display
    private void updateUI() {
        // Set current player information
        Player currentPlayer = gameManager.getCurrentPlayer();
        gameFrame.updatePlayerInfo(currentPlayer.getName(), currentPlayer.getHand().size());

        // Set current round information
        gameFrame.updateRound(gameManager.getRoundNumber());
    }

    // Start a new game with specified player names
    public static void startNewGame(List<String> playerNames) {
        new Game(playerNames);
    }

    public static void main(String[] args) {
        // Show home page
        new Home();
    }
}