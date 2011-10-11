package com.flume2d;

import java.util.*;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.flume2d.graphics.Graphic;
import com.flume2d.masks.*;
import com.flume2d.math.*;

public class Entity implements ISceneEntity
{
	
	public float x;
	public float y;
	
	public Entity()
	{
		this(0, 0, null, null);
	}
	
	public Entity(int x, int y)
	{
		this(x, y, null, null);
	}
	
	public Entity(int x, int y, Graphic graphic)
	{
		this(x, y, graphic, null);
	}
	
	public Entity(int x, int y, Graphic graphic, Mask mask)
	{
		this.x = x;
		this.y = y;
		this.graphic = graphic;
		this.mask = mask;
		this.layer = 0;
	}
	
	public Entity collide(String type)
	{
		return collide(type, false);
	}
	
	public Entity collide(String type, boolean expel)
	{
		if (scene == null) return null;
		LinkedList<Entity> list = scene.getTypes(type);
		if (list == null) return null;
		Iterator<Entity> it = list.iterator();
		while (it.hasNext())
		{
			Entity e = it.next();
			Vector2 result = null;
			result = mask.collide(e.mask);
			if (result != null)
			{
				if (expel)
				{
					x += result.x;
					y += result.y;
				}
				return e;
			}
		}
		return null;
	}

	public void update()
	{
		if (graphic != null && graphic.isActive()) graphic.update();
		if (mask != null) mask.setPosition(x, y);
	}
	
	public void render(SpriteBatch spriteBatch)
	{
		if (graphic == null) return;
		graphic.render(spriteBatch);
	}
	
	public double distanceFrom(Entity e)
	{
		return Math.sqrt((x - e.x) * (x - e.x) + (y - e.y) * (y - e.y));
	}
	
	public double distanceFromPoint(float px, float py)
	{
		return Math.sqrt((x - px) * (x - px) + (y - py) * (y - py));
	}
	
	public boolean collideAt(int x, int y)
	{
		if (mask != null)
			return mask.collideAt(x, y);
		return false;
	}

	@Override public void setScene(Scene scene) { this.scene = scene; }
	@Override public boolean hasScene() { return (scene != null); }

	@Override public void added() { }
	@Override public void removed() { }
	
	public String name;
	public String type;
	public int layer;

	public Graphic graphic;
	public Mask mask;
	
	protected Scene scene;
	
}
