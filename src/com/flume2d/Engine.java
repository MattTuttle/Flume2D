package com.flume2d;

import java.awt.*;
import java.awt.image.*;

import javax.imageio.ImageIO;

import com.flume2d.utils.Input;

/**
 * The game engine singleton class
 * @author matt.tuttle
 */
public class Engine extends Canvas implements Runnable
{
	public static int width;
	public static int height;
	public static double elapsed;
	
	public Scene world;
	
	/**
	 * Constructs the engine
	 */
	private Engine()
	{
		input = new Input();
		addKeyListener(input);
		addFocusListener(input);
		addMouseListener(input);
		addMouseMotionListener(input);
		
		world = new Scene(); // empty world
		
		emptyCursor = Toolkit.getDefaultToolkit().createCustomCursor(new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB), new Point(0, 0), "empty");
		defaultCursor = getCursor();
	}
	
	/**
	 * Get the engine's single isntance
	 * @return
	 */
	public static Engine getInstance()
	{
		if (instance == null)
		{
			instance = new Engine();
		}
		return instance;
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
		
		backBuffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
	}
	
	/**
	 * Get a buffered image
	 * @param filename the filename of the image to retreive
	 * @return the image requested
	 */
	public BufferedImage getImage(String filename)
	{
		BufferedImage image = null;
		try
		{
			image = ImageIO.read(this.getClass().getResource(filename));
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}
		return image;
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
		Dimension size = new Dimension(width, height);
		setSize(size);
		setPreferredSize(size);
	}
	
	private void update()
	{
		if (hasFocus())
		{
			world.update();
		}
	}
	
	private void render()
	{
		if (hadFocus != hasFocus())
		{
			hadFocus = !hadFocus;
//			setCursor(hadFocus ? emptyCursor : defaultCursor);
		}
		
		BufferStrategy bs = getBufferStrategy();
		if (bs == null)
		{
			createBufferStrategy(3);
			return;
		}
		
		// clear screen
		backBuffer.getGraphics().clearRect(0, 0, width, height);
		world.render();
		
		Graphics g = bs.getDrawGraphics();
		g.fillRect(0, 0, getWidth(), getHeight());
		g.drawImage(backBuffer, 0, 0, screenWidth, screenHeight, null);
		g.dispose();
		bs.show();
	}

	public synchronized void run()
	{
		int frames = 0;
		
		double unprocessedSeconds = 0;
		long lastTime = System.nanoTime();
		double secondsPerTick = 1 / frameRate;
		int tickCount = 0;
		
		requestFocus();
		
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
				update();
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
	
	public void start()
	{
		if (running) return;
		running = true;
		thread = new Thread(this);
		thread.start();
	}
	
	public void stop()
	{
		if (!running) return;
		running = false;
		try
		{
			thread.join();
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}
	
	public Graphics getGraphicsContext()
	{
		return backBuffer.getGraphics();
	}
	
	private int screenWidth;
	private int screenHeight;
	private float frameRate;
	
	private boolean running;
	private Thread thread;
	
	private boolean hadFocus = false;
	private Cursor emptyCursor, defaultCursor;
	private static BufferedImage backBuffer;
	private Input input;
	
	private static Engine instance;
	
	private static final long serialVersionUID = 1L;

}
