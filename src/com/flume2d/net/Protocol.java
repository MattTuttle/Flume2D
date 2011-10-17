package com.flume2d.net;

public enum Protocol
{
	ACK   (0x01, false),
	INPUT (0x03, true);
	
	Protocol(int id, boolean reliable)
	{
		this.id = id;
	}
	
	public int id;
}
