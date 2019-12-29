package com.meyers.survivor.server.networking.requests;

import com.meyers.survivor.server.networking.dtos.InputDTO;

public class InputRequest extends AbstractRequest<InputDTO>
{
    public InputRequest(InputDTO payload) { super(payload); }
    public InputRequest() { super(); };
}
