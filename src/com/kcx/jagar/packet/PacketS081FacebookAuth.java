package com.kcx.jagar.packet;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import com.kcx.jagar.Game;

public class PacketS081FacebookAuth
{
	public String token = "";
	
	public PacketS081FacebookAuth(String token)
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
	
	public void write() throws IOException
	{
		if(Game.socket.session.isOpen())
		{
			ByteBuffer buffer = ByteBuffer.allocate(this.token.getBytes("UTF-8").length+1);
			buffer.put(0, (byte)81);
			
			byte[] bytes = nameBytes();
			
			int offset = 1;
			
			for(byte b : bytes)
			{
				buffer.put(offset, b);
				offset++;
			}
			
			System.out.println("Sending fb token "+this.token);
			
			Game.socket.session.getRemote().sendBytes(buffer);
		}
	}
}
