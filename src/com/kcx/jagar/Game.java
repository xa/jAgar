package com.kcx.jagar;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import javax.swing.JOptionPane;

import org.eclipse.jetty.websocket.client.ClientUpgradeRequest;
import org.eclipse.jetty.websocket.client.WebSocketClient;

import com.kcx.jagar.packet.PacketS016Move;

public class Game
{
	public static Cell[] cells = new Cell[32768];
	public static int cellsNumber = 0;
	public static ArrayList<Cell> player = new ArrayList<Cell>();
	public static String[] leaderboard = new String[10];
	public static double maxSizeX, maxSizeY, minSizeX, minSizeY;
	public static int playerID;
	public static double zoom;
	private double zoomm=-1;
	public static int score;
	private static SocketHandler socket;
	
	public static String serverIP = "ws://" + (JOptionPane.showInputDialog(null, "Ip", "localhost:443"));
	public static String serverToken;
	public static String nick = (JOptionPane.showInputDialog(null, "Nick", "JAgar"));
	public static int bots = 0;

	public Game()
	{	
		try {
			Scanner sc =  new Scanner(new URL("http://m.agar.io/").openStream(), "UTF-8").useDelimiter("\\A");
			String[] result = sc.next().split("\n");
//			serverIP = "ws://"+result[0];
	//		serverToken = result[1].trim();
			System.out.println(serverToken);
			sc.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}			
			
		WebSocketClient clientt = new WebSocketClient();
		this.socket = new SocketHandler();
		new Thread(new Runnable() {				
			@Override
			public void run() {					
				try {	        	
					clientt.start();
					URI serverURI = new URI(serverIP);
					ClientUpgradeRequest request = new ClientUpgradeRequest();
					request.setHeader("Origin", "http://agar.io");
					clientt.connect(socket, serverURI, request);
					System.out.println("Trying to connect <"+serverIP+">");
					socket.awaitClose(7, TimeUnit.DAYS);
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
		}).start();		
	
	    new Thread(new Runnable() {				
			@Override
			public void run() {		
		    	long xx=System.currentTimeMillis();
		    	int yy = 0;
		    	boolean stop=true;
		        while(stop)
		        {
		        	if(System.currentTimeMillis()-xx>100 && yy < bots)
		        	{
		        		yy++;
				        new Thread(new Runnable() {				
							@Override
							public void run() {					
						        try {	  
						    		SocketHandler socket = new SocketHandler();
						            socket.bot = true;				            
						    		WebSocketClient client = new WebSocketClient();
						            client.start();
						            URI serverURI = new URI(serverIP);
						            ClientUpgradeRequest request = new ClientUpgradeRequest();
						            request.setHeader("Origin", "http://agar.io");
						            client.connect(socket, serverURI, request);
						            System.out.println("Bot Trying to connect <"+serverIP+">");
						            socket.awaitClose(7, TimeUnit.DAYS);
						        } catch (Throwable t) {
						            t.printStackTrace();
						        }
							}
						}).start();
				        xx=System.currentTimeMillis();
		        	}
		        	if(yy==bots)
		        	{
		        		stop=false;
		        	}
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
				if(c.id == playerID && !player.contains(c))
				{
					player.add(c);
				}
			}
		}		
		
		if(socket.session != null && player.size()>0)
		{		
			float totalSize = 0;
			score = 0;
			for(Cell c : player)
			{
				totalSize+=c.size;
				score+=c.mass;
			}
			
			zoomm = GameFrame.size.height/(1024 / Math.pow(Math.min(64.0 / totalSize, 1), 0.4));
			   
//			zoomm = 1f/(Game.player.sizeRender/45f);
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
				float avgX = 0;
				float avgY = 0;
				
				for(Cell c : Game.player)
				{
					avgX += c.xRender;
					avgY += c.yRender;
				}
				
				avgX /= Game.player.size();
				avgY /= Game.player.size();			
				
				float x = avgX;
				float y = avgY;
				x += (float) (GameFrame.mouseX - GameFrame.size.width/2);
				y += (float) (GameFrame.mouseY - GameFrame.size.height/2);
				(new PacketS016Move(x,y)).write(socket.session);
			}
		}
		
		for(int i=0;i<cellsNumber;i++)
		{
			if(cells[i]!=null)
			{
				cells[i].tick();
			}
		}
		
		if(System.currentTimeMillis()%100 == 0)
		{
			Arrays.sort(cells, new Comparator<Cell>() {	
				@Override
				public int compare(Cell o1, Cell o2) {
					if(o1 != null && o2 != null)
					{
						if(o1.size > o2.size)
							return 1;
						if(o1.size < o2.size)
							return -1;
						if(o1.size == o2.size)
							return 0;
					}else
					{
						return 0;
					}
					return 0;
				}
			});
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
