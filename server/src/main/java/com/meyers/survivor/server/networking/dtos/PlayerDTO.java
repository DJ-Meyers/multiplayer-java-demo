package com.meyers.survivor.server.networking.dtos;

import com.meyers.survivor.server.game.entities.Player;

public class PlayerDTO
{
    private int id;
    private float x, y;
    private float hp;
    private String name;
    private float reticleX, reticleY;
    
    public PlayerDTO(Player player) {
        this.id = player.getPlayerID();
        this.x = player.getX();
        this.y = player.getY();
        this.hp = player.getHp();
        this.name = player.getName();
        this.reticleX = player.getReticleX();
        this.reticleY = player.getReticleY();
    }
    
    public PlayerDTO() {
        this.id = -1;
        this.x = 0;
        this.y = 0;
        this.hp = 100;
        this.name = "Default";
        this.reticleX = 0;
        this.reticleY = 0;
    };
    
    public int getID()
    {
        return id;
    }
    
    public float getX()
    {
        return x;
    }
    
    public float getY()
    {
        return y;
    }
    
    public float getHp()
    {
        return hp;
    }
    
    public String getName()
    {
        return name;
    }
    
    public float getReticleX()
    {
        return reticleX;
    }
    
    public float getReticleY()
    {
        return reticleY;
    }
}
