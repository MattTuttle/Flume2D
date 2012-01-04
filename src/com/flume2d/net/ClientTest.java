package com.flume2d.net;

import java.net.*;
import java.nio.ByteBuffer;
import java.util.Enumeration;

public class ClientTest extends Connection
{

	public ClientTest() throws UnknownHostException
	{
		super(0xCADD6A3E, 0.1f);
		open(6001);
		connect(new InetSocketAddress(InetAddress.getLocalHost(), 6000));
	}
	
	public void GetPublicHostname() throws SocketException
	{
		Enumeration<NetworkInterface> ifaces = NetworkInterface.getNetworkInterfaces();
		while ( ifaces.hasMoreElements() )
		{
			NetworkInterface iface = ifaces.nextElement();
			System.out.println("Interface: " + iface.getDisplayName());
			Enumeration<InetAddress> ips = iface.getInetAddresses();
			while ( ips.hasMoreElements() )
			{
				InetAddress ia = ips.nextElement();
				System.out.println(ia.getCanonicalHostName() + "\t" + ia.getHostAddress());
			}
		}
	}
	
	public void register()
	{
		byte api = 1;
		byte gameType = 1;
		int ipAddress = 0x00000000;
		byte flags = 0x00;
		byte numPlayers = 1;
		byte maxPlayers = 8;
		String title = "What a wonderful world";
		
		int len = 13 + title.length() * 2;
		ByteBuffer data = ByteBuffer.allocate(len);
		data.put(api); // register api call
		data.put(gameType);
		data.putInt(ipAddress);
		data.put(flags);
		data.put(numPlayers);
		data.put(maxPlayers);
		data.putInt(title.length());
		for (int i = 0; i < title.length(); i++)
			data.putChar(title.charAt(i));
		
		sendPacket(data.array());
	}
	
	public static void main(String[] args)
	{
		try {
			ClientTest test = new ClientTest();
			test.register();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}
	
}
