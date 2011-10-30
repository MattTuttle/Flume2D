package com.flume2d.masks;

import com.flume2d.math.*;

public class AABB extends Polygon
{
	
	protected int halfWidth;
	protected int halfHeight;
	
	/**
	 * Constructs a new AABB
	 * @param width the full width of the AABB
	 * @param height the full height of the AABB
	 */
	public AABB(int width, int height)
	{
		super();
		
		halfWidth = width / 2;
		halfHeight = height / 2;
		bounds.width = width;
		bounds.height = height;
		
		vertices = new Vector2[4];
		for (int i = 0; i < 4; i++)
			vertices[i] = new Vector2();
	}
	
	/**
	 * Convert to polygon vertices
	 */
	protected void setVertices()
	{
		vertices[0].x = parent.x - halfWidth; vertices[0].y = parent.y - halfHeight;
		vertices[1].x = parent.x + halfWidth; vertices[1].y = parent.y - halfHeight;
		vertices[2].x = parent.x + halfWidth; vertices[2].y = parent.y + halfHeight;
		vertices[3].x = parent.x - halfWidth; vertices[3].y = parent.y + halfHeight;
	}
	
	/**
	 *  REALLY fast AABB collision check
	 *  @param mask the mask to check for overlaps
	 *  @return whether the mask overlaps the other
	 */
	@Override
	public boolean overlaps(Mask mask)
	{
		if (mask instanceof AABB)
		{
			AABB box = (AABB) mask;
			if (parent.x - halfWidth > box.parent.x + box.halfWidth) return false;
			if (parent.x + halfWidth < box.parent.x - box.halfWidth) return false;
			if (parent.y - halfWidth > box.parent.y + box.halfWidth) return false;
			if (parent.y + halfWidth < box.parent.y - box.halfWidth) return false;
			return true;
		}
		
		setVertices();
		return super.overlaps(mask);
	}
	
	/**
	 * Checks if the AABB is colliding with another mask
	 * @param mask the mask to check collision against
	 * @return whether we collide or not
	 */
	@Override
	public Vector2 collide(Mask mask)
	{
		if (mask instanceof AABB)
		{
			return collide((AABB) mask);
		}
		
		setVertices();
		return super.collide(mask);
	}
	
	/**
	 * fast AABB collision check using SAT
	 * @param mask the AABB mask to collide with
	 * @return whether we collide with the mask or not
	 */
	public Vector2 collide(AABB mask)
	{
		float min1, max1,
			min2, max2,
			offsetx = 0, offsety = 0;
		
		// check x-axis
		if (parent.x < mask.parent.x)
		{
			min1 = parent.x;
			max1 = parent.x + halfWidth;
			min2 = mask.parent.x - mask.halfWidth;
			max2 = mask.parent.x;
			offsetx = min2 - max1;
			// test collision
			if (min1 - max2 > 0 || min2 - max1 > 0)
				return null;
		}
		else if (parent.x > mask.parent.x)
		{
			min1 = parent.x - halfWidth;
			max1 = parent.x;
			min2 = mask.parent.x;
			max2 = mask.parent.x + mask.halfWidth;
			offsetx = max2 - min1;
			// test collision
			if (min1 - max2 > 0 || min2 - max1 > 0)
				return null;
		}
		
		// check y-axis
		if (parent.y < mask.parent.y)
		{
			min1 = parent.y;
			max1 = parent.y + halfHeight;
			min2 = mask.parent.y - mask.halfHeight;
			max2 = mask.parent.y;
			offsety = min2 - max1;
			// test collision
			if (min1 - max2 > 0 || min2 - max1 > 0)
				return null;
		}
		else if (parent.y > mask.parent.y)
		{
			min1 = parent.y - halfHeight;
			max1 = parent.y;
			min2 = mask.parent.y;
			max2 = mask.parent.y + mask.halfHeight;
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
	
	/**
	 * Check if the AABB collides with a point
	 * @param px the point's x-axis value
	 * @param py the point's y-axis value
	 * @return whether the point collides with the AABB
	 */
	@Override
	public boolean collideAt(int px, int py)
	{
		if (px < parent.x + halfWidth && px > parent.x - halfWidth &&
			py < parent.y + halfHeight && py > parent.y - halfHeight)
			return true;
		return false;
	}
	
	@Override
	public Rectangle getBounds()
	{
		bounds.x = parent.x - halfWidth;
		bounds.y = parent.y - halfHeight;
		return bounds;
	}

}
