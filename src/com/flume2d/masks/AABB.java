package com.flume2d.masks;

import com.flume2d.math.Vector2;

public class AABB extends Polygon
{
	
	public int halfWidth;
	public int halfHeight;
	
	public AABB(int width, int height)
	{
		vertices = new Vector2[4];
		halfWidth = width / 2;
		halfHeight = height / 2;
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
	
	// fast AABB collision check
	public Vector2 collideAABB(AABB mask)
	{
		int min1, max1, min2, max2;
		
		// check x-axis
		if (x < mask.x)
		{
			min1 = x;
			max1 = x + halfWidth;
			min2 = mask.x - mask.halfWidth;
			max2 = mask.x;
		}
		else
		{
			min1 = x - halfWidth;
			max1 = x;
			min2 = mask.x;
			max2 = mask.x + mask.halfWidth;
		}
		// test collision
		if (min1 - max2 > 0 || min2 - max1 > 0)
			return null;
		
		int offsetx = (max2 - min1) * -1;
		
		// check y-axis
		if (y < mask.y)
		{
			min1 = y;
			max1 = y + halfHeight;
			min2 = mask.y - mask.halfHeight;
			max2 = mask.y;
		}
		else
		{
			min1 = y - halfHeight;
			max1 = y;
			min2 = mask.y;
			max2 = mask.y + mask.halfHeight;
		}
		// test collision
		if (min1 - max2 > 0 || min2 - max1 > 0)
			return null;
		
		int offsety = (max2 - min1) * -1;
		
		// Push the object on one axis only
		if (offsetx < offsety)
			offsety = 0;
		else
			offsetx = 0;
		return new Vector2(offsetx, offsety);
	}

}
