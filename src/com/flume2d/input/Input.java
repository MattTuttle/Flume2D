package com.flume2d.input;

import java.util.*;

import com.badlogic.gdx.InputProcessor;

public class Input implements InputProcessor
{
	private static boolean[] keys = new boolean[256];
	private static int scrollAmount;
	public static HashMap<Integer, Touch> touches = new HashMap<Integer, Touch>();
	
	private static HashMap<String, int[]> keymap = new HashMap<String, int[]>();
	private static LinkedList<TypeWriter> writers = new LinkedList<TypeWriter>();
	
	public static void define(String key, int[] values)
	{
		keymap.put(key, values);
	}
	
	public static boolean check(String key)
	{
		int[] values = keymap.get(key);
		if ( values != null )
		{
			for ( int i = 0; i < values.length; i++ )
			{
				if ( keys[ values[ i ] ] )
				{
					return true;
				}
			}
		}
		return false;
	}
	
	public static boolean check(int key)
	{
		return keys[ key ];
	}
	
	public static void registerTypeWriter(TypeWriter writer)
	{
		writers.add(writer);
	}
	
	public static void unregisterTypeWriter(TypeWriter writer)
	{
		writers.remove(writer);
	}
	
	public static int getScrollAmount()
	{
		return scrollAmount;
	}

	@Override
	public boolean keyDown(int code)
	{
		if (code > 0 && code < keys.length)
		{
			keys[code] = true;
		}
		return true;
	}

	@Override
	public boolean keyTyped(char character)
	{
		Iterator<TypeWriter> it = writers.iterator();
		while (it.hasNext())
		{
			it.next().keyTyped(character);
		}
		return false;
	}

	@Override
	public boolean keyUp(int code)
	{
		if (code > 0 && code < keys.length)
		{
			keys[code] = false;
		}
		return true;
	}

	@Override
	public boolean scrolled(int amount)
	{
		scrollAmount = amount;
		return true;
	}

	@Override
	public boolean touchDown(int x, int y, int pointer, int button)
	{
		if (touches.containsKey(pointer))
		{
			Touch touch = touches.get(pointer);
			touch.x = x;
			touch.y = y;
			touch.button = button;
		}
		else
		{
			Touch touch = new Touch(x, y);
			touch.button = button;
			touches.put(pointer, touch);
		}
		return true;
	}

	@Override
	public boolean touchDragged(int x, int y, int pointer)
	{
		if (touches.containsKey(pointer))
		{
			Touch touch = touches.get(pointer);
			touch.x = x;
			touch.y = y;
		}
		return true;
	}

	@Override
	public boolean touchMoved(int x, int y)
	{
		if (touches.containsKey(0))
		{
			Touch touch = touches.get(0);
			touch.x = x;
			touch.y = y;
		}
		return true;
	}

	@Override
	public boolean touchUp(int x, int y, int pointer, int button)
	{
		if (touches.containsKey(pointer))
		{
			touches.remove(pointer);
		}
		return true;
	}
	
}