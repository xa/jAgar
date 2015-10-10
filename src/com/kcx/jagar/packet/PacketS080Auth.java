package com.kcx.jagar.packet;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
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
	
	public byte[] nameBytes()
	{
		byte[] charArray;
		try {
			charArray = this.token.getBytes("UTF-8");
		    byte[] bytes = new byte[charArray.length];
		    ByteBuffer buffer = ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN);
		    for(byte c : charArray)
		    	buffer.put(c);
		    return bytes;
		} catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
		}
	    return null;
	}
	
	public void write(Session session) throws IOException
	{
		ByteBuffer buffer = ByteBuffer.allocate(this.token.getBytes("UTF-8").length+1);
		buffer.put(0, (byte)80);
		
		byte[] bytes = nameBytes();
		
		int offset = 1;
		
		for(byte b : bytes)
		{
			buffer.put(offset, b);
			offset++;
		}
		
		System.out.println("Sending token "+this.token);
		
        session.getRemote().sendBytes(buffer);
	}
}
