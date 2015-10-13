package com.kcx.jagar;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
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

import org.eclipse.jetty.websocket.api.extensions.Frame;
import org.eclipse.jetty.websocket.client.ClientUpgradeRequest;
import org.eclipse.jetty.websocket.client.WebSocketClient;

import com.kcx.jagar.packet.PacketS000SetNick;
import com.kcx.jagar.packet.PacketS016Move;
import com.kcx.jagar.packet.PacketS021EjectMass;
import com.kcx.jagar.packet.PacketS081FacebookAuth;

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
	private int sortTimer;
	public static int score;
	public static SocketHandler socket;
	
	public static String serverIP = "ws://" + (JOptionPane.showInputDialog(null, "Ip (leave blank for official server)", ""));
	public static String serverToken = "";
	public static String nick = (JOptionPane.showInputDialog(null, "Nick", "jAgar"));
	private String fbToken = "";
	private String workingDirectory = "";
	private static boolean playbackRewind;
	public static int maxPlayback;
	public static int bots = 0;
	public static int spawnPlayer = -1;
	public static HashMap<Integer, String> cellNames = new HashMap<Integer, String>();
	public static int level = 0;
	public static int exp = 0;
	public static int maxExp = 1;
	public static String mode = "";
	public static long fps = 60;
	public static boolean rapidEject;
	public static HashMap<Integer, ArrayList<ByteBuffer>> playback = new HashMap<Integer, ArrayList<ByteBuffer>>();
	public static int playbackTime = 0;
	public static boolean isPlaybacking = false;
	private static float playbackSpeed = 0;
	private static boolean playbackSpeeding;
	
	public Game()
	{
		String OS = (System.getProperty("os.name")).toUpperCase();
		if (OS.contains("WIN"))
		{
		    workingDirectory  = System.getenv("AppData");
		}
		else if (OS.contains("OS X"))
		{
		    workingDirectory = "/Library/Application Support";
		}else
		{
			workingDirectory = System.getProperty("user.home");
		}
		
		workingDirectory = workingDirectory + File.separator + ".jagar" + File.separator + "options" + File.separator;
		new File(workingDirectory).mkdirs();
		
		String savedToken = "";
		
		savedToken = loadToken();		
		fbToken = (JOptionPane.showInputDialog(null, "Facebook Token", savedToken));
	
		mode = (JOptionPane.showInputDialog(null, "Game mode:\n\nffa\nteams\nexperimental\nparty", "ffa")).toLowerCase().replace("ffa", "");
		
		if(mode.length()>0)
		{
			mode = ":"+mode;
		}
		
		saveToken(fbToken);

		if(serverIP.equals("ws://"))
		{
			String[] data;
			try
			{
				data = ServerConnector.getServerData();
				serverIP = "ws://" + data[0];
				serverToken = data[1];
			} catch (Exception e) {
				e.printStackTrace();
				System.exit(-99);
			}		
		}
		
		spawnPlayer = 100;
		
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
		        	if(System.currentTimeMillis()-xx>150 && yy < bots)
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
	
	private String loadToken()
	{
		if(!new File(workingDirectory + "fbToken").exists())
		{
			return "";
		}
		try {
			Scanner sc = new Scanner(new File(workingDirectory + "fbToken"), "UTF-8").useDelimiter("\\A");
			String array="";
			if(sc.hasNext())
			{
				return sc.next();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return "";
	}
	

	public void saveToken(String token)
	{
		try {
			new File(workingDirectory + "fbToken").delete();
			new File(workingDirectory + "fbToken").createNewFile();
			PrintWriter out = new PrintWriter(workingDirectory + "fbToken");
	        out.write(token);
	        out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void tick() throws IOException
	{
		if(socket != null && socket.session != null && socket.session.isOpen())
		{
			if(spawnPlayer!=-1)
			{
				spawnPlayer --;
			}
			if(spawnPlayer==99 && fbToken.length()>0)
			{
				new PacketS081FacebookAuth(fbToken).write();
			}
			if(spawnPlayer==0)
			{
	    		System.out.println("Reseting level (death)");
	        	new PacketS000SetNick(Game.nick).write(socket.session);
			}
			if(Game.player.size()==0)
			{
				if(socket.session.isOpen() && spawnPlayer == -1)
				{					
					if(!isPlaybacking)
					{
						score = 0;
						Game.player.clear();
						Game.cells = new Cell[Game.cells.length];
						cellsNumber=0;
						cellNames.clear();

						maxPlayback=playbackTime;
						playbackTime=0;
						isPlaybacking=true;
					}
				}
			}
		}
		
		if(isPlaybacking)
		{
			if(playbackRewind)
			{
				if(playback.get(playbackTime) != null)
				{
					for(ByteBuffer b : playback.get(playbackTime))
					{
						socket.handlePacket(b);
					}
				}				
			}else
			{
				for(int i=playbackTime;i<playbackTime+(playbackSpeed+1);i++)
				{
					if(playback.get(i) != null)
					{
						for(ByteBuffer b : playback.get(i))
						{
							socket.handlePacket(b);
						}
					}
				}
			}
	    	playbackTime += 1+playbackSpeed;
	    	if(playbackTime<0)
	    	{
	    		playbackTime = maxPlayback;	    		
	    	}
	    	if(playbackTime > maxPlayback && maxPlayback > 100)
	    	{	    
    			playbackTime = 0;
				score = 0;
				Game.player.clear();
				Game.cells = new Cell[Game.cells.length];
				cellsNumber=0;
				cellNames.clear();    			
	    	}
	    	if(playbackTime > maxPlayback && maxPlayback <= 100)
	    	{	    
    			respawn();
	    	}
		}
		
		if(playbackSpeeding)
		{
			playbackSpeed += 0.03;
		}

		if(playbackRewind)
		{
			playbackSpeed = -2;
		}

		if(player.size()>0)
		{
			if(!isPlaybacking)
			{
				Game.playbackTime++;
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
			int newScore = 0;
			for(Cell c : player)
			{
				totalSize+=c.size;
				newScore+=(c.size * c.size) / 100;
			}
			
			if(newScore > score)
			{
				score = newScore;
			}
			
			zoomm = GameFrame.size.height/(1024 / Math.pow(Math.min(64.0 / totalSize, 1), 0.4));
			   
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
				
				if(rapidEject)
				{
					new PacketS021EjectMass().write();
				}
			}
		}
		
		for(int i=0;i<cellsNumber;i++)
		{
			if(cells[i]!=null)
			{
				cells[i].tick();
			}
		}
		
		sortTimer++;
		
		if(sortTimer>10)
		{
			sortCells();
			sortTimer = 0;
		}		
	}
	
	public static void pressMouse(int x, int y, int button)
	{		
		if(Game.isPlaybacking)
		{
			if(button==1)
			{
				playbackSpeeding = true;
			}
			if(button==3)
			{
				playbackRewind = true;
			}
		}
	}

	public static void releaseMouse(int x, int y, int button)
	{
		if(Game.isPlaybacking)
		{
			if(button==1)
			{
				playbackSpeeding = false;
				playbackSpeed = 0;
			}
			if(button==3)
			{
				playbackRewind = false;
				playbackSpeed = 0;
			}
		}
	}

	public void afterRender() {}
	
	public static void addCell(Cell c)
	{
		cells[cellsNumber] = c;
		cellsNumber++;
		if(cellsNumber>cells.length)
		{
			cellsNumber=0;
		}
	}

	public static void sortCells()
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

	public static void respawn()
	{
		if(spawnPlayer==-1)
		{
    		playback.clear();
    		playbackTime = 0;
    		isPlaybacking = false;
    		maxPlayback = 0;
			spawnPlayer=100;
		}
	}
}
