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
		this.width = columns * tileWidth;
		this.height = rows * tileHeight;
		
		tiles = new int[columns * rows];
		for (int i = 0; i < tiles.length; i++)
			tiles[i] = -1;
		dirty = false;
	}
	
	@Override
	public void render(SpriteBatch spriteBatch)
	{
		// TODO: Render this to a buffer texture
		int tile, tileX, tileY;
		int tileCols = tileset.getWidth() / tileWidth;
		TextureRegion region = new TextureRegion(tileset, 0, 0, tileWidth, tileHeight);
		for (int i = 0; i < columns; i++)
		{
			for (int j = 0; j < rows; j++)
			{
				tile = tiles[j * columns + i];
				if (tile == -1) continue;
				tileX = tile % tileCols * tileWidth;
				tileY = (int) Math.floor(tile / tileCols) * tileHeight;
				region.setRegion(tileX, tileY, tileWidth, tileHeight);
				spriteBatch.draw(region,
						i * tileWidth, j * tileHeight,
						tileWidth, tileHeight);
			}
		}
		if (dirty)
			dirty = false;
	}

	public void setTile(int x, int y, int tile)
	{
		int index = y * columns + x;
		if (index > tiles.length) return;
		
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
