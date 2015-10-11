package com.kcx.jagar.packet;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import com.kcx.jagar.Cell;
import com.kcx.jagar.Game;

public class PacketC016UpdateCells
{
	public PacketC016UpdateCells(ByteBuffer b)
	{
		if(b==null)return;
		b.order(ByteOrder.LITTLE_ENDIAN);
		short destroy = b.getShort(1);
		int offset = 3;

		for(int i=0;i<destroy;i++)
		{
			for(int i2=0;i2<Game.cellsNumber;i2++)
			{
				Cell c = Game.cells[i2];
				if(c!=null)
				{
					if(c.id == b.getInt(offset+4))
					{
						Game.cells[i2] = null;
						if(Game.player.contains(c))
						{
							Game.player.remove(c);
						}
						System.out.println("Removing " + c.id + " <" + c.name + ">");
						break;
					}
				}
			}
			offset+=8;
		}		
		offset = addCell(offset, b);
						
		offset+=4;
		
		int destroyCells = b.getInt(offset);

		offset+=4;
		
		for(int i=0;i<destroyCells;i++)
		{
			for(int i2=0;i2<Game.cellsNumber;i2++)
			{
				Cell c = Game.cells[i2];
				if(c!=null)
				{
					if(c.id == b.getInt(offset))
					{
						Game.cells[i2] = null;
						if(Game.player.contains(c))
						{
							Game.player.remove(c);
						}
						System.out.println("Removing(2) " + c.id + " <" + c.name + ">");
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
		if(cellID == 0)return offset;
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
		boolean virus = (flags & 1)==1;

        if ((flags&2)==1)
        {
            offset += 4;
        }
        if ((flags&4)==1)
        {
            offset += 8;
        }
        if ((flags&8)==1)
        {
            offset += 16;
        }		
        
		offset+=18;
		String name = "";
        while(b.getShort(offset)!=0)
        {
        	name += b.getChar(offset);
        	offset += 2;
        }
        
		if(!flag)
		{
			System.out.println("Adding new cell " + cellID + " <" + name + ">" + " /"+Game.cellsNumber+"/");
			Cell cell = new Cell(x, y, size, cellID);
			if(name.length()>0)
			{
				Game.cellNames.put(cellID, name);
			}
			cell.setColor(red, green, blue);
			cell.virus = virus;			
			Game.addCell(cell);
			cell.tick();
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
						if(name.length()>0)
						{
							cell.name = name;
							Game.cellNames.put(cellID, name);
						}						
						cell.virus = virus;
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
