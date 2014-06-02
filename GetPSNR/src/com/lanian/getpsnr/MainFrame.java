package com.lanian.getpsnr;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class MainFrame extends JFrame implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5817517276574271532L;

	private static final String ACTION_COMPARE = "action_compare";
	
	private ImageLoadPanel originalImagePanel;
	private ImageLoadPanel targetImagePanel;
	private JLabel infoLabel;
	BufferedImage orig, targ;
	
	public MainFrame() {
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(600, 500);
		setTitle("PSNR");
		getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.PAGE_AXIS));
		
		initImagesPanel();
		
		initInfoPanel();
	}

	private void initInfoPanel() {
		JPanel infoPanel = new JPanel();
		infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.LINE_AXIS));
		infoPanel.setBorder(BorderFactory.createLineBorder(Color.yellow));
		infoPanel.setAlignmentX(RIGHT_ALIGNMENT);
		
		infoLabel = new JLabel("");
		infoPanel.add(infoLabel);
		
		JButton button = new JButton("Compare");
		button.setActionCommand(ACTION_COMPARE);
		button.addActionListener(this);
		infoPanel.add(button);
		
		getContentPane().add(infoPanel);
	}

	private void initImagesPanel() {
		JPanel imagesPanel = new JPanel();
		imagesPanel.setLayout(new BoxLayout(imagesPanel, BoxLayout.LINE_AXIS));
		imagesPanel.setAlignmentX(RIGHT_ALIGNMENT);
		
		originalImagePanel = new ImageLoadPanel();
		targetImagePanel = new ImageLoadPanel();
		originalImagePanel.imagePanel.setImageMoveListener(targetImagePanel.imagePanel);
		targetImagePanel.imagePanel.setImageMoveListener(originalImagePanel.imagePanel);
		imagesPanel.add(originalImagePanel);
		imagesPanel.add(targetImagePanel);
		
		getContentPane().add(imagesPanel);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals(ACTION_COMPARE)) {
			getPSNR();
		}
	}

	private double pow(int n) {
		return n*n;
	}
	private void getPSNR() {
		
		if (!checkImages())
			return;
		
		double mse = getMSE();
		
		if (mse == 0) {
			infoLabel.setText("images are identical");
			return;
		}
		
		double psnr = 20*Math.log10(255)-10*Math.log10(mse);
		infoLabel.setText(String.format("PSNR = %.02fdB", psnr));
	}

	private double getMSE() {
		double mse = 0;
		
		for (int y = 0; y < orig.getHeight(); ++y) {
			for (int x = 0; x < orig.getWidth(); ++x) {
				Color o = new Color(orig.getRGB(x, y));
				Color t = new Color(targ.getRGB(x, y));
				
				mse += pow(o.getRed() - t.getRed());
				mse += pow(o.getGreen() - t.getGreen());
				mse += pow(o.getBlue() - t.getBlue());
			}
		}
		
		mse /= orig.getWidth()*orig.getHeight()*3;
		return mse;
	}

	private boolean checkImages() {
		File originalImageFile, targetImageFile;
		originalImageFile = originalImagePanel.getImageFile();
		if (originalImageFile == null) {
			infoLabel.setText("original image is not loaded");
			return false;
		}
		targetImageFile = targetImagePanel.getImageFile();
		if (targetImageFile == null) {
			infoLabel.setText("target image is not loaded");
			return false;
		}
		
		try {
			orig = ImageIO.read(originalImageFile);
			targ = ImageIO.read(targetImageFile);
		} catch (IOException e) {
			e.printStackTrace();
			infoLabel.setText("image loading failed");
			return false;
		}
		
		if (orig.getWidth() != targ.getWidth() || orig.getHeight() != targ.getHeight()) {
			infoLabel.setText("image size is different");
			return false;
		}
		return true;
	}
}
