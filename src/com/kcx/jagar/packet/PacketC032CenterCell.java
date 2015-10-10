package com.kcx.jagar.packet;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import com.kcx.jagar.Game;

public class PacketC032CenterCell
{
	public PacketC032CenterCell(ByteBuffer b)
	{
		b.order(ByteOrder.LITTLE_ENDIAN);
		Game.playerID.add(b.getInt(1));
		System.out.println("Centering cell "+Game.playerID);
	}
}
