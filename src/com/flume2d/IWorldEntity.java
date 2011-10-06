package com.flume2d;

public interface IWorldEntity
{
	public void setWorld(World world);
	public boolean hasWorld();
	
	public void added();
	public void removed();
}
