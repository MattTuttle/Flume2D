package com.flume2d.net;

import java.nio.*;
import java.nio.charset.*;

public class ByteStream
{
	private ByteBuffer buffer;
	
	/**
	 * Sets up the stream with the given data
	 */
	public ByteStream(byte[] data) {
		this(ByteBuffer.wrap(data));
	}
	/**
	 * Sets up the stream with the given data
	 */
	public ByteStream(ByteBuffer buffer) {
		this.buffer = buffer;
		buffer.order(ByteOrder.LITTLE_ENDIAN);
	}

	/**
	 * Determines the position we are currently at within the data
	 */
	public int getPosition() {
		return buffer.position();
	}
	
	/**
	 * Reads the next byte in the data
	 */
	public int readByte() {
		return buffer.get();
	}
	
	/**
	 * Reads the next unsigned byte in the data
	 */
	public int readUnsignedByte() {
		int data = readByte();
		if (data < 0) {
			data += (Byte.MAX_VALUE + 1) * 2;
		}
		return data;
	}
	
	/**
	 * Reads the next float in the data
	 */
	public float readFloat() {
		return buffer.getFloat();
	}
	
	/**
	 * Reads the next integer in the data
	 * 
	 * @return  the integer
	 */
	public int readInt() {
		return buffer.getInt();
	}
	
	/**
	 * Reads the next short in the data
	 * 
	 * @return  the short
	 */
	public int readShort() {
		return buffer.getShort();
	}
	
	/**
	 * Reads the next String in the data
	 * 
	 * @return  the String
	 */
	public String readString() {
		byte[] buff = new byte[1400];
		int x;
		for (x = 0; x < buff.length && buffer.hasRemaining(); x++) {
			buff[x] = buffer.get();
			if (buff[x] == 0) {
				break;
			}
		}
		return new String(buff, 0, x, Charset.forName("UTF-8"));
	}
	
	/**
	 * @return  the entire data array
	 */
	@Override
	public String toString() {
		return buffer.toString();
	}
}
