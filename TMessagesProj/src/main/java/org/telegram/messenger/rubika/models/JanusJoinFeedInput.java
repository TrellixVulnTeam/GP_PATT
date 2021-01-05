package org.telegram.messenger.rubika.models;

import android.os.Build;

import org.telegram.messenger.rubika.ApiRequestMessangerRx;

public class JanusJoinFeedInput {
    public String janus = "message";
    public String transaction = ApiRequestMessangerRx.getTempSession();
    public Data body = new Data();

    public static class Data {
        public String request = "join";
        public int room = 1234;
        public String ptype = "subscriber";
        public String private_id;
        public String feed;
    }
}
