package com.kcx.jagar.packet;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import com.kcx.jagar.Cell;
import com.kcx.jagar.Game;

public class PacketC016UpdateCells
{
	public PacketC016UpdateCells(ByteBuffer b)
	{
		b.order(ByteOrder.LITTLE_ENDIAN);
		short destroy = b.getShort(1);
		int offset = 3;

		for(int i=0;i<destroy;i++)
		{
			for(Cell c : Game.cells)
			{
				if(c!=null)
				{
					if(c.id == b.getInt(offset+4))
					{
						c.x=-10000;
						System.out.println("Removing " + c.id + " <" + c.name + ">");
						break;
					}
				}
			}
			offset+=8;
		}
		
		try
		{
			offset = addCell(offset, b);
		}catch(IndexOutOfBoundsException e){}
		
		int destroyCells = b.getInt(offset);
		
		offset+=4;
		
		for(int i=0;i<destroyCells;i++)
		{
			for(Cell c : Game.cells)
			{
				System.out.println(b.getInt(offset));
				if(c!=null)
				{
					if(c.id == b.getInt(offset))
					{
						c.x=-10000;
						System.out.println("Removing " + c.id + " <" + c.name + ">");
						break;
					}
				}
			}
			offset+=4;
		}		
	}

	private int addCell(int offset, ByteBuffer b)
	{
		int cellID = b.getInt(offset);
		int x = b.getInt(offset+4);
		int y = b.getInt(offset+8);
		short size = b.getShort(offset+12);
		
		byte red = b.get(offset+14);
		byte green = b.get(offset+15);
		byte blue = b.get(offset+16);
		
		boolean flag = false;
		
		for(int i=0;i<Game.cellsNumber;i++)
		{
			Cell c = Game.cells[i];
			if(c != null)
			{
				if(c.id == cellID)
				{
					flag = true;
				}
			}
		}
		
		byte flags = b.get(offset+17);
		if(flags==1){offset+=0;}
		if(flags==2){offset+=8;}
		if(flags==3){offset+=16;}
		
		offset+=18;
		String name = "";
        while(b.getShort(offset)!=0)
        {
        	name += b.getChar(offset);
        	offset += 2;
        }
        
		if(!flag)
		{
			System.out.println("Adding new cell " + cellID + " <" + name + ">");
			Cell cell = new Cell(x, y, size, cellID, name);
			cell.setColor(red, green, blue);
			Game.addCell(cell);
			
			if(cellID==4)
			{
				Game.player = cell;
			}
		}else
		{
			for(Cell cell : Game.cells)		
			{
				if(cell!=null)
				{				
					if(cell.id == cellID)
					{
						cell.x = x;
						cell.y = y;
						cell.size = size;
						cell.setColor(red, green, blue);
					}
				}
			}
		}
		offset+=2;
		if(b.getInt(offset)!=0)
		{
			offset = addCell(offset, b);
		}
		return offset;
	}
}
