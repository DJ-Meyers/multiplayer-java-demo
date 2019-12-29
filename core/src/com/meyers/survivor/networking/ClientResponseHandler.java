package com.meyers.survivor.networking;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.esotericsoftware.kryonet.Connection;
import com.meyers.survivor.graphics.PlayerSprite;
import com.meyers.survivor.server.networking.dtos.PlayerDTO;
import com.meyers.survivor.server.networking.responses.JoinResponse;
import com.meyers.survivor.server.networking.responses.PlayerJoinedResponse;
import com.meyers.survivor.server.networking.responses.PlayerLeftResponse;
import com.meyers.survivor.server.networking.responses.PlayerUpdatesResponse;
import com.meyers.survivor.SurvivorClient;

import java.util.List;

import static com.meyers.survivor.helpers.Logging.logClient;

public class ClientResponseHandler
{
    private SurvivorClient client;
    public ClientResponseHandler(SurvivorClient client){
        this.client = client;
    }
    
    public boolean handle(PlayerJoinedResponse res, Connection connection) {
        PlayerDTO data = res.getValue();
        if (data.getID() != connection.getID()){
            client.addAlly(data);
        }
        return true;
    }
    
    public boolean handle(JoinResponse res, Connection connection) {
        List<PlayerDTO> list = res.getValue();
        for (PlayerDTO dto : list) {
            if (dto.getID() == connection.getID())
            {
                logClient("Adding Player");
                client.addPlayer(dto);
            }
            else
            {
                logClient("Adding Ally");
                client.addAlly(dto);
            }
        }
        return true;
    }
    
    public boolean handle(PlayerUpdatesResponse res, Connection connection) {
        for (PlayerDTO dto : res.getValue()){
            PlayerSprite temp = client.sprites.get(dto.getID());
            temp.setPosition(dto.getX(), dto.getY());
            
            float deltaX = dto.getReticleX() - temp.getCenter().x, deltaY = Gdx.graphics.getHeight() - dto.getReticleY() - temp.getCenter().y;
            Vector2 direction = new Vector2(deltaX, deltaY);
            temp.setRotation(direction.angle());
        }
        return true;
    }
    
    public boolean handle(PlayerLeftResponse res, Connection connection) {
        PlayerDTO dto = res.getValue();
        client.removePlayer(dto);
        
        return true;
    }
    
}
