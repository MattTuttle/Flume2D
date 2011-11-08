package com.flume2d.ai.heuristic;

import com.flume2d.ai.IHeuristic;
import com.flume2d.ai.PathNode;

public class EuclideanMethod implements IHeuristic
{

	@Override
	public double run(PathNode start, PathNode goal)
	{
		return Math.sqrt((start.x - goal.x) * (start.x - goal.x) + (start.y - goal.y) * (start.y - goal.y));
	}

}
