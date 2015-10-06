package com.kcx.jagar;

public class GameThread extends Thread implements Runnable
{
	@Override
	public void run()
	{
		while(true)
		{
			Main.updateGame();
		}
	}
}
