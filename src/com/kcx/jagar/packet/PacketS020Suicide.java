package com.kcx.jagar.packet;

import java.io.IOException;
import java.nio.ByteBuffer;

import com.kcx.jagar.Game;

public class PacketS020Suicide
{
	public PacketS020Suicide() {}
	
	public void write() throws IOException
	{
		ByteBuffer buffer = ByteBuffer.allocate(1);
		buffer.put(0, (byte)20);
		Game.socket.session.getRemote().sendBytes(buffer);
	}
}
