package org.telegram.messenger.rubika.models;

import org.telegram.messenger.rubika.ApiRequestMessangerRx;

public class JanusAttachInput {
    public String janus = "attach";
    public String transaction = ApiRequestMessangerRx.getTempSession();
    public String plugin = "janus.plugin.videoroom";
    public String opaque_id = "videoroomtest-1";
}
