package com.kcx.jagar;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class MouseListenerr implements MouseListener
{
	@Override
	public void mousePressed(MouseEvent e)
	{
		Game.pressMouse(e.getX(), e.getY(), e.getButton());				
	}

	@Override
	public void mouseReleased(MouseEvent e)
	{
		Game.releaseMouse(e.getX(), e.getY(), e.getButton());		
	}
	
	@Override public void mouseClicked(MouseEvent e){}
	@Override public void mouseEntered(MouseEvent e) {}
	@Override public void mouseExited(MouseEvent e) {}
}
