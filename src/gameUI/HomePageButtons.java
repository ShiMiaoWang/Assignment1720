package gameUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class HomePageButtons extends JButton {

	public HomePageButtons(String iconPath, int width, int height, ActionListener onClick) {
		ImageIcon icon = new ImageIcon(getClass().getResource(iconPath));
		Image scaledImage = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
		setIcon(new ImageIcon(scaledImage));

		setBorderPainted(true);
		setContentAreaFilled(true);
		setFocusPainted(true);
		setPreferredSize(new Dimension(width, height));

		addActionListener(onClick);
	}
}