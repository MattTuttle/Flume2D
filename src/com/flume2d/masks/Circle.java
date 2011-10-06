package com.flume2d.masks;

import com.flume2d.math.Vector2;

public class Circle implements Mask
{
	
	public int x;
	public int y;
	public int radius;
	
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
			return collideCircle((Circle) mask);
		}
		else if (mask instanceof Polygon)
		{
			// we only need this code in one place
			return ((Polygon)mask).collideCircle(this);
		}
		return null;
	}

	public Vector2 collideCircle(Circle mask)
	{
		int totalRadius = radius + mask.radius;
		int distanceSquared = (x - mask.x) * (x - mask.x) + (y - mask.y) * (y - mask.y);
		
		// check the radius length to the distance between centers
		if(distanceSquared < totalRadius * totalRadius)
		{
			double difference = totalRadius - Math.sqrt(distanceSquared);
			return new Vector2((mask.x - x) * difference, (mask.y - y) * difference);
		}
		
		return null;
	}

}
