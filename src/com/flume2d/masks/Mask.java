package com.flume2d.masks;

import com.flume2d.math.Vector2;

public interface Mask
{
	
	public abstract Vector2 collide(Mask mask);
	
}
