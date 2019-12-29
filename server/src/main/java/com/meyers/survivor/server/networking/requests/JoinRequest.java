package com.meyers.survivor.server.networking.requests;

import com.esotericsoftware.kryonet.Connection;

// TODO change to allow players to reconnect.
public class JoinRequest extends AbstractRequest<Integer>
{
    public JoinRequest(Integer payload) { super(payload); }
    public JoinRequest() { super(); };
}
