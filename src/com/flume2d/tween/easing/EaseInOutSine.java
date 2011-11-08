package com.flume2d.tween.easing;

public class EaseInOutSine implements IEasing
{

	@Override
	public double tick(double t, double b, double c, double d)
	{
		return c/2 * (1 - Math.cos(Math.PI*t/d)) + b;
	}

}
