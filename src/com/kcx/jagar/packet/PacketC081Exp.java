package com.kcx.jagar.packet;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import com.kcx.jagar.Game;

public class PacketC081Exp
{
	public PacketC081Exp(ByteBuffer b)
	{
		b.order(ByteOrder.LITTLE_ENDIAN);
		
		Game.level = b.getInt(1);
		Game.exp = b.getInt(5);
		Game.maxExp = b.getInt(9);
		
		System.out.println("Level "+Game.level+" ("+Game.exp+"/"+Game.maxExp+")");
	}
}
