package com.flume2d.graphics;

import java.awt.*;
import java.awt.geom.*;
import java.awt.image.*;

import com.flume2d.*;

public class Image implements Graphic
{
	
	public int frameX;
	public int frameY;
	public int frameWidth;
	public int frameHeight;
	public int imageWidth;
	public int imageHeight;
	
	public float angle;
	public float scale;
	
	public Image(String filename)
	{
		image = Engine.getInstance().getImage(filename);
		// set default frame width to image width
		frameWidth = imageWidth = image.getWidth();
		frameHeight = imageHeight = image.getHeight();
		frameX = frameY = 0;
		scale = 1;
	}

	@Override
	public void render(Graphics g, int x, int y)
	{
		int hw = (int) (frameWidth / 2 * scale);
		int hh = (int) (frameHeight / 2 * scale);
		if (angle == 0 && scale == 1)
		{
			g.drawImage(image, x - hw, y - hh, x + hw, y + hh,
				frameX, frameY, frameX + frameWidth, frameY + frameHeight,
				null);
		}
		else
		{
			Graphics2D g2d = (Graphics2D) g;
			AffineTransform origTX = g2d.getTransform();
			AffineTransform newTX = (AffineTransform)(origTX.clone());
			
			newTX.rotate(Math.toRadians(angle), x, y);
			newTX.scale(scale, scale);
			g2d.setTransform(newTX);
			g2d.drawImage(image, x - hw, y - hh, x + hw, y + hh,
				frameX, frameY, frameX + frameWidth, frameY + frameHeight,
				null);
			// reset the transform
			g2d.setTransform(origTX);
		}
	}

	@Override public void update() { }
	@Override public boolean isActive() { return false; }
	@Override public boolean isVisible() { return true; }
	
	private BufferedImage image;

}
