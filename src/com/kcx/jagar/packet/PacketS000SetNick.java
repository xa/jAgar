package com.kcx.jagar.packet;

import java.io.IOException;
import java.nio.ByteBuffer;

import org.eclipse.jetty.websocket.api.Session;

public class PacketS000SetNick extends Packet
{
	public String name = "JAgar";
	
	public PacketS000SetNick(String name)
	{
		this.name = name;
	}
	
	public void write(Session session) throws IOException
	{
		ByteBuffer buffer = ByteBuffer.allocate(1+name.getBytes("UTF-16").length-2);
		buffer.put(0, (byte)0);
		int i=0;
		for(byte b : name.getBytes("UTF-16"))
		{
			if(i>2)
				buffer.put(i-2, (byte)b);
			i++;
		}
        session.getRemote().sendBytes(buffer);
	}
}
