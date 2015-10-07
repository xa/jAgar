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
	public float sizeRender;
	public double xRender;
	public double yRender;
	
	public Cell(double x, double y, float size, int id, String name)
	{
		this.x = x;
		this.y = y;
		this.size = size;
		this.id = id;
		this.name = name;
		this.xRender = this.x;
		this.yRender = this.y;
		this.sizeRender = this.size;
	}
	
	public void tick()
	{
		if(this.x!=-10000)
		{
			this.xRender -= (this.xRender - x)/10f;
			this.yRender -= (this.yRender - y)/10f;
			this.sizeRender -= (this.sizeRender - size)/10f;
		}else
		{
			this.xRender=-10000;
		}
	}

	public void render(Graphics2D g)
	{		
		if(Game.player != null)
		{
			g.setColor(new Color(this.r, this.g, this.b));			
			int size = (int)((Math.round(this.sizeRender*2))*Game.zoom);
			int x = (int)((this.xRender - Game.player.xRender) * Game.zoom) + GameFrame.size.width/2 - size/2;
			int y = (int)((this.yRender - Game.player.yRender) * Game.zoom) + GameFrame.size.height/2 - size/2;
			g.fillOval(x, y, size, size);
			g.setColor(new Color(255,255,255));
			
			Font font = Main.frame.canvas.getFont();
			BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
			FontMetrics fm = img.getGraphics().getFontMetrics(font);
			int fontSize = fm.stringWidth(this.name);
			
			g.drawString(this.name, x + size/2 - fontSize/2, y + size/2);
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