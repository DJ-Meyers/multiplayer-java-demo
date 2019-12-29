package com.meyers.survivor.server.networking.responses;

public abstract class AbstractResponse<T>
{
    protected T value;
    
    public AbstractResponse(T t) { setValue(t); }
    public AbstractResponse() {};
    
    void setValue(T t) { value = t; };
    public T getValue() { return value; }
}
