package com.flume2d.math;

public class Vector2
{
	
	public double x;
	public double y;
	
	public Vector2()
	{
		x = y = 0;
	}
	
	public Vector2(double x, double y)
	{
		this.x = x;
		this.y = y;
	}
	
	public double length()
	{
		return Math.sqrt(x * x + y * y);
	}
	
	public void normalize()
	{
		double len = length();
		x = x / len;
		y = y / len;
	}
	
	public void truncate(double value)
	{
		double len = Math.min(value, length());
		normalize();
		multiply(len);
		if(Math.abs(x) < 0.00000001) x = 0;
		if(Math.abs(y) < 0.00000001) y = 0;
	}
	
	public void add(Vector2 vec)
	{
		x += vec.x;
		y += vec.y;
	}
	
	public void subtract(Vector2 vec)
	{
		x -= vec.x;
		y -= vec.y;
	}
	
	public void multiply(double scalar)
	{
		x *= scalar;
		y *= scalar;
	}
	
	public void divide(double scalar)
	{
		x /= scalar;
		y /= scalar;
	}
	
	public void inverse()
	{
		x = -x;
		y = -y;
	}
	
	public Vector2 normal(Vector2 vec)
	{
		Vector2 result = new Vector2(-(vec.y - y), vec.x - x);
		result.normalize();
		return result;
	}
	
	public double cross(Vector2 vec)
	{
		return (x * vec.y - y * vec.x);
	}
	
	public double dot(Vector2 vec)
	{
		return (x * vec.x + y * vec.y);
	}
	
}
