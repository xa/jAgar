package com.kcx.jagar.packet;

import java.io.IOException;
import java.nio.ByteBuffer;

import com.kcx.jagar.Game;

public class PacketS017Split
{
	public PacketS017Split() {}
	
	public void write() throws IOException
	{
		ByteBuffer buffer = ByteBuffer.allocate(1);
		buffer.put(0, (byte)17);
        Game.socket.session.getRemote().sendBytes(buffer);
	}
}
