package com.flume2d.ai.heuristic;

import com.flume2d.ai.IHeuristic;
import com.flume2d.ai.PathNode;

public class ManhattanMethod implements IHeuristic
{

	/**
	 * The Manhattan method heuristic
	 * @param start the starting node
	 * @param dest the destination node
	 * @return the heuristic value
	 */
	@Override
	public double run(PathNode start, PathNode dest)
	{
		return Math.abs(start.x - dest.x) + Math.abs(start.y - dest.y);
	}

}
