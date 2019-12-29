package com.meyers.survivor.server.networking.requests;

public abstract class AbstractRequest<T>
{
    protected T value;
    
    public AbstractRequest(T t) { setValue(t); }
    public AbstractRequest() {};
    
    void setValue(T t) { value = t; };
    public T getValue() { return value; }
}
