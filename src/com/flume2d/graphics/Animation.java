package com.flume2d.graphics;

public class Animation implements Cloneable
{
	public String name;
	public int[] frames;
	public boolean loop;
	public int frame;
	public float delay;

	public Animation clone()
	{
		try
		{
			return (Animation) super.clone();
		}
		catch (CloneNotSupportedException e)
		{
			throw new AssertionError();
		}
	}
}
