package com.flume2d.net;

import java.net.*;
import java.util.*;

public class Server extends UdpConnection
{
	
	private class ClientConnection
	{

		public SocketAddress address;
		
		public ClientConnection(SocketAddress address)
		{
			this.address = address;
			states = new LinkedList<IGameState>();
			lastAck = -1;
		}
		
		public void handlePacket(int packetType, ByteStream stream)
		{
			switch (packetType)
			{
				case Protocol.DISCONNECT:
					clients.remove(this);
					break;
				case Protocol.ACK:
					lastAck = stream.readInt();
					break;
			}
		}
		
		public void sendPacket(IGameState state)
		{
			IGameState lastAckState = states.get(lastAck);
			sendData(state.deltaCompress(lastAckState), address);
		}
		
		private LinkedList<IGameState> states;
		private int lastAck;
		
	}

	public Server(InetAddress ip, int port)
	{
		super(ip, port);
		clients = new LinkedList<ClientConnection>();
	}

	@Override
	protected void parseData(ByteStream stream, SocketAddress address)
	{
		int packetType = stream.readInt();
		if (Protocol.CONNECT == packetType)
		{
			clients.add(new ClientConnection(address));
			return;
		}
		
		Iterator<ClientConnection> it = clients.iterator();
		while (it.hasNext())
		{
			ClientConnection client = it.next();
			if (client.address == address)
				client.handlePacket(packetType, stream);
		}
	}
	
	public void update(IGameState gameState)
	{
		// send unique delta state to each client
		Iterator<ClientConnection> it = clients.iterator();
		while (it.hasNext())
		{
			ClientConnection client = it.next();
			client.sendPacket(gameState);
		}
	}
	
	private LinkedList<ClientConnection> clients;
	
}
