package com.flume2d.tween.easing;

public class EaseInExpo implements IEasing
{

	@Override
	public double tick(double t, double b, double c, double d)
	{
		return c * Math.pow(2, 10 * (t/d - 1)) + b;
	}

}
