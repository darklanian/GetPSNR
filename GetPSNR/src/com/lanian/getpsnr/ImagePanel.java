package com.lanian.getpsnr;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

public class ImagePanel extends JPanel implements MouseListener, MouseMotionListener, ImageMoveListener {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8897035314852896739L;
	
	Image image;
	boolean fitToSize = false;
	int ix, iy;
	ImageMoveListener imageMoveListener;
	
	public ImagePanel() {
		setBorder(BorderFactory.createLineBorder(Color.GREEN));
		addMouseListener(this);
		addMouseMotionListener(this);
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		Rectangle rect = getBounds();
		int sw = rect.width, sh = rect.height;
		g.fillRect(0, 0, sw, sh);
		if (image != null) {
			int iw = image.getWidth(null), ih = image.getHeight(null);
			if (fitToSize) {
				
				
				double tw = (double)iw*sh/ih;
				double th;
				if (((int)tw) > sw) {
					th = (double)ih*sw/iw;
					tw = sw;
				} else {
					th = sh;
				}
				
				int tx = (sw-(int)tw)/2, ty = (sh-(int)th)/2;
				g.drawImage(image, tx, ty, tx+(int)tw, ty+(int)th, 0, 0, iw, ih, null);
			} else {
				if (ix < 0)
					ix = 0;
				if (ix > iw-sw)
					ix = iw-sw;
				if (iy < 0)
					iy = 0;
				if (iy > ih-sh)
					iy = ih-sh;
				g.drawImage(image, 0, 0, sw, sh, ix, iy, ix+sw, iy+sh, null);
			}
			
		}
	}
	
	public void loadImage(File imageFile) {
		try {
			image = ImageIO.read(imageFile);
			ix = 0;
			iy = 0;
			repaint();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	int mx, my;
	@Override
	public void mousePressed(MouseEvent e) {
		mx = e.getX();
		my = e.getY();
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		int dx = e.getX()-mx;
		int dy = e.getY()-my;
		System.out.println(String.format("mouseDragged: %d, %d", dx, dy));
		ix -= dx;
		iy -= dy;
		repaint();
		if (imageMoveListener != null)
			imageMoveListener.imageMoved(ix, iy);
		
		mx = e.getX();
		my = e.getY();
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		
	}

	@Override
	public void imageMoved(int x, int y) {
		ix = x;
		iy = y;
		repaint();
	}
	
	public void setImageMoveListener(ImageMoveListener listener) {
		imageMoveListener = listener;
	}
	
	public void toggleMode() {
		fitToSize = !fitToSize;
		ix = 0;
		iy = 0;
		repaint();
		if (imageMoveListener != null)
			imageMoveListener.imageModeChanged(fitToSize);
	}

	@Override
	public void imageModeChanged(boolean fitToSize) {
		this.fitToSize = fitToSize;
		ix = 0;
		iy = 0;
		repaint();
	}
}
