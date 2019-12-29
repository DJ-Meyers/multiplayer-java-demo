package com.meyers.survivor.server;

import com.badlogic.gdx.utils.Queue;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.EndPoint;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.meyers.survivor.server.networking.*;
import com.meyers.survivor.server.networking.dtos.InputDTO;
import com.meyers.survivor.server.networking.dtos.PlayerDTO;
import com.meyers.survivor.server.networking.requests.AbstractRequest;
import com.meyers.survivor.server.networking.requests.JoinRequest;
import com.meyers.survivor.server.networking.requests.InputRequest;
import com.meyers.survivor.server.networking.requests.LeaveRequest;
import com.meyers.survivor.server.networking.responses.JoinResponse;
import com.meyers.survivor.server.networking.responses.PlayerJoinedResponse;
import com.meyers.survivor.server.networking.responses.PlayerLeftResponse;
import com.meyers.survivor.server.networking.responses.PlayerUpdatesResponse;
import javafx.util.Pair;

import java.io.IOException;

import static com.meyers.survivor.server.helpers.Logging.logServer;

public class SurvivorServer
{
    // Server vars
    private Server kryonetServer;
    public static final int TIMEOUT_MS = 5000;
    public static final String IP_ADDRESS = "127.0.0.1";
    public static final int TCP_PORT = 54555;
    public static final int UDP_PORT = 54777;
    
    private ServerRequestHandler handler;
    private Queue<Pair<AbstractRequest, Connection>> requestPackets;
    
    // Game vars
    private boolean running = false;
    
    public SurvivorServer() throws IOException
    {
        kryonetServer = new Server();
        kryonetServer.start();
        kryonetServer.bind(TCP_PORT, UDP_PORT);
        
        logServer(String.format("Listening on Port %d (TCP) and %d (UDP)", TCP_PORT, UDP_PORT));
        
        register(kryonetServer);
        
        handler = new ServerRequestHandler(kryonetServer);
        requestPackets = new Queue<Pair<AbstractRequest, Connection>>();
        
        addListeners();
        run();
    }
    
    public static void register(EndPoint endPoint)
    {
        Kryo kryo = endPoint.getKryo();
    
        // Register Requests
        kryo.register(JoinRequest.class);
        kryo.register(JoinResponse.class);
        kryo.register(PlayerJoinedResponse.class);
        kryo.register(LeaveRequest.class);
        kryo.register(PlayerLeftResponse.class);
        kryo.register(InputRequest.class);
        kryo.register(PlayerUpdatesResponse.class);
        
        // Register Data Transfer Objects
        kryo.register(PlayerDTO.class);
        kryo.register(InputDTO.class);
        kryo.register(java.util.ArrayList.class);
    }
    
    private void addListeners()
    {
        kryonetServer.addListener(new Listener() {
            public void received (Connection connection, Object object) {
                if (object instanceof AbstractRequest)
                {
                    requestPackets.addLast(new Pair<AbstractRequest, Connection>((AbstractRequest) object, connection));
//                    logServer("Request added to queue in position " + requestPackets.size + ": " + object.getClass().toString());
                }
            }
        });
    }
    
    
    public void run()
    {
        running = true;
        float ns = 1000000000/60.0f;
        float delta = 0;
        
        long lastTime = System.nanoTime();
        long timer = System.currentTimeMillis();
        
        while (running) {
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            
            while (delta >= 1) {
                update(delta);
                delta -= 1;
            }
        }
    }
    
    /**
     *
     * @param delta
     */
    private void update(float delta) {
        if (requestPackets.isEmpty()) return;
        boolean handled = false;
        
        // Handle requests in the order they were received
        while(!requestPackets.isEmpty()) {
            handled = false;
            
            AbstractRequest curr = requestPackets.first().getKey();
            Connection connection = requestPackets.first().getValue();
            
            if (curr instanceof InputRequest) {
                handled = handler.handle((InputRequest)curr, connection);
            } else if (curr instanceof JoinRequest) {
                handled = handler.handle((JoinRequest) curr, connection);
            } else if (curr instanceof LeaveRequest) {
                handled = handler.handle((LeaveRequest) curr, connection);
            }
            
            if (handled) requestPackets.removeFirst();
        }
        
        handler.sendUpdates();
    }
    
    
}
