package com.flume2d.graphics;

import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.utils.Disposable;

public class Tilemap implements Graphic, Disposable
{
	
	public Tilemap(String filename, int tileWidth, int tileHeight, int columns, int rows)
	{
		this(filename, tileWidth, tileHeight, columns, rows, 0, 0);
	}
	
	public Tilemap(String filename, int tileWidth, int tileHeight, int columns, int rows, int spacing, int margin)
	{
		if (filename != null)
		{
			tileset = new Texture(filename);
		}
		this.tileWidth = tileWidth;
		this.tileHeight = tileHeight;
		this.columns = columns;
		this.rows = rows;
		this.width = columns * tileWidth;
		this.height = rows * tileHeight;
		this.spacing = spacing;
		this.margin = margin;
		
		tiles = new int[columns][rows];
		buffer = new FrameBuffer(Pixmap.Format.RGBA4444, pow2(width), pow2(height), false);
		dirty = false;
	}
	
	private int pow2(int val)
	{
		int pow = 2;
		while (pow < val)
			pow = pow * 2;
		return pow;
	}
	
	@Override
	public void dispose()
	{
		buffer.dispose();
		tileset.dispose();
	}
	
	@Override
	public void render(SpriteBatch spriteBatch)
	{
		if (tileset == null) return;
		
		if (dirty)
		{
			drawMap(spriteBatch);
			dirty = false;
		}
		
		int x = 0, y = 0;
		spriteBatch.draw(buffer.getColorBufferTexture(), x, y);
	}
	
	public void drawMap(SpriteBatch spriteBatch)
	{
		int tx, ty, txp;
		int tile, tileX, tileY;
		TextureRegion region = new TextureRegion(tileset);
		int tileCols = tileset.getWidth() / tileWidth;
		
		// clear the sprite batch before rendering to a buffer
		spriteBatch.flush();
		buffer.begin();
		for (tx = 0; tx < columns; tx++)
		{
			txp = tx * tileWidth;
			for (ty = 0; ty < rows; ty++)
			{
				tile = tiles[tx][ty];
				if (tile == -1) continue; // skip empty tiles
				
				tileX = tile % tileCols;
				tileY = (int) Math.floor(tile / tileCols);
				
				region.setRegion(
						tileX * tileWidth  + spacing * tileX + margin,
						tileY * tileHeight + spacing * tileY + margin,
						tileWidth, tileHeight);
				region.flip(false, true);
				spriteBatch.draw(region, txp, ty * tileHeight);
			}
		}
		// flush the sprite batch onto the buffer
		spriteBatch.flush();
		buffer.end();
	}

	public void setTile(int x, int y, int tile)
	{
		if (x >= tiles.length || y >= tiles[0].length) return;
		
		tiles[x][y] = tile;
		dirty = true;
	}
	
	public int getTile(int x, int y)
	{
		if (x >= tiles.length || y >= tiles[0].length) return -1;
		
		return tiles[x][y];
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
	
	private int spacing;
	private int margin;
	
	private int[][] tiles;
	private Texture tileset;
	private FrameBuffer buffer;
	private boolean dirty;

}
