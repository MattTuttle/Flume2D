package com.flume2d.masks;

import com.flume2d.math.*;

public class Circle extends Mask
{
	
	public float radius;
	public Rectangle bounds;
	
	public Circle(int radius)
	{
		this.radius = radius;
		bounds = new Rectangle(0, 0, radius * 2, radius * 2);
	}

	@Override
	public Vector2 collide(Mask mask)
	{
		if (mask instanceof Circle)
		{
			return collide((Circle) mask);
		}
		else if (mask instanceof Polygon)
		{
			// we only need this code in one place
			return ((Polygon)mask).collide(this);
		}
		return null;
	}

	public Vector2 collide(Circle mask)
	{
		float totalRadius = radius + mask.radius;
		float distanceSquared = (parent.x - mask.parent.x) * (parent.x - mask.parent.x) + (parent.y - mask.parent.y) * (parent.y - mask.parent.y);
		
		// check the radius length to the distance between centers
		if(Math.abs(distanceSquared) < totalRadius * totalRadius)
		{
			float dist = (float) Math.sqrt(distanceSquared);
			float difference = totalRadius - dist;
			if (dist == 0) dist = 0.1f;
			return new Vector2((parent.x - mask.parent.x) / dist * difference, (parent.y - mask.parent.y) / dist * difference);
		}
		
		return null;
	}
	
	@Override
	public boolean overlaps(Mask mask)
	{
		if (mask instanceof Circle)
		{
			return overlaps((Circle) mask);
		}
		else if (mask instanceof Polygon)
		{
			// we only need this code in one place
			return ((Polygon) mask).collide(this) != null;
		}
		return false;
	}
	
	public boolean overlaps(Circle mask)
	{
		float totalRadius = radius + mask.radius;
		float distanceSquared = (parent.x - mask.parent.x) * (parent.x - mask.parent.x) + (parent.y - mask.parent.y) * (parent.y - mask.parent.y);
		
		// check the radius length to the distance between centers
		if(Math.abs(distanceSquared) < totalRadius * totalRadius)
		{
			return true;
		}
		return false;
	}

	@Override
	public boolean collideAt(int px, int py)
	{
		float distanceSquared = (parent.x - px) * (parent.x - px) + (parent.y - py) * (parent.y - py);
		
		// check the radius length to the distance between centers
		if(Math.abs(distanceSquared) < radius * radius)
			return true;
		
		return false;
	}
	
	@Override
	public Rectangle getBounds()
	{
		bounds.x = parent.x - radius;
		bounds.y = parent.y - radius;
		return bounds;
	}

}
