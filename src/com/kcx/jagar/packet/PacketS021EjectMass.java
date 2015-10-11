package com.kcx.jagar.packet;

import java.io.IOException;
import java.nio.ByteBuffer;

import com.kcx.jagar.Game;

public class PacketS021EjectMass
{
	public PacketS021EjectMass() {}
	
	public void write() throws IOException
	{
		ByteBuffer buffer = ByteBuffer.allocate(1);
		buffer.put(0, (byte)21);
		Game.socket.session.getRemote().sendBytes(buffer);
	}
}
