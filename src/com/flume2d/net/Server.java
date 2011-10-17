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
			IGameState lastAckState = null;
			if (states.containsKey(lastAck))
				lastAckState = states.get(lastAck);
			sendData(state.deltaCompress(lastAckState), address);
		}
		
		private int lastAck;
		
	}

	public Server(InetAddress ip, int port)
	{
		super(ip, port);
		clients = new LinkedList<ClientConnection>();
		states = new HashMap<Integer, IGameState>();
		sequence = 0;
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
		states.put(sequence, gameState);
		// send unique delta state to each client
		Iterator<ClientConnection> it = clients.iterator();
		while (it.hasNext())
		{
			ClientConnection client = it.next();
			client.sendPacket(gameState);
		}
		sequence += 1;
	}
	
	private LinkedList<ClientConnection> clients;
	private HashMap<Integer, IGameState> states;
	private int sequence;
	
}
