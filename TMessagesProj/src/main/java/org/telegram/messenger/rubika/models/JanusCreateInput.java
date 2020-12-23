package org.telegram.messenger.rubika.models;

import org.telegram.messenger.rubika.ApiRequestMessangerRx;

public class JanusCreateInput {
    public String janus = "create";
    public String transaction = ApiRequestMessangerRx.getTempSession();
}
