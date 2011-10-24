package com.flume2d.masks;

import com.flume2d.math.Vector2;

public class AABB extends Polygon
{
	
	public int halfWidth;
	public int halfHeight;
	
	public AABB(int width, int height)
	{
		super();
		halfWidth = width / 2;
		halfHeight = height / 2;
		
		vertices = new Vector2[4];
		for (int i = 0; i < 4; i++)
			vertices[i] = new Vector2();
	}
	
	@Override
	public Vector2 collide(Mask mask)
	{
		if (mask instanceof AABB)
		{
			return collideAABB((AABB) mask);
		}
		
		// convert to polygon vertices
		vertices[0].x = x - halfWidth; vertices[0].y = y - halfHeight;
		vertices[1].x = x + halfWidth; vertices[1].y = y - halfHeight;
		vertices[2].x = x + halfWidth; vertices[2].y = y + halfHeight;
		vertices[3].x = x - halfWidth; vertices[3].y = y + halfHeight;
		return super.collide(mask);
	}
	
	// *really* fast AABB collision check
	public Vector2 collideAABB(AABB mask)
	{
		float min1, max1,
			min2, max2,
			offsetx = 0, offsety = 0;
		
		// check x-axis
		if (x < mask.x)
		{
			min1 = x;
			max1 = x + halfWidth;
			min2 = mask.x - mask.halfWidth;
			max2 = mask.x;
			offsetx = min2 - max1;
			// test collision
			if (min1 - max2 > 0 || min2 - max1 > 0)
				return null;
		}
		else if (x > mask.x)
		{
			min1 = x - halfWidth;
			max1 = x;
			min2 = mask.x;
			max2 = mask.x + mask.halfWidth;
			offsetx = max2 - min1;
			// test collision
			if (min1 - max2 > 0 || min2 - max1 > 0)
				return null;
		}
		
		// check y-axis
		if (y < mask.y)
		{
			min1 = y;
			max1 = y + halfHeight;
			min2 = mask.y - mask.halfHeight;
			max2 = mask.y;
			offsety = min2 - max1;
			// test collision
			if (min1 - max2 > 0 || min2 - max1 > 0)
				return null;
		}
		else if (y > mask.y)
		{
			min1 = y - halfHeight;
			max1 = y;
			min2 = mask.y;
			max2 = mask.y + mask.halfHeight;
			offsety = max2 - min1;
			// test collision
			if (min1 - max2 > 0 || min2 - max1 > 0)
				return null;
		}
		
		if (offsetx == 0 || offsety == 0)
		{
			// only one axis, good!
		}
		else
		{
			// Push the object on one axis only (least resistance)
			if (Math.abs(offsetx) < Math.abs(offsety))
				offsety = 0;
			else
				offsetx = 0;
		}
		
		return new Vector2(offsetx, offsety);
	}
	
	@Override
	public boolean collideAt(int px, int py)
	{
		if (px < x + halfWidth && px > x - halfWidth &&
			py < y + halfHeight && py > y - halfHeight)
			return true;
		return false;
	}

}
