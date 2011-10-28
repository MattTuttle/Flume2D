package com.flume2d.graphics;

import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;

public class Tilemap implements Graphic
{
	
	public Tilemap(String filename, int width, int height, int tileWidth, int tileHeight)
	{
		tileset = new Texture(filename);
		this.tileWidth = tileWidth;
		this.tileHeight = tileHeight;
		
		columns = (int) Math.ceil(width / tileWidth);
		rows = (int) Math.ceil(height / tileHeight);
		this.width = columns * tileWidth - tileWidth;
		this.height = rows * tileHeight - tileHeight;
		
		tiles = new int[columns * rows];
		dirty = false;
	}
	
	@Override
	public void render(SpriteBatch spriteBatch)
	{
		int tileCols = tileset.getWidth() / tileWidth;
		TextureRegion region = new TextureRegion(tileset);
		for (int i = 0; i < columns * rows; i++)
		{
			region.setRegion(
					(tiles[i] % tileCols) * tileWidth,  // x
					(tiles[i] / tileCols) * tileHeight, // y
					tileWidth, tileHeight);
			
			spriteBatch.draw(region,
					(i % columns) * tileWidth,  // x
					height - (i / columns) * tileHeight, // y
					tileWidth, tileHeight);
		}
		if (dirty)
			dirty = false;
	}

	public void setTile(int x, int y, int tile)
	{
		int index = y * columns + x;
		if (index >= tiles.length) return;
		
		tiles[index] = tile;
		dirty = true;
	}
	
	public void setRect(int x, int y, int width, int height, int tile)
	{
		for (int j = y; j < y + height; j++)
		{
			for (int i = x; i < x + width; i++)
			{
				setTile(i, j, tile);
			}
		}
		dirty = true;
	}
	
	public int getWidth() { return width; }
	public int getHeight() { return height; }
	
	@Override public void update() { }
	@Override public boolean isActive() { return false; }
	@Override public boolean isVisible() { return true; }
	
	private int columns;
	private int rows;
	
	private int width;
	private int height;
	private int tileWidth;
	private int tileHeight;
	
	private int[] tiles;
	private Texture tileset;
	private boolean dirty;

}
