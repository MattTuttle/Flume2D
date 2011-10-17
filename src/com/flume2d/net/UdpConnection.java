package com.flume2d.net;

import java.io.*;
import java.net.*;

public abstract class UdpConnection implements Closeable
{

	private class Reader extends Thread
	{
		/**
		 * Sets the reader as a daemon.
		 */
		private Reader()
		{
			super("UDP Reader");
			setDaemon(true);
		}

		/**
		 * Reads data until the socket is closed
		 */
		@Override
		public void run()
		{
			connected = true;
			try {
				while (connected) {
					readData(getSocket());
				}
			} catch (SocketException e) {
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (socket != null) {
					socket.close();
				}
				reader = null;
				socket = null;
			}
		}
		
		private void readData(DatagramSocket sock) throws IOException
		{
			byte[] buff = new byte[256];
			DatagramPacket packet = new DatagramPacket(buff, buff.length);
			sock.receive(packet);
			ByteStream stream = new ByteStream(packet.getData());
			parseData(stream, packet.getSocketAddress());
		}
	}
	
	public UdpConnection(InetAddress ip, int port)
	{
		this(new InetSocketAddress(ip, port));
	}

	public UdpConnection(InetSocketAddress address)
	{
		connected = false;
		reader = new Reader();
		reader.start();
	}
	
	protected boolean sequenceMoreRecent(int s1, int s2, int max)
    {
        return (s1 > s2) && (s1 - s2 <= max / 2) ||
               (s2 > s1) && (s2 - s1 >  max / 2);
    }
	
	private DatagramSocket getSocket() throws SocketException
	{
		if (socket == null)
		{
			socket = new DatagramSocket();
		}
		return socket;
	}
	
	@Override
	public void close()
	{
		connected = false;
	}
	
	protected abstract void parseData(ByteStream data, SocketAddress inetAddress) throws IOException;
	
	protected void sendData(byte[] data, SocketAddress address)
	{
		DatagramPacket req;
		DatagramSocket sock;
		try
		{
			req = new DatagramPacket(data, data.length, address);
			sock = getSocket();
			sock.send(req);
		}
		catch (SocketException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	private DatagramSocket socket;
	private Reader reader;
	private boolean connected;

}