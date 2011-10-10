package com.flume2d.graphics;

public interface Graphic
{
	public boolean isVisible();
	public void render(int x, int y);
	
	public boolean isActive();
	public void update();
}
