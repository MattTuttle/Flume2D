package com.flume2d.masks;

import com.flume2d.Entity;
import com.flume2d.math.*;

public abstract class Mask
{
	
	public Entity parent;
	
	public Mask()
	{
	}
	
	public abstract boolean overlaps(Mask mask);
	
	public abstract Vector2 collide(Mask mask);
	public abstract boolean collideAt(int x, int y);
	
	public abstract Rectangle getBounds();
	
}
