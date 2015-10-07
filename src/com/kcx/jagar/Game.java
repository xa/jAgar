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
	public static double zoom;
	private String serverIP = "ws://localhost:443";
	private double zoomm=-1;
	private static SocketHandler socket;
	
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
			zoomm = 1f/(Game.player.sizeRender/45f);
			if(zoomm>1)
			{
				zoomm=1;
			}
			
			if(zoomm == -1)
			{
				zoomm = zoom;
			}
			zoom += (zoomm - zoom)/40f;
			
			if(socket.session.isOpen())
			{
				float x = (float) player.x;
				float y = (float) player.y;
				x += (float) (GameFrame.mouseX - GameFrame.size.width/2);
				y += (float) (GameFrame.mouseY - GameFrame.size.height/2);
				(new PacketS016Move(x,y)).write(socket.session);
			}
		}
		
		for(int i=0;i<cellsNumber;i++)
		{
			cells[i].tick();
		}
	}
	
	public static void pressMouse(int x, int y, int button)
	{
		//TODO Keyboard
		if(button==1) //SPACE
		{
	        ByteBuffer buf = ByteBuffer.allocate(5);
	        buf.put(0, (byte)17);
	        try {
				socket.session.getRemote().sendBytes(buf);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if(button==2) //Q
		{
	        ByteBuffer buf = ByteBuffer.allocate(5);
	        buf.put(0, (byte)18);
	        try {
				socket.session.getRemote().sendBytes(buf);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if(button==3) //W
		{
	        ByteBuffer buf = ByteBuffer.allocate(5);
	        buf.put(0, (byte)21);
	        try {
				socket.session.getRemote().sendBytes(buf);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
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
