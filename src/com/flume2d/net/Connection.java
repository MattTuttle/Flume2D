package com.flume2d.net;

import java.io.*;
import java.net.*;

public class Connection implements Closeable
{
	
	public Connection(int protocolId, float timeout)
	{
		running = false;
		this.timeout = timeout;
		this.protocolId = protocolId;
		
		mode = Mode.None;
		state = State.Disconnected;
		address = null;
	}
	
	public boolean open(int port)
	{
		return open(port, false);
	}

	public boolean open(int port, boolean block)
	{
		assert( !running );
		System.out.println( "start connection on port " + port );
		try
		{
			socket = new DatagramSocket( port );
			if (!block)
				socket.setSoTimeout( 10 );
		}
		catch (SocketException e)
		{
			e.printStackTrace();
			return false;
		}
		running = true;
		return true;
	}
	
	@Override
	public void close()
	{
		assert( running );
		System.out.println( "closing socket" );
		running = false;
		socket.close();
	}
	
	public void listen()
	{
		System.out.println( "server listening for connection" );
		mode = Mode.Server;
		state = State.Listening;
	}
	
	public void connect(InetSocketAddress address)
	{
		mode = Mode.Client;
		state = State.Connecting;
		this.address = address;
	}
	
	public boolean isConnected()
	{
		return (state == State.Connected);
	}
	
	public boolean isConnecting()
	{
		return (state == State.Connecting);
	}
	
	public boolean isListening()
	{
		return (state == State.Listening);
	}
	
	public void update( float deltaTime )
	{
		assert( running );
		timeoutAccumulator += deltaTime;
		if ( timeoutAccumulator > timeout )
		{
			if ( state == State.Connected || state == State.Connecting )
			{
				state = State.Disconnected;
			}
		}
	}
	
	public int receivePacket(byte[] data)
	{
		DatagramPacket packet = new DatagramPacket(data, data.length);
		try {
			socket.receive(packet);
		} catch (IOException e) {
			return 0;
		}
		
		int len = packet.getLength() - 4; 
		if (len <= 0)
			return 0;
		
		// check packet header for protocolId
		if ( data[0] != (byte) ( protocolId >> 24 ) || 
			 data[1] != (byte) ( ( protocolId >> 16 ) & 0xFF ) ||
			 data[2] != (byte) ( ( protocolId >> 8 ) & 0xFF ) ||
			 data[3] != (byte) ( protocolId & 0xFF ) )
			return 0;
		
		if ( mode == Mode.Server && state != State.Connected )
		{
			state = State.Connected;
			address = (InetSocketAddress) packet.getSocketAddress();
		}
		if ( address.equals( (InetSocketAddress) packet.getSocketAddress() ) )
		{
			if ( mode == Mode.Client && state == State.Connecting )
			{
				state = State.Connected;
			}
			timeoutAccumulator = 0.0f;
			System.arraycopy(data, 4, data, 0, len);
			return len;
		}
		return 0;
	}
	
	public void sendPacket(byte[] data)
	{
		if (address == null) return;
		
		// add protocolId to packet header
		byte[] packetData = new byte[data.length + 4];
		System.arraycopy(data, 0, packetData, 4, data.length);
		packetData[0] = (byte) ( protocolId >> 24 );
		packetData[1] = (byte) ( ( protocolId >> 16 ) & 0xFF );
		packetData[2] = (byte) ( ( protocolId >> 8 ) & 0xFF );
		packetData[3] = (byte) ( ( protocolId ) & 0xFF );
		
		try
		{
			DatagramPacket packet = new DatagramPacket(packetData, packetData.length, address);
			socket.send(packet);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	enum State
	{
		Disconnected,
		Listening,
		Connecting,
		Connected
	};
	
	enum Mode
	{
		None,
		Server,
		Client
	};
	
	private DatagramSocket socket;
	private InetSocketAddress address;
	
	private int protocolId;
	
	private boolean running;
	private Mode mode;
	private State state;
	
	private float timeoutAccumulator = 0;
	private float timeout;

}