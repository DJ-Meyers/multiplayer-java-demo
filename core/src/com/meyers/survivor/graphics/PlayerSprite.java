package com.meyers.survivor.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import static com.meyers.survivor.helpers.Logging.logClient;

public class PlayerSprite extends Sprite
{
    TextureRegion region;
    private Texture texture;
    private int playerId;
    
    public PlayerSprite(int id, Texture texture) {
        this.texture = texture;
        setRegion(this.texture);
        region = new TextureRegion(this.texture);
        
        setOrigin(getCenter().x, getCenter().y);
        
        setBounds(region.getRegionX(), region.getRegionY(), region.getRegionWidth(), region.getRegionHeight());
        playerId = id;
    }
    
    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(region, getRegionX(), getRegionY(), getOriginX(), getOriginY(), getRegionWidth(),
                getRegionHeight(), getScaleX(), getScaleY(), getRotation());
    }
    
    public Vector2 getCenter() {
        Vector2 center = new Vector2(getX() + getRegionWidth()/2, getY() + getRegionHeight()/2);
//        logClient(String.format("(X, Y): (%f, %f) - Center: (%f, %f)", getX(), getY(), center.x, center.y));
        return center;
    }
    
    public int getPlayerID() { return playerId; }
}
