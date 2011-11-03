package com.flume2d;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;
import com.flume2d.input.Input;

/**
 * The game engine singleton class
 * @author matt.tuttle
 */
public class Engine implements ApplicationListener
{
	public static int width;
	public static int height;
	public static float elapsed = 0;
	
	public Scene scene;

	@Override
	public void create()
	{
		Engine.width = Gdx.graphics.getWidth();
		Engine.height = Gdx.graphics.getHeight();
		Gdx.input.setInputProcessor(new Input());
		running = true;
	}

	@Override
	public void dispose()
	{
		scene.destroy();
	}

	@Override
	public void pause()
	{
		running = false;
	}

	@Override
	public void render()
	{
		elapsed += Gdx.graphics.getDeltaTime();
		while(elapsed > frameRate)
		{
			scene.update();
			elapsed -= frameRate;
		}
		
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		scene.render();
	}

	@Override
	public void resize(int width, int height)
	{
	}

	@Override
	public void resume()
	{
		running = true;
	}
	
	public boolean isRunning()
	{
		return running;
	}
	
	private float frameRate = 1.0f / 60.0f;
	private boolean running;

}
