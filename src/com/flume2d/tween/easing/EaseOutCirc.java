package com.flume2d.tween.easing;

public class EaseOutCirc implements IEasing
{

	@Override
	public double tick(double t, double b, double c, double d)
	{
		return c * Math.sqrt(1 - (t=t/d-1)*t) + b;
	}

}
