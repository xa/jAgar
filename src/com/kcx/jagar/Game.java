package com.kcx.jagar;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import javax.swing.JOptionPane;

import org.eclipse.jetty.websocket.client.ClientUpgradeRequest;
import org.eclipse.jetty.websocket.client.WebSocketClient;

import com.kcx.jagar.packet.PacketS000SetNick;
import com.kcx.jagar.packet.PacketS016Move;

public class Game
{
	public static Cell[] cells = new Cell[32768];
	public static int cellsNumber = 0;
	public static ArrayList<Cell> player = new ArrayList<Cell>();
	public static String[] leaderboard = new String[10];
	public static double maxSizeX, maxSizeY, minSizeX, minSizeY;
	public static ArrayList<Integer> playerID = new ArrayList<Integer>();
	public static float followX;
	public static float followY;
	public static double zoom;
	private double zoomm=-1;
	public static int score;
	private static SocketHandler socket;
	
	public static String serverIP = "ws://" + (JOptionPane.showInputDialog(null, "Ip (leave blank for official server)", ""));
	public static String serverToken = "";
	public static String nick = (JOptionPane.showInputDialog(null, "Nick", "jAgar"));
	public static int bots = 0;
	public static int spawnPlayer = -1;
	public static HashMap<Integer, String> cellNames = new HashMap<Integer, String>();

	public Game()
	{
		if(serverIP.equals("ws://"))
		{
			try {
				Scanner sc =  new Scanner(new URL("http://m.agar.io/").openStream(), "UTF-8").useDelimiter("\\A");
				String[] result = sc.next().split("\n");
				serverIP = "ws://"+result[0];
				serverToken = result[1].trim();
				System.out.println(serverToken);
				sc.close();
			} catch (IOException e) {
				e.printStackTrace();
			}			
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
		if(socket != null && socket.session != null && socket.session.isOpen())
		{
			if(spawnPlayer!=-1)
			{
				spawnPlayer --;
			}
			if(spawnPlayer==0)
			{
	        	System.out.println("Spawning player "+Game.nick);
	        	new PacketS000SetNick(Game.nick).write(socket.session);
			}
			if(Game.player.size()==0)
			{
				if(socket.session.isOpen() && spawnPlayer == -1)
				{
					Game.player.clear();
					Game.cells = new Cell[Game.cells.length];
					cellNames.clear();
					System.out.println("Reseting level (death)");
					spawnPlayer = 100;					
				}
			}
		}
		
		ArrayList<Integer> toRemove = new ArrayList<Integer>();
		
		for(int i : playerID)
		{
			try
			{
				for(Cell c : Game.cells)
				{
					if(c!=null)
					{
						if(c.id == i && !player.contains(c))
						{
							System.out.println("Centered cell " + c.name);
							player.add(c);
							toRemove.add(i);
						}
					}
				}
			}catch(ConcurrentModificationException e){}
		}
		
		for(int i : toRemove)
		{
			playerID.remove(playerID.indexOf(i));
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
				totalSize = 0;
				
				for(Cell c : Game.player)
				{
					avgX += c.x;
					avgY += c.y;
					totalSize += c.size;
				}
				
				avgX /= Game.player.size();
				avgY /= Game.player.size();			
				
				float x = avgX;
				float y = avgY;
				x += (float) ((GameFrame.mouseX - GameFrame.size.width/2) / zoom);
				y += (float) ((GameFrame.mouseY - GameFrame.size.height/2) / zoom);
				followX = x;
				followY = y;
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
		
		if(System.currentTimeMillis()%30 == 0)
		{
			Arrays.sort(cells, new Comparator<Cell>()
			{	
				@Override
				public int compare(Cell o1, Cell o2)
				{
					if (o1 == null && o2 == null)
					{
						return 0;
					}
					if (o1 == null)
					{
						return 1;
					}
					if (o2 == null)
					{
						return -1;
					}
					return Float.compare(o1.size, o2.size);
				}
			});
		}
	}
	
	public static void pressMouse(int x, int y, int button)
	{
		if(socket != null && socket.session != null)
		{
			if(socket.session.isOpen())
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
