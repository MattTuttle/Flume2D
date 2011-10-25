package com.flume2d.masks;

import com.flume2d.math.*;

public class Grid extends Mask
{
	
	protected AABB box;
	protected boolean[][] grid;
	
	public Grid(int width, int height, int tileWidth, int tileHeight)
	{
		box = new AABB(tileWidth, tileHeight);
		grid = new boolean[width][height];
	}
	
	public boolean validTile(int column, int row)
	{
		return (column < grid.length && row < grid[0].length);
	}
	
	public void setTile(int column, int row)
	{
		if (validTile(column, row))
			grid[column][row] = true;
	}
	
	public void clearTile(int column, int row)
	{
		if (validTile(column, row))
			grid[column][row] = false;
	}
	
	public boolean getTile(int column, int row)
	{
		if (validTile(column, row))
			return grid[column][row];
		return false;
	}
	
	public void setRect(int column, int row, int width, int height)
	{
		for (int i = 0; i < width; i++)
		{
			for (int j = 0; j < height; j++)
			{
				setTile(column + i, row + height);
			}
		}
	}
	
	public void clearRect(int column, int row, int width, int height)
	{
		for (int i = 0; i < width; i++)
		{
			for (int j = 0; j < height; j++)
			{
				clearTile(column + i, row + height);
			}
		}
	}

	@Override
	public Vector2 collide(Mask mask)
	{
		Rectangle bounds = mask.getBounds();
		
		return null;
	}

	@Override
	public boolean collideAt(int x, int y)
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean overlaps(Mask mask)
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Rectangle getBounds()
	{
		// TODO Auto-generated method stub
		return null;
	}

}
