package com.flume2d.ai;

/* 
 * A* algorithm implementation.
 * Copyright (C) 2007, 2009 Giuseppe Scrivano <gscrivano@gnu.org>

 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
			
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License along
 * with this program; if not, see <http://www.gnu.org/licenses/>.
 */

import java.util.*;

import com.flume2d.ai.heuristic.ManhattanMethod;

/*
 * Example.
 */
public class PathFinder
{
	
	private PathNode[][] nodes;
	private PriorityQueue<PathNode> open;
	private PriorityQueue<PathNode> closed;
	
	private IWalkable walkable;
	private IHeuristic heuristic;
	
	private static int COST_ORTHOGONAL = 10;
	private static int COST_DIAGONAL = 14;
	
	private int columns;
	private int rows;
	
	public boolean allowDiagonal = false;
	public boolean calculateNearestPoint = false;
	
	public PathFinder(int columns, int rows, IWalkable walkable)
	{
		this(columns, rows, walkable, new ManhattanMethod());
	}
	
	public PathFinder(int columns, int rows, IWalkable walkable, IHeuristic heuristic)
	{
		this.columns = columns;
		this.rows = rows;
		this.walkable = walkable;
		this.heuristic = heuristic;
		
		// Initialize node array
		nodes = new PathNode[columns][rows];
		for (int x = 0; x < columns; x++)
			for (int y = 0; y < rows; y++)
				nodes[x][y] = new PathNode(x, y);
		
		open = new PriorityQueue<PathNode>();
		closed = new PriorityQueue<PathNode>();
	}
	
	/**
	 * Cleans up the past run by setting parent values to null
	 * and clearing the open/closed lists
	 */
	private void cleanup()
	{
		Iterator<PathNode> it;
		
		it = closed.iterator();
		while (it.hasNext())
		{
			PathNode node = it.next();
			node.parent = null;
			it.remove();
		}
		
		it = open.iterator();
		while (it.hasNext())
		{
			PathNode node = it.next();
			node.parent = null;
			it.remove();
		}
	}
	
	/**
	 * Find a path from given coordinates
	 * @param startX the starting coord x-axis value
	 * @param startY the starting coord y-axis value
	 * @param destX the ending coord x-axis value
	 * @param destY the ending coord y-axis value
	 * @return a list of path nodes to follow
	 */
	public List<PathNode> findPath(int startX, int startY, int destX, int destY)
	{
		cleanup();
		PathNode start = nodes[startX][startY];
		PathNode dest = nodes[destX][destY];
		
		start.g = 0;
		start.h = heuristic.run(start, dest);
		start.f = start.h;
		open.add(start);
		
		while (open.size() > 0)
		{
			PathNode currentNode = open.remove();
			closed.add(currentNode);
			
			if (currentNode == dest)
			{
				return rebuildPath(currentNode);
			}
			
			for (PathNode n : getNeighbors(currentNode))
			{
				// skip nodes that have already been closed
				if (closed.contains(n))
					continue;
				
				int g = currentNode.g + n.cost;
				if (!open.contains(n))
				{
					n.parent = currentNode;
					n.g = g;
					n.h = heuristic.run(n, dest);
					n.f = n.g + n.h;
					open.offer(n);
				}
				else if (g < n.g)
				{
					n.parent = currentNode;
					n.g = g;
					n.h = heuristic.run(n, dest);
					n.f = n.g + n.h;
				}
			}
		}
		
		if (calculateNearestPoint)
		{
			double min = Double.MAX_VALUE;
			PathNode nearestNode = null;
			
			for (PathNode c : closed)
			{
				double dist = heuristic.run(c, dest);
				if (dist < min)
				{
					min = dist;
					nearestNode = c;
				}
			}
			return rebuildPath(nearestNode);
		}
		
		return null;
	}
	
	private enum Direction
	{
		None,
		Vertical,
		Horizontal
	}

	/**
	 * Retraces the path back to the start giving only end points
	 * nodes in a straight line are ignored
	 * @param dest the destination node to start at
	 * @return a list of the nodes returning to the start
	 */
	private List<PathNode> rebuildPath(PathNode dest) 
	{
		LinkedList<PathNode> path = new LinkedList<PathNode>();
		Direction dir = Direction.None;
		
		if (dest == null)
			return null;
		
		PathNode n = dest;
		while (n.parent != null)
		{
			if (n.y == n.parent.y && dir != Direction.Vertical)
			{
				path.push(n);
				dir = Direction.Vertical;
			}
			if (n.x == n.parent.x && dir != Direction.Horizontal)
			{
				path.push(n);
				dir = Direction.Horizontal;
			}
			
			n = n.parent;
		}
		
		return path;
	}

	/**
	 * Gets the node's neighbors
	 * @param node the node to get neighbors for
	 * @return A list of neighboring nodes that are walkable
	 */
	private List<PathNode> getNeighbors(PathNode node)
	{
		int x = node.x;
		int y = node.y;
		PathNode currentNode = null;
		LinkedList<PathNode> neighbors = new LinkedList<PathNode>();
		
		if (x > 0 && walkable.isWalkable(x - 1, y))
		{
			currentNode = nodes[x - 1][y];
			currentNode.cost = COST_ORTHOGONAL;
			neighbors.push(currentNode);
		}
		if (x < columns && walkable.isWalkable(x + 1, y))
		{
			currentNode = nodes[x + 1][y];
			currentNode.cost = COST_ORTHOGONAL;
			neighbors.push(currentNode);
		}
		if (y > 0 && walkable.isWalkable(x, y - 1))
		{
			currentNode = nodes[x][y - 1];
			currentNode.cost = COST_ORTHOGONAL;
			neighbors.push(currentNode);
		}
		if (y < rows && walkable.isWalkable(x, y + 1))
		{
			currentNode = nodes[x][y + 1];
			currentNode.cost = COST_ORTHOGONAL;
			neighbors.push(currentNode);
		}
		if (allowDiagonal)
		{
			if (x > 0 && y > 0 && walkable.isWalkable(x - 1, y - 1))
			{
				currentNode = nodes[x - 1][y - 1];
				currentNode.cost = COST_DIAGONAL;
				neighbors.push(currentNode);
			}
			if (x > 0 && y < rows && walkable.isWalkable(x - 1, y + 1))
			{
				currentNode = nodes[x - 1][y + 1];
				currentNode.cost = COST_DIAGONAL;
				neighbors.push(currentNode);
			}
			if (x < columns && y < rows && walkable.isWalkable(x + 1, y + 1))
			{
				currentNode = nodes[x + 1][y + 1];
				currentNode.cost = COST_DIAGONAL;
				neighbors.push(currentNode);
			}
			if (x < columns && y > 0 && walkable.isWalkable(x + 1, y - 1))
			{
				currentNode = nodes[x + 1][y - 1];
				currentNode.cost = COST_DIAGONAL;
				neighbors.push(currentNode);
			}
		}
		return neighbors;
	}

}
