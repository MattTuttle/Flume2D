package com.flume2d.graphics;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class TiledSpritemap extends Spritemap
{
	
	private int columns, rows;

	public TiledSpritemap(String filename, int frameWidth, int frameHeight, int width, int height)
	{
		super(filename, frameWidth, frameHeight);
		columns = width / frameWidth;
		rows = height / frameHeight;
	}
	
	@Override
	public void render(SpriteBatch spriteBatch)
	{
		TextureRegion region = new TextureRegion(image, frameX, frameY, frameWidth, frameHeight);
		
		for (int x = 0; x < columns; x++)
		{
			for (int y = 0; y < rows; y++)
			{
				spriteBatch.draw(region, x * frameWidth, y * frameHeight);
			}
		}
	}

}
