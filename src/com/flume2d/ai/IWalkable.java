package com.flume2d.ai;

public interface IWalkable
{
	
	/**
	 * Determines if a tile is walkable
	 * @param x the x-axis tile to check
	 * @param y the y-axis tile to check
	 * @return whether the tile is walkable or not
	 */
	public boolean isWalkable(int x, int y);
	
}
