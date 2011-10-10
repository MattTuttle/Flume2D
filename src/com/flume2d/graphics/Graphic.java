package com.flume2d.graphics;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public interface Graphic
{
	public boolean isVisible();
	public void render(SpriteBatch b, int x, int y);
	
	public boolean isActive();
	public void update();
}
