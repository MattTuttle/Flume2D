package com.flume2d.audio;

import javax.sound.sampled.*;
import javax.sound.sampled.LineEvent.Type;

public class AudioListener implements LineListener
{
	
	private boolean done = false;

	@Override
	public synchronized void update(LineEvent event)
	{
		Type eventType = event.getType();
		if (eventType == Type.STOP || eventType == Type.CLOSE)
		{
			done = true;
			notifyAll();
		}
	}
	
	public synchronized void waitUntilDone() throws InterruptedException
	{
		while (!done) { wait(); }
	}

}
