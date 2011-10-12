package com.flume2d;

import java.util.*;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.flume2d.graphics.Graphic;
import com.flume2d.math.Vector2;

public class Scene
{
	
	public static Vector2 camera = new Vector2();
	
	public Scene()
	{
		added = new LinkedList<ISceneEntity>();
		removed = new LinkedList<ISceneEntity>();
		renderList = new LinkedList<Entity>();
		updateList = new LinkedList<Entity>();
		typeList = new HashMap<String, LinkedList<Entity>>();
		
		Matrix4 projection = new Matrix4();
        projection.setToOrtho(0, Engine.width, Engine.height, 0, -1, 1);
		spriteBatch = new SpriteBatch();
//		spriteBatch.setProjectionMatrix(projection);
	}
	
	public void destroy()
	{
		spriteBatch.dispose();
	}
	
	public void add(ISceneEntity e)
	{
		if (e.hasScene()) return;
		added.add(e);
		e.setScene(this);
	}
	
	public void add(ISceneEntity[] e)
	{
		for (int i = 0; i < e.length; i++)
			add(e);
	}
	
	public void remove(ISceneEntity e)
	{
		removed.add(e);
	}
	
	public void remove(ISceneEntity[] e)
	{
		for (int i = 0; i < e.length; i++)
			remove(e);
	}
	
	public Entity addGraphic(Graphic graphic)
	{
		return addGraphic(graphic, 0, 0);
	}
	
	public Entity addGraphic(Graphic graphic, int x, int y)
	{
		Entity e = new Entity(x, y, graphic);
		add(e);
		return e;
	}
	
	public LinkedList<Entity> getTypes(String type)
	{
		return typeList.get(type);
	}
	
	public Entity findClosest(String type, float x, float y)
	{
		if (!typeList.containsKey(type)) return null;
		
		return findClosest(typeList.get(type), x, y);
	}
	
	public Entity findClosest(String[] types, float x, float y)
	{
		LinkedList<Entity> list = new LinkedList<Entity>();
		for (int i = 0; i < types.length; i++)
		{
			if (!typeList.containsKey(types[i])) continue;
			Iterator<Entity> it = typeList.get(types[i]).iterator();
			while (it.hasNext())
			{
				list.push(it.next());
			}
		}
		
		// check to make sure we actually populated the list
		if (list.size() == 0) return null;
		
		return findClosest(list, x, y);
	}
	
	public Entity findClosest(LinkedList<Entity> list, float x, float y)
	{
		double dist, maxDist = Double.MAX_VALUE;
		Entity closest = null;
		Iterator<Entity> it = list.iterator();
		do
		{
			Entity e = it.next();
			dist = (x - e.x) * (x - e.x) + (y - e.y) * (y - e.y);
			if (dist < maxDist)
			{
				maxDist = dist;
				closest = e;
			}
		} while(it.hasNext());
		
		return closest;
	}
	
	public Entity findAt(String type, int x, int y)
	{
		if (!typeList.containsKey(type)) return null;
		Iterator<Entity> it = typeList.get(type).iterator();
		while(it.hasNext())
		{
			Entity e = it.next();
			if (e.collideAt(x, y)) return e;
		}
		return null;
	}
	
	public Entity getByName(String name)
	{
		Iterator<Entity> it = updateList.iterator();
		while (it.hasNext())
		{
			Entity e = it.next();
			if (e.name == name)
				return e;
		}
		return null;
	}

	public void update()
	{
		Iterator<Entity> it = updateList.iterator();
		while (it.hasNext())
		{
			Entity e = it.next();
			e.update();
		}
		updateLists();
	}
	
	public void render()
	{
        spriteBatch.begin();
		Iterator<Entity> it = renderList.iterator();
		while (it.hasNext())
		{
			Entity e = it.next();
			matrix.setToTranslation(e.x - camera.x, e.y - camera.y, 0);
			// Should this flush the sprite batch every frame?
			spriteBatch.setTransformMatrix(matrix);
			e.render(spriteBatch);
		}
		spriteBatch.end();
	}
	
	private void updateLists()
	{
		Iterator<ISceneEntity> it;
		
		// add any new entities
		it = added.iterator();
		while (it.hasNext())
		{
			Entity e = (Entity) it.next();
			updateList.add(e);
			renderList.add(e);
			if (!typeList.containsKey(e.type))
			{
				typeList.put(e.type, new LinkedList<Entity>());
			}
			typeList.get(e.type).add(e);
			e.added();
		}
		added.clear();
		
		// remove any old entities
		it = removed.iterator();
		while (it.hasNext())
		{
			Entity e = (Entity) it.next();
			updateList.remove(e);
			renderList.remove(e);
			typeList.get(e.type).remove(e);
			e.removed();
		}
		removed.clear();
		
		// sort render list by z-index
		Collections.sort(renderList, EntityZSort.getInstance());
		SortTypeList();
	}
	
	private void SortTypeList() 
	{
		Iterator<String> it = typeList.keySet().iterator();
		while (it.hasNext())
		{
			String key = it.next();
			LinkedList<Entity> list = typeList.get(key);
			Iterator<Entity> it2 = list.iterator();
			while (it2.hasNext())
			{
				Entity e = it2.next();
				// check that the entity is in the right type group
				if (key != e.type)
				{
					list.remove(e);
					typeList.get(e.type).add(e);
				}
			}
		}
	}

	private Matrix4 matrix = new Matrix4();
	private SpriteBatch spriteBatch;
	
	private LinkedList<ISceneEntity> added;
	private LinkedList<ISceneEntity> removed;
	private LinkedList<Entity> renderList;
	private LinkedList<Entity> updateList;
	private HashMap<String, LinkedList<Entity>> typeList;
	
}
