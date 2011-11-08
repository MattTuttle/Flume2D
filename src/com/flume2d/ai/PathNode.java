package com.flume2d.ai;

public class PathNode implements Comparable<PathNode>
{
	public int x;
	public int y;
	
	public double f;
	public int g;
	public double h;
	public int cost;
	
	public PathNode parent;
	
	public PathNode(int x, int y)
	{
		this.x = x;
		this.y = y;
		this.parent = null;
	}
	
	public String toString()
	{
		return "(" + x + ", " + y + ")";
	}

	@Override
	public int compareTo(PathNode node)
	{
		if (f < node.f)
			return -1;
		else if (f > node.f)
			return 1;
		return 0;
	}
	
}
