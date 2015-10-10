package com.kcx.jagar;

public class GameThread extends Thread implements Runnable
{
	@Override
	public void run()
	{
		long time = 0;
		while(true)
		{
			if(System.currentTimeMillis() - time > 5)
			{
				Main.updateGame();
				time = System.currentTimeMillis();
			}
		}
	}
}
