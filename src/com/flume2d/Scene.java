package com.flume2d;

import java.awt.Graphics;
import java.util.*;

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
	}
	
	public void add(ISceneEntity e)
	{
		if (e.hasWorld()) return;
		added.add(e);
		e.setWorld(this);
	}
	
	public void remove(ISceneEntity e)
	{
		removed.add(e);
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
		Graphics g = Engine.getInstance().getGraphicsContext();
		Iterator<Entity> it = renderList.iterator();
		while (it.hasNext())
		{
			Entity e = it.next();
			e.render(g, Scene.camera.x, Scene.camera.y);
		}
		g.dispose();
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

	private LinkedList<ISceneEntity> added;
	private LinkedList<ISceneEntity> removed;
	private LinkedList<Entity> renderList;
	private LinkedList<Entity> updateList;
	private HashMap<String, LinkedList<Entity>> typeList;
	
}
