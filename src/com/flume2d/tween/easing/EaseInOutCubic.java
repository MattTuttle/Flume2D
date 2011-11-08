package com.flume2d.tween.easing;

public class EaseInOutCubic implements IEasing
{

	@Override
	public double tick(double t, double b, double c, double d)
	{
		if ((t/=d/2) < 1) return c/2 * Math.pow (t, 3) + b;
		return c/2 * (Math.pow (t-2, 3) + 2) + b;
	}

}
