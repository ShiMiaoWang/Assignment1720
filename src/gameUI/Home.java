package gameUI;

import main.Game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * The home/title screen of the game
 */
public class Home extends JFrame {

	/**
	 * Create the home screen
	 */
	public Home() {
		// Set window properties
		setTitle("Super MaDiao");
		setSize(1152, 864);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Create background panel
		BackgroundPanel background = new BackgroundPanel();
		setContentPane(background);

		// Create button panel with centered layout
		JPanel buttonPanel = new JPanel(new GridBagLayout());
		buttonPanel.setOpaque(false);

		// Configure layout constraints
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(10, 0, 10, 0); // Button spacing

		// Start playing background music
		try {
			AudioManager audioManager = AudioManager.getInstance();
			audioManager.playBackgroundMusic("/audio/background_music.wav");
			audioManager.setVolume(0.05f); // Set volume to 5%
		} catch (Exception e) {
			System.out.println("Failed to play background music: " + e.getMessage());
			e.printStackTrace();
		}

		// Create start button action handler
		ActionListener startAction = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Show player setup dialog
				PlayerSetupDialog setupDialog = new PlayerSetupDialog(Home.this, 2, 4);
				setupDialog.setVisible(true);

				// If user confirmed, start the game
				if (setupDialog.isConfirmed()) {
					// Get player names
					String[] playerNames = setupDialog.getPlayerNames();

					// Close home screen
					Home.this.dispose();

					// Start new game
					startNewGame(playerNames);
				}
			}
		};

		// Create start button with handler
		HomePageButtons startButton = new HomePageButtons("/icons/start.png", 200, 60, startAction);

		// Add start button to panel
		gbc.gridx = 0;
		gbc.gridy = 0;
		buttonPanel.add(startButton, gbc);

		// Create help button action handler
		ActionListener helpAction = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Show help message dialog
				String helpMessage = "Super Madiao Game Rules:\n\n"
						+ "1. Each player takes turns playing cards and declaring points.\n\n"
						+ "2. On your turn, you can:\n"
						+ "   - Play 1-4 cards and declare their points (can be true or false)\n"
						+ "   - Challenge the previous player's declaration\n\n"
						+ "3. If a player is challenged:\n"
						+ "   - If the declaration is false, the challenged player takes all cards from the pile\n"
						+ "   - If the declaration is true, the challenger takes all cards from the pile\n\n"
						+ "4. The player who takes cards goes next\n\n"
						+ "5. The first player to empty their hand wins!";
				JOptionPane.showMessageDialog(Home.this, helpMessage, "Game Help", JOptionPane.INFORMATION_MESSAGE);
			}
		};

		// Create help button with handler
		HomePageButtons helpButton = new HomePageButtons("/icons/help.png", 200, 60, helpAction);

		// Add help button to panel
		gbc.gridy = 1;
		buttonPanel.add(helpButton, gbc);

		// Set layout of the background panel
		background.add(Box.createVerticalStrut(350), BorderLayout.NORTH); // Top spacing
		background.add(buttonPanel, BorderLayout.CENTER);

		// Show the window
		setVisible(true);
	}

	/**
	 * Start a new game with specified player names
	 */
	private void startNewGame(String[] playerNames) {
		// Convert player names to Player objects
		int playerCount = playerNames.length;
		model.Player[] players = new model.Player[playerCount];

		for (int i = 0; i < playerCount; i++) {
			String name = playerNames[i];
			players[i] = new model.Player(name);
		}

		// Create game manager
		model.Manager gameManager = new model.Manager(players);

		// Create game frame
		GameFrame gameFrame = new GameFrame(gameManager);
	}

	/**
	 * Program entry point
	 */
	public static void main(String[] args) {
		// Create and show the home screen
		Home home = new Home();
	}
}