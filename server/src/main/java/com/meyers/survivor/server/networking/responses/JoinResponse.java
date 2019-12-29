package com.meyers.survivor.server.networking.responses;

import com.meyers.survivor.server.networking.dtos.PlayerDTO;

import java.util.List;

public class JoinResponse extends AbstractResponse<List<PlayerDTO>>
{
    public JoinResponse(List<PlayerDTO> payload) { super(payload); }
    public JoinResponse() {super(); }
}
