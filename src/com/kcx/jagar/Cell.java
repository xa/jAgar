package com.kcx.jagar;

import java.awt.Color;
import java.awt.Graphics2D;

public class Cell
{
	public double x, y;
	public float size;
	
	public Cell(double x, double y, float size)
	{
		this.x = x;
		this.y = y;
		this.size = size;
	}
	
	public void tick()
	{
	}

	public void render(Graphics2D g)
	{
		g.setColor(new Color(255,255,255));
		g.fillOval((int)this.x, (int)this.y, (int)this.size, (int)this.size);
	}	
}