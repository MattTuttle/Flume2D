package com.flume2d.utils;

import java.util.HashMap;

import com.badlogic.gdx.InputProcessor;

public class Input implements InputProcessor
{
	private static boolean[] keys = new boolean[256];
	private static HashMap<String, int[]> keymap = new HashMap<String, int[]>();
	
	public static void add(String key, int[] values)
	{
		keymap.put(key, values);
	}
	
	public static boolean check(String key)
	{
		int[] values = keymap.get(key);
		if (values != null)
		{
			for (int i = 0; i < values.length; i++)
			{
				if (keys[values[i]])
				{
					return true;
				}
			}
		}
		return false;
	}
	
	public static boolean pressed(String key)
	{
		return false;
	}
	
	public static boolean pressed(int key)
	{
		return keys[key];
	}
	
	public static boolean released(String key)
	{
		int[] values = keymap.get(key);
		if (values != null)
		{
			for (int i = 0; i < values.length; i++)
			{
				if (keys[values[i]])
				{
					return false;
				}
			}
		}
		return true;
	}
	
	public static boolean released(int key)
	{
		return !keys[key];
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
		return false;
	}

	@Override
	public boolean touchDown(int x, int y, int pointer, int button)
	{
		return false;
	}

	@Override
	public boolean touchDragged(int x, int y, int pointer)
	{
		return false;
	}

	@Override
	public boolean touchMoved(int x, int y)
	{
		return false;
	}

	@Override
	public boolean touchUp(int x, int y, int pointer, int button)
	{
		return false;
	}
	
}