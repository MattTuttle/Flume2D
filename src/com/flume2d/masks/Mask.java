package com.flume2d.masks;

import com.flume2d.math.Vector2;

public interface Mask
{
	
	public Vector2 collide(Mask mask);
	public void setPosition(float x, float y);
	
}
