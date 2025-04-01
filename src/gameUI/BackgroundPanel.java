package gameUI;

import javax.swing.*;
import java.awt.*;

public class BackgroundPanel extends JPanel {
	private Image backgroundImage;

	public BackgroundPanel() {
		// Load image
		backgroundImage = new ImageIcon(getClass().getResource("/icons/title.png")).getImage();
		setLayout(new BorderLayout());
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		// Draw image
		g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
	}
}