package com.flume2d.net;



public interface GameState
{
	public byte[] deltaCompress(GameState last);
	public void deltaUncompress(ByteStream stream);
}
