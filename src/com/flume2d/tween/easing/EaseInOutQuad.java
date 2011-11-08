package com.flume2d.tween.easing;

public class EaseInOutQuad implements IEasing
{

	@Override
	public double tick(double t, double b, double c, double d)
	{
		if ((t/=d/2) < 1) return c/2*t*t + b;
		return -c/2 * ((--t)*(t-2) - 1) + b;
	}

}
