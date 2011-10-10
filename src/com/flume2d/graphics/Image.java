package com.flume2d.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;

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
		image = new Texture(Gdx.files.internal(filename));
		// set default frame width to image width
		frameWidth = imageWidth = image.getWidth();
		frameHeight = imageHeight = image.getHeight();
		frameX = frameY = 0;
		scale = 1;
	}

	@Override
	public void render(SpriteBatch batch, int x, int y)
	{
		int sfw = (int) (frameWidth * scale);
		int sfh = (int) (frameHeight * scale);
		TextureRegion region = new TextureRegion(image, frameX, frameY, frameWidth, frameHeight);
		batch.draw(region, x, y, -sfw/2, -sfh/2, sfw, sfh, scale, scale, angle);
	}

	@Override public void update() { }
	@Override public boolean isActive() { return false; }
	@Override public boolean isVisible() { return true; }
	
	private Texture image;
	private int diagonal;

}
