package com.flume2d.masks;

import com.flume2d.math.Vector2;

public class Circle implements Mask
{
	
	public float x, y;
	public float radius;
	
	public Circle(int radius)
	{
		this(0, 0, radius);
	}
	
	public Circle(int x, int y, int radius)
	{
		this.x = x;
		this.y = y;
		this.radius = radius;
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
		float distanceSquared = (x - mask.x) * (x - mask.x) + (y - mask.y) * (y - mask.y);
		
		// check the radius length to the distance between centers
		if(Math.abs(distanceSquared) < totalRadius * totalRadius)
		{
			float dist = (float) Math.sqrt(distanceSquared);
			float difference = totalRadius - dist;
			if (dist == 0) dist = 0.1f;
			return new Vector2((x - mask.x) / dist * difference, (y - mask.y) / dist * difference);
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
		float distanceSquared = (x - mask.x) * (x - mask.x) + (y - mask.y) * (y - mask.y);
		
		// check the radius length to the distance between centers
		if(Math.abs(distanceSquared) < totalRadius * totalRadius)
		{
			return true;
		}
		return false;
	}

	@Override
	public void setPosition(float x, float y)
	{
		this.x = x;
		this.y = y;
	}

	@Override
	public boolean collideAt(int px, int py)
	{
		float distanceSquared = (x - px) * (x - px) + (y - py) * (y - py);
		
		// check the radius length to the distance between centers
		if(Math.abs(distanceSquared) < radius * radius)
			return true;
		
		return false;
	}

}
