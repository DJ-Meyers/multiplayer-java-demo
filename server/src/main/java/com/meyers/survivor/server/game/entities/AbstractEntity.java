package com.meyers.survivor.server.game.entities;

import com.badlogic.gdx.math.Vector2;

import static com.meyers.survivor.server.game.Common.*;

public abstract class AbstractEntity
{
    private float hp;
    private Vector2 position;
    private String name;
    
    public AbstractEntity() {
        hp = 100;
        position = new Vector2(0, 0);
        name = "AbstractEntity";
    }
    
    public float getHp() {
        return hp;
    }
    
    public Vector2 getPosition() {
        return position;
    }
    
    public void setPosition(Vector2 position)
    {
        this.position = position;
    }
    
    public void setPosition(float x, float y) {
        this.position.x = x;
        this.position.y = y;
    }
    
    public float getX() {
        return position.x;
    }
    
    public float getY() {
        return position.y;
    }
    
    public String getName()  {
        return name;
    }
    
    public void setName(String name)
    {
        this.name = name;
    }
}
