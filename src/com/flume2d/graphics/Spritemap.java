package com.flume2d.graphics;

import java.util.*;

import com.flume2d.*;

public class Spritemap extends Image
{

	public Spritemap(String filename, int width, int height)
	{
		super(filename);
		frameWidth = width;
		frameHeight = height;
		cells = imageWidth / frameWidth;
		
		animations = new HashMap<String, Animation>();
		frameTime = 0;
	}
	
	public void add(String name, int[] frames)
	{
		add(name, frames, 0, true);
	}
	
	public void add(String name, int[] frames, float delay)
	{
		add(name, frames, delay, true);
	}
	
	public void add(String name, int[] list, float delay, boolean loop)
	{
		Animation a = new Animation();
		a.frames = list;
		a.delay = delay;
		a.loop = loop;
		a.name = name;
		animations.put(name, a);
	}
	
	public void play(String name)
	{
		play(name, false);
	}
	
	public void play(String name, boolean reset)
	{
		if (reset || (currentAnim != null && currentAnim.name != name))
		{
			if (currentAnim != null) currentAnim.frame = 0;
			frameTime = 0;
		}
		currentAnim = animations.get(name);
	}
	
	@Override
	public void update()
	{
		if (currentAnim != null)
		{
			// set the frame to display
			int index = currentAnim.frames[currentAnim.frame];
			frameX = index % cells * frameWidth;
			frameY = index / cells * frameWidth;
			
			// check if the frame can advance
			frameTime += Engine.elapsed;
			if (currentAnim.delay > 0 && frameTime > currentAnim.delay)
				advance();
		}
	}
	
	public void advance()
	{
		currentAnim.frame += 1;
		if (currentAnim.frame >= currentAnim.frames.length)
			currentAnim.frame = 0;
		frameTime = 0;
	}
	
	@Override public boolean isActive() { return true; }
	
	private HashMap<String, Animation> animations;
	private Animation currentAnim;
	private double frameTime;
	private int cells; // horizontal cells

}
