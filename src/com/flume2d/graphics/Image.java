package com.flume2d.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;

public class Image implements Graphic
{
	
	public float x, y;
	public int frameX, frameY;
	public int frameWidth, frameHeight;
	public int imageWidth, imageHeight;
	
	public float angle;
	public float scale;
	
	public Image(String filename)
	{
		image = new Texture(Gdx.files.internal(filename));
		// set default frame width to image width
		frameWidth = imageWidth = image.getWidth();
		frameHeight = imageHeight = image.getHeight();
		x = y = frameX = frameY = 0;
		scale = 1;
	}

	@Override
	public void render(SpriteBatch spriteBatch)
	{
		if (scale == 1 && angle == 0)
		{
			spriteBatch.draw(image, x - frameHeight / 2, y - frameHeight / 2);
		}
		else
		{
			TextureRegion region = new TextureRegion(image, frameX, frameY, frameWidth, frameHeight);
			spriteBatch.draw(region, x, y, -frameHeight / 2, -frameHeight / 2, frameHeight, frameHeight, scale, scale, angle);
		}
	}

	@Override public void update() { }
	@Override public boolean isActive() { return false; }
	@Override public boolean isVisible() { return true; }
	
	private Texture image;

}
