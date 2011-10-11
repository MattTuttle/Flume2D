package com.flume2d.math;

public class Vector2
{
	
	public float x;
	public float y;
	
	public Vector2()
	{
		x = y = 0;
	}
	
	public Vector2(float x, float y)
	{
		this.x = x;
		this.y = y;
	}
	
	public String toString()
	{
		return "(" + x + ", " + y + ")";
	}
	
	public float length()
	{
		return (float) Math.sqrt(x * x + y * y);
	}
	
	public void normalize()
	{
		float len = length();
		x = x / len;
		y = y / len;
	}
	
	public void truncate(float value)
	{
		float len = Math.min(value, length());
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
	
	public void multiply(float scalar)
	{
		x *= scalar;
		y *= scalar;
	}
	
	public void divide(float scalar)
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
	
	public float cross(Vector2 vec)
	{
		return (x * vec.y - y * vec.x);
	}
	
	public float dot(Vector2 vec)
	{
		return (x * vec.x + y * vec.y);
	}
	
}
