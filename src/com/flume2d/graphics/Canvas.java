package com.flume2d.graphics;

import java.awt.*;
import java.awt.image.*;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Canvas implements Graphic
{
	
	public Canvas(int width, int height)
	{
		this(width, height, false);
	}
	
	public Canvas(int width, int height, boolean clearOnRender)
	{
		canvas = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		this.clearOnRender = clearOnRender;
	}
	
	public void moveTo(int x, int y)
	{
		posx = x;
		posy = y;
	}
	
	public void lineTo(int x, int y)
	{
		canvas.getGraphics().drawLine(posx, posy, x, y);
	}
	
	public void fillRect(int width, int height)
	{
		canvas.getGraphics().fillRect(posx, posy, width, height);
	}
	
	public void setColor(int rgb)
	{
		canvas.getGraphics().setColor(new Color(rgb));
	}
	
	public void clear()
	{
		canvas.getGraphics().clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
	}

	@Override
	public boolean isVisible()
	{
		return true;
	}

	@Override
	public void render(SpriteBatch b, int x, int y)
	{
//		b.draw(canvas, x, y, null);
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
	
	private BufferedImage canvas;
	private int posx;
	private int posy;
	private boolean clearOnRender;

}
