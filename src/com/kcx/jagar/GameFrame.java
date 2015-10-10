package com.kcx.jagar;

import java.awt.Dimension;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JFrame;

public class GameFrame extends JFrame implements MouseListener
{
	private static long startTime = System.currentTimeMillis();
	private static long frames = 0;
	private static final long serialVersionUID = 3637327282806739934L;
	public GameCanvas canvas;
	public static double mouseX, mouseY;
	public static Dimension size = new Dimension(800, 600);
	
	public GameFrame()
	{
		setSize(size);
		setMinimumSize(size);
		setMaximumSize(size);
		setPreferredSize(size);
		addMouseListener(this);
		canvas = new GameCanvas();
		getContentPane().add(canvas);		
		setResizable(false);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("JAgar r1 · Java Agar.io implementation");
		//setCursor(getToolkit().createCustomCursor(new BufferedImage(3, 3, BufferedImage.TYPE_INT_ARGB), new Point(0, 0), "null"));
		pack();
		setVisible(true);
	}
	
	public void render()
	{
		Point mouseP = getMouseLocation();
		mouseX = mouseP.getX();
		mouseY = mouseP.getY();
		frames++;
		if(System.currentTimeMillis() - startTime > 1000)
		{
			if(frames<10)
			{
				System.err.println("LAG > There were only "+frames+" frames in " + (System.currentTimeMillis() - startTime) + "ms!!!");
			}
			frames = 0;
			startTime = System.currentTimeMillis();
		}
		canvas.render();
	}
	
	private Point getMouseLocation()
	{
		int x = (MouseInfo.getPointerInfo().getLocation().x-getLocationOnScreen().x);
		int y = (MouseInfo.getPointerInfo().getLocation().y-getLocationOnScreen().y-24);
	    return new Point(x, y);
	}

	@Override
	public void mousePressed(MouseEvent e) {
		Game.pressMouse(e.getX(), e.getY(), e.getButton());				
	}

	@Override
	public void mouseReleased(MouseEvent e)
	{
		Game.releaseMouse(e.getX(), e.getY(), e.getButton());		
	}

	public void mouseClicked(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
}
