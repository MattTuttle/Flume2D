package com.flume2d.tween.easing;

public class EaseOutCubic implements IEasing
{

	@Override
	public double tick(double t, double b, double c, double d)
	{
		return c * (Math.pow (t/d-1, 3) + 1) + b;
	}

}
