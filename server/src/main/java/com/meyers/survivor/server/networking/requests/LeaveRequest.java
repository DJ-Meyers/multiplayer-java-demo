package com.meyers.survivor.server.networking.requests;

public class LeaveRequest extends AbstractRequest<Integer>
{
    public LeaveRequest(Integer payload) { super(payload); }
    public LeaveRequest() { super(); };
}
