package com.flume2d.net;

import java.net.*;
import java.nio.ByteBuffer;

public class Client extends UdpConnection
{

	public Client(InetAddress ip, int port)
	{
		super(ip, port);
	}

	@Override
	protected void parseData(ByteStream data, SocketAddress address)
	{
		serverAddr = address;
		
		int newSequence = data.readInt();
		if (newSequence < lastSequence)
		{
		   //discard packet
		}
		else if (newSequence > lastSequence)
		{
		   lastState.deltaUncompress(data);
		   lastSequence = newSequence;
		   ackServer(lastSequence);
		}
	}
	
	private void ackServer(int sequence)
	{
		ByteBuffer data = ByteBuffer.allocate(8);
		data.putInt(Protocol.ACK);
		data.putInt(sequence);
		sendData(data.array(), serverAddr);
	}

	public void update(IGameState state)
	{
		sendData(state.deltaCompress(lastState), serverAddr);
	}
	
	private SocketAddress serverAddr;
	private IGameState lastState;
	private int lastSequence = 0;

}
