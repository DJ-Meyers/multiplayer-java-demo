package com.meyers.survivor.server.networking.responses;

import com.meyers.survivor.server.networking.dtos.PlayerDTO;

public class PlayerLeftResponse extends AbstractResponse<PlayerDTO>
{
    public PlayerLeftResponse(PlayerDTO payload){ super(payload); }
    PlayerLeftResponse(){}
}
