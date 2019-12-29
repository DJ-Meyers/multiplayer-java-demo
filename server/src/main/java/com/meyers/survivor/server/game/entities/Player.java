package com.meyers.survivor.server.game.entities;

import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.kryonet.Connection;

import static com.meyers.survivor.server.game.Common.*;
import static com.meyers.survivor.server.helpers.Logging.*;

public class Player extends AbstractEntity
{
    private static int playerNumber = 0;
    private int playerID;
    private float reticleX, reticleY;
    
    public Player(int id) {
        playerID = id;
//        setPosition(SPAWN_POINT_X, SPAWN_POINT_Y);
        setPosition(MathUtils.random(0, 300), MathUtils.random(0, 300));
        setName("P" + ++playerNumber);
        
        logGame("New Player\n" + this.toString());
    }
    
    @Override
    public String toString() {
        return String.format("ID: %d\nName: %s\nPosition: (%4f, %4f)", playerID, getName(), getX(), getY());
    }
    
    public void setReticle(float x, float y)
    {
        reticleX = x;
        reticleY = y;
    }
    
    public float getReticleX()
    {
        return reticleX;
    }
    
    public float getReticleY()
    {
        return reticleY;
    }
    
    public int getPlayerID()
    {
        return playerID;
    }
}
