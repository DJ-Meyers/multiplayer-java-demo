package com.meyers.survivor.server.networking.responses;

import com.meyers.survivor.server.networking.dtos.PlayerDTO;

public class PlayerJoinedResponse extends AbstractResponse<PlayerDTO>
{
    public PlayerJoinedResponse(PlayerDTO payload) { super(payload); };
    PlayerJoinedResponse() {}
}
