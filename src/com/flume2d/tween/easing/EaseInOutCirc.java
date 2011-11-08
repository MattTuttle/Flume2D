package com.flume2d.tween.easing;

public class EaseInOutCirc implements IEasing
{

	@Override
	public double tick(double t, double b, double c, double d)
	{
		if ((t/=d/2) < 1) return c/2 * (1 - Math.sqrt(1 - t*t)) + b;
		return c/2 * (Math.sqrt(1 - (t-=2)*t) + 1) + b;
	}

}
