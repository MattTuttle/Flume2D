package com.flume2d;

import java.util.*;

public class EntityZSort implements Comparator<Entity>
{
	
	private EntityZSort()
	{
	}
	
	public static EntityZSort getInstance()
	{
		if (instance == null)
			instance = new EntityZSort();
		return instance;
	}

	@Override
	public int compare(Entity e1, Entity e2) {
		if (e1.layer > e2.layer) return 1;
		if (e1.layer < e2.layer) return -1;
		return 0;
	}
	
	private static EntityZSort instance;

}
