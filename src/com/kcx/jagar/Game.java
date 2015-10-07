package com.kcx.jagar;

import java.io.IOException;
import java.net.URI;
import java.nio.ByteBuffer;
import java.util.concurrent.TimeUnit;

import org.eclipse.jetty.websocket.client.ClientUpgradeRequest;
import org.eclipse.jetty.websocket.client.WebSocketClient;

import com.kcx.jagar.packet.PacketS016Move;

public class Game
{
	public static Cell[] cells = new Cell[32768];
	public static int cellsNumber = 0;
	public static Cell player;
	public static String[] leaderboard = new String[10];
	public static double maxSizeX, maxSizeY, minSizeX, minSizeY;
	public static int playerID;
	private String serverIP = "ws://localhost:443";
	private SocketHandler socket;
	
	public Game()
	{	
		WebSocketClient client = new WebSocketClient();
		socket = new SocketHandler();
		new Thread(new Runnable() {				
			@Override
			public void run() {					
				try {	        	
					client.start();
					URI serverURI = new URI(serverIP);
					ClientUpgradeRequest request = new ClientUpgradeRequest();
					request.setHeader("Origin", "http://agar.io");
					client.connect(socket, serverURI, request);
					System.out.println("Trying to connect <"+serverIP+">");
					socket.awaitClose(7, TimeUnit.DAYS);
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
		}).start();
	}
	
	public void tick() throws IOException
	{
		for(Cell c : Game.cells)
		{
			if(c!=null)
			{
				if(c.id == playerID)
				{
					player = c;
				}
			}
		}	
		
		if(socket.session != null && player != null)
		{
			if(socket.session.isOpen())
			{
				float x = (float) player.x;
				float y = (float) player.y;
				x += (float) (GameFrame.mouseX - GameFrame.size.width/2);
				y += (float) (GameFrame.mouseY - GameFrame.size.height/2);
				System.out.println(player.x + "|" + player.y);
				(new PacketS016Move(x,y)).write(socket.session);
			}
		}
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
	
	public static void addCell(Cell c)
	{
		cells[cellsNumber] = c;
		cellsNumber++;
		if(cellsNumber>cells.length)
		{
			cellsNumber=0;
		}
	}
}
