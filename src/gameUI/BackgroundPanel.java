package gameUI;

import javax.swing.*;
import java.awt.*;

/**
 * A panel that displays a background image
 */
public class BackgroundPanel extends JPanel {
	// The image to display
	private Image backgroundImage;

	/**
	 * Create a new background panel
	 */
	public BackgroundPanel() {
		// Load the background image
		ImageIcon imageIcon = new ImageIcon(getClass().getResource("/icons/title.png"));
		backgroundImage = imageIcon.getImage();

		// Set layout
		setLayout(new BorderLayout());
	}

	/**
	 * This method is called automatically to paint the panel
	 */
	@Override
	protected void paintComponent(Graphics g) {
		// Call the parent class method
		super.paintComponent(g);

		// Draw the background image to fill the panel
		g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
	}
}