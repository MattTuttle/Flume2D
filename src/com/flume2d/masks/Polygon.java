package com.flume2d.masks;

import com.flume2d.math.Range;
import com.flume2d.math.Vector2;

public class Polygon implements Mask
{
	
	public int x;
	public int y;
	
	public Vector2[] vertices;
	
	public Range project(Vector2 axis, Vector2[] list)
	{
		Range r = new Range();
		// set initial min/max values
		r.min = r.max = axis.dot(list[0]);
		
		for (int j = 1; j < list.length; j++)
		{
			double t = axis.dot(list[j]);
			if (t < r.min) r.min = t;
			if (t > r.max) r.max = t;
		}
		return r;
	}
	
	public void padding(Vector2[] list)
	{
		Vector2 temp = new Vector2(-(list[1].y - list[0].y), list[1].x - list[0].x);
		temp.truncate(0.00000001);
		Vector2 vec = new Vector2(list[1].x, list[1].y);
		vec.add(temp);
		list[list.length] = vec;
	}

	public Vector2 collidePolygon(Polygon mask)
	{
		Range r1 = null, r2 = null;
		Vector2 axis = null;
		Vector2 offset = new Vector2(x - mask.x, y - mask.y);

		// add padding if this is a line
		if (vertices.length == 2) padding(vertices);
		if (mask.vertices.length == 2) padding(mask.vertices);
		
		for (int i = 1; i < mask.vertices.length; i++)
		{
			// find the side's axis vector
			axis = mask.vertices[i].normal(
					// make sure the vertex exists
					(i >= mask.vertices.length) ? mask.vertices[0] : mask.vertices[i+1]
				);
			
			// project polygons onto the axis
			r1 = project(axis, mask.vertices);
			r2 = project(axis, vertices);
			
			// shift to match mask projected points
			double off = axis.dot(offset);
			r1.min += off;
			r1.max += off;
			
			// test for intersections
			double test1 = r1.min - r2.max;
			double test2 = r2.min - r1.max;
			if (test1 > 0 || test2 > 0)
				return null; // no intersection so we bail
		}
		
		double range = (r2.max - r1.min) * -1;
		return new Vector2(axis.x * range, axis.y * range);
	}

	public Vector2 collideCircle(Circle mask)
	{
		Vector2 closestVector = new Vector2();
		double testDistance = Integer.MAX_VALUE;
		Vector2 offset = new Vector2(x - mask.x, y - mask.y);
		
		// add padding if this is a line
		if (vertices.length == 2) padding(vertices);
		
		// find the closest vertex to use to find the normal
		for (int i = 0; i < vertices.length; i++)
		{
			double distance = (mask.x - (x + vertices[i].x)) * (mask.x - (x + vertices[i].x)) + (mask.y - (y + vertices[i].y)) * (mask.y - (y + vertices[i].y));
			if (distance < testDistance)
			{
				testDistance = distance;
				closestVector.x = x + vertices[i].x;
				closestVector.y = y + vertices[i].y;
			}
		}
		
		Vector2 axis = new Vector2(closestVector.x - mask.x, closestVector.y - mask.y);
		axis.normalize();
		
		// project the polygon and circle onto the axis
		Range r1 = project(axis, vertices);
		Range r2 = new Range();
		r2.min = -mask.radius;
		r2.max = mask.radius;
		
		// shift to match mask projected points
		double off = axis.dot(offset);
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
					(i >= vertices.length) ? vertices[0] : vertices[i+1]
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
		
		double range = (r2.max - r1.min) * -1;
		return new Vector2(axis.x * range, axis.y * range);
	}

	@Override
	public Vector2 collide(Mask mask)
	{
		if (mask instanceof Circle)
		{
			collideCircle((Circle) mask);
		}
		else if (mask instanceof Polygon)
		{
			collidePolygon((Polygon) mask);
		}
		return null;
	}

}