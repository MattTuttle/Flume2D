package com.flume2d.graphics;

import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Canvas implements Graphic
{
	
	public Canvas(int width, int height)
	{
		this(width, height, false);
	}
	
	public Canvas(int width, int height, boolean clearOnRender)
	{
		canvas = new Pixmap(width, height, Pixmap.Format.RGB565);
		texture = new Texture(canvas);
		this.clearOnRender = clearOnRender;
	}
	
	public void moveTo(int x, int y)
	{
		posx = x;
		posy = y;
	}
	
	public void lineTo(int x, int y)
	{
		canvas.drawLine(posx, posy, x, y);
	}
	
	public void fillRect(int width, int height)
	{
		canvas.fillRectangle(posx, posy, width, height);
	}
	
	public void setColor(float r, float g, float b, float a)
	{
		canvas.setColor(r, g, b, a);
	}
	
	public void clear()
	{
//		canvas.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
	}

	@Override
	public boolean isVisible()
	{
		return true;
	}

	@Override
	public void render(SpriteBatch spriteBatch)
	{
		spriteBatch.draw(texture, 0, 0);
		if (clearOnRender) clear();
	}

	@Override
	public boolean isActive()
	{
		return false;
	}

	@Override
	public void update()
	{
	}
	
	@Override
	public void dispose()
	{
		texture.dispose();
		canvas.dispose();
	}
	
	private Pixmap canvas;
	private Texture texture;
	private int posx, posy;
	private boolean clearOnRender;

}
