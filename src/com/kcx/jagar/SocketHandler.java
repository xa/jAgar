package com.kcx.jagar;
 
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketFrame;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.eclipse.jetty.websocket.api.extensions.Frame;

import com.kcx.jagar.packet.PacketC016UpdateCells;
import com.kcx.jagar.packet.PacketC032CenterCell;
import com.kcx.jagar.packet.PacketC049Leaderboard;
import com.kcx.jagar.packet.PacketC064MapSize;
import com.kcx.jagar.packet.PacketS000SetNick;
 
@WebSocket(maxTextMessageSize = 2^32)
public class SocketHandler {
 
    private final CountDownLatch closeLatch;
	public Session session; 
    
    public SocketHandler()
    {
        this.closeLatch = new CountDownLatch(1);
    }
 
    public boolean awaitClose(int duration, TimeUnit unit) throws InterruptedException
    {
        return this.closeLatch.await(duration, unit);
    }
 
    @OnWebSocketClose
    public void onClose(int statusCode, String reason) {
        System.out.println("Closed." + statusCode + "<" + reason + ">");
        this.closeLatch.countDown();
    }
 
    @OnWebSocketConnect
    public void onConnect(Session session) throws IOException
    {
    	this.session = session;
    	
        System.out.println("Connected!");
        
        ByteBuffer buf = ByteBuffer.allocate(5);
        buf.put(0, (byte)254);
        buf.putInt(1, 0x05000000);
        session.getRemote().sendBytes(buf);

        buf = ByteBuffer.allocate(5);
        buf.put(0, (byte)255);
        buf.putInt(1, 0x33182283);
        session.getRemote().sendBytes(buf);
        
        new PacketS000SetNick("królik to siki").write(session);        
    }
 
	@OnWebSocketFrame
    public void onPacket(Frame frame)
	{
        byte id = frame.getPayload().get(0);
        if(id == 16)
        {
        	new PacketC016UpdateCells(frame.getPayload());
        }
        if(id == 49)
        {
        	new PacketC049Leaderboard(frame.getPayload());
        }
        if(id == 32)
        {
        	new PacketC032CenterCell(frame.getPayload());
        }
        if(id == 64)
        {
        	new PacketC064MapSize(frame.getPayload());
        }
    }
}
