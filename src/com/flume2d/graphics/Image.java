package com.flume2d.graphics;

import com.badlogic.gdx.graphics.Texture;
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
		image = new Texture(filename);
		// set default frame width to image width
		frameWidth = imageWidth = image.getWidth();
		frameHeight = imageHeight = image.getHeight();
		frameX = frameY = 0;
		scale = 1;
	}

	@Override
	public void render(int x, int y)
	{
		int sfw = (int) (frameWidth * scale);
		int sfh = (int) (frameHeight * scale);
		/*
		if (angle == 0)
		{
			// untransformed (much faster)
			int hw = sfw / 2;
			int hh = sfh / 2;
			g.drawImage(image, x - hw, y - hh, x + hw, y + hh,
				frameX, frameY, frameX + frameWidth, frameY + frameHeight,
				this);
		}
		else
		{
			diagonal = (int) Math.sqrt(sfw * sfw + sfh * sfh);
			if (transformedImage == null || diagonal != transformedImage.getWidth())
			{
				transformedImage = new BufferedImage(diagonal, diagonal, BufferedImage.TYPE_INT_ARGB);
				Graphics2D g2d = (Graphics2D) transformedImage.getGraphics();
				g2d.drawImage(image, (diagonal - sfw) / 2, (diagonal - sfh) / 2, null);
			}
			
			int half = diagonal / 2;
			AffineTransform at = new AffineTransform();
			at.scale(scale, scale);
			at.rotate(Math.toRadians(angle), half, half);
			BufferedImageOp bio = new AffineTransformOp(at, AffineTransformOp.TYPE_BICUBIC);
			
			g.drawImage(bio.filter(transformedImage, null), x - half, y - half, x + half, y + half,
				frameX, frameY, frameX + diagonal, frameY + diagonal,
				this);
		}
		*/
	}

	@Override public void update() { }
	@Override public boolean isActive() { return false; }
	@Override public boolean isVisible() { return true; }
	
	private Texture image;
	private int diagonal;

}
