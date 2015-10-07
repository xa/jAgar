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
		g.setColor(new Color(20,20,20));
		g.fillRect(0, 0, GameFrame.size.width, GameFrame.size.height);		

		g.setColor(new Color(80,80,80));
		
		int pX = 0, pY = 0;
		
/*		if(Game.player != null)
		{
			pX = (int)(Game.player.xRender*Game.zoom);
			pY = (int)(Game.player.yRender*Game.zoom);
		
			for(double i=Game.minSizeX;i<Game.maxSizeX;i+=100*Game.zoom)
			{				
				double x = i + 100 / Game.zoom;
				g.drawLine((int)x-pX,(int)Game.minSizeY-pY,(int)x-pX,(int)Game.maxSizeY-pY);
			}
			for(double i=Game.minSizeY;i<Game.maxSizeY;i+=100*Game.zoom)
			{
				double y = i + 100 / Game.zoom;
				g.drawLine((int)Game.minSizeX-pX,(int)y-pY,(int)Game.maxSizeX-pX,(int)y-pY);
			}			
		}*/

		try
		{		
			for(Cell cell : Game.cells)
			{
				if(cell!=null)
				{
					cell.render(g);
				}
			}
		}catch(ConcurrentModificationException e){System.err.println("ERR > TODO: fix this");}
		
		g.setColor(Color.WHITE);
		
		int i=0;
		
		for(String s : Game.leaderboard)
		{
			if(s != null)
			{
				g.drawString(s, GameFrame.size.width-140, 15 + 13*i);
			}
			i++;
		}
		
		g.dispose();
		
		Graphics gg = this.getGraphics();
		gg.drawImage(screen, 0, 0, null);
		gg.dispose();
	}
}
