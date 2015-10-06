package com.kcx.jagar.packet;

import java.io.IOException;
import java.nio.ByteBuffer;

import org.eclipse.jetty.websocket.api.Session;

public class PacketS016Move
{
	public double x;
	public double y;
	
	public PacketS016Move(double x, double y)
	{
		this.x = x;
		this.y = y;
	}
	
	public void write(Session session) throws IOException
	{
		ByteBuffer buffer = ByteBuffer.allocate(13);
		buffer.put(0, (byte)16);
		buffer.putFloat(1, 600f);
		buffer.putFloat(5, 600f);
		buffer.putInt(9, 0);
        session.getRemote().sendBytes(buffer);
	}
}
