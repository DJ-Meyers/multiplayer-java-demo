package com.meyers.survivor.server.networking.dtos;

import java.util.ArrayList;
import java.util.List;

public class InputDTO
{
    private List<Integer> keycodes;
    private float delta;
    float mouseX, mouseY;
    
    public InputDTO(List<Integer> keycodes, float x, float y, float delta) {
        this.keycodes = keycodes;
        this.delta = delta;
        this.mouseX = x;
        this.mouseY = y;
    }
    
    public InputDTO() {
        keycodes = new ArrayList<>();
        delta = 0;
        mouseX = 0;
        mouseY = 0;
    }
    
    public List<Integer> getKeycodes()
    {
        return keycodes;
    }
    
    public float getMouseX()
    {
        return mouseX;
    }
    
    public float getMouseY()
    {
        return mouseY;
    }
    
    public float getDelta()
    {
        return delta;
    }
}
