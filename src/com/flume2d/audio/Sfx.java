package com.flume2d.audio;

import com.badlogic.gdx.*;
import com.badlogic.gdx.audio.*;
import com.badlogic.gdx.files.FileHandle;

public class Sfx
{
	
	Sound sfx;
	Music music;
	
	public Sfx(String filename)
	{
		FileHandle handle = Gdx.files.internal(filename);
		String ext = handle.extension();
		
		if (ext.equalsIgnoreCase("mp3") ||
			ext.equalsIgnoreCase("wav") ||
			ext.equalsIgnoreCase("ogg"))
		{
			music = Gdx.audio.newMusic(handle);
		}
		else
		{
			sfx = Gdx.audio.newSound(handle);
		}
	}
	
	public void play(float volume)
	{
		if (sfx == null)
			music.play();
		else
			sfx.play(volume);
	}
	
}
