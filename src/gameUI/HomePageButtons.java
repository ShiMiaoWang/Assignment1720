package gameUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Custom button for the home page
 */
public class HomePageButtons extends JButton {

	/**
	 * Create a new home page button
	 * @param iconPath Path to the button image
	 * @param width Button width
	 * @param height Button height
	 * @param onClick Action to perform when clicked
	 */
	public HomePageButtons(String iconPath, int width, int height, ActionListener onClick) {
		// Load the button image
		ImageIcon icon = new ImageIcon(getClass().getResource(iconPath));

		// Get the original image
		Image originalImage = icon.getImage();

		// Create a scaled version with the specified dimensions
		Image scaledImage = originalImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);

		// Create a new icon with the scaled image
		ImageIcon scaledIcon = new ImageIcon(scaledImage);

		// Set the icon for this button
		setIcon(scaledIcon);

		// Set button appearance properties
		setBorderPainted(true);
		setContentAreaFilled(true);
		setFocusPainted(true);

		// Set button size
		Dimension size = new Dimension(width, height);
		setPreferredSize(size);

		// Create button click handler
		ButtonClickHandler clickHandler = new ButtonClickHandler(onClick);

		// Add the click handler
		addActionListener(clickHandler);
	}

	/**
	 * Helper class to handle button clicks
	 */
	private class ButtonClickHandler implements ActionListener {
		// The action listener to call when button is clicked
		private ActionListener targetListener;

		/**
		 * Create a new click handler
		 * @param listener The action listener to call
		 */
		public ButtonClickHandler(ActionListener listener) {
			this.targetListener = listener;
		}

		/**
		 * Called when button is clicked
		 */
		public void actionPerformed(java.awt.event.ActionEvent e) {
			// Forward the event to the target listener
			targetListener.actionPerformed(e);
		}
	}
}