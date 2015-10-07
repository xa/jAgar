package com.kcx.jagar.packet;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import com.kcx.jagar.Game;

public class PacketC064MapSize
{
	public PacketC064MapSize(ByteBuffer b)
	{
		b.order(ByteOrder.LITTLE_ENDIAN);
		
		Game.minSizeX = b.getDouble(1);
		Game.minSizeY = b.getDouble(9);
		Game.maxSizeX = b.getDouble(17);
		Game.maxSizeY = b.getDouble(25);
		
		System.out.println("Map size is: ");
		System.out.println("  MinX: "+Game.minSizeX);
		System.out.println("  MinY: "+Game.minSizeY);
		System.out.println("  MaxX: "+Game.maxSizeX);
		System.out.println("  MaxY: "+Game.maxSizeY);
	}
}
