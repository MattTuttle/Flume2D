package com.flume2d;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;
import com.flume2d.utils.InputHandler;

/**
 * The game engine singleton class
 * @author matt.tuttle
 */
public class Engine implements ApplicationListener
{
	public static int width;
	public static int height;
	public static double elapsed;
	
	public Scene scene;
	
	/**
	 * Constructs the engine
	 */
	public Engine()
	{
		scene = new Scene(); // empty world
	}
	
	/**
	 * Initializes the engine
	 * @param width the screen height
	 * @param height the screen width
	 */
	public void init(int width, int height)
	{
		init(width, height, 60);
	}

	/**
	 * Initializes the engine
	 * @param width the screen height
	 * @param height the screen width
	 * @param frameRate the rate at which the screen should refresh
	 */
	public void init(int width, int height, int frameRate)
	{
		Engine.width = width;
		Engine.height = height;
		this.frameRate = frameRate;
		setScreen(width, height);
	}
	
	/**
	 * Sets the screen dimensions
	 * @param width screen width
	 * @param height screen height
	 */
	public void setScreen(int width, int height)
	{
		screenWidth = width;
		screenHeight = height;
	}

	public synchronized void run()
	{
		int frames = 0;
		
		double unprocessedSeconds = 0;
		long lastTime = System.nanoTime();
		double secondsPerTick = 1 / frameRate;
		int tickCount = 0;
		
		while (running)
		{
			long now = System.nanoTime();
			long passedTime = now - lastTime;
			lastTime = now;
			if (passedTime < 0) passedTime = 0;
			if (passedTime > 100000000) passedTime = 100000000;

			unprocessedSeconds += passedTime / 1000000000.0;

			boolean ticked = false;
			while (unprocessedSeconds > secondsPerTick) {
				Engine.elapsed = secondsPerTick;
				scene.update();
				unprocessedSeconds -= secondsPerTick;
				ticked = true;

				tickCount++;
				if (tickCount % frameRate == 0) {
					System.out.println(frames + " fps");
					lastTime += 1000;
					frames = 0;
				}
			}

			if (ticked)
			{
				render();
				frames++;
			}
			else
			{
				try
				{
					Thread.sleep(1);
				}
				catch (InterruptedException e)
				{
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public void create()
	{
		
	}

	@Override
	public void dispose()
	{
		
	}

	@Override
	public void pause()
	{
		running = false;
	}

	@Override
	public void render()
	{
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		Gdx.graphics.getGL10().glEnable(GL10.GL_TEXTURE_2D);
		scene.render();
	}

	@Override
	public void resize(int width, int height)
	{
		setScreen(width, height);
	}

	@Override
	public void resume()
	{
		running = true;
	}
	
	private int screenWidth;
	private int screenHeight;
	private float frameRate;
	
	private boolean running;

}
