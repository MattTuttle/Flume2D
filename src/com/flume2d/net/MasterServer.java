package com.flume2d.net;

import java.nio.ByteBuffer;
import java.util.*;

public class MasterServer extends Connection
{
	
	private class Game
	{
		int  ipAddress;
		byte flags;
		byte numPlayers;
		byte maxPlayers;
		char[] title;
		
		public boolean checkFlag(byte flag)
		{
			return (flags | flag) == 0;
		}
	}
	
	private static final int API_REGISTER = 1;
	private static final int API_LIST = 2;
	private static final int API_ACK = 3;
	
	private static final byte FLAG_PRIVATE = 0x01;
	
	public MasterServer(int port)
	{
		super(0xCADD6A3E, 0.1f);
		open(port, true);
		listen();
		
		// TODO: move to non-volatile storage
		games = new HashMap<Integer, LinkedList<Game>>();
	}
	
	public void update()
	{
		while ( true )
		{
			byte packet[] = new byte[256];
			if ( receivePacket( packet ) == 0 )
				break;
			
			ByteBuffer buffer = ByteBuffer.wrap(packet);
			int type = buffer.get();
			switch(type)
			{
				case API_REGISTER:
					registerGame(buffer);
					break;
				case API_LIST:
					listGames(buffer);
					break;
				case API_ACK:
					break;
				default:
					log("Unknown request");
			}
		}
		
		// TODO: remove servers that don't update for a minute or so
	}
	
	private void log(String message)
	{
		// TODO: store log messages in a file
		System.out.println(message);
	}
	
	private void listGames(ByteBuffer buffer)
	{
		int gameType = buffer.get();
		
		if ( games.containsKey(gameType) )
		{
			LinkedList<Game> list = games.get(gameType);
			Iterator<Game> it = list.iterator();
			while ( it.hasNext() )
			{
				Game game = it.next();
				
				if ( !game.checkFlag( FLAG_PRIVATE ) )
				{
					ByteBuffer response = ByteBuffer.allocate(8);
					
					response.putInt( game.ipAddress );
					response.put( game.numPlayers );
					response.put( game.maxPlayers );
					
					// put the title
					response.putInt( game.title.length );
					for (int i = 0; i < game.title.length; i++)
						response.putChar( game.title[i] );
					
					sendPacket( response.array() );
				}
			}
		}
	}

	private void registerGame(ByteBuffer buffer)
	{
		int gameType = buffer.get();
		
		Game game = new Game();
		game.ipAddress  = buffer.getInt();
		game.flags      = buffer.get();
		game.numPlayers = buffer.get();
		game.maxPlayers = buffer.get();
		
		int len = buffer.getInt();
		game.title = new char[len];
		for (int i = 0; i < len; i++)
			game.title[i] = buffer.getChar();
		
		// store data
		if (!games.containsKey(gameType))
			games.put(gameType, new LinkedList<Game>());
		
		LinkedList<Game> list = games.get(gameType);
		list.add(game);
		int gameId = list.size();
		
		// send response
		ByteBuffer response = ByteBuffer.allocate(4);
		response.putInt(gameId);
		sendPacket(response.array());
	}
	
	public static void main(String[] args)
	{
		MasterServer server = new MasterServer(6000);
		while ( true )
		{
			server.update();
		}
	}
	
	private HashMap<Integer, LinkedList<Game>> games;

}
