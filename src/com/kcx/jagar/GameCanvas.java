package com.kcx.jagar;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.ConcurrentModificationException;

import javax.swing.JPanel;

public class GameCanvas extends JPanel
{
	private static final long serialVersionUID = 5570080027060608254L;
	private Image screen;

	public GameCanvas()
	{
		screen = new BufferedImage(GameFrame.size.width, GameFrame.size.height, BufferedImage.TYPE_INT_ARGB);
		setSize(GameFrame.size);
		setVisible(true);
	}
	
	public void render()
	{
		Graphics ggg = screen.getGraphics();
		Graphics2D g = ((Graphics2D)ggg);
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, GameFrame.size.width, GameFrame.size.height);		
		
		try
		{		
			for(Cell cell : Game.cells)
			{
				cell.render(g);
			}
		}catch(ConcurrentModificationException e){System.err.println("ERR > TODO: fix this");}
		
		g.dispose();
		
		Graphics gg = this.getGraphics();
		gg.drawImage(screen, 0, 0, null);
		gg.dispose();
	}
}
