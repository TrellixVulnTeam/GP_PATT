package org.telegram.messenger.rubika.models;

public class JanusAttachOutput {
    public String janus;
    public String transaction;
    public String session_id;
    public Data data;

    public static class Data {
        public String id;
    }
}
