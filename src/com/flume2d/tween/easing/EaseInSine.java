package com.flume2d.tween.easing;

public class EaseInSine implements IEasing
{

	@Override
	public double tick(double t, double b, double c, double d)
	{
		return c * (1 - Math.cos(t/d * (Math.PI/2))) + b;
	}

}
