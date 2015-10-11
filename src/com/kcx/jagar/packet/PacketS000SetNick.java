package com.kcx.jagar.packet;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import com.kcx.jagar.Game;

public class PacketS000SetNick
{
	public String name = "JAgar";
	
	public PacketS000SetNick(String name)
	{
		this.name = name;
	}
	
	public byte[] nameBytes()
	{
	    byte[] bytes = new byte[this.name.toCharArray().length*2];
	    ByteBuffer buffer = ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN);
	    for(char c : this.name.toCharArray())
	    	buffer.putChar(c);
	    return bytes;
	}
	
	public void write() throws IOException
	{
		ByteBuffer buffer = ByteBuffer.allocate(this.name.toCharArray().length*2+1);
		buffer.put(0, (byte)0);
		
		byte[] bytes = nameBytes();
		
		int offset = 1;
		
		for(byte b : bytes)
		{
			buffer.put(offset, b);
			offset++;
		}
		
		Game.socket.session.getRemote().sendBytes(buffer);
	}
}
