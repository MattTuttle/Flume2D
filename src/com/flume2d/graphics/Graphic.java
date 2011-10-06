package com.flume2d.graphics;

import java.awt.Graphics;

public interface Graphic
{
	public boolean isVisible();
	public void render(Graphics g, int x, int y);
	
	public boolean isActive();
	public void update();
}
