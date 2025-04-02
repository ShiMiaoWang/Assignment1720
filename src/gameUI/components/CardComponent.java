package gameUI.components;

import model.Card;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * A UI component representing a playing card
 */
public class CardComponent extends JLabel {
	// Whether this card is currently selected
	private boolean selected;

	// The model Card this component represents
	private Card modelCard;

	/**
	 * Create a new card component
	 * @param icon The icon to display for this card
	 * @param card The model Card this component represents
	 */
	public CardComponent(ImageIcon icon, Card card) {
		// Initialize fields
		this.selected = false;
		this.modelCard = card;

		// Resize image to fit component
		Image originalImage = icon.getImage();
		Image scaledImage = originalImage.getScaledInstance(60, 90, Image.SCALE_SMOOTH);
		setIcon(new ImageIcon(scaledImage));

		// Create panel layout
		setLayout(new BorderLayout());

		// Add rank display at the top
		JLabel rankLabel = new JLabel(String.valueOf(card.getRank().getValue()), SwingConstants.CENTER);
		rankLabel.setForeground(Color.WHITE);
		rankLabel.setFont(new Font("Arial", Font.BOLD, 14));
		add(rankLabel, BorderLayout.NORTH);

		// Set border and mouse cursor style
		setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
		setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

		// Add click event handler
		CardClickListener clickListener = new CardClickListener();
		addMouseListener(clickListener);
	}

	/**
	 * Toggle selection state of this card
	 */
	private void toggleSelect() {
		// Flip selection state
		selected = !selected;

		if (selected) {
			// If selected, show yellow border
			setBorder(BorderFactory.createLineBorder(Color.YELLOW, 2));
		} else {
			// If not selected, show light gray border
			setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
		}
	}

	/**
	 * Check if this card is selected
	 */
	public boolean isSelected() {
		return selected;
	}

	/**
	 * Get the icon of this card
	 */
	public ImageIcon getIcon() {
		return (ImageIcon) super.getIcon();
	}

	/**
	 * Get the model Card this component represents
	 */
	public Card getModelCard() {
		return modelCard;
	}

	/**
	 * Mouse listener for card clicks
	 */
	private class CardClickListener implements MouseListener {
		/**
		 * Called when mouse is clicked
		 */
		public void mouseClicked(MouseEvent e) {
			toggleSelect();
		}

		// Other required methods that we don't use
		public void mousePressed(MouseEvent e) { }
		public void mouseReleased(MouseEvent e) { }
		public void mouseEntered(MouseEvent e) { }
		public void mouseExited(MouseEvent e) { }
	}
}