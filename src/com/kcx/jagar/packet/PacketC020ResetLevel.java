package com.kcx.jagar.packet;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import com.kcx.jagar.Cell;
import com.kcx.jagar.Game;

public class PacketC020ResetLevel
{
	public PacketC020ResetLevel(ByteBuffer b)
	{
		b.order(ByteOrder.LITTLE_ENDIAN);
		Game.player.clear();
		Game.cells = new Cell[Game.cells.length];
		System.out.println("Reseting level.");
	}
}
