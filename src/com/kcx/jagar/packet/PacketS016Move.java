package com.kcx.jagar.packet;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.eclipse.jetty.websocket.api.Session;

import com.kcx.jagar.Game;

public class PacketS016Move
{
	public float x;
	public float y;
	
	public PacketS016Move(float x, float y)
	{
		this.x = x;
		this.y = y;
	}
	
	public static byte[] toByteArray(float value)
	{
	    byte[] bytes = new byte[4];
	    ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN).putInt((int)value);
	    return bytes;
	}
	
	public void write(Session s) throws IOException
	{
		ByteBuffer buffer = ByteBuffer.allocate(13);
		buffer.put(0,(byte)16);
		
		buffer.put(1,toByteArray(x)[0]);
		buffer.put(2,toByteArray(x)[1]);
		buffer.put(3,toByteArray(x)[2]);
		buffer.put(4,toByteArray(x)[3]);
		
		buffer.put(5,toByteArray(y)[0]);
		buffer.put(6,toByteArray(y)[1]);
		buffer.put(7,toByteArray(y)[2]);
		buffer.put(8,toByteArray(y)[3]);
		
		buffer.putInt(9, 0);
		s.getRemote().sendBytes(buffer);
	}
}
