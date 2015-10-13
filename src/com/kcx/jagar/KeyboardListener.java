package com.kcx.jagar;

import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;

import com.kcx.jagar.packet.PacketS017Split;
import com.kcx.jagar.packet.PacketS018QPress;
import com.kcx.jagar.packet.PacketS019QRelease;
import com.kcx.jagar.packet.PacketS020Suicide;
import com.kcx.jagar.packet.PacketS021EjectMass;

public class KeyboardListener implements KeyListener
{	
	@Override
	public void keyPressed(KeyEvent e)
	{
		try
		{
			if(Game.socket != null && Game.socket.session != null)
			{
				if(Game.socket.session.isOpen())
				{
					if(e.getKeyCode() == KeyEvent.VK_SPACE)
					{
						new PacketS017Split().write();
					}
					if(e.getKeyCode() == KeyEvent.VK_Q)
					{
						new PacketS018QPress().write();
					}
					if(e.getKeyCode() == KeyEvent.VK_E)
					{
						new PacketS020Suicide().write();
					}
					if(e.getKeyCode() == KeyEvent.VK_W)
					{
						new PacketS021EjectMass().write();
					}
					if(e.getKeyCode() == KeyEvent.VK_T)
					{
						Game.rapidEject = true;
					}
					if(e.getKeyCode() == KeyEvent.VK_R)
					{
						if(Game.player.size()==0 || Game.isPlaybacking)
						{
							Game.respawn();
						}
					}
				}				
			}
		}catch(IOException ioEx){ioEx.printStackTrace();}
	}
	
	@Override
	public void keyReleased(KeyEvent e)
	{
		try
		{
			if(Game.socket != null && Game.socket.session != null)
			{
				if(Game.socket.session.isOpen())
				{
					if(e.getKeyCode() == KeyEvent.VK_Q)
					{
						new PacketS019QRelease().write();
					}
					if(e.getKeyCode() == KeyEvent.VK_T)
					{
						Game.rapidEject = false;
					}
				}
			}
		}catch(IOException ioEx){ioEx.printStackTrace();}
	}
	
	@Override public void keyTyped(KeyEvent e){}
}
