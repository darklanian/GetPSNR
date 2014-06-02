package com.lanian.getpsnr;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;

public class ImageLoadPanel extends JPanel implements ActionListener {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5123278364877617358L;
	private static final String ACTION_CAPTURE = "action_capture";
	private static final String ACTION_OPEN = "action_open";
	private static final String ACTION_TOGGLE_MODE = "action_toggle_mode";

	File imageFile;
	ImagePanel imagePanel;
	
	public ImageLoadPanel() {
		setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		
		JPanel panel = new JPanel();
		panel.setBorder(BorderFactory.createLineBorder(Color.RED));
		panel.setLayout(new BoxLayout(panel, BoxLayout.LINE_AXIS));
		panel.setAlignmentX(RIGHT_ALIGNMENT);
		//panel.add(createButton(ACTION_CAPTURE, "Capture", null));
		panel.add(createButton(ACTION_OPEN, "Open", null));
		panel.add(createButton(ACTION_TOGGLE_MODE, "<>", null));
		add(panel);
		
		imagePanel = new ImagePanel();
		imagePanel.setAlignmentX(RIGHT_ALIGNMENT);
		add(imagePanel);
	}
	
	private JButton createButton(String actionCommand, String title, Icon icon) {
		JButton button = new JButton(title, icon);
		button.setActionCommand(actionCommand);
		button.addActionListener(this);
		return button;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals(ACTION_CAPTURE)) {
			
		} else if (e.getActionCommand().equals(ACTION_OPEN)) {
			openImageFile();
		} else if (e.getActionCommand().equals(ACTION_TOGGLE_MODE)) {
			imagePanel.toggleMode();
		}
	}

	private void openImageFile() {
		JFileChooser fc = new JFileChooser();
		
		if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
			imageFile = fc.getSelectedFile();
			imagePanel.loadImage(imageFile);
		}
	}
	
	public File getImageFile() {
		return imageFile;
	}
}
