package com.flume2d;

public interface ISceneEntity
{
	public void setScene(Scene scene);
	public boolean hasScene();
	
	public void added();
	public void removed();
}
