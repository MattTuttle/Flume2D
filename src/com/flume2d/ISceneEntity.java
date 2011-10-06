package com.flume2d;

public interface ISceneEntity
{
	public void setWorld(Scene world);
	public boolean hasWorld();
	
	public void added();
	public void removed();
}
