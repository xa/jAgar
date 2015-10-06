package com.kcx.jagar;

public class Main
{
	public static Main INSTANCE;
	public static GameThread thread;
	public static GameFrame frame;
	private static Game game;
	
	public Main()
	{
		
	}
	
	public static void main(String[] args)
	{
		INSTANCE = new Main();
		thread = new GameThread();
		frame = new GameFrame();
		game = new Game();
		
		thread.run();
	}

	public static void updateGame()
	{
		try
		{
			game.tick();
		}catch(Exception e){};
		frame.render();
		game.afterRender();
	}
}
