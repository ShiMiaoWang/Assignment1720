package gameUI;

import main.Game;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class Home extends JFrame {

	public Home() {
		// Set window properties
		setTitle("Super MaDiao");
		setSize(1152, 864);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Create background panel
		BackgroundPanel background = new BackgroundPanel();
		setContentPane(background);

		// Create button panel
		JPanel buttonPanel = new JPanel(new GridBagLayout());
		buttonPanel.setOpaque(false);

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(10, 0, 10, 0);

		HomePageButtons startButton = new HomePageButtons("/icons/start.png", 200, 60, e -> {
			// Show player setup dialog
			PlayerSetupDialog setupDialog = new PlayerSetupDialog(Home.this, 2, 4);
			setupDialog.setVisible(true);

			if (setupDialog.isConfirmed()) {
				List<String> playerNames = setupDialog.getPlayerNames();
				Home.this.dispose();

				// Start the game with the player names
				Game.startNewGame(playerNames);
			}
		});

		// Add start button
		gbc.gridx = 0;
		gbc.gridy = 0;
		buttonPanel.add(startButton, gbc);

		// Create help button
		HomePageButtons helpButton = new HomePageButtons("/icons/help.png", 200, 60, e -> {
			String helpMessage = "Super Madiao Game Rules:\n\n"
					+ "1. Each player takes turns playing cards and declaring card points.\n\n"
					+ "2. On your turn, you can:\n"
					+ "   - Play 1-4 cards and declare their rank (can be truthful or bluffing)\n"
					+ "   - Challenge the previous player's declaration\n\n"
					+ "3. If a player is challenged:\n"
					+ "   - If the declaration was false, the challenged player takes all cards from the pile\n"
					+ "   - If the declaration was true, the challenger takes all cards from the pile\n\n"
					+ "4. The player who takes cards from a challenge goes next\n\n"
					+ "5. The first player to empty their hand wins!";
			JOptionPane.showMessageDialog(Home.this, helpMessage, "Game Help", JOptionPane.INFORMATION_MESSAGE);
		});

		// Add help button
		gbc.gridy = 1;
		buttonPanel.add(helpButton, gbc);

		// Set button layout
		background.add(Box.createVerticalStrut(350), BorderLayout.NORTH);
		background.add(buttonPanel, BorderLayout.CENTER);

		setVisible(true);
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(Home::new);
	}
}