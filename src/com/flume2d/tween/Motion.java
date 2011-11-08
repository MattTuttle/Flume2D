package com.flume2d.tween;

import com.flume2d.Engine;
import com.flume2d.tween.easing.*;

public class Motion
{
	
	private IEasing ease;
	private double begin;
	private double change;
	private double duration;
	private double time;
	private double value;
	
	public Motion(double begin, double end, double duration)
	{
		this(begin, end, duration, new Linear());
	}
	
	public Motion(double begin, double end, double duration, IEasing ease)
	{
		this.ease = ease;
		this.begin = begin;
		this.change = end - begin;
		this.duration = duration;
		this.time = 0;
		this.value = begin;
	}
	
	public double getValue()
	{
		return value;
	}
	
	public void update()
	{
		time += Engine.elapsed;
		value = ease.tick(time, begin, change, duration);
	}
	
}
