package com.kcx.jagar.packet;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import com.kcx.jagar.Cell;
import com.kcx.jagar.Game;

public class PacketC016UpdateCells
{
	public PacketC016UpdateCells(ByteBuffer b)
	{
		try
		{
	    b.order(ByteOrder.LITTLE_ENDIAN);
		byte packetId = b.get(0);
		short destroy = b.getShort(1);
		int offset = 3;
		offset+=0;
		int cellID = b.getInt(offset);
		int x = b.getInt(offset+4);
		int y = b.getInt(offset+8);
		short size = b.getShort(offset+12);
		
		boolean flag = false;
		
		for(Cell c : Game.cells)
		{
			if(c.id == cellID)
			{
				flag = true;
			}		
		}	

		if(!flag)
		{
			System.out.println("Adding new cell "+cellID);
			Cell cell = new Cell(x, y, size, cellID);
			Game.cells.add(cell);
			
			if(cellID==4)
			{
				Game.player = cell;
			}
		}else
		{
			for(Cell cell : Game.cells)
			{
				if(cell.id == cellID)
				{
					cell.x = x;
					cell.y = y;
					cell.size = size;
				}
			}
		}
		
		System.out.println("   x:"+x);
		System.out.println("   y:"+y);
		System.out.println("   size:"+size);
		}catch(IndexOutOfBoundsException e){}
	}
}
