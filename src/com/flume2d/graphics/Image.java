package com.flume2d.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;

public class Image implements Graphic
{
	
	public float originX, originY;
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
		originX = originY = frameX = frameY = 0;
		scale = 1;
	}
	
	public void setOrigin(int x, int y)
	{
		originX = x;
		originY = y;
	}

	@Override
	public void render(SpriteBatch spriteBatch)
	{
		TextureRegion region = new TextureRegion(image, frameX, frameY, frameWidth, frameHeight);
		region.flip(false, true);
		spriteBatch.draw(region, 0, 0, originX, originY, frameWidth, frameHeight, scale, scale, angle);
	}
	
	@Override
	public void dispose()
	{
		image.dispose();
	}

	@Override public void update() { }
	@Override public boolean isActive() { return false; }
	@Override public boolean isVisible() { return true; }
	
	protected Texture image;

}
