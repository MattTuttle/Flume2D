package com.flume2d.net;

import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.util.*;

import com.flume2d.net.*;

public class Server extends UdpConnection
{
	
	private class ClientConnection
	{

		public SocketAddress address;
		public GameState lastAckState;
		
		public void handleData(ByteStream stream)
		{
			
		}
		
	}

	public Server(InetAddress ip, int port)
	{
		super(ip, port);
		clients = new LinkedList<ClientConnection>();
	}

	@Override
	protected void parseData(ByteStream stream, SocketAddress address) throws IOException
	{
		Iterator<ClientConnection> it = clients.iterator();
		while (it.hasNext())
		{
			ClientConnection client = it.next();
			if (client.address == address)
				client.handleData(stream);
		}
	}
	
	public void update(GameState gameState)
	{
		// send unique delta state to each client
		Iterator<ClientConnection> it = clients.iterator();
		while (it.hasNext())
		{
			ClientConnection client = it.next();
			sendData(gameState.deltaCompress(client.lastAckState), client.address);
		}
	}
	
	private LinkedList<ClientConnection> clients;
	
}
