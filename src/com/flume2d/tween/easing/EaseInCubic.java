package com.flume2d.tween.easing;

public class EaseInCubic implements IEasing
{

	@Override
	public double tick(double t, double b, double c, double d)
	{
		return c * Math.pow (t/d, 3) + b;
	}

}
