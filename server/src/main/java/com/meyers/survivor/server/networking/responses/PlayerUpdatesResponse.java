package com.meyers.survivor.server.networking.responses;

import com.badlogic.gdx.math.Vector2;
import com.meyers.survivor.server.networking.dtos.PlayerDTO;
import javafx.util.Pair;

import java.util.List;

public class PlayerUpdatesResponse extends AbstractResponse<List<PlayerDTO>>
{
    List<PlayerDTO> updatedPositions;
    
    public PlayerUpdatesResponse(List<PlayerDTO> payload) { super(payload); }
    public PlayerUpdatesResponse() { super(); };
}
