package com.flume2d.net;

public interface IGameState
{
	public byte[] deltaCompress(IGameState last);
	public void deltaUncompress(ByteStream stream);
}
