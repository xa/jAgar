package com.kcx.jagar;

import java.awt.Color;
import java.awt.Graphics2D;

public class Cell
{
	public double x, y;
	public int id;
	public float size;
	
	public Cell(double x, double y, float size, int id)
	{
		this.x = x;
		this.y = y;
		this.size = size;
		this.id = id;
	}
	
	public void tick()
	{
	}

	public void render(Graphics2D g)
	{		
		g.setColor(new Color(255,255,255));
		
		g.fillOval((int)this.x/100, (int)this.y/100, (int)this.size/10, (int)this.size/10);
	}	
}