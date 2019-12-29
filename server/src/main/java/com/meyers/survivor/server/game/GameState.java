package com.meyers.survivor.server.game;

import com.badlogic.gdx.math.Vector2;
import com.esotericsoftware.kryonet.Connection;
import com.meyers.survivor.server.game.entities.Player;
import com.meyers.survivor.server.helpers.Logging.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.meyers.survivor.server.helpers.Logging.logGame;


public class GameState
{
    // Singleton
    private static GameState instance;
    public static GameState getInstance() {
        if (instance == null)
            instance = new GameState();
        
        return instance;
    }
    
    private HashMap<Integer, Player> players;
    
    
    public GameState() {
       players = new HashMap<Integer, Player>();
    }
    
    public Player joinPlayer(Connection connection) {
        Player player = new Player(connection.getID());
        players.put(player.getPlayerID(), player);
        
        return player;
    }
    
    public Player leavePlayer(Connection connection) {
        Player p = getPlayer(connection.getID());
        players.remove(p.getPlayerID());
        
        return p;
    }
    
    public Player getPlayer(int playerId) {
        return players.get(playerId);
    }
    
    public List<Player> getAllPlayers() {
        List<Player> list = new ArrayList<Player>();
        for (Map.Entry<Integer, Player> e: players.entrySet()) {
            list.add(e.getValue());
        }
        return list;
    }
    
    public List<Player> getOtherPlayers(int id) {
        List<Player> list = new ArrayList<Player>();
        for (Map.Entry<Integer, Player> e: players.entrySet()) {
            if (e.getKey() != id) list.add(e.getValue());
        }
        
        return list;
    }
    
    public void movePlayer(Connection connection, Vector2 input, float deltaT) {}
    
}
