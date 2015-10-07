package com.kcx.jagar;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Cell
{
	public double x, y;
	public int id;
	public float size;
	private int r, g, b;
	public String name;
	
	public Cell(double x, double y, float size, int id, String name)
	{
		this.x = x;
		this.y = y;
		this.size = size;
		this.id = id;
		this.name = name;
	}
	
	public void tick()
	{
	}

	public void render(Graphics2D g)
	{		
		if(Game.player != null)
		{
			g.setColor(new Color(this.r, this.g, this.b));
			int x = (int)(this.x - Game.player.x) + GameFrame.size.width/2;
			int y = (int)(this.y - Game.player.y) + GameFrame.size.height/2;
			g.fillOval(x-(int)(Math.round(this.size)), y-(int)(Math.round(this.size)), (int)Math.round(this.size)*2, (int)Math.round(this.size)*2);
			g.setColor(new Color(255,255,255));
			
			Font font = Main.frame.canvas.getFont();
			BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
			FontMetrics fm = img.getGraphics().getFontMetrics(font);
			int fontSize = fm.stringWidth(this.name);
			
			g.drawString(this.name, x - fontSize/2, y);
		}
	}

	public void setColor(byte r, byte g, byte b)
	{
		this.r = r;
		this.g = g;
		this.b = b;
		if(r<0){this.r = r + 256;}
		if(g<0){this.g = g + 256;}
		if(b<0){this.b = b + 256;}
	}	
}