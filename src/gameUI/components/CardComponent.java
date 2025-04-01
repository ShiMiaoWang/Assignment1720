package gameUI.components;

import model.Card;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class CardComponent extends JLabel {
	private boolean selected = false;
	private Card modelCard; // Reference to the model Card object

	public CardComponent(ImageIcon icon, Card card) {
		// Store reference to model card
		this.modelCard = card;

		// Resize image
		Image scaled = icon.getImage().getScaledInstance(60, 90, Image.SCALE_SMOOTH);
		setIcon(new ImageIcon(scaled));

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

		// Add click event
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				toggleSelect();
			}
		});
	}

	// Toggle selection state
	private void toggleSelect() {
		selected = !selected;
		if (selected) {
			setBorder(BorderFactory.createLineBorder(Color.YELLOW, 2));
		} else {
			setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
		}
	}

	public boolean isSelected() {
		return selected;
	}

	public ImageIcon getIcon() {
		return (ImageIcon) super.getIcon();
	}

	public Card getModelCard() {
		return modelCard;
	}
}