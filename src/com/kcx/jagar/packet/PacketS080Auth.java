package com.kcx.jagar.packet;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.eclipse.jetty.websocket.api.Session;

public class PacketS080Auth extends Packet
{
	public String token = "";
	
	public PacketS080Auth(String token)
	{
		this.token = token;
	}
	
	public void write(Session session) throws IOException
	{
		/*ByteBuffer buffer = ByteBuffer.allocate(1+token.getBytes("UTF-16").length-2);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		buffer.put(0, (byte)80);
		
		int i=0;
		
		for(byte b : token.getBytes("UTF-16"))
		{
			if(i>2)
				buffer.put(i-2, (byte)b);
			i++;
		}
	    
        session.getRemote().sendBytes(buffer);*/
	}
}
