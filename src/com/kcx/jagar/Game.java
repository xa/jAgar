package com.kcx.jagar;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import org.eclipse.jetty.websocket.client.ClientUpgradeRequest;
import org.eclipse.jetty.websocket.client.WebSocketClient;

public class Game
{
	public static ArrayList<Cell> cells = new ArrayList<Cell>();
	public static Cell player;
	private String serverIP = "ws://localhost:443";
	
	public Game()
	{	
	        WebSocketClient client = new WebSocketClient();
	        SocketHandler socket = new SocketHandler();
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
	
	public void tick()
	{
		if(System.currentTimeMillis()%10==0)
			cells.clear();
		//TODO
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
