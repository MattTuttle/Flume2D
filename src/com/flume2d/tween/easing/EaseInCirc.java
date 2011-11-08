package com.flume2d.tween.easing;

public class EaseInCirc implements IEasing
{

	@Override
	public double tick(double t, double b, double c, double d)
	{
		return c * (1 - Math.sqrt(1 - (t/=d)*t)) + b;
	}

}
