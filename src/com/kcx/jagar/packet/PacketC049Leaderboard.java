package com.kcx.jagar.packet;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import com.kcx.jagar.Game;

public class PacketC049Leaderboard
{
	public PacketC049Leaderboard(ByteBuffer b)
	{
		b.order(ByteOrder.LITTLE_ENDIAN);
		int leaderboardSize = b.getInt(1);
		
		Game.leaderboard = new String[leaderboardSize];
		
		int offset = 9;
		
		for(int i=0;i<leaderboardSize;i++)
		{
			String nick = "";
							
	        while(b.getShort(offset)!=0)
	        {
	        	nick += b.getChar(offset);
	        	offset += 2;
	        }
	        
	        offset+=6;
	        
			Game.leaderboard[i] = (i+1)+". "+nick;
		}
	}
}
