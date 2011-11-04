package com.flume2d;

import java.util.*;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.Disposable;
import com.flume2d.graphics.Graphic;
import com.flume2d.math.Vector2;

public class Scene implements Disposable
{
	
	public Vector2 camera = new Vector2();
	
	public Scene()
	{
		added = new LinkedList<Entity>();
		removed = new LinkedList<Entity>();
		renderList = new LinkedList<Entity>();
		updateList = new LinkedList<Entity>();
		typeList = new HashMap<String, LinkedList<Entity>>();
		
		Matrix4 projection = new Matrix4();
        projection.setToOrtho(0, Engine.width, Engine.height, 0, -1, 1);
		spriteBatch = new SpriteBatch();
		spriteBatch.setProjectionMatrix(projection);
	}
	
	@Override
	public void dispose()
	{
		spriteBatch.dispose();
		Iterator<Entity> it = updateList.iterator();
		while (it.hasNext())
		{
			Entity e = it.next();
			e.dispose();
		}
	}
	
	public void add(Entity e)
	{
		if (e.hasScene()) return;
		e.setScene(this);
		added.add(e);
	}
	
	public void add(Entity[] e)
	{
		for (int i = 0; i < e.length; i++)
			add(e);
	}
	
	public void remove(Entity e)
	{
		removed.add(e);
	}
	
	public void remove(Entity[] e)
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
	
	private void addType(Entity e)
	{
		LinkedList<Entity> list;
		if (typeList.containsKey(e.type))
		{
			list = typeList.get(e.type);
		}
		else
		{
			list = new LinkedList<Entity>();
			typeList.put(e.type, list);
		}
		list.add(e);
	}
	
	private void removeType(Entity e)
	{
		if (typeList.containsKey(e.type))
		{
			typeList.get(e.type).remove(e);
		}
	}
	
	private void updateLists()
	{
		Object[] list;
		
		// add any new entities
		if (added.size() > 0)
		{
			// copy list to array for traversal
			list = added.toArray();
			added.clear();
			for (int i = 0; i < list.length; i++)
			{
				Entity e = (Entity) list[i];
				updateList.add(e);
				renderList.add(e);
				addType(e);
				e.added();
			}
		}
		
		// remove any old entities
		if (removed.size() > 0)
		{
			// copy list to array for traversal
			list = removed.toArray();
			removed.clear();
			for (int i = 0; i < list.length; i++)
			{
				Entity e = (Entity) list[i];
				updateList.remove(e);
				renderList.remove(e);
				removeType(e);
				e.removed();
			}
		}
		
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
					it2.remove();
					addType(e);
				}
			}
			// remove an empty list
			if (list.size() == 0)
			{
				typeList.remove(key);
			}
		}
	}
	
	/**
	 * Class that sorts entities by z-index
	 */
	private static class EntityZSort implements Comparator<Entity>
	{
		
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

	private Matrix4 matrix = new Matrix4();
	private SpriteBatch spriteBatch;
	
	private LinkedList<Entity> added;
	private LinkedList<Entity> removed;
	private LinkedList<Entity> renderList;
	private LinkedList<Entity> updateList;
	private HashMap<String, LinkedList<Entity>> typeList;
	
}
