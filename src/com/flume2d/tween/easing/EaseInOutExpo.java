package com.flume2d.tween.easing;

public class EaseInOutExpo implements IEasing
{

	@Override
	public double tick(double t, double b, double c, double d)
	{
		if ((t/=d/2) < 1) return c/2 * Math.pow(2, 10 * (t - 1)) + b;
		return c/2 * (-Math.pow(2, -10 * --t) + 2) + b;
	}

}
