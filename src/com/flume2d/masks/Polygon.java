package com.flume2d.masks;

import com.flume2d.math.*;

public class Polygon extends Mask
{
	
	public Rectangle bounds;
	public Vector2[] vertices;
	
	public Polygon()
	{
		bounds = new Rectangle();
	}
	
	public Range project(Vector2 axis, Vector2[] list)
	{
		Range r = new Range();
		// set initial min/max values
		r.min = r.max = axis.dot(list[0]);
		
		for (int j = 1; j < list.length; j++)
		{
			float t = axis.dot(list[j]);
			if (t < r.min) r.min = t;
			if (t > r.max) r.max = t;
		}
		return r;
	}
	
	public void padding(Vector2[] list)
	{
		Vector2 temp = new Vector2(-(list[1].y - list[0].y), list[1].x - list[0].x);
		temp.truncate(0.00000001f);
		Vector2 vec = new Vector2(list[1].x, list[1].y);
		vec.add(temp);
		list[list.length] = vec;
	}

	public Vector2 collidePolygon(Polygon mask)
	{
		// TODO: fix collision
		Range r1 = null, r2 = null;
		Vector2 axis = null;
		Vector2 offset = new Vector2(parent.x - mask.parent.x, parent.y - mask.parent.y);

		// add padding if this is a line
		if (vertices.length == 2) padding(vertices);
		if (mask.vertices.length == 2) padding(mask.vertices);
		
		for (int i = 1; i < mask.vertices.length; i++)
		{
			// find the side's axis vector
			axis = mask.vertices[i].normal(
					// make sure the vertex exists
					(i >= mask.vertices.length - 1) ? mask.vertices[0] : mask.vertices[i+1]
				);
			
			// project polygons onto the axis
			r1 = project(axis, mask.vertices);
			r2 = project(axis, vertices);
			
			// shift to match mask projected points
			float off = axis.dot(offset);
			r1.min += off;
			r1.max += off;
			
			// test for intersections
			float test1 = r1.min - r2.max;
			float test2 = r2.min - r1.max;
			if (test1 > 0 || test2 > 0)
				return null; // no intersection so we bail
		}
		
		float range = (r2.max - r1.min) * -1;
		return new Vector2(axis.x * range, axis.y * range);
	}

	public Vector2 collideCircle(Circle mask)
	{
		// TODO: fix collision
		Vector2 closestVector = new Vector2();
		float testDistance = Integer.MAX_VALUE;
		Vector2 offset = new Vector2(parent.x - mask.parent.x, parent.y - mask.parent.y);
		
		// add padding if this is a line
		if (vertices.length == 2) padding(vertices);
		
		// find the closest vertex to use to find the normal
		for (int i = 0; i < vertices.length; i++)
		{
			float distance = (mask.parent.x - (parent.x + vertices[i].x)) * (mask.parent.x - (parent.x + vertices[i].x))
					+ (mask.parent.y - (parent.y + vertices[i].y)) * (mask.parent.y - (parent.y + vertices[i].y));
			if (distance < testDistance)
			{
				testDistance = distance;
				closestVector.x = parent.x + vertices[i].x;
				closestVector.y = parent.y + vertices[i].y;
			}
		}
		
		Vector2 axis = new Vector2(closestVector.x - mask.parent.x, closestVector.y - mask.parent.y);
		axis.normalize();
		
		// project the polygon and circle onto the axis
		Range r1 = project(axis, vertices);
		Range r2 = new Range();
		r2.min = -mask.radius;
		r2.max = mask.radius;
		
		// shift to match mask projected points
		float off = axis.dot(offset);
		r1.min += off;
		r1.max += off;
		
		// test to see if we collide
		if ((r1.min - r2.max) > 0 || (r2.min - r1.max) > 0)
			return null;
		
		// now we have to loop through every vertex...
		for (int i = 0; i < vertices.length; i++)
		{
			// find the side's axis vector
			axis = vertices[i].normal(
					// make sure the vertex exists
					(i >= vertices.length - 1) ? vertices[0] : vertices[i+1]
				);
		
			r1 = project(axis, vertices);
			
			// shift to match mask projected points
			off = axis.dot(offset);
			r1.min += off;
			r1.max += off;
			
			// test to see if we collide
			if ((r1.min - r2.max) > 0 || (r2.min - r1.max) > 0)
				return null;
		}
		
		float range = (r2.max - r1.min) * -1;
		return new Vector2(axis.x * range, axis.y * range);
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
			return collidePolygon((Polygon) mask);
		}
		return null;
	}
	
	@Override
	public boolean overlaps(Mask mask)
	{
		return (collide(mask) != null);
	}

	@Override
	public boolean collideAt(int x, int y)
	{
		throw new UnsupportedOperationException("Not Implemented");
	}

	@Override
	public Rectangle getBounds()
	{
		Range xr = new Range();
		Range yr = new Range();
		for (int i = 0; i < vertices.length; i++)
		{
			if (vertices[i].x < xr.min)
				xr.min = vertices[i].x;
			if (vertices[i].x > xr.max)
				xr.max = vertices[i].x;
			
			if (vertices[i].y < yr.min)
				yr.min = vertices[i].y;
			if (vertices[i].y > yr.max)
				yr.max = vertices[i].y;
		}
		return bounds;
	}

}
