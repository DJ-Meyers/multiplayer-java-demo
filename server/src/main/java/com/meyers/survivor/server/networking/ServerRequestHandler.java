package com.meyers.survivor.server.networking;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Server;
import com.meyers.survivor.server.game.GameState;
import com.meyers.survivor.server.game.entities.Player;
import com.meyers.survivor.server.networking.dtos.InputDTO;
import com.meyers.survivor.server.networking.dtos.PlayerDTO;
import com.meyers.survivor.server.networking.requests.JoinRequest;
import com.meyers.survivor.server.networking.requests.InputRequest;
import com.meyers.survivor.server.networking.requests.LeaveRequest;
import com.meyers.survivor.server.networking.responses.JoinResponse;
import com.meyers.survivor.server.networking.responses.PlayerJoinedResponse;
import com.meyers.survivor.server.networking.responses.PlayerLeftResponse;
import com.meyers.survivor.server.networking.responses.PlayerUpdatesResponse;

import java.util.ArrayList;
import java.util.List;

import static com.meyers.survivor.server.game.Common.PLAYER_MOVE_SPEED;
import static com.meyers.survivor.server.helpers.Logging.logServer;

public class ServerRequestHandler {
    private Server server;
    private List<PlayerDTO> changes;
    private final float DIAGONAL_MULTIPLIER = (float)Math.sin(Math.toRadians(45));
    
    public ServerRequestHandler(Server server) {
        logServer("RequestHandler ready");
        this.server = server;
        changes = new ArrayList<>();
    };
    
    // Handlers
    
    /**
     * Player requested to join.  Add them to the server and alert other players.
     * @param req request carrying an empty Integer DTO
     * @param connection kryonet connection used to get ID
     * @return success
     * TODO Validate Login Credentials
     */
    public boolean handle(JoinRequest req, Connection connection) {
        // Create new player in the game state
        Player p = GameState.getInstance().joinPlayer(connection);
    
        // Get the other players already on the server
        List<PlayerDTO> playerDTOs = getDTOs(GameState.getInstance().getAllPlayers());
        JoinResponse otherPlayersRes = new JoinResponse(playerDTOs);
        server.sendToUDP(connection.getID(), otherPlayersRes);
        
        // Create and send response with player data to other players
        PlayerDTO dto = new PlayerDTO(p);
        PlayerJoinedResponse res = new PlayerJoinedResponse(dto);
        server.sendToAllExceptUDP(connection.getID(), res);
        
        return true;
    }

    
    /**
     * Player sent input to the server
     * @param req request carrying an InputDTO
     * @param connection kryonet connection used to get ID
     * @return success
     */
    public boolean handle(InputRequest req, Connection connection) {
        InputDTO input = req.getValue();
        Player player = GameState.getInstance().getPlayer(connection.getID());
        float delta = input.getDelta();
        float horiz = 0, vert = 0;
        float mouseX = input.getMouseX(), mouseY = input.getMouseY();
        
        boolean up = false, down = false, left = false, right = false;
        for (int keycode : input.getKeycodes()) {
            if (keycode == Input.Keys.W) up = true;
            if (keycode == Input.Keys.S) down = true;
            if (keycode == Input.Keys.A) left = true;
            else if (keycode == Input.Keys.D) right = true;
        }
        
        if (!left && right) horiz = 1;
        else if (left && !right) horiz = -1;
        
        if (up && !down) vert = 1;
        else if(!up && down) vert = -1;
        
        // Don't allow diagonal movement to be faster
        if (vert != 0 && horiz != 0) {
            vert *= DIAGONAL_MULTIPLIER;
            horiz *= DIAGONAL_MULTIPLIER;
        }
        
        Vector2 deltaP = new Vector2(horiz * PLAYER_MOVE_SPEED * delta, vert * PLAYER_MOVE_SPEED * delta);
        player.setPosition(player.getPosition().add(deltaP));
    
        if (mouseX != -1 && mouseY != -1) {
            player.setReticle(mouseX, mouseY);
        }
        
        // Add to the list of changes to send together after this server.update()
        changes.add(new PlayerDTO(player));
        
        return true;
    }

    
    public boolean handle(LeaveRequest req, Connection connection) {
        logServer("Handling LeaveRequest: " + connection.getID());
        // Remove the player from the GameState
        Player leftPlayer = GameState.getInstance().leavePlayer(connection);
        
        // Update the other players
        PlayerDTO dto = new PlayerDTO(leftPlayer);
        PlayerLeftResponse res = new PlayerLeftResponse(dto);
        server.sendToAllExceptUDP(connection.getID(), res);
        
        return true;
    }
    
    
    // Helpers
    
    /**
     * Helper to get a list of PlayerDTO objects from a list of Player objects
     * @param otherPlayers list of Player objects
     * @return list of PlayerDTO objects
     * @see PlayerDTO
     * @see Player
     */
    private List<PlayerDTO> getDTOs(List<Player> otherPlayers) {
        List<PlayerDTO> list = new ArrayList<>();
        for (Player p : otherPlayers) {
            list.add(new PlayerDTO(p));
        }
        return list;
    }
    
    /**
     * Send updated GameState to players
     */
    public void sendUpdates() {
        if (!changes.isEmpty()) {
            PlayerUpdatesResponse updatesResponse = new PlayerUpdatesResponse(changes);
            server.sendToAllUDP(updatesResponse);
            
            changes.clear();
        }
    }
}

