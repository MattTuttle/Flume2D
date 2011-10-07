package com.flume2d.audio;

import javax.sound.sampled.*;

public class Sound
{
	
	private Clip clip;
	private AudioListener listener;
	
	public Sound(String filename)
	{
		try {
			AudioInputStream ais = AudioSystem.getAudioInputStream(this.getClass().getResource(filename));
			clip = AudioSystem.getClip();
			listener = new AudioListener();
			clip.addLineListener(listener);
			clip.open(ais);
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	
	public void play()
	{
		if (clip == null) return;
		try
		{
			clip.start();
			listener.waitUntilDone();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			clip.close();
		}
	}
	
	public void stop()
	{
		clip.stop();
	}
}
