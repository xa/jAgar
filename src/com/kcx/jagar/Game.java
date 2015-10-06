package com.kcx.jagar;

import java.util.ArrayList;

public class Game
{
	public static ArrayList<Cell> cells = new ArrayList<Cell>();
	
	public Game()
	{
		cells.add(new Cell(0,0,10));
	}
	
	public void tick()
	{
	}
	
	public static void pressMouse(int x, int y, int button)
	{
	}

	public static void releaseMouse(int x, int y, int button)
	{
	}

	public void afterRender()
	{
	}
}
