package com.kcx.jagar;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.VolatileImage;
import java.util.ConcurrentModificationException;

import javax.swing.JPanel;

public class GameCanvas extends JPanel
{
	private static final long serialVersionUID = 5570080027060608254L;
	private BufferedImage screen;

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

		/*g.setColor(new Color(80,80,80));
		
		int pX = 0, pY = 0;
		if(Game.player != null)
		{
			int size = (int)((Math.round(Game.player.sizeRender*2))*Game.zoom);
			pX = (int)(Game.player.xRender*Game.zoom+size);
			pY = (int)(Game.player.yRender*Game.zoom+size);
		
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

		for(int i2=0;i2<Game.cellsNumber;i2++)
		{
			Cell cell = Game.cells[i2];
			if(cell!=null)
			{
				cell.render(g);
			}
		}
		
		g.setColor(Color.WHITE);
		
		int i=0;
		
		for(String s : Game.leaderboard)
		{
			if(s != null)
			{
				g.drawString(s, GameFrame.size.width-140, 15+15*(i+1));
			}
			i++;
		}

		g.drawString("Score: "+Game.score, 20, 30);
		
		g.dispose();
		
		Graphics gg = this.getGraphics();
		gg.drawImage(screen, 0, 0, null);
		gg.dispose();
	}
}
