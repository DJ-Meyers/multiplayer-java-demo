package com.meyers.survivor.server;

import com.meyers.survivor.server.game.GameState;

import java.io.IOException;

public class ServerLauncher
{
    public static void main(String[] args) throws IOException
    {
        GameState state = new GameState();
        SurvivorServer server = new SurvivorServer();
        server.run();
    }
}
