package org.telegram.messenger.rubika.models;

import android.os.Build;

import org.telegram.messenger.rubika.ApiRequestMessangerRx;

import java.util.ArrayList;

public class JanusJoinFeedInput {
    public String janus = "message";
    public String transaction = ApiRequestMessangerRx.getTempSession();
    public Data body = new Data();

    public static class Data {
        public String request = "join";
        public int room = 1234;
        public String ptype = "subscriber";
        public long private_id;
        public ArrayList<StreamObject> streams=new ArrayList<>();

    }

    public static class StreamObject {
        public  long feed;
        public String mid;
    }
}
