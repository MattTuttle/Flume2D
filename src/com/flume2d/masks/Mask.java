package com.flume2d.masks;

import com.flume2d.math.Rectangle;
import com.flume2d.math.Vector2;

public abstract class Mask
{
	
	public float x;
	public float y;
	
	public Mask()
	{
		x = y = 0;
	}
	
	public abstract boolean overlaps(Mask mask);
	
	public abstract Vector2 collide(Mask mask);
	public abstract boolean collideAt(int x, int y);
	
	public abstract Rectangle getBounds();
	
}
